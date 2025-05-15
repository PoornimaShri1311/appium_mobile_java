# trustWallet-clean

This is a clean and scalable mobile automation framework for Android, enhanced from the original [sample-mobile-framework](https://github.com/HadiTW/sample-mobile-framework).

## ✅ Key Enhancements

This fork includes several improvements for better maintainability, structure, and reporting:

### 🔧 Framework Structure Improvements
- **Added new utilities under `utils/`:**
    - `CapabilitiesManager`: Handles capability setup in a modular way
    - `ExtentManager`: Centralized ExtentReports configuration
    - `PropertyLoader`: Loads properties from config files
    - `ScreenshotUtils`: Captures screenshots during test execution
    - `WaitUtils`: Handles explicit waits across the framework

- **Structured `pages/` folder**: Created new page classes representing different screens and integrated them with utility classes.

### 📦 Build & Dependency Management
- Updated the `pom.xml` to include:
    - Latest **Java client** version for Appium
    - Additional **plugins** and dependencies required for reporting, screenshots, and utility classes

### 🧪 Test Execution Flow
- Updated `BaseTest` class:
    - Added `@BeforeSuite` and `@AfterSuite` for suite-level setup and teardown
    - Enhanced `@AfterMethod` to capture screenshots and log results in ExtentReports

### 📂 Added Support For
- `reports/` – Auto-generated test execution reports
- `logs/` – Organized log files
- `screenshots/` – Failure screenshots for test analysis

> ✅ These folders are excluded from Git using `.gitignore`.

### ⚠️ APK File Note
Due to GitHub's 100MB file limit, the test APK (`latest.apk`) was not pushed. Users executing the test must manually place the `.apk` file at:


---

## 🔜 Planned Enhancements

The following features are part of future roadmap:

- 📶 **WiFi toggle mechanism**
- 📊 **External data provider** (e.g., Excel or JSON)
- ♻️ **Retry mechanism for failed tests**
- 🚀 **CI/CD pipeline integration** (e.g., GitHub Actions, Jenkins)

---

## 📌 How to Run

1. Clone the repository
2. Add the APK at `src/apps/android/latest.apk`
3. Install dependencies:
   ```bash
   Option 1 : mvn test -Dtest=PasscodeTest
   Option 2 : testng.xml > Run
   

