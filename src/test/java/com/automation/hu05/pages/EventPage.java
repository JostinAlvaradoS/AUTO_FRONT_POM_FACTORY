package com.automation.hu05.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;
import com.automation.hu05.constants.SelectorConstants;
import com.automation.hu05.constants.UrlConstants;
import com.automation.hu05.constants.TestDataConstants;
import com.automation.hu05.constants.TimeoutConstants;

public class EventPage extends BasePage {
    
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
    
    public EventPage(WebDriver driver) {
        super(driver);
    }
    
    public void navigateToEventRegistration() {
        driver.navigate().to(UrlConstants.EVENT_REGISTRATION_PAGE);
        implicitlyWaitForPageLoad();
    }
    
    private void implicitlyWaitForPageLoad() {
        findElement(SelectorConstants.EVENT_NAME_INPUT);
    }
    
    public void enterEventName(String name) {
        fillTextField(SelectorConstants.EVENT_NAME_INPUT, name);
    }

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
    
    public void enterEventDescription(String description) {
        fillTextField(SelectorConstants.EVENT_DESCRIPTION_INPUT, description);
    }
    
    public void enterEventDateTimeJS(String dateTime) {
        WebElement element = findElement(SelectorConstants.EVENT_DATE_INPUT);
        
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

    public void enterEventDate(String date) {
        
        if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            enterEventDateTimeJS(date + "T12:00");
        } else {
            fillTextField(SelectorConstants.EVENT_DATE_INPUT, date);
        }
    }
    
    public void enterEventDateTime(String date, String time) {
        
        if (date.length() == 8 && time.length() == 4) {
            String iso = date.substring(4, 8) + "-" + date.substring(2, 4) + "-" + date.substring(0, 2) + 
                         "T" + time.substring(0, 2) + ":" + time.substring(2, 4);
            enterEventDateTimeJS(iso);
        } else {
            enterEventDateTimeJS(date + "T" + time);
        }
    }
    
    public void enterEventLocation(String location) {
        fillTextField(SelectorConstants.EVENT_LOCATION_INPUT, location);
    }
    
    public void enterEventCapacity(String capacity) {
        fillTextField(SelectorConstants.EVENT_CAPACITY_INPUT, capacity);
    }

    public void enterBasePrice(String price) {
        fillTextField(SelectorConstants.EVENT_PRICE_INPUT, price);
    }

    public void enterImageUrl(String url) {
        fillTextField(SelectorConstants.EVENT_IMAGE_URL_INPUT, url);
    }

    public void enterTags(String tags) {
        fillTextField(SelectorConstants.EVENT_TAGS_INPUT, tags);
    }

    public void toggleActiveStatus() {
        click(SelectorConstants.EVENT_ACTIVE_CHECKBOX);
    }

    public boolean isActiveChecked() {
        return isElementPresent(SelectorConstants.EVENT_ACTIVE_CHECKED);
    }
    
    public void clickSubmitButton() {
        click(SelectorConstants.SUBMIT_BUTTON);
    }

    public boolean isFormClean() {
        return invalidFields.isEmpty() && errorMessages.isEmpty();
    }

    public int getValidationErrorsCount() {
        return errorMessages.size();
    }
    
    public void fillValidEventForm() {
        enterEventName(TestDataConstants.VALID_EVENT_NAME);
        enterEventDescription(TestDataConstants.VALID_EVENT_DESCRIPTION);
        
        enterEventDateTime("15062026", "1200");
        enterEventLocation(TestDataConstants.VALID_EVENT_LOCATION);
        enterEventCapacity(String.valueOf(TestDataConstants.VALID_EVENT_CAPACITY));
        enterBasePrice("50");
    }
    
    public boolean isSuccessToastDisplayed() {
        return isElementVisible(SelectorConstants.TOAST_TITLE);
    }
    
    public String getToastDescriptionText() {
        return getText(SelectorConstants.TOAST_DESCRIPTION);
    }

    public java.util.List<String> getAllErrorMessages() {
        return errorMessages.stream()
            .map(WebElement::getText)
            .collect(java.util.stream.Collectors.toList());
    }

    public boolean areFormErrorsDisplayed() {
        return !errorMessages.isEmpty();
    }
}
