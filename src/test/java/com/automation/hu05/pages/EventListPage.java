package com.automation.hu05.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;
import com.automation.hu05.constants.SelectorConstants;
import com.automation.hu05.constants.UrlConstants;

/**
 * Page Object for Event List page.
 * Encapsulates all interactions with the event list/confirmation page after event creation.
 * 
 * Responsibilities:
 * - Navigation to event list page
 * - Verifying events are displayed in the list
 * - Validating event details in the list
 * - Checking for badges and status indicators
 */
public class EventListPage extends BasePage {
    
    // ================== List Elements ==================
    
    @FindBy(id = "eventListTable")
    private WebElement eventListTable;
    
    @FindBy(css = "table tbody tr")
    private List<WebElement> eventListRows;
    
    @FindBy(css = ".badge-new")
    private WebElement newEventBadge;
    
    /**
     * Constructor initializes WebDriver and initializes all @FindBy elements.
     * 
     * @param driver WebDriver instance
     */
    public EventListPage(WebDriver driver) {
        super(driver);
    }
    
    // ================== NAVIGATION METHODS ==================
    
    /**
     * Navigates to the event list page and waits for page to load.
     */
    public void navigateToEventList() {
        driver.navigate().to(UrlConstants.EVENT_LIST_PAGE);
        implicitlyWaitForPageLoad();
    }
    
    /**
     * Waits for the page to be fully loaded by checking for presence of table element.
     */
    private void implicitlyWaitForPageLoad() {
        findElement(SelectorConstants.EVENT_LIST_TABLE);
    }
    
    // ================== EVENT VERIFICATION METHODS ==================
    
    /**
     * Verifies that a specific event is displayed in the list by event name.
     * 
     * @param eventName Name of the event to search for
     * @return true if event exists in list, false otherwise
     */
    public boolean isEventInListByName(String eventName) {
        try {
            for (WebElement row : eventListRows) {
                String rowText = row.getText();
                if (rowText.contains(eventName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Verifies that an event is displayed with complete details.
     * Checks for name, location, and date in the same row.
     * 
     * @param eventName Name of event to verify
     * @param location Location of event
     * @param datePattern Date pattern to match (partial match allowed)
     * @return true if event with all details found, false otherwise
     */
    public boolean isEventDisplayedWithDetails(String eventName, String location, String datePattern) {
        try {
            for (WebElement row : eventListRows) {
                String rowText = row.getText();
                if (rowText.contains(eventName) && 
                    rowText.contains(location) && 
                    rowText.contains(datePattern)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Gets the total count of events displayed in the list.
     * 
     * @return Number of event rows in the table
     */
    public int getEventRowCount() {
        try {
            return eventListRows.size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Verifies that the "New Event" badge is displayed.
     * Badge indicates a recently created event.
     * 
     * @return true if new event badge is visible, false otherwise
     */
    public boolean hasNewEventBadge() {
        return isElementVisible(SelectorConstants.NEW_EVENT_BADGE);
    }
    
    /**
     * Gets the text content of the new event badge.
     * 
     * @return Badge text (typically "NEW" or "NUEVO")
     */
    public String getNewEventBadgeText() {
        return getText(SelectorConstants.NEW_EVENT_BADGE);
    }
    
    /**
     * Verifies that the event list table is visible and loaded.
     * 
     * @return true if table is visible, false otherwise
     */
    public boolean isEventListTableLoaded() {
        return isElementVisible(SelectorConstants.EVENT_LIST_TABLE);
    }
    
    /**
     * Gets the text from a specific cell in the event list.
     * Useful for extracting specific data from event rows.
     * 
     * @param rowIndex Index of the row (0-based)
     * @param columnIndex Index of the column (0-based)
     * @return Cell text, or empty string if cell not found
     */
    public String getEventTableCellText(int rowIndex, int columnIndex) {
        try {
            if (rowIndex >= eventListRows.size()) {
                return "";
            }
            WebElement row = eventListRows.get(rowIndex);
            List<WebElement> cells = row.findElements(org.openqa.selenium.By.tagName("td"));
            if (columnIndex >= cells.size()) {
                return "";
            }
            return cells.get(columnIndex).getText();
        } catch (Exception e) {
            return "";
        }
    }
}
