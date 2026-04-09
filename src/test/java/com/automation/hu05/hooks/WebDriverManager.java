package com.automation.hu05.hooks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.automation.hu05.factories.PageObjectFactory;
import com.automation.hu05.constants.TimeoutConstants;
import java.time.Duration;

public class WebDriverManager {
    
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    
    private static final ThreadLocal<PageObjectFactory> pageObjectFactory = new ThreadLocal<>();
    
    private WebDriverManager() {
        
    }
    
    public static void initialiseDriver() {
        if (driver.get() != null) {
            throw new IllegalStateException("WebDriver is already initialized. Call closeDriver() first.");
        }
        
        ChromeOptions options = new ChromeOptions();
        
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popups");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        
        options.setAcceptInsecureCerts(true);
        
        WebDriver chromeDriver = new ChromeDriver(options);
        
        chromeDriver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(TimeoutConstants.IMPLICIT_WAIT_SECONDS)
        );
        chromeDriver.manage().timeouts().pageLoadTimeout(
            Duration.ofSeconds(TimeoutConstants.PAGE_LOAD_TIMEOUT_SECONDS)
        );
        
        chromeDriver.manage().window().maximize();
        
        driver.set(chromeDriver);
        
        pageObjectFactory.set(new PageObjectFactory(chromeDriver));
    }
    
    public static WebDriver getDriver() {
        WebDriver driverInstance = driver.get();
        if (driverInstance == null) {
            throw new IllegalStateException(
                "WebDriver is not initialized. Call initialiseDriver() first in @Before hook."
            );
        }
        return driverInstance;
    }
    
    public static PageObjectFactory getPageObjectFactory() {
        PageObjectFactory factory = pageObjectFactory.get();
        if (factory == null) {
            throw new IllegalStateException(
                "PageObjectFactory is not initialized. Call initialiseDriver() first in @Before hook."
            );
        }
        return factory;
    }
    
    public static void closeDriver() {
        try {
            WebDriver driverInstance = driver.get();
            if (driverInstance != null) {
                driverInstance.quit();
            }
        } catch (Exception e) {
            System.err.println("Error while closing WebDriver: " + e.getMessage());
        } finally {
            
            pageObjectFactory.remove();
            driver.remove();
        }
    }
    
    public static boolean isDriverInitialized() {
        return driver.get() != null;
    }
}
