package com.automation.hu05.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import com.automation.hu05.constants.UrlConstants;
import com.automation.hu05.constants.TestDataConstants;
import org.json.JSONObject;

/**
 * Cucumber Hooks for HU-05 Test Scenarios.
 * 
 * Hooks are special Cucumber methods that execute before/after scenarios.
 * They are used for:
 * - Setup: Initialize WebDriver, create test data via API
 * - Teardown: Clean up resources, delete test data
 * 
 * Hook Execution Order:
 * ┌──────────────────────────────────────────┐
 * │ Scenario Start                           │
 * ├──────────────────────────────────────────┤
 * │ @Before (setup)                          │
 * │ ├── Initialize WebDriver                 │
 * │ ├── Set implicit/explicit waits          │
 * │ ├── Create PageObjectFactory             │
 * │ └── Create test event via API POST       │
 * ├──────────────────────────────────────────┤
 * │ Scenario Steps (feature file)            │
 * ├──────────────────────────────────────────┤
 * │ @After (teardown)                        │
 * │ ├── Delete test event via API DELETE     │
 * │ ├── Close WebDriver                      │
 * │ └── Clean up resources                   │
 * ├──────────────────────────────────────────┤
 * │ Scenario Complete                        │
 * └──────────────────────────────────────────┘
 */
public class HU05Hooks {
    
    /**
     * Cucumber @Before hook - Executes before each scenario.
     * 
     * Responsibilities:
     * 1. Initialize Chrome WebDriver via WebDriverManager
     * 2. Configure timeouts and browser window
     * 3. Create PageObjectFactory for scenario
     * 4. Create test event via API (if needed) - for test data setup
     * 
     * Exception Handling:
     * - Driver initialization failures will stop the test
     * - API failures are logged but don't stop the test (non-critical setup)
     */
    @Before(order = 1)
    public void setupWebDriver() {
        try {
            WebDriverManager.initialiseDriver();
            System.out.println("[HU05] ✓ WebDriver initialized successfully");
        } catch (Exception e) {
            System.err.println("[HU05] ✗ Failed to initialize WebDriver: " + e.getMessage());
            throw new RuntimeException("WebDriver initialization failed", e);
        }
    }

    /**
     * Store current Cucumber Scenario in a ThreadLocal so step definitions can
     * access it to attach screenshots or other evidence.
     */
    @Before(order = 0)
    public void storeScenario(io.cucumber.java.Scenario scenario) {
        CurrentScenario.set(scenario);
    }
    
    /**
     * Login step: Creates a test user via API and logs in via UI.
     * This happens after WebDriver initialization.
     */
    @Before(order = 2)
    public void setupUserAndLogin() {
        try {
            // Create test user via API. UI login is handled explicitly in step definitions
            createTestUserViaAPI();
        } catch (Exception e) {
            System.err.println("[HU05] ✗ Failed to setup user and login: " + e.getMessage());
            // Don't throw - allow test to continue if login setup fails
        }
    }
    
    /**
     * Creates a test user via API POST request.
     * 
     * API Endpoint: POST http://localhost:50000/users
     * Payload: User object with email, password, and role
     * 
     * Error Handling: Logs error but doesn't fail test (non-critical setup)
     */
    private void createTestUserViaAPI() {
        try {
            // Build user payload
            JSONObject userPayload = new JSONObject();
            userPayload.put("email", TestDataConstants.ADMIN_EMAIL);
            userPayload.put("password", TestDataConstants.ADMIN_PASSWORD);
            userPayload.put("role", TestDataConstants.ADMIN_ROLE);
            
            // Send POST request to create user in user service
            Response response = RestAssured
                .given()
                    .contentType("application/json")
                    .body(userPayload.toString())
                .when()
                    .post(UrlConstants.CREATE_USER_ENDPOINT)
                .then()
                    .extract().response();
            
            if (response.getStatusCode() == 201 || response.getStatusCode() == 200) {
                System.out.println("[HU05] ✓ Test user created via API - Email: " + TestDataConstants.ADMIN_EMAIL);
            } else {
                System.out.println("[HU05] ⓘ User already exists or creation failed - Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("[HU05] ⓘ API error during user creation: " + e.getMessage());
            // Don't throw - user might already exist
        }
    }
    
    /**
     * Performs login via UI with test user credentials.
     * Navigates to login page and submits credentials.
     */
    private void loginAsTestUser() {
        try {
            com.automation.hu05.pages.LoginPage loginPage = WebDriverManager
                .getPageObjectFactory()
                .getLoginPage();
            
            loginPage.navigateToLoginPage();
            System.out.println("[HU05] ✓ Navigated to login page");
            
            loginPage.performLogin(TestDataConstants.ADMIN_EMAIL, TestDataConstants.ADMIN_PASSWORD);
            System.out.println("[HU05] ✓ User logged in successfully - Email: " + TestDataConstants.ADMIN_EMAIL);
        } catch (Exception e) {
            System.err.println("[HU05] ✗ Error during login: " + e.getMessage());
            // Don't throw - allow test to continue
        }
    }
    
    /**
     * Creates a test event via API POST request.
     * 
     * API Endpoint: POST /api/events
     * Payload: Event object with name, description, date, location, capacity
     * Response: Event ID needed for cleanup in @After hook
     * 
     * Error Handling: Logs error but doesn't fail test (soft assertion)
     */
    public void createTestEventViaAPI() {
        try {
            // Build event payload
            JSONObject eventPayload = buildEventPayload(
                TestDataConstants.VALID_EVENT_NAME,
                TestDataConstants.VALID_EVENT_DESCRIPTION,
                TestDataConstants.VALID_EVENT_DATE,
                TestDataConstants.VALID_EVENT_LOCATION,
                String.valueOf(TestDataConstants.VALID_EVENT_CAPACITY)
            );
            
            // Send POST request to create event
            Response response = RestAssured
                .given()
                    .contentType("application/json")
                    .body(eventPayload.toString())
                .when()
                    .post(UrlConstants.BASE_URL + "/api/events")
                .then()
                    .extract().response();
            
            if (response.getStatusCode() == 201 || response.getStatusCode() == 200) {
                String eventId = response.jsonPath().getString("id");
                System.out.println("[HU05] ✓ Test event created via API - Event ID: " + eventId);
                // Store eventId in scenario context for cleanup in @After
                ScenarioContext.setEventId(eventId);
            } else {
                System.err.println("[HU05] ✗ Failed to create test event - Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("[HU05] ✗ API error during event creation: " + e.getMessage());
            // Don't throw - this is non-critical setup
        }
    }
    
    /**
     * Cucumber @After hook - Executes after each scenario.
     * 
     * Responsibilities:
     * 1. Delete test event via API DELETE (cleanup test data)
     * 2. Close WebDriver and browser
     * 3. Clean up ThreadLocal resources
     * 
     * Exception Handling:
     * - API failures are logged but don't throw (cleanup must complete)
     * - WebDriver closure is guaranteed via try-finally
     */
    @After(order = 1)
    public void teardownTestData() {
        try {
            deleteTestEventViaAPI();
        } catch (Exception e) {
            System.err.println("[HU05] ✗ Error during test data cleanup: " + e.getMessage());
            // Continue with driver cleanup even if API cleanup fails
        } finally {
            // Always clean scenario context
            HU05Hooks.ScenarioContext.clear();
            CurrentScenario.clear();
        }
    }
    
    /**
     * Cucumber @After hook - Executes after each scenario.
     * Closes WebDriver last (order = 2, executes after other @After hooks).
     * 
     * This ensures:
     * 1. All logging/cleanup happens while driver is still open
     * 2. Driver is closed last as final cleanup step
     */
    @After(order = 2)
    public void teardownDriver() {
        try {
            WebDriverManager.closeDriver();
            System.out.println("[HU05] ✓ WebDriver closed successfully");
        } catch (Exception e) {
            System.err.println("[HU05] ✗ Error closing WebDriver: " + e.getMessage());
            // Continue - cleanup was attempted even if close failed
        }
    }
    
    /**
     * Deletes the test event via API DELETE request.
     * Called during @After hook for test data cleanup.
     * 
     * API Endpoint: DELETE /api/events/{eventId}
     * Response: 204 No Content on success
     * 
     * Error Handling: Logs error but doesn't fail (cleanup attempt made)
     */
    private void deleteTestEventViaAPI() {
        try {
            String eventId = ScenarioContext.getEventId();
            
            // Only attempt delete if event was created
            if (eventId != null && !eventId.isEmpty()) {
                Response response = RestAssured
                    .given()
                    .when()
                        .delete(UrlConstants.BASE_URL + "/api/events/" + eventId)
                    .then()
                        .extract().response();
                
                if (response.getStatusCode() == 204 || response.getStatusCode() == 200) {
                    System.out.println("[HU05] ✓ Test event deleted via API - Event ID: " + eventId);
                } else {
                    System.err.println("[HU05] ✗ Failed to delete test event - Status: " + response.getStatusCode());
                }
            }
        } catch (Exception e) {
            System.err.println("[HU05] ✗ API error during event deletion: " + e.getMessage());
            // Don't throw - this is cleanup, test already completed
        }
    }
    
    /**
     * Builds a JSON payload for creating an event.
     * Used by both setup (API POST) and step definitions.
     * 
     * @param name Event name
     * @param description Event description
     * @param date Event date (YYYY-MM-DD)
     * @param location Event location
     * @param capacity Event capacity as string
     * @return JSONObject with event data
     */
    public static JSONObject buildEventPayload(String name, String description, String date, 
                                               String location, String capacity) {
        JSONObject payload = new JSONObject();
        payload.put("name", name);
        payload.put("description", description);
        payload.put("date", date);
        payload.put("location", location);
        payload.put("capacity", Integer.parseInt(capacity));
        return payload;
    }
    
    /**
     * Simple Scenario Context to store test data between steps.
     * Used to pass eventId from @Before hook to @After hook.
     */
    public static class ScenarioContext {
        private static final ThreadLocal<String> eventId = new ThreadLocal<>();
        
        public static void setEventId(String id) {
            eventId.set(id);
        }
        
        public static String getEventId() {
            return eventId.get();
        }
        
        public static void clear() {
            eventId.remove();
        }
    }

    /**
     * Holds the current Cucumber Scenario for the running thread.
     */
    public static class CurrentScenario {
        private static final ThreadLocal<io.cucumber.java.Scenario> current = new ThreadLocal<>();

        public static void set(io.cucumber.java.Scenario scenario) {
            current.set(scenario);
        }

        public static io.cucumber.java.Scenario get() {
            return current.get();
        }

        public static void clear() {
            current.remove();
        }
    }
}
