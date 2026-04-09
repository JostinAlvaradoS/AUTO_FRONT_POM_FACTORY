package com.automation.hu05.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.automation.hu05.constants.TimeoutConstants;
import java.time.Duration;

public abstract class BasePage {
    
    protected WebDriver driver;
    
    protected WebDriverWait wait;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(
            TimeoutConstants.EXPLICIT_WAIT_SECONDS));
        
        PageFactory.initElements(driver, this);
    }
    
    protected WebElement findElement(String locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(
            parseLocator(locator)));
    }
    
    protected void click(String locator) {
        System.out.println("[BASEPAGE] Attempting to click element: " + locator);
        try {
            
            By byLocator = parseLocator(locator);
            System.out.println("[BASEPAGE] Parsed locator: " + byLocator);
            
            WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(byLocator));
            System.out.println("[BASEPAGE] Element is clickable, proceeding with click");
            
            System.out.println("[BASEPAGE] Scrolling element into view");
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", element);
            
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            System.out.println("[BASEPAGE] Executing standard click");
            element.click();
            System.out.println("[BASEPAGE] Click executed successfully");
        } catch (Exception e) {
            
            System.out.println("[BASEPAGE] Standard click failed, attempting JavaScript click fallback");
            try {
                By byLocator = parseLocator(locator);
                WebElement element = driver.findElement(byLocator);
                System.out.println("[BASEPAGE] Element found, executing JavaScript click");
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();", element);
                System.out.println("[BASEPAGE] JavaScript click executed successfully");
            } catch (Exception ex) {
                System.err.println("[BASEPAGE] Both click methods failed for locator: " + locator);
                throw new RuntimeException("Failed to click element at locator: " + locator + " - Error: " + ex.getMessage(), ex);
            }
        }
    }
    
    protected void fillTextField(String locator, String text) {
        WebElement element = findElement(locator);
        
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center'});", element);
            
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        element.click();
        
        element.clear();
        if (!element.getAttribute("value").isEmpty()) {
            element.sendKeys(org.openqa.selenium.Keys.chord(org.openqa.selenium.Keys.CONTROL, "a"), org.openqa.selenium.Keys.BACK_SPACE);
        }
        
        element.sendKeys(text);
        
        System.out.println("[BASEPAGE] Filled " + locator + " with: " + text);
    }
    
    protected String getText(String locator) {
        return findElement(locator).getText();
    }
    
    public boolean isElementVisible(String locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                parseLocator(locator)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isElementPresent(String locator) {
        try {
            findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    protected By parseLocator(String locator) {
        if (locator == null || !locator.contains(":")) {
            throw new IllegalArgumentException("Invalid locator format: " + locator + 
                " (expected format: 'type:value')");
        }
        
        String[] parts = locator.split(":", 2);
        String type = parts[0].toLowerCase().trim();
        String value = parts[1].trim();
        
        switch (type) {
            case "id":
                return By.id(value);
            case "class":
                return By.className(value);
            case "css":
                return By.cssSelector(value);
            case "xpath":
                return By.xpath(value);
            case "name":
                return By.name(value);
            default:
                throw new IllegalArgumentException("Unknown locator type: '" + type + 
                    "'. Supported types: id, class, css, xpath, name");
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
