package com.automation.hu05.step_definitions;

import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Y;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import com.automation.hu05.hooks.WebDriverManager;
import com.automation.hu05.pages.EventPage;
import com.automation.hu05.pages.EventListPage;
import com.automation.hu05.factories.PageObjectFactory;
import com.automation.hu05.constants.TestDataConstants;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Step Definitions for HU-05 Event Registration Scenarios.
 * 
 * This class maps Gherkin steps from the feature file to Java methods.
 * Each method is a step binding decorated with @Dado, @Y, @Cuando, @Entonces
 * 
 * Step Binding Pattern:
 * ┌────────────────────────────────────────────────────────────┐
 * │ Gherkin Step:                                              │
 * │ "Dado que el usuario administrador navega a la página"    │
 * │                                                            │
 * │ Java Step Definition:                                      │
 * │ @Dado("que el usuario administrador navega a la página")  │
 * │ public void navigateToEventPage() {                       │
 * │     eventPage.navigateToEventRegistration();              │
 * │ }                                                          │
 * └────────────────────────────────────────────────────────────┘
 * 
 * Step Categories:
 * 1. @Dado (Given) - Preconditions/Setup: navigation, login, test data
 * 2. @Y (And) - Continues previous step type: fill more fields, more assertions
 * 3. @Cuando (When) - User Action: click button, submit form
 * 4. @Entonces (Then) - Expected Result/Assertion: verify message, check list
 * 
 * Best Practice:
 * - Each step should do ONE thing (Single Responsibility)
 * - Use page objects for all UI interactions (NO direct WebDriver calls)
 * - Use assertions for validations (@Entonces steps)
 * - Keep step code readable and maintainable
 */
public class EventRegistrationSteps {
    
    private EventPage eventPage;
    private EventListPage eventListPage;
    private PageObjectFactory pageObjectFactory;
    
    /**
     * Constructor - Gets PageObjectFactory from WebDriverManager.
     * PageObjectFactory is initialized in @Before hook by WebDriverManager.
     */
    public EventRegistrationSteps() {
        this.pageObjectFactory = WebDriverManager.getPageObjectFactory();
        this.eventPage = pageObjectFactory.getEventPage();
        this.eventListPage = pageObjectFactory.getEventListPage();
    }
    
    // ==================== @Dado (Given) Steps - Preconditions ====================
    
    /**
     * @Dado - Precondition: Navigate to event registration page.
     * This is typically called in the Background section of the feature file.
     * 
     * Step:
     * "Dado que el usuario administrador navega a la página de registro de eventos"
     * 
     * Action: Navigates to EVENT_REGISTRATION_PAGE URL
     */
    @Dado("que el usuario administrador navega a la página de registro de eventos")
    public void navigateToEventRegistrationPage() {
        eventPage.navigateToEventRegistration();
        assertTrue(eventPage.isElementPresent("id:eventName"), 
                  "Event registration page did not load properly");
    }
    
    // ==================== @Y (And) Steps - Field Entry ====================
    
    /**
     * @Y - User enters event name into the form field.
     * 
     * Step Parameter: Event name as a string
     * Example: "Concierto de Rock 2024"
     */
    @Y("el usuario ingresa un nombre de evento \"([^\"]*)\"")
    public void enterEventName(String eventName) {
        eventPage.enterEventName(eventName);
    }
    
    /**
     * @Y - User enters event description into the form field.
     * 
     * Step Parameter: Event description as a string
     */
    @Y("el usuario ingresa una descripción \"([^\"]*)\"")
    public void enterEventDescription(String description) {
        eventPage.enterEventDescription(description);
    }
    
    /**
     * @Y - User enters event date into the form field.
     * 
     * Step Parameter: Event date in format YYYY-MM-DD
     * Example: "2024-12-15"
     */
    @Y("el usuario ingresa una fecha de evento \"([^\"]*)\"")
    public void enterEventDate(String date) {
        eventPage.enterEventDate(date);
    }
    
    /**
     * @Y - User enters event location into the form field.
     * 
     * Step Parameter: Event location as a string
     */
    @Y("el usuario ingresa una ubicación \"([^\"]*)\"")
    public void enterEventLocation(String location) {
        eventPage.enterEventLocation(location);
    }
    
    /**
     * @Y - User enters event capacity into the form field.
     * 
     * Step Parameter: Event capacity as a numeric string
     * Example: "5000"
     */
    @Y("el usuario ingresa una capacidad de evento \"([^\"]*)\"")
    public void enterEventCapacity(String capacity) {
        eventPage.enterEventCapacity(capacity);
    }
    
    // ==================== @Cuando (When) Steps - User Actions ====================
    
    /**
     * @Cuando - User submits the form by clicking the submit button.
     * This should be called after all form fields are filled.
     * 
     * Action: Clicks submit button and waits for page response
     */
    @Cuando("el usuario hace clic en el botón enviar")
    public void clickSubmitButton() {
        eventPage.clickSubmitButton();
    }
    
    /**
     * @Cuando - User submits empty form (negative test scenario).
     * The user clicks submit without filling any form fields first.
     * 
     * This is used in the "Negative" scenario to test field validation.
     * 
     * Action: Directly clicks submit button without pre-filling form
     */
    @Cuando("el usuario hace clic en el botón enviar sin llenar ningún campo")
    public void clickSubmitButtonWithoutFillingForm() {
        // User is already on the form (from Background: Dado step)
        // No fields are filled - directly click submit
        eventPage.clickSubmitButton();
    }
    
    // ==================== @Entonces (Then) Steps - Assertions ====================
    
    /**
     * @Entonces - Validates success message is displayed (Positive test).
     * Called after successful form submission in the positive scenario.
     * 
     * Assertion: Verifies that success message is visible to the user
     */
    @Entonces("debe mostrarse un mensaje de éxito")
    public void validateSuccessMessage() {
        // Small wait for success message to appear
        try {
            Thread.sleep(1000); // 1 second wait for message to render
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        assertTrue(eventPage.isSuccessMessageDisplayed(),
                  "Success message is not displayed after form submission");
        
        String successText = eventPage.getSuccessMessageText();
        assertNotNull(successText, "Success message text is null");
        assertTrue(successText.toLowerCase().contains("éxito") || 
                  successText.toLowerCase().contains("creado") ||
                  successText.toLowerCase().contains("exitoso"),
                  "Success message does not contain expected text");
    }
    
    /**
     * @Entonces - Validates that event appears in the event list (Positive test).
     * Called after successful form submission to verify the event is persisted.
     * 
     * Assertion: Verifies event can be found in the event list by name
     */
    @Entonces("el evento debe mostrarse en la lista de eventos")
    public void validateEventInList() {
        // Navigate to event list page to verify creation
        eventListPage.navigateToEventList();
        
        // Check if event is in the list
        boolean eventFound = eventListPage.isEventInListByName(TestDataConstants.VALID_EVENT_NAME);
        assertTrue(eventFound,
                  "Event '" + TestDataConstants.VALID_EVENT_NAME + "' was not found in the event list");
    }
    
    /**
     * @Entonces - Validates error message for required name field (Negative test).
     * Called when form is submitted without filling the name field.
     * 
     * Assertion: Verifies error message is displayed below the name field
     */
    @Entonces("debe mostrarse un mensaje de error para el campo nombre")
    public void validateNameFieldError() {
        assertTrue(eventPage.isNameFieldErrorDisplayed(),
                  "Error message for name field is not displayed");
        
        String errorText = eventPage.getNameFieldErrorText();
        assertNotNull(errorText, "Error message text for name field is null");
        assertTrue(errorText.toLowerCase().contains("requerido") ||
                  errorText.toLowerCase().contains("obligatorio") ||
                  errorText.toLowerCase().contains("nombre"),
                  "Error message does not contain expected text for name field");
    }
    
    /**
     * @Entonces - Validates error message for required description field (Negative test).
     */
    @Entonces("debe mostrarse un mensaje de error para el campo descripción")
    public void validateDescriptionFieldError() {
        assertTrue(eventPage.isDescriptionFieldErrorDisplayed(),
                  "Error message for description field is not displayed");
        
        String errorText = eventPage.getDescriptionFieldErrorText();
        assertNotNull(errorText, "Error message text for description field is null");
    }
    
    /**
     * @Entonces - Validates error message for required date field (Negative test).
     */
    @Entonces("debe mostrarse un mensaje de error para el campo fecha")
    public void validateDateFieldError() {
        assertTrue(eventPage.isDateFieldErrorDisplayed(),
                  "Error message for date field is not displayed");
        
        String errorText = eventPage.getDateFieldErrorText();
        assertNotNull(errorText, "Error message text for date field is null");
    }
    
    /**
     * @Entonces - Validates error message for required location field (Negative test).
     */
    @Entonces("debe mostrarse un mensaje de error para el campo ubicación")
    public void validateLocationFieldError() {
        assertTrue(eventPage.isLocationFieldErrorDisplayed(),
                  "Error message for location field is not displayed");
        
        String errorText = eventPage.getLocationFieldErrorText();
        assertNotNull(errorText, "Error message text for location field is null");
    }
    
    /**
     * @Entonces - Validates error message for required capacity field (Negative test).
     */
    @Entonces("debe mostrarse un mensaje de error para el campo capacidad")
    public void validateCapacityFieldError() {
        assertTrue(eventPage.isCapacityFieldErrorDisplayed(),
                  "Error message for capacity field is not displayed");
        
        String errorText = eventPage.getCapacityFieldErrorText();
        assertNotNull(errorText, "Error message text for capacity field is null");
    }
}
