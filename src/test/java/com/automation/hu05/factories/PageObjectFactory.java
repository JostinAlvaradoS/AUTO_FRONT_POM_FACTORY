package com.automation.hu05.factories;

import org.openqa.selenium.WebDriver;
import com.automation.hu05.pages.EventPage;
import com.automation.hu05.pages.EventListPage;
import com.automation.hu05.pages.LoginPage;
import com.automation.hu05.pages.WaitlistPage;

/**
 * PageObjectFactory - Centralized Page Object Instantiation.
 * 
 * This factory ensures that:
 * 1. All page objects are created consistently
 * 2. No direct "new PageObject()" calls in step definitions
 * 3. Page objects are reused within a scenario (lazy initialization)
 * 4. WebDriver reference is properly passed to all pages
 * 
 * Design Pattern: Factory Pattern with lazy initialization
 * 
 * Usage in Step Definitions:
 * ┌─────────────────────────────────────────────────┐
 * │ CORRECT:                                        │
 * │ PageObjectFactory factory = new PageObjectFactory(driver);
 * │ EventPage eventPage = factory.getEventPage();  │
 * │                                                 │
 * │ INCORRECT:                                      │
 * │ EventPage eventPage = new EventPage(driver);   │
 * └─────────────────────────────────────────────────┘
 */
public class PageObjectFactory {
    
    private WebDriver driver;
    
    // Lazy-initialized page objects (cached)
    private EventPage eventPage;
    private EventListPage eventListPage;
    private LoginPage loginPage;
    private WaitlistPage waitlistPage;
    
    /**
     * Constructor initializes the factory with a WebDriver instance.
     * 
     * @param driver WebDriver instance to be used for all page objects
     */
    public PageObjectFactory(WebDriver driver) {
        this.driver = driver;
    }
    
    /**
     * Gets or creates LoginPage instance (lazy initialization with caching).
     * 
     * @return Cached instance of LoginPage
     */
    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(driver);
        }
        return loginPage;
    }
    
    /**
     * Gets or creates EventPage instance (lazy initialization with caching).
     * 
     * Benefits of lazy initialization:
     * - Page objects created only when needed
     * - Same instance reused throughout scenario (reduces object creation overhead)
     * - Ensures consistent WebDriver reference
     * 
     * @return Cached instance of EventPage
     */
    public EventPage getEventPage() {
        if (eventPage == null) {
            eventPage = new EventPage(driver);
        }
        return eventPage;
    }
    
    /**
     * Gets or creates EventListPage instance (lazy initialization with caching).
     * 
     * @return Cached instance of EventListPage
     */
    public EventListPage getEventListPage() {
        if (eventListPage == null) {
            eventListPage = new EventListPage(driver);
        }
        return eventListPage;
    }
    
    /**
     * Resets all cached page objects.
     * Should be called:
     * - Between test scenarios (in @After hook after driver cleanup)
     * - When switching to a new browser instance
     * - When testing requires fresh page object state
     */
    public WaitlistPage getWaitlistPage() {
        if (waitlistPage == null) {
            waitlistPage = new WaitlistPage(driver);
        }
        return waitlistPage;
    }

    public void reset() {
        eventPage = null;
        eventListPage = null;
        loginPage = null;
        waitlistPage = null;
    }
    
    /**
     * Gets the underlying WebDriver instance.
     * Use sparingly - prefer page object methods over direct driver access.
     * 
     * @return WebDriver instance
     */
    public WebDriver getDriver() {
        return driver;
    }
}
