package com.automation.hu05.hooks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.automation.hu05.factories.PageObjectFactory;
import com.automation.hu05.constants.TimeoutConstants;
import java.time.Duration;

/**
 * WebDriver Manager - Thread-Safe WebDriver and PageObjectFactory Management.
 * 
 * Design Pattern: Singleton per Thread (ThreadLocal)
 * 
 * Why ThreadLocal?
 * - Allows parallel test execution (each thread gets its own driver)
 * - Prevents driver conflicts in multi-threaded environments
 * - Automatic cleanup with ThreadLocal.remove()
 * - Thread-safe access without synchronization overhead
 * 
 * Usage in Hooks:
 * ┌───────────────────────────────────────┐
 * │ @Before                               │
 * │ public void setup() {                 │
 * │   WebDriverManager.initialiseDriver() │
 * │   factory = WebDriverManager.getPageObjectFactory() │
 * │ }                                     │
 * │                                       │
 * │ @After                                │
 * │ public void teardown() {              │
 * │   WebDriverManager.closeDriver()      │
 * │ }                                     │
 * └───────────────────────────────────────┘
 */
public class WebDriverManager {
    
    /**
     * ThreadLocal storage for WebDriver instance.
     * Each thread executing tests gets its own WebDriver instance.
     */
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    
    /**
     * ThreadLocal storage for PageObjectFactory instance.
     * Each thread gets its own factory associated with its WebDriver.
     */
    private static final ThreadLocal<PageObjectFactory> pageObjectFactory = new ThreadLocal<>();
    
    /**
     * Private constructor prevents direct instantiation.
     * This is a utility class with only static methods.
     */
    private WebDriverManager() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Initializes Chrome WebDriver with optimized options for automation testing.
     * 
     * Key configurations:
     * - Headless mode (configurable via application.properties)
     * - Disabled notifications and popups
     * - Disabled extensions and plugins
     * - Accepts insecure certificates (for test environments)
     * 
     * Also initializes PageObjectFactory associated with the created driver.
     * 
     * @throws IllegalStateException if driver is already initialized
     */
    public static void initialiseDriver() {
        if (driver.get() != null) {
            throw new IllegalStateException("WebDriver is already initialized. Call closeDriver() first.");
        }
        
        // Configure Chrome options for testing
        ChromeOptions options = new ChromeOptions();
        
        // Disable notifications and popups
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popups");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        
        // Accept insecure certificates (useful in test environments)
        options.setAcceptInsecureCerts(true);
        
        // Optional: Run in headless mode for CI/CD pipelines
        // Uncomment the line below for headless execution
        // options.addArguments("--headless");
        
        // Initialize ChromeDriver
        WebDriver chromeDriver = new ChromeDriver(options);
        
        // Set implicit and explicit wait timeouts
        chromeDriver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(TimeoutConstants.IMPLICIT_WAIT_SECONDS)
        );
        chromeDriver.manage().timeouts().pageLoadTimeout(
            Duration.ofSeconds(TimeoutConstants.PAGE_LOAD_TIMEOUT_SECONDS)
        );
        
        // Maximize browser window for consistent UI testing
        chromeDriver.manage().window().maximize();
        
        // Store driver in ThreadLocal
        driver.set(chromeDriver);
        
        // Initialize PageObjectFactory with the new driver
        pageObjectFactory.set(new PageObjectFactory(chromeDriver));
    }
    
    /**
     * Gets the WebDriver instance for the current thread.
     * 
     * @return WebDriver instance from ThreadLocal
     * @throws IllegalStateException if driver is not initialized
     */
    public static WebDriver getDriver() {
        WebDriver driverInstance = driver.get();
        if (driverInstance == null) {
            throw new IllegalStateException(
                "WebDriver is not initialized. Call initialiseDriver() first in @Before hook."
            );
        }
        return driverInstance;
    }
    
    /**
     * Gets the PageObjectFactory instance for the current thread.
     * 
     * @return PageObjectFactory instance from ThreadLocal
     * @throws IllegalStateException if factory is not initialized
     */
    public static PageObjectFactory getPageObjectFactory() {
        PageObjectFactory factory = pageObjectFactory.get();
        if (factory == null) {
            throw new IllegalStateException(
                "PageObjectFactory is not initialized. Call initialiseDriver() first in @Before hook."
            );
        }
        return factory;
    }
    
    /**
     * Closes the WebDriver and cleans up ThreadLocal resources.
     * Must be called in @After hook to ensure proper cleanup.
     * 
     * Cleanup sequence:
     * 1. Close all browser windows and sessions
     * 2. Clear PageObjectFactory from ThreadLocal
     * 3. Clear WebDriver from ThreadLocal
     */
    public static void closeDriver() {
        try {
            WebDriver driverInstance = driver.get();
            if (driverInstance != null) {
                driverInstance.quit();
            }
        } catch (Exception e) {
            System.err.println("Error while closing WebDriver: " + e.getMessage());
        } finally {
            // Always clean up ThreadLocal to prevent memory leaks
            pageObjectFactory.remove();
            driver.remove();
        }
    }
    
    /**
     * Checks if WebDriver is currently initialized for this thread.
     * 
     * @return true if driver is initialized, false otherwise
     */
    public static boolean isDriverInitialized() {
        return driver.get() != null;
    }
}
