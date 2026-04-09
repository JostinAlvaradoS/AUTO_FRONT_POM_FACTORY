package com.automation.hu05.constants;

public class TimeoutConstants {
    
    public static final int IMPLICIT_WAIT_SECONDS = 10;
    
    public static final int EXPLICIT_WAIT_SECONDS = 20;
    
    public static final int PAGE_LOAD_TIMEOUT_SECONDS = 30;
    
    public static final int SHORT_WAIT_SECONDS = 5;
    
    private TimeoutConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
