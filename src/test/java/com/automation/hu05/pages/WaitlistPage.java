package com.automation.hu05.pages;

import com.automation.hu05.constants.SelectorConstants;
import com.automation.hu05.constants.UrlConstants;
import org.openqa.selenium.WebDriver;

/**
 * Page Object para el flujo de Lista de Espera (Waitlist).
 *
 * Encapsula la interacción con:
 * - La página del evento público (/events/{eventId}) — banner "sold out" + botón waitlist
 * - El WaitlistModal (Dialog de Radix UI) — formulario de email
 */
public class WaitlistPage extends BasePage {

    public WaitlistPage(WebDriver driver) {
        super(driver);
    }

    /** Navega a la página pública del evento */
    public void navigateToEventPage(String eventId) {
        driver.navigate().to(UrlConstants.EVENT_DETAIL_PAGE + eventId);
    }

    /** Verifica si el botón "Join the Waitlist" está visible (evento agotado) */
    public boolean isWaitlistButtonVisible() {
        return isElementVisible(SelectorConstants.WAITLIST_JOIN_BUTTON);
    }

    /** Hace clic en el botón "Join the Waitlist" para abrir el modal */
    public void clickJoinWaitlist() {
        click(SelectorConstants.WAITLIST_JOIN_BUTTON);
    }

    /** Ingresa el email en el campo del modal */
    public void enterEmail(String email) {
        fillTextField(SelectorConstants.WAITLIST_EMAIL_INPUT, email);
    }

    /** Hace clic en el botón "Join Waitlist" para enviar el formulario */
    public void submitWaitlistForm() {
        click(SelectorConstants.WAITLIST_SUBMIT_BUTTON);
    }

    /** Flujo completo: abrir modal → ingresar email → submit */
    public void joinWaitlist(String email) {
        clickJoinWaitlist();
        enterEmail(email);
        submitWaitlistForm();
    }

    /** Verifica si el mensaje de éxito "You're on the list!" es visible */
    public boolean isSuccessMessageVisible() {
        return isElementVisible(SelectorConstants.WAITLIST_SUCCESS_MSG);
    }

    /** Obtiene el texto de la posición en la cola (ej: "#1") */
    public String getQueuePosition() {
        if (isElementPresent(SelectorConstants.WAITLIST_POSITION_TEXT)) {
            return getText(SelectorConstants.WAITLIST_POSITION_TEXT);
        }
        return null;
    }

    /** Verifica si hay un mensaje de error visible */
    public boolean isErrorMessageVisible() {
        return isElementVisible(SelectorConstants.WAITLIST_ERROR_MSG);
    }

    /** Obtiene el texto del mensaje de error */
    public String getErrorMessage() {
        if (isElementPresent(SelectorConstants.WAITLIST_ERROR_MSG)) {
            return getText(SelectorConstants.WAITLIST_ERROR_MSG);
        }
        return "";
    }
}
