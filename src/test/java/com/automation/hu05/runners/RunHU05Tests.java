package com.automation.hu05.runners;

import io.cucumber.junit.platform.engine.Cucumber;
import org.junit.platform.suite.api.*;

/**
 * Test Runner for HU-05 Event Automation Tests.
 * 
 * Cucumber Configuration:
 * - Features: Loads all .feature files from features/ directory
 * - Glue code: Step definitions from com.automation.hu05.step_definitions
 * - Options:
 *   - publish=true: Publishes test results to Cucumber Reports
 *   - snippets=camelcase: Auto-generates steps in camelCase
 *   - tags: Filters scenarios by @tags for selective test runs
 * 
 * Execution:
 * - Run via: gradle test --tests RunHU05Tests
 * - IntelliJ: Right-click > Run 'RunHU05Tests'
 * - All scenarios in hu05_event_registration.feature will execute
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = "cucumber.glue", value = "com.automation.hu05.step_definitions,com.automation.hu05.hooks")
@ConfigurationParameter(key = "cucumber.features", value = "classpath:features/catalog-admin.feature")
@ConfigurationParameter(key = "cucumber.plugin", value = "pretty, html:target/cucumber-reports/catalog-admin-report.html")
@ConfigurationParameter(key = "cucumber.publish", value = "true")
@ConfigurationParameter(key = "cucumber.publish.quiet", value = "true")
@ConfigurationParameter(key = "cucumber.snippets", value = "camelcase")
public class RunHU05Tests {
    
    /**
     * Cucumber JUnit Platform Engine will automatically:
     * 1. Discover .feature files from resources/features/
     * 2. Parse Scenario steps (Dado/Y/Cuando/Entonces)
     * 3. Match steps to methods in StepDefinitions classes
     * 4. Execute hooks (@Before/@After)
     * 5. Generate HTML reports in target/cucumber-reports/
     * 
     * Test Execution Flow:
     * ┌─────────────────────────────────────────────────────┐
     * │ @Before hook                                        │
     * │ ├── Initialize Chrome driver                        │
     * │ ├── Set implicit waits                              │
     * │ ├── Create PageObjectFactory                        │
     * │ └── Create test event via API (if required)         │
     * ├─────────────────────────────────────────────────────┤
     * │ Scenario Execution                                  │
     * │ ├── Each Given step navigates or sets up state      │
     * │ ├── Each And step performs action or validation     │
     * │ ├── Each When step executes user action             │
     * │ └── Each Then step validates expected outcome       │
     * ├─────────────────────────────────────────────────────┤
     * │ @After hook                                         │
     * │ ├── Clean up test event via API DELETE              │
     * │ ├── driver.quit()                                   │
     * │ └── Release resources                               │
     * └─────────────────────────────────────────────────────┘
     */
}
