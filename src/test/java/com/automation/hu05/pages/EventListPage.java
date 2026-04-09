package com.automation.hu05.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;
import com.automation.hu05.constants.SelectorConstants;
import com.automation.hu05.constants.UrlConstants;

public class EventListPage extends BasePage {
    
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

    public EventListPage(WebDriver driver) {
        super(driver);
    }
    
    public void navigateToEventList() {
        driver.navigate().to(UrlConstants.EVENT_LIST_PAGE);
        implicitlyWaitForPageLoad();
    }
    
    private void implicitlyWaitForPageLoad() {
        findElement(SelectorConstants.EVENT_LIST_TABLE);
    }
    
    public void clickCreateNewEvent() {
        click(SelectorConstants.CREATE_EVENT_BUTTON_LIST);
    }

    public void clickEditOnEvent(String eventName) {
        for (WebElement row : eventListRows) {
            if (row.getText().contains(eventName)) {
                WebElement editBtn = row.findElement(By.xpath(".//button[contains(text(), 'Editar')]"));
                editBtn.click();
                return;
            }
        }
    }

    public int getTotalEventsCount() {
        String text = getText(SelectorConstants.TOTAL_EVENTS_SUMMARY);
        
        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
    }
    
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
    
    public int getEventRowCount() {
        try {
            return eventListRows.size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    public boolean hasNewEventBadge() {
        return isElementVisible(SelectorConstants.NEW_EVENT_BADGE);
    }
    
    public String getNewEventBadgeText() {
        return getText(SelectorConstants.NEW_EVENT_BADGE);
    }
    
    public boolean isEventListTableLoaded() {
        return isElementVisible(SelectorConstants.EVENT_LIST_TABLE);
    }
    
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
