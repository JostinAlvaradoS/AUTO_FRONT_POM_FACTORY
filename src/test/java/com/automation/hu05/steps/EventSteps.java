package com.automation.hu05.steps;

import com.automation.hu05.pages.EventPage;
import com.automation.hu05.pages.EventListPage;
import com.automation.hu05.factories.PageObjectFactory;
import com.automation.hu05.hooks.WebDriverManager;
import com.automation.hu05.constants.TestDataConstants;

public class EventSteps {

    private final PageObjectFactory factory;
    private final EventPage eventPage;
    private final EventListPage eventListPage;

    public EventSteps() {
        this.factory = WebDriverManager.getPageObjectFactory();
        this.eventPage = factory.getEventPage();
        this.eventListPage = factory.getEventListPage();
    }

    public void createEvent(String name, String description, String date, String location, String capacity, String basePrice) {
        eventListPage.clickCreateNewEvent();
        eventPage.enterEventName(name);
        eventPage.enterEventDescription(description);
        eventPage.enterEventDate(date);
        eventPage.enterEventLocation(location);
        eventPage.enterEventCapacity(capacity);
        eventPage.enterBasePrice(basePrice);
        eventPage.clickSubmitButton();
    }

    public void createEventWithoutName(String description, String date, String location, String capacity, String basePrice) {
        eventListPage.clickCreateNewEvent();
        eventPage.enterEventDescription(description);
        eventPage.enterEventDate(date);
        eventPage.enterEventLocation(location);
        eventPage.enterEventCapacity(capacity);
        eventPage.enterBasePrice(basePrice);
        eventPage.clickSubmitButton();
    }

    public void shouldSeeSuccessToast() {
        if (!eventPage.isSuccessToastDisplayed()) {
            throw new AssertionError("Success toast not displayed");
        }
    }

    public void shouldSeeEventInList(String name) {
        if (!eventListPage.isEventInListByName(name)) {
            throw new AssertionError("Event not found in list: " + name);
        }
    }

    public void shouldSeeNameRequiredError() {
        boolean found = eventPage.getAllErrorMessages().stream().anyMatch(m -> {
            String low = m.toLowerCase();
            return low.contains("nombre") || low.contains("campo requerido") || low.contains("campo obligatorio");
        });
        if (!found) {
            throw new AssertionError("Name required error message not found. Errors: " + eventPage.getAllErrorMessages());
        }
    }
}
