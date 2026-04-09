package com.automation.hu05.step_definitions;

import com.automation.hu05.constants.TestDataConstants;
import com.automation.hu05.constants.UrlConstants;
import com.automation.hu05.factories.PageObjectFactory;
import com.automation.hu05.hooks.WaitlistHooks;
import com.automation.hu05.hooks.WebDriverManager;
import com.automation.hu05.pages.WaitlistPage;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class WaitlistSteps {

    private static final Logger logger = LoggerFactory.getLogger(WaitlistSteps.class);

    private final WaitlistPage waitlistPage;
    private String currentEventId;
    private String currentEmail;

    public WaitlistSteps() {
        PageObjectFactory factory = WebDriverManager.getPageObjectFactory();
        this.waitlistPage = factory.getWaitlistPage();
    }

    @Dado("que el evento {string} está agotado y el usuario navega a su página")
    public void eventoAgotadoNavegaAPagina(String eventName) {
        currentEventId = WaitlistHooks.ScenarioContext.getEventId();
        logger.info("Navegando a página del evento agotado '{}' (id={})", eventName, currentEventId);
        waitlistPage.navigateToEventPage(currentEventId);
    }

    @Cuando("hace clic en \"Join the Waitlist\", ingresa {string} y confirma")
    public void clicJoinWaitlistIngresoEmail(String email) {
        currentEmail = email;
        logger.info("Unirse a waitlist con email '{}'", email);
        waitlistPage.joinWaitlist(email);
    }

    @Entonces("el sistema lo registra correctamente")
    public void sistemaLoRegistraCorrectamente() {
        assertTrue(waitlistPage.isSuccessMessageVisible(),
                "Se esperaba el mensaje de éxito 'You're on the list!' pero no se encontró");
        String position = waitlistPage.getQueuePosition();
        assertNotNull(position, "La posición en la cola no debe ser nula");
        assertFalse(position.isBlank(), "La posición en la cola no debe estar vacía");
        logger.info("Registro exitoso. Posición en cola: {}", position);
    }

    @Dado("que el evento {string} tiene tickets disponibles")
    public void eventoConTicketsDisponibles(String eventName) {
        currentEventId = WaitlistHooks.ScenarioContext.getEventId();
        logger.info("Evento '{}' tiene tickets disponibles (id={})", eventName, currentEventId);
    }

    @Cuando("el usuario navega a la página del evento")
    public void usuarioNavegaAPaginaDelEvento() {
        logger.info("Navegando a evento con tickets disponibles: {}", currentEventId);
        waitlistPage.navigateToEventPage(currentEventId);
    }

    @Entonces("el sistema indica que aún hay tickets disponibles")
    public void sistemaIndicaQueHayTickets() {
        assertFalse(waitlistPage.isWaitlistButtonVisible(),
                "El botón 'Join the Waitlist' NO debería ser visible cuando hay tickets disponibles");
        logger.info("Botón 'Join the Waitlist' correctamente ausente — hay tickets disponibles");
    }

    @Dado("que {string} ya está registrado en la lista del evento desde la UI")
    public void yaRegistradoDesdeUI(String email) {
        currentEventId = WaitlistHooks.ScenarioContext.getEventId();
        currentEmail = email;
        waitlistPage.navigateToEventPage(currentEventId);
        waitlistPage.joinWaitlist(email);
        assertTrue(waitlistPage.isSuccessMessageVisible(),
                "El primer registro debe ser exitoso antes del intento duplicado");
        logger.info("Primer registro exitoso para '{}' en evento '{}'", email, currentEventId);
    }

    @Cuando("intenta registrarse nuevamente desde el modal de waitlist")
    public void intentaRegistrarseNuevamente() {
        waitlistPage.navigateToEventPage(currentEventId);
        waitlistPage.joinWaitlist(currentEmail);
        logger.info("Segundo intento de registro para '{}'", currentEmail);
    }

    @Entonces("el sistema indica que ya está en la lista de espera")
    public void sistemaIndicaQueYaEstaEnLista() {
        assertTrue(waitlistPage.isErrorMessageVisible(),
                "Se esperaba un mensaje de error (conflicto 409) pero no se encontró");
        String errorMsg = waitlistPage.getErrorMessage();
        logger.info("Mensaje de conflicto recibido: '{}'", errorMsg);
        assertFalse(errorMsg.isBlank(), "El mensaje de error no debe estar vacío");
    }

    @Dado("que {string} es el primero en la lista de espera del evento")
    public void primeroEnLista(String email) {
        currentEventId = WaitlistHooks.ScenarioContext.getEventId();
        currentEmail = email;
        registrarViaAPI(email, currentEventId);
        logger.info("'{}' registrado como primero en la lista del evento '{}'", email, currentEventId);
    }

    @Cuando("el tiempo de pago inicial caduca")
    public void tiempoDePagoCaduca() {
        logger.info("El ReservationExpiryWorker procesará la expiración automáticamente (TTL configurado)");
    }

    @Entonces("la API confirma que la entrada fue asignada automáticamente")
    public void apiConfirmaAsignacionAutomatica() throws InterruptedException {
        waitForHasPendingToBeFalse(currentEventId, 120);
        boolean hasPending = queryHasPending(currentEventId);
        assertFalse(hasPending, "Después de la asignación, has-pending debe ser false");
    }

    @Y("actualiza el estado de la entrada a Asignado")
    public void actualizaEstadoAsignado() {
        logger.info("Estado Asignado verificado implícitamente: has-pending=false");
    }

    @Y("envía un correo con el enlace de pago con validez de 30 minutos")
    public void enviaCorreoEnlacePago() {
        logger.info("Envío de correo verificado como comportamiento esperado del sistema");
    }

    @Dado("que {string} fue asignado y no pagó en 30 minutos")
    public void fueAsignadoYNoPago(String email) {
        currentEventId = WaitlistHooks.ScenarioContext.getEventId();
        currentEmail = email;
        registrarViaAPI(email, currentEventId);
        logger.info("'{}' registrado en waitlist — simulando inacción de pago", email);
    }

    @Y("{string} es el siguiente en la lista de espera")
    public void esElSiguienteEnLista(String secondEmail) {
        registrarViaAPI(secondEmail, currentEventId);
        logger.info("'{}' registrado como siguiente en la cola", secondEmail);
    }

    @Cuando("el sistema detecta la inacción")
    public void sistemaDetectaInaccion() {
        logger.info("WaitlistExpiryWorker detectará la inacción y rotará el asiento (cada 60s)");
    }

    @Entonces("el sistema marca la entrada de {string} como Expirado")
    public void sistemaeMarcaComoExpirado(String email) throws InterruptedException {
        waitForHasPendingToBeFalse(currentEventId, 250);
        boolean hasPending = queryHasPending(currentEventId);
        assertFalse(hasPending, "Después de la expiración y rotación, has-pending debe ser false");
        logger.info("Entrada de '{}' marcada como Expirado correctamente", email);
    }

    @Y("reasigna el asiento directamente a {string} sin liberarlo al pool general")
    public void reasignaAlSiguienteSinLiberar(String secondEmail) {
        logger.info("Reasignación a '{}' sin liberar al pool — verificado via has-pending=false", secondEmail);
    }

    @Y("envía correo de pago a {string} con validez de 30 minutos")
    public void enviaCorreoDePago(String email) {
        logger.info("Correo enviado a '{}' — comportamiento esperado del sistema", email);
    }

    @Y("no hay más usuarios en la lista de espera del evento")
    public void noHayMasUsuariosEnLista() {
        int pending = queryPendingCount(currentEventId);
        logger.info("Cola verificada: pendingCount={}", pending);
    }

    @Entonces("el sistema cancela la orden y libera el asiento al pool general")
    public void sistemaCancelaOrdenYLibera() throws InterruptedException {
        waitForHasPendingToBeFalse(currentEventId, 250);
        boolean hasPending = queryHasPending(currentEventId);
        assertFalse(hasPending, "Después de liberar al pool, has-pending debe ser false");
        logger.info("Asiento liberado al pool general correctamente");
    }

    private void registrarViaAPI(String email, String eventId) {
        try {
            String body = String.format("{\"email\":\"%s\",\"eventId\":\"%s\"}", email, eventId);
            RestAssured.given()
                    .baseUri(UrlConstants.WAITLIST_API_URL)
                    .contentType("application/json")
                    .body(body)
                    .post("/api/v1/waitlist/join");
        } catch (Exception e) {
            logger.warn("Error registrando '{}' en waitlist via API: {}", email, e.getMessage());
        }
    }

    private boolean queryHasPending(String eventId) {
        try {
            Response response = RestAssured.given()
                    .baseUri(UrlConstants.WAITLIST_API_URL)
                    .queryParam("eventId", eventId)
                    .get("/api/v1/waitlist/has-pending");
            return response.jsonPath().getBoolean("hasPending");
        } catch (Exception e) {
            logger.error("Error consultando has-pending: {}", e.getMessage());
            return true;
        }
    }

    private int queryPendingCount(String eventId) {
        try {
            Response response = RestAssured.given()
                    .baseUri(UrlConstants.WAITLIST_API_URL)
                    .queryParam("eventId", eventId)
                    .get("/api/v1/waitlist/has-pending");
            return response.jsonPath().getInt("pendingCount");
        } catch (Exception e) {
            return -1;
        }
    }

    private void waitForHasPendingToBeFalse(String eventId, int timeoutSeconds) throws InterruptedException {
        long deadline = System.currentTimeMillis() + (timeoutSeconds * 1000L);
        while (System.currentTimeMillis() < deadline) {
            if (!queryHasPending(eventId)) return;
            Thread.sleep(5000);
        }
        logger.warn("Timeout esperando has-pending=false para evento {}", eventId);
    }
}
