package com.automation.hu05.step_definitions;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import com.automation.hu05.hooks.WebDriverManager;
import com.automation.hu05.pages.EventPage;
import com.automation.hu05.pages.EventListPage;
import com.automation.hu05.factories.PageObjectFactory;
import com.automation.hu05.constants.TestDataConstants;
import com.automation.hu05.steps.EventSteps;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Steps specific to the successful event registration scenario.
 */
public class CatalogAdminRegisterSuccessSteps {

    private EventPage eventPage;
    private EventListPage eventListPage;
    private PageObjectFactory pageObjectFactory;
    private EventSteps eventSteps;

    public CatalogAdminRegisterSuccessSteps() {
        this.pageObjectFactory = WebDriverManager.getPageObjectFactory();
        this.eventPage = pageObjectFactory.getEventPage();
        this.eventListPage = pageObjectFactory.getEventListPage();
        this.eventSteps = new EventSteps();
    }

    @Cuando("registro un nuevo evento con información válida")
    public void registerValidEvent() {
        eventSteps.createEvent(TestDataConstants.VALID_EVENT_NAME,
                               TestDataConstants.VALID_EVENT_DESCRIPTION,
                               TestDataConstants.VALID_EVENT_DATE,
                               TestDataConstants.VALID_EVENT_LOCATION,
                               String.valueOf(TestDataConstants.VALID_EVENT_CAPACITY),
                               "50");
    }

    @Entonces("debería visualizar un mensaje de confirmación de registro exitoso")
    public void verifyConfirmationMessage() {
        eventSteps.shouldSeeSuccessToast();
        String description = eventPage.getToastDescriptionText();
        assertTrue(description.contains("correctamente"), "Unexpected toast description: " + description);
    }

    @Y("el evento debería aparecer en el listado de eventos")
    public void eventShouldAppearInList() {
        eventSteps.shouldSeeEventInList(TestDataConstants.VALID_EVENT_NAME);
    }
}
