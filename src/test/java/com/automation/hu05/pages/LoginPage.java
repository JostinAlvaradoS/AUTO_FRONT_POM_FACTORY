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
        System.out.println("[LOGIN] Filling email field with: " + email);
        fillTextField(SelectorConstants.LOGIN_EMAIL_INPUT, email);
        System.out.println("[LOGIN] Email field filled successfully");
    }
    
    /**
     * Enters password into the login password field.
     * 
     * @param password Admin password
     */
    public void enterPassword(String password) {
        System.out.println("[LOGIN] Filling password field");
        fillTextField(SelectorConstants.LOGIN_PASSWORD_INPUT, password);
        System.out.println("[LOGIN] Password field filled successfully");
    }
    
    /**
     * Clicks the login button to submit credentials.
     */
    public void clickLoginButton() {
        System.out.println("[LOGIN] Clicking login button with selector: " + SelectorConstants.LOGIN_BUTTON);
        click(SelectorConstants.LOGIN_BUTTON);
        System.out.println("[LOGIN] Login button click executed");
    }
    
    /**
     * Helper method to perform complete login flow.
     * Fills email and password fields, then submits the form.
     * 
     * @param email Admin email
     * @param password Admin password
     */
    public void performLogin(String email, String password) {
        System.out.println("[LOGIN] Starting login process with email: " + email);
        
        // Step 1: Enter email
        System.out.println("[LOGIN] Entering email address...");
        enterEmail(email);
        try {
            Thread.sleep(800); // Wait for field validation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Step 2: Enter password
        System.out.println("[LOGIN] Entering password...");
        enterPassword(password);
        try {
            Thread.sleep(800); // Wait for field validation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Step 3: Click login button with enhanced diagnostics
        System.out.println("[LOGIN] Attempting to click login button...");
        
        try {
            // Check if button is visible
            boolean isVisible = isElementVisible(SelectorConstants.LOGIN_BUTTON);
            System.out.println("[LOGIN] Login button visible: " + isVisible);
            
            if (!isVisible) {
                throw new RuntimeException("Login button is not visible - cannot proceed with login");
            }
            
            // Try to click the button
            System.out.println("[LOGIN] Clicking login button with selector: " + SelectorConstants.LOGIN_BUTTON);
            clickLoginButton();
            System.out.println("[LOGIN] Login button clicked successfully");
            
        } catch (Exception e) {
            System.err.println("[LOGIN] Error clicking button: " + e.getMessage());
            throw new RuntimeException("Failed to click login button", e);
        }
        
        // Step 4: Wait for login processing and page redirect
        System.out.println("[LOGIN] Waiting for authentication and page redirect...");
        try {
            Thread.sleep(3000); // Wait for authentication and redirect
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("[LOGIN] Login process completed");
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
