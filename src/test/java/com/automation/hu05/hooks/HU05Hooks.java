package com.automation.hu05.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import com.automation.hu05.constants.UrlConstants;
import com.automation.hu05.constants.TestDataConstants;
import org.json.JSONObject;

public class HU05Hooks {
    
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

    @Before(order = 0)
    public void storeScenario(io.cucumber.java.Scenario scenario) {
        CurrentScenario.set(scenario);
    }
    
    @Before(order = 2)
    public void setupUserAndLogin() {
        try {
            
            createTestUserViaAPI();
        } catch (Exception e) {
            System.err.println("[HU05] ✗ Failed to setup user and login: " + e.getMessage());
            
        }
    }
    
    private void createTestUserViaAPI() {
        try {
            
            JSONObject userPayload = new JSONObject();
            userPayload.put("email", TestDataConstants.ADMIN_EMAIL);
            userPayload.put("password", TestDataConstants.ADMIN_PASSWORD);
            userPayload.put("role", TestDataConstants.ADMIN_ROLE);
            
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
            
        }
    }
    
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
            
        }
    }
    
    public void createTestEventViaAPI() {
        try {
            
            JSONObject eventPayload = buildEventPayload(
                TestDataConstants.VALID_EVENT_NAME,
                TestDataConstants.VALID_EVENT_DESCRIPTION,
                TestDataConstants.VALID_EVENT_DATE,
                TestDataConstants.VALID_EVENT_LOCATION,
                String.valueOf(TestDataConstants.VALID_EVENT_CAPACITY)
            );
            
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
                
                ScenarioContext.setEventId(eventId);
            } else {
                System.err.println("[HU05] ✗ Failed to create test event - Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("[HU05] ✗ API error during event creation: " + e.getMessage());
            
        }
    }
    
    @After(order = 1)
    public void teardownTestData() {
        try {
            deleteTestEventViaAPI();
        } catch (Exception e) {
            System.err.println("[HU05] ✗ Error during test data cleanup: " + e.getMessage());
            
        } finally {
            
            HU05Hooks.ScenarioContext.clear();
            CurrentScenario.clear();
        }
    }
    
    @After(order = 2)
    public void teardownDriver() {
        try {
            WebDriverManager.closeDriver();
            System.out.println("[HU05] ✓ WebDriver closed successfully");
        } catch (Exception e) {
            System.err.println("[HU05] ✗ Error closing WebDriver: " + e.getMessage());
            
        }
    }
    
    private void deleteTestEventViaAPI() {
        try {
            String eventId = ScenarioContext.getEventId();
            
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
            
        }
    }
    
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
