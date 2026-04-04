# Feature Specification: Sistema de Lista de Espera Inteligente — UI Automation (POM)

**Feature Branch**: `004-waitlist-pom`
**Created**: 2026-04-03
**Status**: Draft
**Input**: Automatizar los 6 escenarios del Sistema de Lista de Espera Inteligente mediante la UI del frontend Next.js usando el patrón Page Object Model + Factory.
**Technical Base**: Frontend Next.js (`http://localhost:3000`) + WaitlistModal component + Waitlist.Api (`http://localhost:5006`).

---

## User Scenarios & Testing *(mandatory)*

### User Story 1 — Registro via UI en Lista de Espera (Priority: P1) 🎯 MVP

Como usuario que visita la página de un evento agotado, quiero ver el modal de lista de espera, ingresar mi email y recibir confirmación de mi posición en la cola.

**Why this priority**: Es el flujo de usuario principal. El modal `WaitlistModal` es el punto de contacto UI para los escenarios 1-3.

**Independent Test**: Navegar a `/events/{eventId}` (evento agotado), hacer clic en "Join the Waitlist", ingresar email, verificar mensaje de éxito con posición.

**Acceptance Scenarios**:

1. **Given** el evento "Concierto Rock 2026" está agotado y el usuario navega a su página, **When** hace clic en "Join the Waitlist", ingresa "jostin@example.com" y confirma, **Then** la UI muestra "You're on the list!" con la posición en la cola.
2. **Given** el evento tiene tickets disponibles, **When** el usuario navega a la página del evento, **Then** el botón "Join the Waitlist" NO es visible (el evento no está agotado).
3. **Given** "jostin@example.com" ya está registrado en el evento, **When** intenta registrarse nuevamente desde la UI, **Then** la UI muestra el mensaje de error retornado por la API (conflicto 409).

---

### User Story 2 — Verificación de Estado Post-Asignación (Priority: P2)

Como sistema de automatización, quiero verificar que tras la expiración de una reserva, el estado del waitlist refleja la asignación correcta.

**Why this priority**: Complementa los escenarios de backend con verificación de estado vía API desde el contexto UI.

**Acceptance Scenarios**:

4. **Given** "jostin@example.com" es el primero en la lista de espera, **When** el tiempo de pago inicial caduca (trigger por worker), **Then** la API confirma que el estado de la entrada cambió a `Asignado` y `has-pending` es `false`.
5. **Given** "jostin@example.com" fue asignado y no pagó, y "segundo@example.com" es el siguiente, **When** el sistema detecta la inacción, **Then** la API confirma que "jostin@example.com" expiró y "segundo@example.com" fue asignado sin liberar el asiento al pool.
6. **Given** "jostin@example.com" fue asignado y no pagó, y no hay más usuarios, **When** el sistema detecta la inacción, **Then** la API confirma que la orden fue cancelada y el asiento volvió al pool general.

---

### Edge Cases

- El usuario intenta enviar el form de waitlist sin email → el campo `required` del HTML impide el envío.
- El servicio Waitlist no está disponible → la UI muestra "Could not join waitlist" (error genérico del catch).
- El modal se cierra y se vuelve a abrir → el estado del formulario se resetea (email, error, position).

---

## Requirements *(mandatory)*

### Architectural & Framework Requirements

- **FR-001**: El framework DEBE implementar `WaitlistPage` como Page Object que encapsula el modal de waitlist y la sección de evento agotado.
- **FR-002**: `WaitlistPage` DEBE extender `BasePage` y usar locators en formato `"tipo:valor"`.
- **FR-003**: Todos los selectores DEBEN estar centralizados en `SelectorConstants` bajo la sección `// ======= WaitlistPage =======`.
- **FR-004**: Las URLs de eventos DEBEN estar en `UrlConstants` (`EVENT_DETAIL_PAGE`).
- **FR-005**: Los datos de prueba (emails, event names) DEBEN estar en `TestDataConstants`.
- **FR-006**: `WaitlistPage` DEBE ser accesible vía `PageObjectFactory.getWaitlistPage()`.
- **FR-007**: Para escenarios 4-6, los steps DEBEN usar `RestAssured` directamente para verificar el estado de la API (híbrido UI + API).

### Key Selectors (from WaitlistModal component)

- Botón "Join the Waitlist": `xpath://button[contains(text(), 'Join the Waitlist')]`
- Modal email input: `css:#waitlist-email`
- Submit button en modal: `xpath://button[normalize-space()='Join Waitlist']`
- Mensaje de éxito: `xpath://*[contains(text(), "You're on the list!")]`
- Posición en cola: `css:span.font-bold.text-accent.text-base`
- Mensaje de error: `css:p.text-sm.text-destructive`

---

## Success Criteria *(mandatory)*

- **SC-001**: Escenario 1 (registro exitoso) pasa con UI mostrando "You're on the list!" y posición numérica.
- **SC-002**: Escenario 2 (tickets disponibles) verifica ausencia del botón "Join the Waitlist" en la página.
- **SC-003**: Escenario 3 (duplicado) muestra el mensaje de error del API en la UI.
- **SC-004**: Escenarios 4-6 verifican estado correcto de la API como complemento de los tests de backend.
- **SC-005**: Todos los selectores pasan por `SelectorConstants` sin hardcoding en step definitions.

---

## Clarifications

- **Q**: ¿En qué URL aparece el modal de waitlist? → **A**: `/events/{eventId}` — solo cuando `isSoldOut = true` (todos los asientos no disponibles).
- **Q**: ¿El texto del botón y modal está en inglés o español? → **A**: **Inglés** — "Join the Waitlist", "Join Waitlist", "You're on the list!".
- **Q**: ¿Cómo crear un evento agotado para la UI? → **A**: Via API en el `@Before` hook: crear evento + generar asientos + reservar todos vía Inventory.Api.
- **Q**: ¿Escenarios 4-6 tienen UI verificable? → **A**: No directamente. Se verifican vía API (`has-pending` endpoint) en los step definitions.

---

## Assumptions

- **A-001**: El frontend corre en `http://localhost:3000`.
- **A-002**: El `@Before` hook crea un evento con todos los asientos bloqueados via API para asegurar `isSoldOut=true` en la UI.
- **A-003**: El evento de prueba se limpia (desactivado) en el `@After` hook.
- **A-004**: Los escenarios 4-6 requieren la infraestructura completa (Kafka + workers) para ejecutarse en modo de integración real.
