# BILD Mobile Automation Framework

A robust mobile automation framework for testing the BILD Android News App using Java 11, TestNG, and Appium 9.0.0.

## 🎯 **About**

Framework optimized for **BILD News App** testing with focus on search functionality and premium feature verification.

**Key Features:**
- 📱 BILD-specific mobile automation with coordinate-based interactions  
- 🏗️ Page Object Model with SOLID principles
- 📊 ExtentReports with screenshots and retry mechanism
- ⚙️ JSON-based test data and flexible configuration

## ️ **Technology Stack**

| Component | Version | Purpose |
|-----------|---------|---------|
| Java | 11+ | Programming language |
| Appium | 9.0.0 | Mobile automation |
| TestNG | 7.8.0 | Test framework |
| Maven | 3.9.4 | Build management |
| ExtentReports | 5.1.1 | Test reporting |

## 📋 **Current Test Cases**

### **1. Search Functionality** (`testPatternSearchWithCoordinateTap`)
- **Purpose**: Verify search functionality with coordinate-based tapping
- **Actions**: Navigate → Search → Enter term → Verify results
- **Data**: Uses JSON configuration for search coordinates and terms

### **2. Premium Access Verification** (`testBildLoginwithoutPremium`) 
- **Purpose**: Verify non-premium users cannot access premium content
- **Actions**: Navigate → Premium article → Login attempt → Verify restriction
- **Focus**: Ensures premium paywall functionality

## 🚀 **Quick Start**

### **Prerequisites**
```bash
# Install requirements
- Java 11+
- Android SDK
- Appium Server 2.x
- Maven 3.6+
```

### **Setup Device**
Edit `src/test/resources/capabilities.properties`:
```properties
deviceName=YourDeviceName
udid=your_device_udid            # from 'adb devices'
platformVersion=your_android_version
```

### **Run Tests**
```bash
# All tests
mvn test

# Specific test
mvn test -Dtest=BildHomePageTest#testPatternSearchWithCoordinateTap

# With custom device
mvn test -DdeviceName="Pixel 8" -Dudid="ABC123"
```

## 📊 **Reports & Logs**

**Generated Files:**
- `reports/ExtentReport.html` - Detailed test results with screenshots
- `logs/test-execution.log` - Execution logs
- `screenshots/` - Failure screenshots

## ⚙️ **Configuration**

### **Framework Settings** (`framework.properties`)
```properties
appium.server.auto.start=false
appium.server.port=4723
retry.count=2
implicit.wait=10
```

### **App Configuration** (`capabilities.properties`)
```properties
appPackage=com.netbiscuits.bild.android
appActivity=de.bild.android.app.MainActivity
platformName=Android
automationName=UiAutomator2
```

## 🔧 **Common Issues**

| Issue | Solution |
|-------|----------|
| Element not found | Check locators in `BildAppLocators.java` |
| Coordinate tap fails | Update coordinates in `BildHomePageTestData.json` |
| App not launching | Verify device connection: `adb devices` |
| Server connection | Start Appium: `appium -p 4723` |

## 🎯 **Best Practices**

**Do's:**
- ✅ Use explicit waits with meaningful conditions
- ✅ Implement multi-level fallback selectors  
- ✅ Configure appropriate retry mechanisms

**Don'ts:**
- ❌ Rely solely on Thread.sleep() for timing
- ❌ Use fragile XPath selectors with indices
- ❌ Hardcode timeouts without configuration

## 📁 **Project Structure**

```
src/
├── main/java/com/company/framework/
│   ├── pages/bild/                    # BILD-specific pages
│   ├── managers/                      # Driver & config management
│   └── utils/                         # Utilities & helpers
├── test/java/                         # Test classes
└── test/resources/                    # Configuration files
    ├── capabilities.properties
    ├── framework.properties
    └── testdata/BildHomePageTestData.json
```

## 🚀 **CI/CD Integration**

**GitHub Actions** - Automated testing on API 29 & 30 with Docker support.

For detailed setup and troubleshooting, refer to framework documentation or contact the test automation team.

## 🚀 **CI/CD Integration**

**GitHub Actions** - Automated testing on API 29 & 30 with Docker support.

For detailed setup and troubleshooting, refer to framework documentation or contact the test automation team.
- Supports different implementation strategies (e.g., iOS vs Android)

**⚡ Interface Segregation Principle**
- Multiple small, specific interfaces instead of large monolithic ones
- Classes only depend on methods they actually use
- Reduces coupling and improves maintainability
- Supports incremental testing and development

**🔗 Dependency Inversion Principle**
- High-level modules don't depend on low-level modules
- Both depend on abstractions (interfaces)
- Factory patterns for object creation
- Dependency injection for test configuration

#### **3. Project Structure Design**

**📁 Modular Component Architecture:**
```
Component Layer (Domain Logic)
├── Elements (Locator Definitions)
├── Actions (User Interactions)
└── Verifications (Assertions)

Infrastructure Layer (Framework)  
├── Managers (Driver, Configuration)
├── Utils (Helpers, Screenshots)
└── Reports (ExtentReports, Logging)

Test Layer (Test Cases)
├── Base Classes (Setup/Teardown)
├── Test Classes (Business Logic)
└── Configuration (Capabilities, Properties)
```

**🎯 Benefits of This Design:**
- **🔍 Maintainability**: Changes isolated to specific components
- **🚀 Scalability**: Easy to add new features and test cases
- **🧪 Testability**: Individual components can be unit tested
- **🔄 Reusability**: Components shared across multiple test scenarios
- **📊 Debugging**: Clear separation makes issue identification easier

#### **4. Mobile-Specific Design Decisions**

**📱 Touch Interaction Strategy:**
- **PointerInput API**: Modern W3C-compliant touch interactions
- **Coordinate-Based Actions**: Precise touch targeting for complex UI elements
- **Gesture Support**: Tap, double-tap, long-press, swipe implementations
- **Timing Configuration**: Configurable delays and durations

**⏳ Wait Strategy Implementation:**
- **WebDriverWait**: Explicit waits for element conditions
- **FluentWait**: Custom polling intervals and timeout configurations
- **No Thread.sleep**: Eliminated unreliable static delays
- **Smart Retry**: Intelligent retry mechanism for flaky mobile elements

**🎯 Selector Strategy Hierarchy:**
1. **Resource IDs** (highest priority) - Most stable across app versions
2. **Accessibility IDs** - Accessibility-compliant and stable
3. **Class Names** - Reliable for standard UI components  
4. **XPath with attributes** - Flexible but performance-aware
5. **Fallback Strategies** - Multiple selector options for robustness

#### **5. Configuration Management Design**

**🔧 Multi-Environment Support:**
- **Properties-Based**: Easy configuration without code changes
- **Environment Variables**: CI/CD and security-sensitive values
- **Capability Management**: Dynamic device and app configuration
- **Profile Support**: Different configurations for dev/test/prod

**📊 Reporting & Observability:**
- **ExtentReports Integration**: Rich HTML reports with screenshots
- **Screenshot Strategy**: Automatic capture on failures and key steps
- **Logging Framework**: Structured logging with different levels
- **Test Artifacts**: Organized storage of reports, logs, and screenshots

### **Dependency Inversion Principle**
- High-level modules don't depend on low-level modules
- Both depend on abstractions (interfaces)
- Uses `DependencyFactory` for dependency injection

---

## ⚙️ **Configuration**

### BILD App Configuration (`capabilities.properties`)
```properties
# BILD Android App Configuration
appPackage=com.netbiscuits.bild.android
appActivity=de.bild.android.app.MainActivity
platformName=Android
deviceName=Pixel 8
automationName=UiAutomator2
udid=YOUR_DEVICE_UDID
autoGrantPermissions=true
noReset=true
newCommandTimeout=300
appiumServer=http://127.0.0.1:4723/

# Optional: Locale configuration for German content
# cap.setCapability("language", "de");
# cap.setCapability("locale", "DE");
```

### Framework Configuration (`framework.properties`)
```properties
# Framework Information
framework.name=AS Mobile Automation Framework
framework.version=2.0.0

# Wait timeouts (seconds)
implicit.wait=10
explicit.wait=30
page.load.timeout=30

# Retry configuration for flaky mobile tests
retry.failed.tests=true
retry.count=2

# Screenshot configuration
screenshot.on.failure=true
screenshot.on.success=false

# Logging configuration
log.level=INFO

# Appium Server Management (NEW!)
appium.server.auto.start=true
appium.server.host=127.0.0.1
appium.server.port=4723
appium.server.startup.timeout=30
```

---

## 📊 **Reporting & Logs**

### **ExtentReports**
- **Location**: `reports/ExtentReport_TIMESTAMP.html`
- **Features**: Rich HTML reports with test details, screenshots, and execution timeline
- **Screenshots**: Automatic capture on test failures
- **System Info**: Device details, app version, and environment information

### **Screenshots**
- **Location**: `screenshots/` directory
- **Naming**: Organized by test name and timestamp
- **Automatic**: Captured on test failures and specific checkpoints

### **Logs**
- **Location**: `logs/test-execution.log`
- **Level**: Configurable via `log.level` property
- **Format**: Structured logging with timestamps and thread information

## 🔄 **Retry Mechanism**

The framework includes an intelligent retry mechanism for handling flaky mobile tests:
- **Configurable**: Set retry count via `framework.properties`
- **Smart Retry**: Only retries genuine failures (not assertions)
- **Detailed Logging**: Full retry history in ExtentReports
- **TestNG Integration**: Uses TestNG's IRetryAnalyzer interface

---

## 📁 **Project Files Overview**

### **Configuration Files**
- **`pom.xml`** - Maven dependencies and build configuration
- **`src/testng.xml`** - TestNG suite configuration for BILD tests
- **`capabilities.properties`** - Device and app capabilities
- **`framework.properties`** - Framework behavior settings

### **Test Execution Results**
- **`reports/`** - ExtentReports HTML files with execution details
- **`screenshots/`** - Test failure screenshots and verification images
- **`logs/`** - Detailed execution logs for debugging

## 🤝 **Contributing**

1. Follow SOLID principles when adding new features
2. Create component-based page objects for new screens
3. Add comprehensive test documentation
4. Include proper error handling and logging
5. Update this README for significant changes
---

## 🛡️ **Flakiness Mitigation**

### **Understanding Test Flakiness in Mobile Automation**

Mobile test flakiness occurs due to various factors including network latency, device performance variations, app initialization delays, and UI element rendering inconsistencies. This framework implements multiple layers of defense against flaky test behavior.

---

#### **1. Intelligent Retry Mechanism** 🔄

**Automatic Test Retries**
- **Retry Analyzer**: Automatically applied to tests via AnnotationTransformer
- **Configuration**: Set retry behavior in framework.properties
- **Smart Delays**: Configurable wait time between retry attempts

**Smart Retry Logic:**
- ✅ **Retries genuine failures** (network timeouts, element not found)
- ❌ **Doesn't retry assertion failures** (prevents masking real bugs)
- 📊 **Logs all retry attempts** in ExtentReports for analysis
- 🎯 **Configurable retry count** per test environment

---

#### **2. Robust Wait Strategies** ⏱️

**Dynamic Waits (Recommended)**
- **WebDriverWait**: Use ExpectedConditions for reliable element interaction
- **Custom Utilities**: Framework provides enhanced wait operations
- **WaitUtils Integration**: Utility methods for element interaction timing
- **Click and Visibility**: Automated wait-for-clickable and wait-for-visibility operations

**Wait Configuration:**
- **Properties File**: Configure timeouts in `framework.properties`
- **Implicit/Explicit Waits**: Separate timeout settings for different scenarios
- **Page Load Timeout**: App screen transition timeout configuration

**Wait Best Practices:**
- **Explicit Conditions**: Use WebDriverWait with ExpectedConditions for reliability
- **Avoid Thread.sleep**: Prefer condition-based waits over fixed delays
- **Smart Polling**: Custom wait conditions for complex element states

---

#### **3. Resilient Element Selectors** 🎯

**Multi-Level Selector Strategy**
- **Primary Selectors**: ID-based locators for maximum stability
- **Fallback Options**: XPath and content-desc alternatives
- **Robust Backup**: Class-based selectors as last resort
- **Implementation**: Located in BildHomeElements.java with PageFactory annotations

**Smart Element Location**
- **Fallback Strategy**: Multiple locator attempts with automatic retry
- **Error Recovery**: Graceful handling when primary selectors fail
- **Logging**: Detailed selector failure tracking for debugging

**Selector Stability Guidelines:**
- 🥇 **Resource IDs** (most stable) - `com.netbiscuits.bild.android:id/element`
- 🥈 **Content Descriptions** - `content-desc="Search"`  
- 🥉 **XPath with attributes** - `//android.widget.Button[@text='Search']`
- 🚫 **Avoid XPath with indices** - `//android.view.View[3]/android.widget.Button[1]`

---

### 🎯 **Best Practices Summary**

#### **Do's** ✅
- ✅ Use **explicit waits** with meaningful conditions
- ✅ Implement **multi-level fallback selectors**
- ✅ Configure **appropriate retry mechanisms**

#### **Don'ts** ❌  
- ❌ Rely solely on **Thread.sleep()** for timing
- ❌ Use **fragile XPath selectors** with indices
- ❌ **Hardcode timeouts** without configuration

---

## 🚀 **CI/CD Integration**

### **GitHub Actions**
```yaml
# .github/workflows/ci.yml
name: BILD Mobile Test Suite
on: [push, pull_request]

# Runs on Android API 29 & 30
# Auto-installs: Java 11, Node.js, Appium 2.x
# Uploads: Test reports, screenshots, logs
```

### **Docker Support**
```bash
# Run tests in container
./scripts/docker-ci.sh test

# Requirements: 4GB RAM, 20GB disk space
```

### **CI Features**
- ✅ Automated test execution on PR/push
- ✅ Multi-API level testing (Android 10 & 11)  
- ✅ Test artifact upload (30-day retention)
- ✅ Docker containerization for isolation
---

## 🆘 **Troubleshooting**

### 🔧 **Common Pitfalls & Solutions**

---

#### **1. Appium Server Issues** 🚫

**❌ Problem: "No route found for /wd/hub/session"**
```bash
[HTTP] No route found for /wd/hub/session
[HTTP] <-- POST /wd/hub/session 404 17 ms - 211
```

**✅ Solution: Appium 2.x Compatibility**
- **Root Cause**: Appium 2.x doesn't use `/wd/hub` path by default
- **Fix**: Start Appium with base-path flag
```bash
# Correct command for Appium 2.x
appium -p 4723 --base-path /wd/hub --allow-cors

# Verify server is running with correct endpoints
netstat -an | findstr "4723"
```

**❌ Problem: "SessionNotCreatedException: Could not start a new session"**
```bash
org.openqa.selenium.SessionNotCreatedException:
Could not start a new session. Possible causes are invalid address of the remote server
```

**✅ Solution: Server Configuration**
1. **Check Appium server status**:
   ```bash
   # Kill any existing node processes
   taskkill /F /IM node.exe
   
   # Start fresh server
   appium -p 4723 --base-path /wd/hub --allow-cors
   ```

2. **Verify server is listening**:
   ```bash
   netstat -an | findstr "4723"
   # Should show: TCP 0.0.0.0:4723 LISTENING
   ```

3. **Update capabilities.properties**:
   ```properties
   appiumServer=http://127.0.0.1:4723/wd/hub
   ```

---

#### **2. Touch Action Implementation Issues** 📱

**❌ Problem: Touch actions not working or coordinates inaccurate**

**✅ Solution: Proper TouchActionUtils Implementation**
1. **Use W3C Actions API** (recommended over deprecated TouchAction)
2. **Verify coordinate accuracy** - Check device resolution and scaling
3. **Update TouchActionUtils.java** with proper PointerInput implementation

2. **Configure Coordinates**: Define tap coordinates and duration in ApplicationConstants.java

3. **Use Configuration**: Store coordinates and timing in app-config.properties for flexibility

---

#### **3. Framework Auto-Start Issues** ⚙️

**❌ Problem: Tests fail because auto-start is disabled**
```bash
INFO utils.AppiumServerManager - Appium server auto-start is disabled. 
Please start Appium server manually.
```

**✅ Solution: Manual Server Management**
1. **Check Configuration**: Verify `appium.server.auto.start` setting in framework.properties
2. **Manual Start**: Use Appium server command with proper port and CORS settings  
3. **Auto-Start Option**: Enable automatic server startup in properties if preferred

---

#### **4. Element Location Issues** 🔍

**❌ Problem: "NoSuchElementException" or elements not found**

**✅ Solution: Dynamic Element Handling**
1. **Use Proper Wait Strategies**: WebDriverWait with ExpectedConditions instead of Thread.sleep
2. **Implement Retry Mechanism**: Framework includes automatic retry with @Retry annotation

3. **Use SearchElementInspector**: Run element inspector test to discover current UI locators

---

#### **5. Test Data & Configuration Issues** 📋

**❌ Problem: Tests use hardcoded values or wrong configurations**

**✅ Solution: Configuration Management**
1. **Use TestUtils**: Leverage reusable operations instead of inline test code
2. **Centralized Configuration**: Separate files for app constants, device capabilities, framework settings
3. **Environment Support**: Dynamic configuration loading based on environment variables

---

#### **6. Compilation & Dependency Issues** ⚠️

**❌ Problem: "The method X is undefined" or import errors**

**✅ Solution: Maven Dependency Management**
1. **Clean and Rebuild**: Use Maven clean compile and install commands
2. **Java Compatibility**: Verify Java 11+ compatibility in Maven compiler settings
3. **Dependency Updates**: Check for outdated dependencies using Maven versions plugin

---

#### **7. Test Execution & Reporting Issues** 📊

**❌ Problem: Tests pass but reports show failures or missing steps**

**✅ Solution: Proper Test Structure**
1. **Use Utility Methods**: Encapsulate complex logic in TestUtils for reusability
2. **Exception Handling**: Implement graceful error handling to prevent test suite failures
3. **Proper Logging**: Use ExtentReports for detailed test step documentation

---

#### **8. Device & ADB Issues** 📱

**❌ Problem: Device not detected or connection issues**

**✅ Solution: ADB Troubleshooting**
1. **Check Device Connection**: Use ADB commands to verify device detection
2. **Restart ADB Service**: Kill and restart ADB server if connection issues occur
3. **Update Device Capabilities**: Use correct UDID from device list in capabilities configuration

---

### 🚀 **Quick Debug Checklist**

**Before Running Tests:**
- ✅ Appium server running with `--base-path /wd/hub`
- ✅ Device connected (`adb devices`)
- ✅ BILD app installed on device
- ✅ Correct UDID in capabilities.properties

**When Tests Fail:**
1. 🔍 Check `logs/test-execution.log`
2. 📊 Review latest ExtentReport HTML
3. 📸 Check `screenshots/` directory
4. 🔄 Verify retry attempts in reports

**Performance Optimization:**
- 🏃‍♂️ Use utility methods to reduce code duplication
- ⏱️ Implement proper waits instead of Thread.sleep
- 🔄 Leverage retry mechanism for flaky tests
- 📱 Use touch actions for more realistic user interactions

---

### 📚 **Support Resources**
- **Framework Documentation**: `BILD_FRAMEWORK_SUMMARY.md`
- **Usage Guide**: `BILD_HOMEPAGE_GUIDE.md`
- **Execution Logs**: `logs/test-execution.log`
- **Latest Report**: `reports/ExtentReport_[timestamp].html`
- **TouchAction Examples**: `utils/TouchActionUtils.java`
- **Test Utilities**: `utils/TestUtils.java`

---

### 🆘 **Emergency Commands**

```bash
# Complete framework reset
taskkill /F /IM node.exe
adb kill-server && adb start-server
mvn clean compile
appium -p 4723 --base-path /wd/hub --allow-cors

# Quick test execution
mvn test -Dtest=BildHomePageTest#testUmfrageSearch -q

# Check latest report
Start-Process "reports/ExtentReport_[latest-timestamp].html"
```