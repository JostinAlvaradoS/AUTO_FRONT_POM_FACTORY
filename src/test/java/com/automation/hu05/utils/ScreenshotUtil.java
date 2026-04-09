package com.automation.hu05.utils;

import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.automation.hu05.hooks.WebDriverManager;
import net.serenitybdd.core.Serenity;

public class ScreenshotUtil {

    private ScreenshotUtil() {}

    public static void attachScreenshot(Scenario scenario) {
        try {
            if (scenario == null) return;
            if (!WebDriverManager.isDriverInitialized()) return;
            byte[] screenshot = ((TakesScreenshot) WebDriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            
            scenario.attach(screenshot, "image/png", "screenshot-" + System.currentTimeMillis() + ".png");
            
        } catch (Exception e) {
            System.err.println("Could not capture screenshot: " + e.getMessage());
        }
    }
}
