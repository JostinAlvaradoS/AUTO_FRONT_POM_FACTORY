package com.automation.hu05.constants;

public class UrlConstants {
    
    public static final String BASE_URL = "http://localhost:3000";
    
    public static final String USER_SERVICE_URL = "http://localhost:50000";
    
    public static final String CREATE_USER_ENDPOINT = USER_SERVICE_URL + "/users";
    
    public static final String ADMIN_LOGIN_PAGE = BASE_URL + "/admin/login";
    
    public static final String EVENT_REGISTRATION_PAGE = BASE_URL + "/admin/events/create";
    
    public static final String EVENT_LIST_PAGE = BASE_URL + "/admin/events";

    public static final String EVENT_DETAIL_PAGE = BASE_URL + "/events/";

    public static final String WAITLIST_API_URL = "http://localhost:5006";

    public static final String WAITLIST_HAS_PENDING = WAITLIST_API_URL + "/api/v1/waitlist/has-pending";
    
    private UrlConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
