package com.automation.hu05.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/waitlist.feature",
        glue     = { "com.automation.hu05.step_definitions", "com.automation.hu05.hooks" },
        tags     = "@ListaEspera",
        plugin   = { "pretty", "html:target/cucumber-reports/waitlist-report.html" },
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class RunWaitlistTests {
}
