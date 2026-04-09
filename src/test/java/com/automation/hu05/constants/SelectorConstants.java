package com.automation.hu05.constants;

public class SelectorConstants {
    
    public static final String LOGIN_EMAIL_INPUT = "css:#email";
    
    public static final String LOGIN_PASSWORD_INPUT = "css:#password";

    public static final String LOGIN_PASSWORD_TOGGLE = "css:#password ~ button";
    
    public static final String LOGIN_BUTTON = "xpath://button[contains(text(), 'Iniciar Sesión')]";
    
    public static final String LOGIN_AUTOFILL_BUTTON = "xpath://button[contains(text(), 'Auto-completar formulario')]";
    
    public static final String UNAUTHORIZED_ALERT = "css:div[data-slot='alert'].text-destructive";

    public static final String UNAUTHORIZED_ROLE_MESSAGE = "xpath://div[contains(text(), 'Acceso no autorizado. Se requiere rol de administrador.')]";

    public static final String LOGIN_INFO_ALERT = "css:div[data-slot='alert']:not(.text-destructive)";

    public static final String SIDEBAR_EVENTS_LINK = "css:a[href='/admin/events']";

    public static final String SIDEBAR_LOGOUT_BUTTON = "css:button[title='Cerrar sesión']";

    public static final String EVENT_NAME_INPUT = "css:#name";
    
    public static final String EVENT_DESCRIPTION_INPUT = "css:#description";
    
    public static final String EVENT_DATE_INPUT = "css:#eventDate";
    
    public static final String EVENT_LOCATION_INPUT = "css:#venue";
    
    public static final String EVENT_CAPACITY_INPUT = "css:#maxCapacity";

    public static final String EVENT_PRICE_INPUT = "css:#basePrice";

    public static final String EVENT_IMAGE_URL_INPUT = "css:#imageUrl";

    public static final String EVENT_TAGS_INPUT = "css:#tags";

    public static final String EVENT_ACTIVE_CHECKBOX = "css:button[role='checkbox']";

    public static final String EVENT_ACTIVE_CHECKED = "css:button[role='checkbox'][data-state='checked']";
    
    public static final String SUBMIT_BUTTON = "xpath://button[contains(text(), 'Crear Evento')]";

    public static final String CANCEL_BUTTON = "xpath://button[contains(text(), 'Cancelar')]";
    
    public static final String TOAST_CONTAINER = "css:ol[aria-label='Notifications (F8)']";

    public static final String TOAST_TITLE = "xpath://div[contains(text(), 'Éxito')]";

    public static final String TOAST_DESCRIPTION = "xpath://div[contains(text(), 'Evento creado correctamente.')]";

    public static final String TOAST_SUCCESS_ITEM = "xpath://li[contains(., 'Evento creado correctamente.')]";
    
    public static final String FIELD_ERROR_GENERIC = "css:p.mt-1.text-sm.text-destructive";

    public static final String INVALID_FIELDS = "css:[aria-invalid='true']";
    
    public static final String CREATE_EVENT_BUTTON_LIST = "xpath://button[contains(text(), '+ Crear Evento')]";

    public static final String EVENT_LIST_TABLE = "css:table[data-slot='table']";
    
    public static final String EVENT_LIST_ROW = "css:tr[data-slot='table-row']";

    public static final String ROW_EDIT_BUTTON = "xpath://button[contains(text(), 'Editar')]";

    public static final String ROW_GENERATE_SEATS_BUTTON = "xpath://button[contains(text(), 'Generar Asientos')]";

    public static final String BADGE_ACTIVE = "xpath://span[@data-slot='badge' and contains(text(), 'Activo')]";

    public static final String TOTAL_EVENTS_SUMMARY = "css:div.flex.justify-center > div.bg-white";
    
    public static final String NEW_EVENT_BADGE = "class:new-event-badge";
    
    public static final String PAGE_LOADING_SPINNER = "id:loadingSpinner";
    
    public static final String WAITLIST_JOIN_BUTTON    = "xpath://button[contains(text(), 'Join the Waitlist')]";

    public static final String WAITLIST_EMAIL_INPUT    = "css:#waitlist-email";

    public static final String WAITLIST_SUBMIT_BUTTON  = "xpath://button[normalize-space()='Join Waitlist']";

    public static final String WAITLIST_SUCCESS_MSG    = "xpath://*[contains(text(),\"You're on the list!\")]";

    public static final String WAITLIST_POSITION_TEXT  = "css:span.font-bold.text-accent.text-base";

    public static final String WAITLIST_ERROR_MSG      = "css:p.text-sm.text-destructive";

    private SelectorConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
