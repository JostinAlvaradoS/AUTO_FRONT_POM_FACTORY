package com.automation.hu05.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.automation.hu05.constants.SelectorConstants;
import com.automation.hu05.constants.UrlConstants;

public class LoginPage extends BasePage {
    
    @FindBy(css = "#email")
    private WebElement emailInput;
    
    @FindBy(css = "#password")
    private WebElement passwordInput;
    
    @FindBy(css = "#password ~ button")
    private WebElement togglePasswordButton;
    
    @FindBy(xpath = "//button[contains(text(), 'Iniciar Sesión')]")
    private WebElement loginButton;

    @FindBy(xpath = "//button[contains(text(), 'Auto-completar formulario')]")
    private WebElement autoFillButton;
    
    @FindBy(css = "div[data-slot='alert'].text-destructive")
    private WebElement unauthorizedAlert;

    @FindBy(xpath = "//div[contains(text(), 'Acceso no autorizado. Se requiere rol de administrador.')]")
    private WebElement unauthorizedRoleMessage;
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    public void navigateToLoginPage() {
        driver.navigate().to(UrlConstants.ADMIN_LOGIN_PAGE);
        implicitlyWaitForPageLoad();
    }
    
    private void implicitlyWaitForPageLoad() {
        findElement(SelectorConstants.LOGIN_EMAIL_INPUT);
    }

    public void useAutoFillButton() {
        click(SelectorConstants.LOGIN_AUTOFILL_BUTTON);
    }
    
    public void enterEmail(String email) {
        fillTextField(SelectorConstants.LOGIN_EMAIL_INPUT, email);
    }
    
    public void enterPassword(String password) {
        fillTextField(SelectorConstants.LOGIN_PASSWORD_INPUT, password);
    }
    
    public void clickLoginButton() {
        click(SelectorConstants.LOGIN_BUTTON);
    }

    public boolean isUnauthorizedRoleErrorDisplayed() {
        return isElementVisible(SelectorConstants.UNAUTHORIZED_ALERT);
    }
    
    public void performLogin(String email, String password) {
        System.out.println("[LOGIN] Starting login process with email: " + email);
        
        System.out.println("[LOGIN] Entering email address...");
        enterEmail(email);
        try {
            Thread.sleep(800); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("[LOGIN] Entering password...");
        enterPassword(password);
        try {
            Thread.sleep(800); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("[LOGIN] Attempting to click login button...");
        
        try {
            
            boolean isVisible = isElementVisible(SelectorConstants.LOGIN_BUTTON);
            System.out.println("[LOGIN] Login button visible: " + isVisible);
            
            if (!isVisible) {
                throw new RuntimeException("Login button is not visible - cannot proceed with login");
            }
            
            System.out.println("[LOGIN] Clicking login button with selector: " + SelectorConstants.LOGIN_BUTTON);
            clickLoginButton();
            System.out.println("[LOGIN] Login button clicked successfully");
            
        } catch (Exception e) {
            System.err.println("[LOGIN] Error clicking button: " + e.getMessage());
            throw new RuntimeException("Failed to click login button", e);
        }
        
        System.out.println("[LOGIN] Waiting for authentication and page redirect...");
        try {
            Thread.sleep(3000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("[LOGIN] Login process completed");
    }
    
    public boolean isLoginErrorDisplayed() {
        return isUnauthorizedRoleErrorDisplayed();
    }
    
    public String getLoginErrorText() {
        return getText(SelectorConstants.UNAUTHORIZED_ALERT);
    }
    
    public boolean isLoginSuccessful() {
        return !isLoginErrorDisplayed();
    }
}
