package com.automation.hu05.step_definitions;

import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import com.automation.hu05.hooks.WebDriverManager;
import com.automation.hu05.pages.EventPage;
import com.automation.hu05.pages.EventListPage;
import com.automation.hu05.pages.LoginPage;
import com.automation.hu05.factories.PageObjectFactory;
import com.automation.hu05.constants.TestDataConstants;
import com.automation.hu05.constants.UrlConstants;
import static org.junit.jupiter.api.Assertions.*;

public class CatalogAdminSteps {
    
    private EventPage eventPage;
    private EventListPage eventListPage;
    private LoginPage loginPage;
    private PageObjectFactory pageObjectFactory;
    
    public CatalogAdminSteps() {
        this.pageObjectFactory = WebDriverManager.getPageObjectFactory();
        this.eventPage = pageObjectFactory.getEventPage();
        this.eventListPage = pageObjectFactory.getEventListPage();
        this.loginPage = pageObjectFactory.getLoginPage();
    }

    @Y("no tengo permisos de administrador")
    public void noAdminPermissions() {
        
    }

    @Cuando("intento acceder al módulo de gestión de eventos")
    public void attemptAccessEventManagement() {
        eventListPage.navigateToEventList();
    }

    @Entonces("debería visualizar un mensaje indicando que el precio debe ser mayor a cero")
    public void verifyPriceErrorMessage() {
        assertTrue(eventPage.getAllErrorMessages().stream().anyMatch(m -> m.toLowerCase().contains("precio") || m.contains("negativo")),
                  "Price error message not found");
    }

    @Entonces("debería visualizar un mensaje de acceso denegado")
    public void verifyAccessDenied() {
        assertTrue(loginPage.isUnauthorizedRoleErrorDisplayed() || loginPage.getCurrentUrl().contains("/login"),
                  "Access denied message or redirect not detected");
    }
}
