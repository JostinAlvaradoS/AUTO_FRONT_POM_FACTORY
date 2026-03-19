package com.automation.hu05.step_definitions;

import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Y;
import com.automation.hu05.hooks.WebDriverManager;
import com.automation.hu05.pages.EventListPage;
import com.automation.hu05.pages.LoginPage;
import com.automation.hu05.factories.PageObjectFactory;
import com.automation.hu05.constants.TestDataConstants;

/**
 * Common shared steps for Catalog Admin feature (authentication and navigation).
 */
public class CatalogAdminCommonSteps {

    private LoginPage loginPage;
    private EventListPage eventListPage;
    private PageObjectFactory pageObjectFactory;

    public CatalogAdminCommonSteps() {
        this.pageObjectFactory = WebDriverManager.getPageObjectFactory();
        this.loginPage = pageObjectFactory.getLoginPage();
        this.eventListPage = pageObjectFactory.getEventListPage();
    }

    @Dado("que soy un administrador autenticado")
    public void authenticatedAdmin() {
        loginPage.navigateToLoginPage();
        loginPage.performLogin(TestDataConstants.ADMIN_EMAIL, TestDataConstants.ADMIN_PASSWORD);
    }

    @Y("me encuentro en el módulo de gestión de eventos")
    public void inEventManagementModule() {
        eventListPage.navigateToEventList();
    }
}
