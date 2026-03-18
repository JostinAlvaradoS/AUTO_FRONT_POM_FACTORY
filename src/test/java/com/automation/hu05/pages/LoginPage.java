package com.automation.hu05.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.automation.hu05.constants.SelectorConstants;
import com.automation.hu05.constants.UrlConstants;

/**
 * Page Object for Admin Login page.
 * Encapsulates all interactions with the login form.
 * 
 * Responsibilities:
 * - Navigation to login page
 * - Entering email and password credentials
 * - Submitting login form
 * - Validating successful login or error messages
 */
public class LoginPage extends BasePage {
    
    // ================== Form Input Elements ==================
    
    @FindBy(id = "email")
    private WebElement emailInput;
    
    @FindBy(id = "password")
    private WebElement passwordInput;
    
    @FindBy(id = "loginBtn")
    private WebElement loginButton;
    
    // ================== Error Messages ==================
    
    @FindBy(id = "loginError")
    private WebElement loginErrorMessage;
    
    /**
     * Constructor initializes WebDriver and initializes all @FindBy elements.
     * 
     * @param driver WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    // ================== NAVIGATION METHODS ==================
    
    /**
     * Navigates to the admin login page and waits for page to load.
     */
    public void navigateToLoginPage() {
        driver.navigate().to(UrlConstants.ADMIN_LOGIN_PAGE);
        implicitlyWaitForPageLoad();
    }
    
    /**
     * Waits for the login page to be fully loaded by checking for email input field.
     */
    private void implicitlyWaitForPageLoad() {
        findElement(SelectorConstants.LOGIN_EMAIL_INPUT);
    }
    
    // ================== LOGIN METHODS ==================
    
    /**
     * Enters email address into the login email field.
     * 
     * @param email Admin email address
     */
    public void enterEmail(String email) {
        fillTextField(SelectorConstants.LOGIN_EMAIL_INPUT, email);
    }
    
    /**
     * Enters password into the login password field.
     * 
     * @param password Admin password
     */
    public void enterPassword(String password) {
        fillTextField(SelectorConstants.LOGIN_PASSWORD_INPUT, password);
    }
    
    /**
     * Clicks the login button to submit credentials.
     */
    public void clickLoginButton() {
        click(SelectorConstants.LOGIN_BUTTON);
    }
    
    /**
     * Helper method to perform complete login flow.
     * Fills email and password fields, then submits the form.
     * 
     * @param email Admin email
     * @param password Admin password
     */
    public void performLogin(String email, String password) {
        enterEmail(email);
        try {
            Thread.sleep(500); // Wait for field validation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        enterPassword(password);
        try {
            Thread.sleep(500); // Wait for field validation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Make sure button is visible and clickable before clicking
        if (isElementVisible(SelectorConstants.LOGIN_BUTTON)) {
            clickLoginButton();
        } else {
            throw new RuntimeException("Login button is not visible - cannot proceed with login");
        }
        
        // Wait for login processing and page redirect
        try {
            Thread.sleep(2000); // Wait for authentication and redirect
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // ================== VALIDATION METHODS ==================
    
    /**
     * Validates that login error message is displayed.
     * 
     * @return true if error message visible, false otherwise
     */
    public boolean isLoginErrorDisplayed() {
        return isElementVisible(SelectorConstants.LOGIN_ERROR_MESSAGE);
    }
    
    /**
     * Retrieves the login error message text.
     * 
     * @return Error message text
     */
    public String getLoginErrorText() {
        return getText(SelectorConstants.LOGIN_ERROR_MESSAGE);
    }
    
    /**
     * Validates that login was successful by checking if page redirected.
     * Can be used to verify successful authentication.
     * 
     * @return true if login error is NOT displayed (indicates successful login)
     */
    public boolean isLoginSuccessful() {
        return !isLoginErrorDisplayed();
    }
}
