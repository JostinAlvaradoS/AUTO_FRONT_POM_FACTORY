package com.automation.hu05.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.automation.hu05.constants.TimeoutConstants;
import java.time.Duration;

/**
 * Base Page Object class providing common functionality for all Page Objects.
 * All concrete page classes must extend this class.
 * 
 * This class handles:
 * - WebDriver initialization with Page Factory
 * - WebDriverWait initialization
 * - Common element interaction methods (click, fillText, getText, etc.)
 * - Locator parsing from the format "type:value"
 */
public abstract class BasePage {
    
    /** WebDriver instance shared across page objects */
    protected WebDriver driver;
    
    /** WebDriverWait instance for explicit waits */
    protected WebDriverWait wait;
    
    /**
     * Constructor - initializes WebDriver and WebDriverWait.
     * Automatically initializes all @FindBy annotated elements via PageFactory.
     * 
     * @param driver WebDriver instance to be used
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(
            TimeoutConstants.EXPLICIT_WAIT_SECONDS));
        // Initialize all @FindBy annotated elements
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Finds an element with explicit wait for presence.
     * Waits up to EXPLICIT_WAIT_SECONDS for element to be present in DOM.
     * 
     * @param locator Selector string in format "type:value" (e.g., "id:myId", "css:.myClass")
     * @return WebElement when found
     */
    protected WebElement findElement(String locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(
            parseLocator(locator)));
    }
    
    /**
     * Clicks an element with explicit wait for clickability.
     * Waits for element to be present AND clickable before clicking.
     * Uses JavaScript click as fallback if standard click fails.
     * 
     * @param locator Selector string in format "type:value"
     */
    protected void click(String locator) {
        try {
            // Try standard click with explicit wait
            WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(parseLocator(locator)));
            
            // Scroll element into view first
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", element);
            
            // Brief pause to ensure scroll completes
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            element.click();
        } catch (Exception e) {
            // Fallback: use JavaScript click if standard click fails
            try {
                WebElement element = driver.findElement(parseLocator(locator));
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();", element);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to click element at locator: " + locator, ex);
            }
        }
    }
    
    /**
     * Fills a text field by clearing it first, then typing text.
     * 
     * @param locator Selector string in format "type:value"
     * @param text Text to enter into the field
     */
    protected void fillTextField(String locator, String text) {
        WebElement element = findElement(locator);
        element.clear();
        element.sendKeys(text);
    }
    
    /**
     * Gets the text content of an element.
     * 
     * @param locator Selector string in format "type:value"
     * @return Text content of the element
     */
    protected String getText(String locator) {
        return findElement(locator).getText();
    }
    
    /**
     * Checks if an element is visible on the page (with explicit wait).
     * 
     * @param locator Selector string in format "type:value"
     * @return true if element is visible, false if wait times out
     */
    protected boolean isElementVisible(String locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                parseLocator(locator)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Checks if an element is present in the DOM (with explicit wait).
     * 
     * @param locator Selector string in format "type:value"
     * @return true if element is present, false if not found
     */
    public boolean isElementPresent(String locator) {
        try {
            findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Parses locator string and returns appropriate By locator.
     * Supported formats:
     * - "id:myId" → By.id("myId")
     * - "class:myClass" → By.className("myClass")
     * - "css:.mySelector" → By.cssSelector(".mySelector")
     * - "xpath://div" → By.xpath("//div")
     * - "name:myName" → By.name("myName")
     * 
     * @param locator Locator string in "type:value" format
     * @return By locator for use with Selenium
     * @throws IllegalArgumentException if locator type is not recognized
     */
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
}
