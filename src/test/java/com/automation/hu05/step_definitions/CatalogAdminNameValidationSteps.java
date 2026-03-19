package com.automation.hu05.step_definitions;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import com.automation.hu05.hooks.WebDriverManager;
import com.automation.hu05.pages.EventPage;
import com.automation.hu05.pages.EventListPage;
import com.automation.hu05.factories.PageObjectFactory;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Steps specific to the name-required validation scenario.
 */
public class CatalogAdminNameValidationSteps {

    private EventPage eventPage;
    private EventListPage eventListPage;
    private PageObjectFactory pageObjectFactory;

    public CatalogAdminNameValidationSteps() {
        this.pageObjectFactory = WebDriverManager.getPageObjectFactory();
        this.eventPage = pageObjectFactory.getEventPage();
        this.eventListPage = pageObjectFactory.getEventListPage();
    }

    @Cuando("intento registrar un evento sin nombre")
    public void registerWithoutName() {
        eventListPage.clickCreateNewEvent();
        // Fill all fields except the name to simulate missing-name submission
        eventPage.enterEventDescription(com.automation.hu05.constants.TestDataConstants.VALID_EVENT_DESCRIPTION);
        eventPage.enterEventDate(com.automation.hu05.constants.TestDataConstants.VALID_EVENT_DATE);
        eventPage.enterEventLocation(com.automation.hu05.constants.TestDataConstants.VALID_EVENT_LOCATION);
        eventPage.enterEventCapacity(String.valueOf(com.automation.hu05.constants.TestDataConstants.VALID_EVENT_CAPACITY));
        eventPage.enterBasePrice("50");
        eventPage.clickSubmitButton();
    }

    @Entonces("debería visualizar un mensaje indicando que el nombre es obligatorio")
    public void verifyNameRequiredMessage() {
        // Accept either a field-specific message mentioning 'nombre' or a generic 'Campo requerido'
        boolean found = eventPage.getAllErrorMessages().stream().anyMatch(m -> {
            String low = m.toLowerCase();
            return low.contains("nombre") || low.contains("campo requerido") || low.contains("campo obligatorio");
        });
        assertTrue(found, "Name required error message not found. Errors: " + eventPage.getAllErrorMessages());
    }
}
