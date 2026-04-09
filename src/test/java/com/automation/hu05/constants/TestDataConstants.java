package com.automation.hu05.constants;

public class TestDataConstants {
    
    public static final String ADMIN_EMAIL = generateUniqueEmail();
    
    public static final String ADMIN_PASSWORD = "Admin@123456";
    
    public static final int ADMIN_ROLE = 1;
    
    private static String generateUniqueEmail() {
        long timestamp = System.currentTimeMillis();
        return "admin_" + timestamp + "@test.com";
    }
    
    public static final String VALID_EVENT_NAME = "Concert Night 2026";
    
    public static final String VALID_EVENT_DESCRIPTION = "Amazing live concert event experience with international artists";
    
    public static final String VALID_EVENT_DATE = "2026-06-15";
    
    public static final String VALID_EVENT_LOCATION = "Concert Hall A, Downtown";
    
    public static final int VALID_EVENT_CAPACITY = 500;
    
    public static final String EMPTY_STRING = "";
    
    public static final String INVALID_CAPACITY_NEGATIVE = "-100";
    
    public static final String INVALID_CAPACITY_STRING = "abc123xyz";
    
    public static final String PAST_DATE = "2020-01-01";
    
    public static final String LONG_NAME = generateLongString(300);
    
    public static final int ZERO_CAPACITY = 0;
    
    public static final String SUCCESS_MESSAGE_TEXT = "Evento registrado exitosamente";
    
    public static final String ERROR_REQUIRED_FIELD = "Campo requerido";
    
    public static final String ERROR_INVALID_CAPACITY = "La capacidad debe ser un número positivo";
    
    public static final String ERROR_INVALID_DATE = "La fecha debe ser en el futuro";
    
    public static final String ERROR_DUPLICATE_EVENT = "Un evento con este nombre ya existe";
    
    public static final String WAITLIST_EMAIL        = "jostin@example.com";

    public static final String WAITLIST_SECOND_EMAIL = "segundo@example.com";

    public static final String WAITLIST_EVENT_NAME   = "Concierto Rock 2026";

    public static final String WAITLIST_SUCCESS_TEXT = "You're on the list!";

    private static String generateLongString(int length) {
        return "x".repeat(Math.max(0, length));
    }
    
    private TestDataConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
