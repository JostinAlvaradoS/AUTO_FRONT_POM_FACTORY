package com.automation.hu05.pages;

import com.automation.hu05.constants.SelectorConstants;
import com.automation.hu05.constants.UrlConstants;
import org.openqa.selenium.WebDriver;

public class WaitlistPage extends BasePage {

    public WaitlistPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToEventPage(String eventId) {
        driver.navigate().to(UrlConstants.EVENT_DETAIL_PAGE + eventId);
    }

    public boolean isWaitlistButtonVisible() {
        return isElementVisible(SelectorConstants.WAITLIST_JOIN_BUTTON);
    }

    public void clickJoinWaitlist() {
        click(SelectorConstants.WAITLIST_JOIN_BUTTON);
    }

    public void enterEmail(String email) {
        fillTextField(SelectorConstants.WAITLIST_EMAIL_INPUT, email);
    }

    public void submitWaitlistForm() {
        click(SelectorConstants.WAITLIST_SUBMIT_BUTTON);
    }

    public void joinWaitlist(String email) {
        clickJoinWaitlist();
        enterEmail(email);
        submitWaitlistForm();
    }

    public boolean isSuccessMessageVisible() {
        return isElementVisible(SelectorConstants.WAITLIST_SUCCESS_MSG);
    }

    public String getQueuePosition() {
        if (isElementPresent(SelectorConstants.WAITLIST_POSITION_TEXT)) {
            return getText(SelectorConstants.WAITLIST_POSITION_TEXT);
        }
        return null;
    }

    public boolean isErrorMessageVisible() {
        return isElementVisible(SelectorConstants.WAITLIST_ERROR_MSG);
    }

    public String getErrorMessage() {
        if (isElementPresent(SelectorConstants.WAITLIST_ERROR_MSG)) {
            return getText(SelectorConstants.WAITLIST_ERROR_MSG);
        }
        return "";
    }
}
