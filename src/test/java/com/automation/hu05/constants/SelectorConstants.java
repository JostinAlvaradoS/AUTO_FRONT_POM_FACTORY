package com.automation.hu05.constants;

/**
 * Centralized collection of all UI locators/selectors for the automation framework.
 * Format: "type:value" where type = id, css, xpath, name, class
 * No hardcoded selectors should exist outside this class.
 */
public class SelectorConstants {
    
    // ================== LoginPage - Form Inputs ==================
    /** Email input field on login page */
    public static final String LOGIN_EMAIL_INPUT = "css:#email";
    
    /** Password input field on login page */
    public static final String LOGIN_PASSWORD_INPUT = "css:#password";

    /** Password visibility toggle */
    public static final String LOGIN_PASSWORD_TOGGLE = "css:#password ~ button";
    
    // ================== LoginPage - Buttons ==================
    /** Login button on login page */
    public static final String LOGIN_BUTTON = "xpath://button[contains(text(), 'Iniciar Sesión')]";
    
    /** Auto-fill button for quick testing */
    public static final String LOGIN_AUTOFILL_BUTTON = "xpath://button[contains(text(), 'Auto-completar formulario')]";
    
    // ================== LoginPage - Messages ==================
    /** Unauthorized role alert */
    public static final String UNAUTHORIZED_ALERT = "css:div[data-slot='alert'].text-destructive";

    /** Unauthorized role specific text message */
    public static final String UNAUTHORIZED_ROLE_MESSAGE = "xpath://div[contains(text(), 'Acceso no autorizado. Se requiere rol de administrador.')]";

    /** Success/Info alert for default credentials */
    public static final String LOGIN_INFO_ALERT = "css:div[data-slot='alert']:not(.text-destructive)";

    // ================== Admin Layout / Sidebar ==================
    /** Sidebar events navigation link */
    public static final String SIDEBAR_EVENTS_LINK = "css:a[href='/admin/events']";

    /** Logout button in sidebar */
    public static final String SIDEBAR_LOGOUT_BUTTON = "css:button[title='Cerrar sesión']";

    // ================== EventPage (Create) - Form Inputs ==================
    /** Event name input field */
    public static final String EVENT_NAME_INPUT = "css:#name";
    
    /** Event description textarea */
    public static final String EVENT_DESCRIPTION_INPUT = "css:#description";
    
    /** Event date input field */
    public static final String EVENT_DATE_INPUT = "css:#eventDate";
    
    /** Event venue input field */
    public static final String EVENT_LOCATION_INPUT = "css:#venue";
    
    /** Event capacity input field (numeric) */
    public static final String EVENT_CAPACITY_INPUT = "css:#maxCapacity";

    /** Base price input field */
    public static final String EVENT_PRICE_INPUT = "css:#basePrice";

    /** Image URL input field */
    public static final String EVENT_IMAGE_URL_INPUT = "css:#imageUrl";

    /** Tags input field */
    public static final String EVENT_TAGS_INPUT = "css:#tags";

    /** Is Active checkbox toggle button */
    public static final String EVENT_ACTIVE_CHECKBOX = "css:button[role='checkbox']";

    /** Is Active checked state locator */
    public static final String EVENT_ACTIVE_CHECKED = "css:button[role='checkbox'][data-state='checked']";
    
    // ================== EventPage - Buttons ==================
    /** Form submit button */
    public static final String SUBMIT_BUTTON = "xpath://button[contains(text(), 'Crear Evento')]";

    /** Form cancel button */
    public static final String CANCEL_BUTTON = "xpath://button[contains(text(), 'Cancelar')]";
    
    // ================== EventPage - Toast / Success Messages ==================
    /** Success toast container */
    public static final String TOAST_CONTAINER = "css:ol[aria-label='Notifications (F8)']";

    /** Toast notification title */
    public static final String TOAST_TITLE = "xpath://div[contains(text(), 'Éxito')]";

    /** Toast notification description */
    public static final String TOAST_DESCRIPTION = "xpath://div[contains(text(), 'Evento creado correctamente.')]";

    /** Success toast full item locator */
    public static final String TOAST_SUCCESS_ITEM = "xpath://li[contains(., 'Evento creado correctamente.')]";
    
    // ================== EventPage - Error Messages ==================
    /** Generic error message selector for fields */
    public static final String FIELD_ERROR_GENERIC = "css:p.mt-1.text-sm.text-destructive";

    /** Selector for fields with validation errors */
    public static final String INVALID_FIELDS = "css:[aria-invalid='true']";
    
    // ================== EventListPage - Table Elements ==================
    /** Create Event button on list page */
    public static final String CREATE_EVENT_BUTTON_LIST = "xpath://button[contains(text(), '+ Crear Evento')]";

    /** Events table container */
    public static final String EVENT_LIST_TABLE = "css:table[data-slot='table']";
    
    /** Individual event row in table */
    public static final String EVENT_LIST_ROW = "css:tr[data-slot='table-row']";

    /** Edit button in a row */
    public static final String ROW_EDIT_BUTTON = "xpath://button[contains(text(), 'Editar')]";

    /** Generate seats button in a row */
    public static final String ROW_GENERATE_SEATS_BUTTON = "xpath://button[contains(text(), 'Generar Asientos')]";

    /** Active badge in a row */
    public static final String BADGE_ACTIVE = "xpath://span[@data-slot='badge' and contains(text(), 'Activo')]";

    /** Total events summary text */
    public static final String TOTAL_EVENTS_SUMMARY = "css:div.flex.justify-center > div.bg-white";
    
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
