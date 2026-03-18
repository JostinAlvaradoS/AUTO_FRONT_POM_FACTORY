package com.automation.hu05.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.automation.hu05.constants.SelectorConstants;
import com.automation.hu05.constants.UrlConstants;
import com.automation.hu05.constants.TestDataConstants;
import com.automation.hu05.constants.TimeoutConstants;

/**
 * Page Object for Event Registration page.
 * Encapsulates all interactions with the event registration form and validations.
 * 
 * Responsibilities:
 * - Navigation to event registration page
 * - Filling form fields with user data
 * - Submitting the form
 * - Validating success and error messages
 */
public class EventPage extends BasePage {
    
    // ================== Form Input Elements (using @FindBy with Page Factory) ==================
    
    @FindBy(id = "eventName")
    private WebElement eventNameInput;
    
    @FindBy(id = "eventDescription")
    private WebElement eventDescriptionInput;
    
    @FindBy(id = "eventDate")
    private WebElement eventDateInput;
    
    @FindBy(id = "eventLocation")
    private WebElement eventLocationInput;
    
    @FindBy(id = "eventCapacity")
    private WebElement eventCapacityInput;
    
    @FindBy(id = "submitBtn")
    private WebElement submitButton;
    
    // ================== Success/Error Message Elements ==================
    
    @FindBy(id = "successMessage")
    private WebElement successMessage;
    
    @FindBy(id = "error-name")
    private WebElement errorNameField;
    
    @FindBy(id = "error-description")
    private WebElement errorDescriptionField;
    
    @FindBy(id = "error-date")
    private WebElement errorDateField;
    
    @FindBy(id = "error-location")
    private WebElement errorLocationField;
    
    @FindBy(id = "error-capacity")
    private WebElement errorCapacityField;
    
    /**
     * Constructor initializes WebDriver and initializes all @FindBy elements.
     * 
     * @param driver WebDriver instance
     */
    public EventPage(WebDriver driver) {
        super(driver);
    }
    
    // ================== NAVIGATION METHODS ==================
    
    /**
     * Navigates to the event registration page and waits for page to load.
     */
    public void navigateToEventRegistration() {
        driver.navigate().to(UrlConstants.EVENT_REGISTRATION_PAGE);
        implicitlyWaitForPageLoad();
    }
    
    /**
     * Waits for the page to be fully loaded by checking for presence of key element.
     */
    private void implicitlyWaitForPageLoad() {
        findElement(SelectorConstants.EVENT_NAME_INPUT);
    }
    
    // ================== FORM FILLING METHODS (User Actions) ==================
    
    /**
     * Enters event name into the name field.
     * 
     * @param name Event name to enter
     */
    public void enterEventName(String name) {
        fillTextField(SelectorConstants.EVENT_NAME_INPUT, name);
    }
    
    /**
     * Enters event description into the description field.
     * 
     * @param description Event description to enter
     */
    public void enterEventDescription(String description) {
        fillTextField(SelectorConstants.EVENT_DESCRIPTION_INPUT, description);
    }
    
    /**
     * Enters event date into the date field.
     * 
     * @param date Event date in format YYYY-MM-DD
     */
    public void enterEventDate(String date) {
        fillTextField(SelectorConstants.EVENT_DATE_INPUT, date);
    }
    
    /**
     * Enters event location into the location field.
     * 
     * @param location Event location to enter
     */
    public void enterEventLocation(String location) {
        fillTextField(SelectorConstants.EVENT_LOCATION_INPUT, location);
    }
    
    /**
     * Enters event capacity into the capacity field.
     * 
     * @param capacity Event capacity as string
     */
    public void enterEventCapacity(String capacity) {
        fillTextField(SelectorConstants.EVENT_CAPACITY_INPUT, capacity);
    }
    
    /**
     * Clicks the submit button to submit the event registration form.
     */
    public void clickSubmitButton() {
        click(SelectorConstants.SUBMIT_BUTTON);
    }
    
    /**
     * Helper method to fill the entire form with valid event data.
     * Used in positive test scenarios.
     */
    public void fillValidEventForm() {
        enterEventName(TestDataConstants.VALID_EVENT_NAME);
        enterEventDescription(TestDataConstants.VALID_EVENT_DESCRIPTION);
        enterEventDate(TestDataConstants.VALID_EVENT_DATE);
        enterEventLocation(TestDataConstants.VALID_EVENT_LOCATION);
        enterEventCapacity(String.valueOf(TestDataConstants.VALID_EVENT_CAPACITY));
    }
    
    // ================== VALIDATION METHODS (Assertions) ==================
    
    /**
     * Validates that the success message is displayed after form submission.
     * 
     * @return true if success message is visible, false otherwise
     */
    public boolean isSuccessMessageDisplayed() {
        return isElementVisible(SelectorConstants.SUCCESS_MESSAGE);
    }
    
    /**
     * Retrieves the text of the success message.
     * 
     * @return Success message text
     */
    public String getSuccessMessageText() {
        return getText(SelectorConstants.SUCCESS_MESSAGE);
    }
    
    /**
     * Validates that error message for name field is displayed.
     * 
     * @return true if error message visible, false otherwise
     */
    public boolean isNameFieldErrorDisplayed() {
        return isElementVisible(SelectorConstants.FIELD_ERROR_NAME);
    }
    
    /**
     * Retrieves the error message text for name field.
     * 
     * @return Error message text for name field
     */
    public String getNameFieldErrorText() {
        return getText(SelectorConstants.FIELD_ERROR_NAME);
    }
    
    /**
     * Validates that error message for description field is displayed.
     * 
     * @return true if error message visible, false otherwise
     */
    public boolean isDescriptionFieldErrorDisplayed() {
        return isElementVisible(SelectorConstants.FIELD_ERROR_DESCRIPTION);
    }
    
    /**
     * Retrieves the error message text for description field.
     * 
     * @return Error message text for description field
     */
    public String getDescriptionFieldErrorText() {
        return getText(SelectorConstants.FIELD_ERROR_DESCRIPTION);
    }
    
    /**
     * Validates that error message for date field is displayed.
     * 
     * @return true if error message visible, false otherwise
     */
    public boolean isDateFieldErrorDisplayed() {
        return isElementVisible(SelectorConstants.FIELD_ERROR_DATE);
    }
    
    /**
     * Retrieves the error message text for date field.
     * 
     * @return Error message text for date field
     */
    public String getDateFieldErrorText() {
        return getText(SelectorConstants.FIELD_ERROR_DATE);
    }
    
    /**
     * Validates that error message for location field is displayed.
     * 
     * @return true if error message visible, false otherwise
     */
    public boolean isLocationFieldErrorDisplayed() {
        return isElementVisible(SelectorConstants.FIELD_ERROR_LOCATION);
    }
    
    /**
     * Retrieves the error message text for location field.
     * 
     * @return Error message text for location field
     */
    public String getLocationFieldErrorText() {
        return getText(SelectorConstants.FIELD_ERROR_LOCATION);
    }
    
    /**
     * Validates that error message for capacity field is displayed.
     * 
     * @return true if error message visible, false otherwise
     */
    public boolean isCapacityFieldErrorDisplayed() {
        return isElementVisible(SelectorConstants.FIELD_ERROR_CAPACITY);
    }
    
    /**
     * Retrieves the error message text for capacity field.
     * 
     * @return Error message text for capacity field
     */
    public String getCapacityFieldErrorText() {
        return getText(SelectorConstants.FIELD_ERROR_CAPACITY);
    }
    
    /**
     * Checks if any form validation error is displayed.
     * 
     * @return true if any error message visible, false if form is clean
     */
    public boolean areFormErrorsDisplayed() {
        return isNameFieldErrorDisplayed() || 
               isDescriptionFieldErrorDisplayed() || 
               isDateFieldErrorDisplayed() || 
               isLocationFieldErrorDisplayed() || 
               isCapacityFieldErrorDisplayed();
    }
}
