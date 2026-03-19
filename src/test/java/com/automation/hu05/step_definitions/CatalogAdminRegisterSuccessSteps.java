package com.automation.hu05.step_definitions;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import com.automation.hu05.hooks.WebDriverManager;
import com.automation.hu05.pages.EventPage;
import com.automation.hu05.pages.EventListPage;
import com.automation.hu05.factories.PageObjectFactory;
import com.automation.hu05.constants.TestDataConstants;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Steps specific to the successful event registration scenario.
 */
public class CatalogAdminRegisterSuccessSteps {

    private EventPage eventPage;
    private EventListPage eventListPage;
    private PageObjectFactory pageObjectFactory;

    public CatalogAdminRegisterSuccessSteps() {
        this.pageObjectFactory = WebDriverManager.getPageObjectFactory();
        this.eventPage = pageObjectFactory.getEventPage();
        this.eventListPage = pageObjectFactory.getEventListPage();
    }

    @Cuando("registro un nuevo evento con información válida")
    public void registerValidEvent() {
        eventListPage.clickCreateNewEvent();
        eventPage.fillValidEventForm();
        eventPage.clickSubmitButton();
    }

    @Entonces("debería visualizar un mensaje de confirmación de registro exitoso")
    public void verifyConfirmationMessage() {
        assertTrue(eventPage.isSuccessToastDisplayed(), "Success toast not displayed");
        String description = eventPage.getToastDescriptionText();
        assertTrue(description.contains("correctamente"), "Unexpected toast description: " + description);
    }

    @Y("el evento debería aparecer en el listado de eventos")
    public void eventShouldAppearInList() {
        assertTrue(eventListPage.isEventInListByName(TestDataConstants.VALID_EVENT_NAME),
                  "Event not found in list");
    }
}
