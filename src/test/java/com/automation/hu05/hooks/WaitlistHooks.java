package com.automation.hu05.hooks;

import com.automation.hu05.constants.UrlConstants;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Hooks para el ciclo de vida de los tests de Waitlist.
 *
 * @Before: Crea el evento de prueba via Catalog + Inventory API.
 *   - Eventos agotados: genera asientos y bloquea TODOS (@RegistroExitoso, @RegistroDuplicado, etc.)
 *   - Eventos con stock: genera asientos SIN bloquear (@TicketsDisponibles)
 * @After: Desactiva el evento de prueba.
 */
public class WaitlistHooks {

    private static final Logger logger = LoggerFactory.getLogger(WaitlistHooks.class);
    private static final String CATALOG_URL   = "http://localhost:50001";
    private static final String INVENTORY_URL = "http://localhost:50002";

    private static final List<String> SOLD_OUT_TAGS = List.of(
            "@RegistroExitoso", "@RegistroDuplicado",
            "@AsignacionAutomatica", "@LiberacionConSiguiente", "@LiberacionSinCola"
    );

    @Before(order = 3)
    public void setupWaitlistTestEvent(Scenario scenario) {
        boolean needsSoldOut = scenario.getSourceTagNames().stream()
                .anyMatch(SOLD_OUT_TAGS::contains);

        String eventId = createEvent();
        if (eventId == null) {
            logger.warn("No se pudo crear el evento de prueba para waitlist");
            return;
        }

        generateSeat(eventId);

        if (needsSoldOut) {
            blockFirstSeat(eventId);
        }

        ScenarioContext.setEventId(eventId);
        logger.info("Evento de prueba waitlist listo: {} (agotado={})", eventId, needsSoldOut);
    }

    @After(order = 3)
    public void teardownWaitlistTestEvent() {
        String eventId = ScenarioContext.getEventId();
        if (eventId != null) {
            try {
                RestAssured.given()
                        .baseUri(CATALOG_URL)
                        .post("/admin/events/" + eventId + "/deactivate");
                logger.info("Evento waitlist desactivado: {}", eventId);
            } catch (Exception e) {
                logger.warn("No se pudo desactivar el evento de prueba: {}", eventId);
            }
        }
        ScenarioContext.clear();
    }

    private String createEvent() {
        try {
            String date = LocalDateTime.now().plusDays(30)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
            String body = String.format(
                    "{\"name\":\"Concierto Rock 2026 - Test %d\","
                    + "\"description\":\"Evento prueba waitlist\","
                    + "\"eventDate\":\"%s\","
                    + "\"venue\":\"Teatro Test\","
                    + "\"maxCapacity\":1,"
                    + "\"basePrice\":50.0}",
                    System.currentTimeMillis(), date);

            Response response = RestAssured.given()
                    .baseUri(CATALOG_URL)
                    .contentType("application/json")
                    .body(body)
                    .post("/admin/events");

            if (response.statusCode() == 201) {
                return response.jsonPath().getString("id");
            }
        } catch (Exception e) {
            logger.error("Error creando evento: {}", e.getMessage());
        }
        return null;
    }

    private void generateSeat(String eventId) {
        try {
            String body = "{\"sectionConfigurations\":[{\"sectionCode\":\"A\",\"rows\":1,\"seatsPerRow\":1,\"priceMultiplier\":1.0}]}";
            RestAssured.given()
                    .baseUri(CATALOG_URL)
                    .contentType("application/json")
                    .body(body)
                    .post("/admin/events/" + eventId + "/seats");
        } catch (Exception e) {
            logger.warn("Error generando asiento: {}", e.getMessage());
        }
    }

    private void blockFirstSeat(String eventId) {
        try {
            Response seatmap = RestAssured.given()
                    .baseUri(CATALOG_URL)
                    .get("/events/" + eventId + "/seatmap/");

            String seatId = seatmap.jsonPath().getString("seats[0].id");
            if (seatId == null) return;

            String body = String.format("{\"seatId\":\"%s\",\"customerId\":\"%s\"}",
                    seatId, java.util.UUID.randomUUID());

            RestAssured.given()
                    .baseUri(INVENTORY_URL)
                    .contentType("application/json")
                    .body(body)
                    .post("/reservations");

            logger.info("Asiento {} bloqueado (evento agotado para waitlist)", seatId);
        } catch (Exception e) {
            logger.warn("Error bloqueando asiento: {}", e.getMessage());
        }
    }

    // ---- Scenario Context ----

    public static class ScenarioContext {
        private static final ThreadLocal<String> eventId = new ThreadLocal<>();

        public static void setEventId(String id) { eventId.set(id); }
        public static String getEventId()         { return eventId.get(); }
        public static void clear()                { eventId.remove(); }
    }

    public static class CurrentScenario {
        private static final ThreadLocal<Scenario> current = new ThreadLocal<>();

        public static void set(Scenario s) { current.set(s); }
        public static Scenario get()       { return current.get(); }
        public static void clear()         { current.remove(); }
    }
}
