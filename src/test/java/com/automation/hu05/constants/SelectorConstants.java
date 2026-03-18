package com.automation.hu05.constants;

/**
 * Centralized collection of all UI locators/selectors for the automation framework.
 * Format: "type:value" where type = id, css, xpath, name, class
 * No hardcoded selectors should exist outside this class.
 */
public class SelectorConstants {
    
    // ================== LoginPage - Form Inputs ==================
    /** Email input field on login page */
    public static final String LOGIN_EMAIL_INPUT = "id:email";
    
    /** Password input field on login page */
    public static final String LOGIN_PASSWORD_INPUT = "id:password";
    
    // ================== LoginPage - Buttons ==================
    /** Login button on login page */
    public static final String LOGIN_BUTTON = "id:loginBtn";
    
    // ================== LoginPage - Messages ==================
    /** Login error message container */
    public static final String LOGIN_ERROR_MESSAGE = "id:loginError";
    
    // ================== EventPage - Form Inputs ==================
    /** Event name input field */
    public static final String EVENT_NAME_INPUT = "id:eventName";
    
    /** Event description textarea */
    public static final String EVENT_DESCRIPTION_INPUT = "id:eventDescription";
    
    /** Event date input field */
    public static final String EVENT_DATE_INPUT = "id:eventDate";
    
    /** Event location input field */
    public static final String EVENT_LOCATION_INPUT = "id:eventLocation";
    
    /** Event capacity input field (numeric) */
    public static final String EVENT_CAPACITY_INPUT = "id:eventCapacity";
    
    // ================== EventPage - Buttons ==================
    /** Form submit button */
    public static final String SUBMIT_BUTTON = "id:submitBtn";
    
    // ================== EventPage - Success Messages ==================
    /** Success message container */
    public static final String SUCCESS_MESSAGE = "id:successMessage";
    
    /** Success message text (within success message container) */
    public static final String SUCCESS_MESSAGE_TEXT = "id:successMessage-text";
    
    // ================== EventPage - Error Messages ==================
    /** Error message for event name field */
    public static final String FIELD_ERROR_NAME = "id:error-name";
    
    /** Error message for event description field */
    public static final String FIELD_ERROR_DESCRIPTION = "id:error-description";
    
    /** Error message for event date field */
    public static final String FIELD_ERROR_DATE = "id:error-date";
    
    /** Error message for event location field */
    public static final String FIELD_ERROR_LOCATION = "id:error-location";
    
    /** Error message for event capacity field */
    public static final String FIELD_ERROR_CAPACITY = "id:error-capacity";
    
    // ================== EventListPage - Table Elements ==================
    /** Events table container */
    public static final String EVENT_LIST_TABLE = "id:eventTable";
    
    /** Individual event row in table */
    public static final String EVENT_LIST_ROW = "class:event-row";
    
    /** Badge/indicator for newly created event */
    public static final String NEW_EVENT_BADGE = "class:new-event-badge";
    
    // ================== Page Load Indicators ==================
    /** Generic page loading spinner */
    public static final String PAGE_LOADING_SPINNER = "id:loadingSpinner";
    
    // Utility: prevent instantiation
    private SelectorConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
