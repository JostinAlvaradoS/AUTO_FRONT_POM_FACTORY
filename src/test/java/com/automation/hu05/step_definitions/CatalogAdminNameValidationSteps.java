package com.automation.hu05.step_definitions;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import com.automation.hu05.hooks.WebDriverManager;
import com.automation.hu05.pages.EventPage;
import com.automation.hu05.pages.EventListPage;
import com.automation.hu05.factories.PageObjectFactory;
import com.automation.hu05.steps.EventSteps;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Steps specific to the name-required validation scenario.
 */
public class CatalogAdminNameValidationSteps {

    private EventPage eventPage;
    private EventListPage eventListPage;
    private PageObjectFactory pageObjectFactory;
    private EventSteps eventSteps;

    public CatalogAdminNameValidationSteps() {
        this.pageObjectFactory = WebDriverManager.getPageObjectFactory();
        this.eventPage = pageObjectFactory.getEventPage();
        this.eventListPage = pageObjectFactory.getEventListPage();
        this.eventSteps = new EventSteps();
    }

    @Cuando("intento registrar un evento sin nombre")
    public void registerWithoutName() {
        eventSteps.createEventWithoutName(
            com.automation.hu05.constants.TestDataConstants.VALID_EVENT_DESCRIPTION,
            com.automation.hu05.constants.TestDataConstants.VALID_EVENT_DATE,
            com.automation.hu05.constants.TestDataConstants.VALID_EVENT_LOCATION,
            String.valueOf(com.automation.hu05.constants.TestDataConstants.VALID_EVENT_CAPACITY),
            "50"
        );
    }

    @Entonces("debería visualizar un mensaje indicando que el nombre es obligatorio")
    public void verifyNameRequiredMessage() {
        eventSteps.shouldSeeNameRequiredError();
    }
}
