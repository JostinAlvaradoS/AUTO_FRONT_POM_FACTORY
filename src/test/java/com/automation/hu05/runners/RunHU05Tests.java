package com.automation.hu05.runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    features = "src/test/resources/features/catalog-admin.feature",
    glue = { "com.automation.hu05.step_definitions", "com.automation.hu05.hooks" },
    plugin = { "pretty", "html:target/cucumber-reports/catalog-admin-report.html" },
    snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class RunHU05Tests {

}
