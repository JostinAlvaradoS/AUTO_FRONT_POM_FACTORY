package com.automation.hu05.factories;

import org.openqa.selenium.WebDriver;
import com.automation.hu05.pages.EventPage;
import com.automation.hu05.pages.EventListPage;
import com.automation.hu05.pages.LoginPage;
import com.automation.hu05.pages.WaitlistPage;

public class PageObjectFactory {
    
    private WebDriver driver;
    
    private EventPage eventPage;
    private EventListPage eventListPage;
    private LoginPage loginPage;
    private WaitlistPage waitlistPage;
    
    public PageObjectFactory(WebDriver driver) {
        this.driver = driver;
    }
    
    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(driver);
        }
        return loginPage;
    }
    
    public EventPage getEventPage() {
        if (eventPage == null) {
            eventPage = new EventPage(driver);
        }
        return eventPage;
    }
    
    public EventListPage getEventListPage() {
        if (eventListPage == null) {
            eventListPage = new EventListPage(driver);
        }
        return eventListPage;
    }
    
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
    
    public WebDriver getDriver() {
        return driver;
    }
}
