package com.automation.hu05.constants;

/**
 * Centralized timeout values for wait operations.
 * Hierarchy:
 * - IMPLICIT_WAIT: Basic element presence checks
 * - EXPLICIT_WAIT: Element visibility, clickability, text presence
 * - PAGE_LOAD_TIMEOUT: Full page load completion
 */
public class TimeoutConstants {
    
    /** Implicit wait time in seconds - used for basic element presence */
    public static final int IMPLICIT_WAIT_SECONDS = 10;
    
    /** Explicit wait time in seconds - used for visibility, clickability conditions */
    public static final int EXPLICIT_WAIT_SECONDS = 20;
    
    /** Page load timeout in seconds - maximum time to wait for page load */
    public static final int PAGE_LOAD_TIMEOUT_SECONDS = 30;
    
    /** Short wait for quick operations (animations, quick transitions) */
    public static final int SHORT_WAIT_SECONDS = 5;
    
    // Utility: prevent instantiation
    private TimeoutConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
