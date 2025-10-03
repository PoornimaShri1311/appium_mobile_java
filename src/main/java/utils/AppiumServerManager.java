package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * AppiumServerManager - Manages Appium server lifecycle
 * Provides functionality to start, stop, and check Appium server status
 */
public class AppiumServerManager {
    
    private static final Logger logger = LogManager.getLogger(AppiumServerManager.class);
    private static Process appiumServerProcess;
    private static boolean isServerStarted = false;
    
    /**
     * Starts the Appium server if auto-start is enabled and server is not already running
     */
    public static void startAppiumServer() {
        if (!FrameworkConfig.isAppiumServerAutoStart()) {
            logger.info("Appium server auto-start is disabled. Please start Appium server manually.");
            return;
        }
        
        if (isServerRunning()) {
            logger.info("Appium server is already running on {}:{}", 
                FrameworkConfig.getAppiumServerHost(), FrameworkConfig.getAppiumServerPort());
            isServerStarted = true;
            return;
        }
        
        // Check if port is in use by another process
        int port = FrameworkConfig.getAppiumServerPort();
        if (isPortInUse(port)) {
            logger.warn("Port {} is in use by another process. Attempting to free it...", port);
            killProcessUsingPort(port);
            
            // Wait a moment for the port to be freed
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Check again
            if (isPortInUse(port)) {
                throw new RuntimeException("Port " + port + " is still in use after attempting to free it. " + 
                    "Please manually stop any processes using this port or change the port in framework.properties");
            }
        }
        
        try {
            logger.info("Starting Appium server on {}:{}", 
                FrameworkConfig.getAppiumServerHost(), FrameworkConfig.getAppiumServerPort());
            
            String command = buildAppiumCommand();
            logger.info("Executing command: {}", command);
            
            ProcessBuilder processBuilder;
            if (isWindows()) {
                // On Windows, use cmd.exe to execute the command properly
                processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            } else {
                // On Unix-like systems, split the command
                processBuilder = new ProcessBuilder(command.split(" "));
            }
            
            processBuilder.redirectErrorStream(true);
            appiumServerProcess = processBuilder.start();
            
            // Start a thread to read process output for debugging
            Thread outputReaderThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(appiumServerProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        logger.debug("Appium Server Output: {}", line);
                        // Look for server ready indicators
                        if (line.contains("Appium REST http interface listener started") || 
                            line.contains("server started") || 
                            line.contains("listener started on")) {
                            logger.info("Appium server ready indicator found in output");
                        }
                    }
                } catch (Exception e) {
                    logger.debug("Error reading Appium server output: {}", e.getMessage());
                }
            });
            outputReaderThread.setDaemon(true);
            outputReaderThread.start();
            
            // Wait for server to start
            if (waitForServerToStart()) {
                isServerStarted = true;
                logger.info("Appium server started successfully");
            } else {
                // Capture any remaining output before failing
                logger.error("Server startup failed. Process is alive: {}", appiumServerProcess.isAlive());
                throw new RuntimeException("Failed to start Appium server within timeout period");
            }
            
        } catch (Exception e) {
            logger.error("Failed to start Appium server: {}", e.getMessage());
            throw new RuntimeException("Failed to start Appium server", e);
        }
    }
    
    /**
     * Stops the Appium server if it was started by this manager
     */
    public static void stopAppiumServer() {
        if (!isServerStarted || appiumServerProcess == null) {
            logger.info("Appium server was not started by this manager or is already stopped");
            return;
        }
        
        try {
            logger.info("Stopping Appium server...");
            
            // Gracefully terminate the process
            appiumServerProcess.destroy();
            
            // Wait for graceful shutdown
            boolean terminated = appiumServerProcess.waitFor(
                FrameworkConfig.getAppiumServerShutdownTimeout(), TimeUnit.SECONDS);
            
            if (!terminated) {
                logger.warn("Appium server did not terminate gracefully, forcing shutdown");
                appiumServerProcess.destroyForcibly();
            }
            
            isServerStarted = false;
            logger.info("Appium server stopped successfully");
            
        } catch (Exception e) {
            logger.error("Error stopping Appium server: {}", e.getMessage());
        }
    }
    
    /**
     * Checks if Appium server is running by making a status request
     * @return true if server is running, false otherwise
     */
    public static boolean isServerRunning() {
        try {
            URI uri = URI.create(FrameworkConfig.getAppiumServerUrl() + "/status");
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000); // Increased timeout
            connection.setReadTimeout(8000);   // Increased timeout
            
            int responseCode = connection.getResponseCode();
            logger.debug("Server status check - Response code: {}", responseCode);
            connection.disconnect();
            
            return responseCode == 200;
            
        } catch (Exception e) {
            logger.debug("Server status check failed: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Builds the Appium server command based on configuration
     * @return Appium server command string
     */
    private static String buildAppiumCommand() {
        StringBuilder command = new StringBuilder();
        
        // Use custom executable path if specified, otherwise try common defaults
        String executablePath = FrameworkConfig.getAppiumServerExecutablePath();
        if (executablePath != null && !executablePath.trim().isEmpty()) {
            command.append(executablePath);
        } else {
            // Try different common Appium installation methods
            if (isWindows()) {
                command.append("npx appium"); // Most common on Windows with npm
            } else {
                command.append("appium"); // Default for Unix-like systems
            }
        }
        
        // Add server parameters
        command.append(" --address ").append(FrameworkConfig.getAppiumServerHost());
        command.append(" --port ").append(FrameworkConfig.getAppiumServerPort());
        command.append(" --log-level ").append(FrameworkConfig.getAppiumServerLogLevel());
        
        return command.toString();
    }
    
    /**
     * Checks if the current OS is Windows
     * @return true if Windows, false otherwise
     */
    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
    
    /**
     * Waits for the Appium server to start within the configured timeout
     * @return true if server started successfully, false if timeout occurred
     */
    private static boolean waitForServerToStart() {
        int timeout = FrameworkConfig.getAppiumServerStartupTimeout();
        int elapsed = 0;
        int checkInterval = 2; // Check every 2 seconds
        
        logger.info("Waiting for Appium server to start (timeout: {} seconds)", timeout);
        
        while (elapsed < timeout) {
            // Give the server a few seconds to start before checking
            if (elapsed >= 4 && isServerRunning()) {
                logger.info("Appium server is ready after {} seconds", elapsed);
                return true;
            }
            
            try {
                Thread.sleep(checkInterval * 1000);
                elapsed += checkInterval;
                logger.debug("Waiting for Appium server... ({}/{} seconds)", elapsed, timeout);
                
                // After 8 seconds, try multiple status checks
                if (elapsed >= 8) {
                    for (int attempt = 0; attempt < 3; attempt++) {
                        logger.debug("Status check attempt {} at {} seconds", attempt + 1, elapsed);
                        if (isServerRunning()) {
                            logger.info("Appium server is ready after {} seconds (attempt {})", elapsed, attempt + 1);
                            return true;
                        }
                        if (attempt < 2) { // Don't sleep after last attempt
                            Thread.sleep(1000);
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Interrupted while waiting for Appium server to start");
                return false;
            }
        }
        
        // Final attempt
        logger.info("Making final server status check...");
        if (isServerRunning()) {
            logger.info("Appium server is ready (final check)");
            return true;
        }
        
        logger.error("Appium server failed to start within {} seconds", timeout);
        return false;
    }
    
    /**
     * Gets the current server status
     * @return true if server is running, false otherwise
     */
    public static boolean getServerStatus() {
        return isServerRunning();
    }
    
    /**
     * Gets the Appium server URL
     * @return Appium server URL
     */
    public static String getServerUrl() {
        return FrameworkConfig.getAppiumServerUrl();
    }
    
    /**
     * Checks if a port is currently in use
     * @param port The port to check
     * @return true if port is in use, false otherwise
     */
    private static boolean isPortInUse(int port) {
        try {
            java.net.ServerSocket serverSocket = new java.net.ServerSocket(port);
            serverSocket.close();
            return false; // Port is free
        } catch (Exception e) {
            return true; // Port is in use
        }
    }
    
    /**
     * Kills any process using the specified port (Windows only)
     * @param port The port to free
     */
    private static void killProcessUsingPort(int port) {
        if (!isWindows()) {
            logger.warn("Port killing is only implemented for Windows");
            return;
        }
        
        try {
            // Find process using the port
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "netstat -ano | findstr " + port);
            Process process = pb.start();
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(":" + port + " ") && line.contains("LISTENING")) {
                        String[] parts = line.trim().split("\\s+");
                        if (parts.length > 0) {
                            String pid = parts[parts.length - 1];
                            logger.info("Killing process {} using port {}", pid, port);
                            
                            ProcessBuilder killPb = new ProcessBuilder("cmd.exe", "/c", "taskkill /F /PID " + pid);
                            Process killProcess = killPb.start();
                            killProcess.waitFor(5, TimeUnit.SECONDS);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to kill process using port {}: {}", port, e.getMessage());
        }
    }
}