package com.automation.hu05.constants;

/**
 * Centralized test data for event registration scenarios.
 * Organized in three categories:
 * 1. Valid data for positive scenarios
 * 2. Invalid data for negative scenarios
 * 3. Expected messages for assertions
 */
public class TestDataConstants {
    
    // ================== USER CREDENTIALS (Authentication) ==================
    /** Admin user email for login (dynamically generated with timestamp) */
    public static final String ADMIN_EMAIL = generateUniqueEmail();
    
    /** Admin user password for login */
    public static final String ADMIN_PASSWORD = "Admin@123456";
    
    /** Admin role ID (1 = admin) */
    public static final int ADMIN_ROLE = 1;
    
    /**
     * Generates a unique email address using current timestamp.
     * This ensures each test run creates a unique user (idempotent).
     * Format: admin_1234567890@test.com
     * 
     * @return Unique email with timestamp
     */
    private static String generateUniqueEmail() {
        long timestamp = System.currentTimeMillis();
        return "admin_" + timestamp + "@test.com";
    }
    
    // ================== VALID EVENT DATA (Positive Scenarios) ==================
    /** Valid event name for registration */
    public static final String VALID_EVENT_NAME = "Concert Night 2026";
    
    /** Valid event description */
    public static final String VALID_EVENT_DESCRIPTION = "Amazing live concert event experience with international artists";
    
    /** Valid event date (future date) */
    public static final String VALID_EVENT_DATE = "2026-06-15";
    
    /** Valid event location */
    public static final String VALID_EVENT_LOCATION = "Concert Hall A, Downtown";
    
    /** Valid event capacity */
    public static final int VALID_EVENT_CAPACITY = 500;
    
    // ================== INVALID EVENT DATA (Negative Scenarios) ==================
    /** Empty string for field validation */
    public static final String EMPTY_STRING = "";
    
    /** Negative number for capacity validation */
    public static final String INVALID_CAPACITY_NEGATIVE = "-100";
    
    /** Non-numeric string for capacity field */
    public static final String INVALID_CAPACITY_STRING = "abc123xyz";
    
    /** Past date for date validation */
    public static final String PAST_DATE = "2020-01-01";
    
    /** Very long name for boundary testing */
    public static final String LONG_NAME = generateLongString(300);
    
    /** Zero capacity */
    public static final int ZERO_CAPACITY = 0;
    
    // ================== EXPECTED MESSAGES (Test Assertions) ==================
    /** Message shown when event is created successfully */
    public static final String SUCCESS_MESSAGE_TEXT = "Evento registrado exitosamente";
    
    /** Generic required field error message */
    public static final String ERROR_REQUIRED_FIELD = "Campo requerido";
    
    /** Error message for invalid capacity value */
    public static final String ERROR_INVALID_CAPACITY = "La capacidad debe ser un número positivo";
    
    /** Error message for invalid date (past or current) */
    public static final String ERROR_INVALID_DATE = "La fecha debe ser en el futuro";
    
    /** Error message for duplicate event name */
    public static final String ERROR_DUPLICATE_EVENT = "Un evento con este nombre ya existe";
    
    // ================== HELPER METHODS ==================
    /**
     * Generates a string of specified length filled with 'x' characters.
     * Used for boundary testing.
     */
    private static String generateLongString(int length) {
        return "x".repeat(Math.max(0, length));
    }
    
    // Utility: prevent instantiation
    private TestDataConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
