package com.automation.hu05.pages;

import org.openqa.selenium.By;
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
    
    @FindBy(xpath = "//button[contains(text(), '+ Crear Evento')]")
    private WebElement createEventButton;

    @FindBy(css = "table[data-slot='table']")
    private WebElement eventListTable;
    
    @FindBy(css = "tr[data-slot='table-row']")
    private List<WebElement> eventListRows;
    
    @FindBy(css = "span[data-slot='badge']")
    private WebElement statusBadge;

    @FindBy(css = "div.flex.justify-center > div.bg-white")
    private WebElement totalEventsText;
    
    @FindBy(xpath = "//button[contains(text(), 'Editar')]")
    private List<WebElement> editButtons;

    @FindBy(xpath = "//button[contains(text(), 'Generar Asientos')]")
    private List<WebElement> generateSeatsButtons;

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
    
    /**
     * Clicks the "+ Crear Evento" button.
     */
    public void clickCreateNewEvent() {
        click(SelectorConstants.CREATE_EVENT_BUTTON_LIST);
    }

    /**
     * Clicks the Edit button for a specific event by name.
     * 
     * @param eventName Name of the event to edit
     */
    public void clickEditOnEvent(String eventName) {
        for (WebElement row : eventListRows) {
            if (row.getText().contains(eventName)) {
                WebElement editBtn = row.findElement(By.xpath(".//button[contains(text(), 'Editar')]"));
                editBtn.click();
                return;
            }
        }
    }

    // ================== EVENT VERIFICATION METHODS ==================
    
    /**
     * Gets the total count of events displayed in the summary text.
     * 
     * @return count as integer
     */
    public int getTotalEventsCount() {
        String text = getText(SelectorConstants.TOTAL_EVENTS_SUMMARY);
        // Extract number from "Total de eventos: X"
        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
    }
    
    /**
     * Verifies that a specific event is displayed in the list by event name.
     * 
     * @param eventName Name of the event to search for
     * @return true if event exists in list, false otherwise
     */
    public boolean isEventInListByName(String eventName) {
        try {
            for (WebElement row : eventListRows) {
                if (row.getText().contains(eventName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if an event is marked as "Activo" (badge verification).
     * 
     * @param eventName Name of the event
     * @return true if active badge found in that row
     */
    public boolean isEventStatusActive(String eventName) {
        for (WebElement row : eventListRows) {
            if (row.getText().contains(eventName)) {
                try {
                    WebElement badge = row.findElement(By.xpath(".//span[@data-slot='badge' and contains(text(), 'Activo')]"));
                    return badge.isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
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
