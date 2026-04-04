# Tasks: Sistema de Lista de Espera — UI Automation (POM)

**Feature**: `004-waitlist-pom` | **Date**: 2026-04-03
**Plan**: [plan.md](./plan.md) | **Spec**: [spec.md](./spec.md)
**Total Tasks**: 18

---

## Phase 1: Feature File

- [x] **T001** Crear `src/test/resources/features/waitlist.feature` con los 6 escenarios en español, tags: `@ListaEspera`, `@RegistroExitoso`, `@TicketsDisponibles`, `@RegistroDuplicado`, `@AsignacionAutomatica`, `@LiberacionConSiguiente`, `@LiberacionSinCola`

---

## Phase 2: Constants

- [x] **T002** Editar `constants/SelectorConstants.java` — agregar sección `// ======= WaitlistPage =======`:
  - `WAITLIST_JOIN_BUTTON = "xpath://button[contains(text(), 'Join the Waitlist')]"`
  - `WAITLIST_EMAIL_INPUT = "css:#waitlist-email"`
  - `WAITLIST_SUBMIT_BUTTON = "xpath://button[normalize-space()='Join Waitlist']"`
  - `WAITLIST_SUCCESS_MESSAGE = "xpath://*[contains(text(), \"You're on the list!\")]"`
  - `WAITLIST_POSITION_TEXT = "css:span.font-bold.text-accent.text-base"`
  - `WAITLIST_ERROR_MESSAGE = "css:p.text-sm.text-destructive"`

- [x] **T003** Editar `constants/UrlConstants.java` — agregar:
  - `EVENT_DETAIL_PAGE = BASE_URL + "/events/"` (se concatena el eventId en el step)

- [x] **T004** Editar `constants/TestDataConstants.java` — agregar:
  - `WAITLIST_EMAIL = "jostin@example.com"`
  - `WAITLIST_SECOND_EMAIL = "segundo@example.com"`
  - `WAITLIST_EVENT_NAME = "Concierto Rock 2026"`

---

## Phase 3: Page Object

- [x] **T005** Crear `pages/WaitlistPage.java` extendiendo `BasePage`
  - `navigateToEventPage(String eventId)` → `driver.get(UrlConstants.EVENT_DETAIL_PAGE + eventId)`
  - `isWaitlistButtonVisible()` → `isElementVisible(SelectorConstants.WAITLIST_JOIN_BUTTON)`
  - `clickJoinWaitlist()` → `click(SelectorConstants.WAITLIST_JOIN_BUTTON)`
  - `enterEmail(String email)` → `fillTextField(SelectorConstants.WAITLIST_EMAIL_INPUT, email)`
  - `submitWaitlistForm()` → `click(SelectorConstants.WAITLIST_SUBMIT_BUTTON)`
  - `isSuccessMessageVisible()` → `isElementVisible(SelectorConstants.WAITLIST_SUCCESS_MESSAGE)`
  - `getQueuePosition()` → `getText(SelectorConstants.WAITLIST_POSITION_TEXT)`
  - `getErrorMessage()` → `getText(SelectorConstants.WAITLIST_ERROR_MESSAGE)`
  - `joinWaitlist(String email)` → `clickJoinWaitlist()` + `enterEmail(email)` + `submitWaitlistForm()`

---

## Phase 4: PageObjectFactory

- [x] **T006** Editar `factories/PageObjectFactory.java` — agregar:
  - `private WaitlistPage waitlistPage;`
  - `public WaitlistPage getWaitlistPage()` con lazy init

---

## Phase 5: Step Definitions (RED → GREEN)

- [x] **T007** 🔴 Crear `step_definitions/WaitlistSteps.java`

  **Escenario 1 (RegistroExitoso)**:
  - `Dado que el evento ... está agotado y el usuario navega a su página` → `waitlistPage.navigateToEventPage(eventId)`
  - `Cuando hace clic en "Join the Waitlist", ingresa ... y confirma` → `waitlistPage.joinWaitlist(email)`
  - `Entonces la UI muestra "You're on the list!"` → `assertTrue(waitlistPage.isSuccessMessageVisible())`
  - `Y el usuario recibe su posición en la cola` → `assertNotNull(waitlistPage.getQueuePosition())`

  **Escenario 2 (TicketsDisponibles)**:
  - `Dado que el evento tiene tickets disponibles` → no-op (setup en hook)
  - `Cuando el usuario navega a la página del evento` → `waitlistPage.navigateToEventPage(eventId)`
  - `Entonces el botón "Join the Waitlist" NO es visible` → `assertFalse(waitlistPage.isWaitlistButtonVisible())`

  **Escenario 3 (RegistroDuplicado)**:
  - Primera llamada → joinWaitlist → success
  - `Cuando intenta registrarse nuevamente` → `waitlistPage.joinWaitlist(email)` (segundo intento)
  - `Entonces la UI muestra el mensaje de conflicto` → `assertTrue(waitlistPage.isElementVisible(SelectorConstants.WAITLIST_ERROR_MESSAGE))`

  **Escenarios 4-6**: Verificación vía API directa con `RestAssured`
  - `GET /api/v1/waitlist/has-pending?eventId=X` → assert `hasPending` booleano
  - Steps usan `RestAssured.given().get(...)` dentro del step definition

---

## Phase 6: Hooks

- [x] **T008** Crear `hooks/WaitlistHooks.java` (o extender `HU05Hooks`)
  - `@Before(order=0)`: `storeScenario(scenario)`
  - `@Before(order=1)`: `WebDriverManager.initialiseDriver()`
  - `@Before(order=2, "@RegistroExitoso or @RegistroDuplicado or ...")`: crear evento + bloquear TODOS los asientos via Catalog + Inventory API → guardar `eventId` en `ScenarioContext`
  - `@Before(order=2, "@TicketsDisponibles")`: crear evento + generar asientos SIN bloquear → guardar `eventId`
  - `@After(order=1)`: desactivar evento, limpiar contexto
  - `@After(order=2)`: `WebDriverManager.closeDriver()`

---

## Phase 7: Runner

- [x] **T009** Crear `runners/RunWaitlistTests.java`
  - `@RunWith(CucumberWithSerenity.class)`
  - `features = "src/test/resources/features/waitlist.feature"`
  - `glue = { "com.automation.hu05.step_definitions", "com.automation.hu05.hooks" }`
  - `tags = "@ListaEspera"`

---

## Phase 8: Polish

- [ ] **T010** [P] Verificar que los timeouts en `WaitlistPage` son adecuados para el Dialog de Radix UI (puede necesitar wait extra por animación)
- [ ] **T011** [P] Agregar screenshot en `@After` si el escenario falló (usando `CurrentScenario`)
- [ ] **T012** Ejecutar escenarios 1-3 y verificar que pasan; marcar 4-6 como `@IntegracionCompleta`

---

## TDD Cycle Summary

| Ciclo | Escenario | Componente | Estado |
|---|---|---|---|
| 1 | Registro exitoso UI | `WaitlistPage.joinWaitlist()` + success assertion | 🔴 → 🟢 |
| 2 | Botón no visible | `WaitlistPage.isWaitlistButtonVisible()` = false | 🔴 → 🟢 |
| 3 | Duplicado UI | Error message visible | 🔴 → 🟢 |
| 4 | Asignación automática | RestAssured `has-pending` polling | 🔴 (requiere Kafka) |
| 5 | Rotación | RestAssured polling | 🔴 (requiere Kafka) |
| 6 | Cola vacía | RestAssured polling | 🔴 (requiere Kafka) |
