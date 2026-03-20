package com.automation.hu05.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;
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
    
    @FindBy(css = "#name")
    private WebElement eventNameInput;
    
    @FindBy(css = "#description")
    private WebElement eventDescriptionInput;
    
    @FindBy(css = "#eventDate")
    private WebElement eventDateInput;
    
    @FindBy(css = "#venue")
    private WebElement eventLocationInput;
    
    @FindBy(css = "#maxCapacity")
    private WebElement eventCapacityInput;

    @FindBy(css = "#basePrice")
    private WebElement basePriceInput;

    @FindBy(css = "#imageUrl")
    private WebElement imageUrlInput;

    @FindBy(css = "#tags")
    private WebElement tagsInput;

    @FindBy(css = "#isActive")
    private WebElement isActiveCheckbox;
    
    @FindBy(xpath = "//button[contains(text(), 'Crear Evento')]")
    private WebElement submitButton;

    @FindBy(xpath = "//button[contains(text(), 'Cancelar')]")
    private WebElement cancelButton;
    
    // ================== Success/Error Message Elements ==================
    
    @FindBy(css = "div[data-slot='alert']:not(.text-destructive)")
    private WebElement infoAlert;

    @FindBy(css = "ol[aria-label='Notifications (F8)']")
    private WebElement toastContainer;
    
    @FindBy(xpath = "//div[contains(text(), 'Éxito')]")
    private WebElement toastTitle;
    
    @FindBy(xpath = "//div[contains(text(), 'Evento creado correctamente.')]")
    private WebElement toastDescription;
    
    @FindBy(css = "p.mt-1.text-sm.text-destructive")
    private List<WebElement> errorMessages;

    @FindBy(css = "[aria-invalid='true']")
    private List<WebElement> invalidFields;
    
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
     * Clears the event name input using JavaScript and dispatches input/change events.
     * Useful when standard clear() doesn't trigger framework change handlers.
     */
    public void clearEventNameJS() {
        WebElement element = findElement(SelectorConstants.EVENT_NAME_INPUT);
        String script =
            "var el = arguments[0];" +
            "el.value = '';" +
            "el.dispatchEvent(new Event('input', { bubbles: true }));" +
            "el.dispatchEvent(new Event('change', { bubbles: true }));" +
            "el.dispatchEvent(new Event('blur', { bubbles: true }));";
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(script, element);
        System.out.println("[EVENTPAGE] Cleared event name via JS");
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
     * Enters event date into the date field using JavaScript to bypass browser-specific datetime-local validation issues.
     * Format expected by datetime-local: YYYY-MM-DDTHH:mm
     * This version uses a more robust approach triggering React/DOM events to ensure the value is captured.
     * 
     * @param dateTime ISO format string (e.g., 2026-12-31T12:00)
     */
    public void enterEventDateTimeJS(String dateTime) {
        WebElement element = findElement(SelectorConstants.EVENT_DATE_INPUT);
        // Robust JS injection for React/Next.js components
        String script = 
            "var el = arguments[0];" +
            "var val = arguments[1];" +
            "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "nativeInputValueSetter.call(el, val);" +
            "el.dispatchEvent(new Event('input', { bubbles: true }));" +
            "el.dispatchEvent(new Event('change', { bubbles: true }));" +
            "el.dispatchEvent(new Event('blur', { bubbles: true }));";
            
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(script, element, dateTime);
        System.out.println("[EVENTPAGE] Set DateTime via Robust JS: " + dateTime);
    }

    /**
     * Enters event date into the date field.
     * Uses JS for reliability with datetime-local inputs.
     */
    public void enterEventDate(String date) {
        // If date is YYYY-MM-DD, add default time
        if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            enterEventDateTimeJS(date + "T12:00");
        } else {
            fillTextField(SelectorConstants.EVENT_DATE_INPUT, date);
        }
    }
    
    /**
     * Legacy method for DDMMYYYY HHmm format, now uses JS for compatibility.
     */
    public void enterEventDateTime(String date, String time) {
        // Convert DDMMYYYY HHmm to YYYY-MM-DDTHH:mm
        if (date.length() == 8 && time.length() == 4) {
            String iso = date.substring(4, 8) + "-" + date.substring(2, 4) + "-" + date.substring(0, 2) + 
                         "T" + time.substring(0, 2) + ":" + time.substring(2, 4);
            enterEventDateTimeJS(iso);
        } else {
            enterEventDateTimeJS(date + "T" + time);
        }
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
     * Enters base price into the base price field.
     * 
     * @param price Base price as string
     */
    public void enterBasePrice(String price) {
        fillTextField(SelectorConstants.EVENT_PRICE_INPUT, price);
    }

    /**
     * Enters image URL into its field.
     * @param url Image URL string
     */
    public void enterImageUrl(String url) {
        fillTextField(SelectorConstants.EVENT_IMAGE_URL_INPUT, url);
    }

    /**
     * Enters tags into the tags field.
     * @param tags Comma-separated tags
     */
    public void enterTags(String tags) {
        fillTextField(SelectorConstants.EVENT_TAGS_INPUT, tags);
    }

    /**
     * Toggle the status checkbox button.
     */
    public void toggleActiveStatus() {
        click(SelectorConstants.EVENT_ACTIVE_CHECKBOX);
    }

    /**
     * Checks if the status is currently Active/Checked.
     * @return true if checked
     */
    public boolean isActiveChecked() {
        return isElementPresent(SelectorConstants.EVENT_ACTIVE_CHECKED);
    }
    
    /**
     * Clicks the submit button to submit the event registration form.
     */
    public void clickSubmitButton() {
        click(SelectorConstants.SUBMIT_BUTTON);
    }

    /**
     * Checks if the form is empty/clean by verifying aria-invalid is false or error list is empty.
     * 
     * @return true if no validation errors present
     */
    public boolean isFormClean() {
        return invalidFields.isEmpty() && errorMessages.isEmpty();
    }

    /**
     * Gets the count of active validation error messages visible on the form.
     * 
     * @return count of error paragraphs
     */
    public int getValidationErrorsCount() {
        return errorMessages.size();
    }
    
    /**
     * Helper method to fill the entire form with valid event data.
     * Used in positive test scenarios.
     */
    public void fillValidEventForm() {
        enterEventName(TestDataConstants.VALID_EVENT_NAME);
        enterEventDescription(TestDataConstants.VALID_EVENT_DESCRIPTION);
        // Standard input format conversion
        enterEventDateTime("15062026", "1200");
        enterEventLocation(TestDataConstants.VALID_EVENT_LOCATION);
        enterEventCapacity(String.valueOf(TestDataConstants.VALID_EVENT_CAPACITY));
        enterBasePrice("50");
    }
    
    // ================== VALIDATION METHODS (Assertions) ==================
    
    /**
     * Validates that the success toast message is displayed.
     * 
     * @return true if toast title is visible, false otherwise
     */
    public boolean isSuccessToastDisplayed() {
        return isElementVisible(SelectorConstants.TOAST_TITLE);
    }
    
    /**
     * Retrieves the text of the toast message description.
     * 
     * @return Toast message text
     */
    public String getToastDescriptionText() {
        return getText(SelectorConstants.TOAST_DESCRIPTION);
    }

    /**
     * Retrieves all visible error message texts from the form.
     * 
     * @return List of error message strings
     */
    public java.util.List<String> getAllErrorMessages() {
        return errorMessages.stream()
            .map(WebElement::getText)
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Checks if any form validation error is displayed.
     * 
     * @return true if any error message visible, false if form is clean
     */
    public boolean areFormErrorsDisplayed() {
        return !errorMessages.isEmpty();
    }
}
