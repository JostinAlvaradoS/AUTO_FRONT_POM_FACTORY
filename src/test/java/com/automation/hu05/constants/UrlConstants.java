package com.automation.hu05.constants;

/**
 * Centralized URLs for application navigation.
 * No hardcoded URLs should exist in Page Objects or Step Definitions.
 */
public class UrlConstants {
    
    /** Base URL for the application */
    public static final String BASE_URL = "http://localhost:3000";
    
    /** User service base URL (separate microservice for user management) */
    public static final String USER_SERVICE_URL = "http://localhost:50000";
    
    /** User creation endpoint */
    public static final String CREATE_USER_ENDPOINT = USER_SERVICE_URL + "/users";
    
    /** Admin login page */
    public static final String ADMIN_LOGIN_PAGE = BASE_URL + "/admin/login";
    
    /** Event registration/creation page */
    public static final String EVENT_REGISTRATION_PAGE = BASE_URL + "/admin/events/create";
    
    /** Event list/management page */
    public static final String EVENT_LIST_PAGE = BASE_URL + "/admin/events";

    /** Public event detail page (se concatena /{eventId}) */
    public static final String EVENT_DETAIL_PAGE = BASE_URL + "/events/";

    /** Waitlist API base URL */
    public static final String WAITLIST_API_URL = "http://localhost:5006";

    /** Waitlist has-pending endpoint */
    public static final String WAITLIST_HAS_PENDING = WAITLIST_API_URL + "/api/v1/waitlist/has-pending";
    
    // Utility: prevent instantiation
    private UrlConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
