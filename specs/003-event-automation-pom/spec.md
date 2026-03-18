# Feature Specification: Test Automation Framework for Event Management (HU-05) with Page Object Model

**Feature Branch**: `003-event-automation-pom`  
**Created**: 18 de marzo de 2026  
**Status**: Draft  
**Input**: Front-End UI automation for Event Management and Registration using Page Object Model with Page Factory in Serenity BDD

## Source of Truth & Contracts

- La única fuente autorizada de escenarios, criterios y requisitos es el submódulo shared-specs; cada historia referenciada aquí debe apuntar a un archivo bajo shared-specs/specs.
- El comportamiento de UI y los pasos automatizados deben mapearse contra shared-specs/contracts/openapi/catalog.yaml para mantener la fidelidad al servicio `catalog`.

## Clarifications

### Session 2026-03-18

- Q1: Campos exactos del formulario de evento → A: Solo los 5 campos mencionados (nombre, descripción, fecha, ubicación, capacidad) son inputs del usuario; ID y timestamps son autogenerados por el sistema

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Automatizar validación del flujo exitoso de registro de evento (Priority: P1)

El framework debe ejecutar un escenario de prueba que valide que un usuario puede crear y registrar un evento exitosamente, verificando que todos los campos se persisten correctamente y los mensajes de éxito se muestran en la UI.

**Why this priority**: Es el flujo crítico más importante. Verificar que el registro exitoso funciona es la base de cualquier estrategia de automatización de esta funcionalidad. Sin este escenario, no hay valor de prueba.

**Independent Test**: Este escenario puede ejecutarse de forma completamente independiente mediante:
1. Navegar a la página de registro de eventos
2. Llenar los campos requeridos (nombre, descripción, fecha, ubicación, capacidad)
3. Enviar el formulario
4. Verificar en UI que aparezca el mensaje de éxito
5. Verificar que el evento aparezca en la lista de eventos
Esto valida el happy path completo sin dependencias de otros escenarios.

**Acceptance Scenarios**:

1. **Given** el usuario accede a la página de registro de eventos, **When** completa todos los campos obligatorios con datos válidos, **Then** se muestra un mensaje de éxito y el evento aparece en la lista de eventos

2. **Given** el usuario ha creado un evento exitosamente, **When** navega a la lista de eventos, **Then** el evento recién creado es visible con todos los datos ingresados

---

### User Story 2 - Automatizar validación de errores de validación en el formulario (Priority: P2)

El framework debe ejecutar un escenario que valide que cuando el usuario intenta registrar un evento con datos inválidos o incompletos, el sistema muestra mensajes de error apropiados sin procesar la solicitud.

**Why this priority**: Es esencial validar que el sistema rechaza datos inválidos correctamente. Esto asegura confiabilidad de los datos y que la UI proporciona retroalimentación clara al usuario. Crítico para la calidad funcional.

**Independent Test**: Este escenario es completamente independiente y puede ejecutarse aisladamente:
1. Navegar a la página de registro
2. Intentar enviar el formulario sin llenar campos obligatorios
3. Verificar que se muestren mensajes de error específicos en UI
4. Verificar que no se cree el evento
Esto valida el manejo de caso negativo sin afectar otros escenarios.

**Acceptance Scenarios**:

1. **Given** el usuario intenta enviar el formulario de registro sin llenar campos obligatorios, **When** hace clic en el botón enviar, **Then** se muestran mensajes de error para cada campo vacío

2. **Given** el usuario ingresa una capacidad numérica negativa, **When** intenta enviar el formulario, **Then** se muestra un mensaje de error indicando que la capacidad debe ser un número positivo

---

### Edge Cases

- ¿Qué sucede cuando el usuario intenta registrar un evento con un nombre que ya existe en el sistema?
- ¿Cómo maneja el sistema cuando el usuario cierra el navegador mientras se envía el formulario?
- ¿Qué pasa cuando el campo de capacidad contiene caracteres no numéricos?
- ¿Cómo valida el sistema fechas en el pasado?
- ¿Qué sucede cuando los campos contienen caracteres especiales o inyección SQL?

## Requirements *(mandatory)*

### Architectural & Framework Requirements

- **FR-001**: El framework DEBE implementar el patrón Page Object Model (POM) donde cada página o pantalla de la aplicación se representa como una clase Java independiente
- **FR-002**: Cada Page Object DEBE utilizar anotaciones @FindBy (Page Factory de Selenium) para declarar todos los elementos de la UI, sin hardcoding de selectores dentro de métodos
- **FR-003**: El framework DEBE incluir una clase centralizada `PageObjectFactory` responsable de instanciar y gestionar todos los Page Objects
- **FR-004**: Las Step Definitions NO DEBEN instanciar Page Objects directamente; todo acceso a páginas DEBE realizarse a través de la `PageObjectFactory`
- **FR-005**: El framework DEBE implementar cuatro clases de constantes centralizadas sin excepción:
  - `SelectorConstants.java` → Todos los localizadores de elementos UIxpath, CSS, ID)
  - `UrlConstants.java` → URLs base y endpoints de navegación
  - `TimeoutConstants.java` → Tiempos de espera configurables (implicit, explicit, page load)
  - `TestDataConstants.java` → Datos de prueba reutilizables
- **FR-006**: CERO strings hardcodeados están permitidos en el código de automatización. Todos los valores deben provenir de las clases de constantes
- **FR-007**: El framework DEBE usar Gradle como herramienta de gestión de dependencias y compilación

### Project Structure Requirements

- **FR-008**: La estructura del proyecto DEBE seguir la organización bajo `src/test/java/` con los siguientes paquetes:
  - `pages/` → Todas las clases Page Object
  - `factories/` → PageObjectFactory y otras factories
  - `constants/` → SelectorConstants, UrlConstants, TimeoutConstants, TestDataConstants
  - `step_definitions/` → Step definitions de Cucumber
  - `hooks/` → Hooks de ejecución (@Before, @After)
- **FR-009**: La estructura DEBE incluir un directorio `src/test/resources/` con:
  - `features/` → Archivos .feature de Cucumber con escenarios
  - `cucumber.properties` → Configuración de Cucumber
  - `application.properties` → Propiedades de la aplicación bajo test

### Cucumber & BDD Requirements

- **FR-010**: El framework DEBE implementar mínimo 2 escenarios Cucumber independientes:
  - Escenario 1 (positivo): Registro exitoso de evento validando el flujo completo
  - Escenario 2 (negativo): Validación de error cuando se proporcionan datos inválidos
- **FR-011**: Los escenarios de Cucumber DEBEN estar escritos en Gherkin declarativo, enfocados en comportamiento de negocio (Given-When-Then)
- **FR-012**: Los pasos de Gherkin NO DEBEN incluir detalles técnicos como clicks específicos, selectores o nombres de campos
- **FR-013**: Cada escenario DEBE ser independiente y no DEBE depender del resultado de otros escenarios
- **FR-014**: Los archivos .feature DEBEN estar en español para alineación con la audiencia del proyecto

### Hooks & Test Data Management

- **FR-015**: El framework DEBE implementar hooks @Before que preparen el estado de prueba:
  - Crear eventos de prueba utilizando API REST Assured (únicamente para preparación, NO para validación)
  - Generar asientos disponibles en el sistema (llamadas a endpoints específicos del servicio `catalog`)
  - Los datos creados deben ser almacenados para referencia posterior en el escenario
- **FR-016**: El framework DEBE implementar hooks @After que realicen limpieza de datos:
  - Eliminar eventos creados en el @Before (DELETE calls)
  - Limpiar datos de sesión/autenticación
  - Cada escenario DEBE terminar en estado limpio
- **FR-017**: Los hooks MUST garantizar idempotencia: cualquier escenario puede ejecutarse múltiples veces sin fallar por datos residuales
- **FR-018**: API calls en hooks DEBEN limitar el uso a:
  - Autenticación y obtención de tokens
  - Preparación de datos de prueba (POST, PUT en datos iniciales)
  - Limpieza de datos (DELETE)
  - NO se permite validar resultados funcionales mediante API

### Validation & Assertion Requirements

- **FR-019**: Las validaciones DEBEN enfocarse exclusivamente en comportamiento visible en la UI:
  - Mensajes de éxito y error en pantalla
  - Cambios de estado visual (elementos aparecen/desaparecen)
  - Cambios en listas o tablas
  - Valores mostrados en campos de entrada
- **FR-020**: Las validaciones NO DEBEN incluir:
  - Inspección de respuestas HTTP
  - Consultas a base de datos
  - Detalles técnicos internos
  - Estado de sesión servidorizada
- **FR-021**: Cada validación DEBE usar métodos descriptivos con nombres semánticos (ej: `assertEventSuccessMessageIsDisplayed()`, `assertErrorMessageContains()`)

### Technology & Tool Requirements

- **FR-022**: El framework DEBE usar **Serenity BDD** como orquestador de pruebas y generador de reportes
- **FR-023**: El framework DEBE usar **Cucumber** como herramienta BDD para definición de escenarios
- **FR-024**: El framework DEBE usar **Selenium Grid / Selenium WebDriver** para automatización de interacciones con el navegador
- **FR-025**: El framework DEBE usar **REST Assured** para llamadas API en hooks (únicamente para setup/cleanup)
- **FR-026**: El framework DEBE soportar múltiples navegadores (Chrome, Firefox, Edge) configurables
- **FR-027**: El framework NO DEBE usar Screenplay Pattern en ningún escenario
- **FR-028**: El framework NO DEBE mezclar patrones de automatización diferentes
- **FR-029**: El framework DEBE generar reportes HTML automáticos con Serenity que incluyan:
  - Capturas de pantalla automáticas por paso
  - Logs de ejecución
  - Resultados de escenarios
  - Tiempos de ejecución

### Code Quality Requirements

- **FR-030**: El código DEBE seguir principios SOLID en toda la estructura
- **FR-031**: Cada clase Page Object DEBE tener una responsabilidad única (interactuar con una página específica)
- **FR-032**: La nomenclatura en el código DEBE ser semántica y autoexplicativa
- **FR-033**: Métodos Page Object DEBE describir acciones del usuario (ej: `enterEventName()`, `clickSubmit()`, `getSuccessMessage()`)
- **FR-034**: NO se permite lógica de negocio en Page Objects; solo interacción con UI
- **FR-035**: Los métodos en Page Objects DEBEN ser pequeños, cohesivos y fáciles de entender
- **FR-036**: El código DEBE permitir alta reutilización de Page Objects y métodos

### Reporting & Evidence Requirements

- **FR-037**: Serenity DEBE capturar automáticamente evidencia visual (screenshots) en cada paso del escenario
- **FR-038**: Los reportes DEBEN incluir logs detallados de ejecución con timestamps
- **FR-039**: Los reportes DEBEN estar disponibles en formato HTML tras la ejecución
- **FR-040**: Los reportes DEBEN ser accesibles en la ruta `target/site/serenity/` tras ejecutar los tests

### Mapping a Shared-Specs

- **FR-041**: Todos los escenarios de automatización DEBEN estar alineados con la especificación funcional en `shared-specs/specs/hu05.feature` o archivo equivalente
- **FR-042**: Los Page Objects y selectores DEBEN corresponder a los componentes definidos en `shared-specs/frontend/components.json`
- **FR-043**: El comportamiento UI esperado DEBE validarse contra los contratos en `shared-specs/contracts/openapi/catalog.yaml` para mantener fidel idad al servicio de catálogo

### Key Entities *(include if feature involves data)*

- **EventPage (Page Object)**: Representa la interfaz de usuario para crear eventos. Contiene 5 @FindBy elements:
  - `eventNameInput` → campo de texto para nombre (required)
  - `eventDescriptionInput` → campo de texto para descripción (required)
  - `eventDateInput` → campo de fecha para fecha del evento (required)
  - `eventLocationInput` → campo de texto para ubicación (required)
  - `eventCapacityInput` → campo numérico para capacidad de asientos (required)
  - `submitButton` → botón de envío del formulario
  - Error message containers para validación

- **EventListPage (Page Object)**: Representa la página que muestra el listado de eventos registrados. Contiene:
  - tabla/grid con columnas: nombre, descripción, fecha, ubicación, capacidad
  - elementos de búsqueda y filtrado
  - elemento de confirmación (badge/label) que indica evento recién creado

- **Event Entity**: Estructura de datos con los 5 campos confirmados:
  - `name` (String, required, <255 chars)
  - `description` (String, required, <1000 chars)
  - `date` (LocalDate, required, debe ser fecha futura)
  - `location` (String, required, <255 chars)
  - `capacity` (Integer, required, debe ser número positivo)
  - `id` y `createdAt` son autogenerados en el servidor

- **TestData Entity**: Almacena conjuntos de datos reutilizables:
  - Flujo positivo: evento válido completo con los 5 campos
  - Flujo negativo: datasets con campos vacíos, números negativos, caracteres especiales
  - Credenciales de usuario (si autenticación es requerida)

### Mapping a Shared-Specs

- **FR-041**: Todos los escenarios de automatización DEBEN estar alineados con la especificación funcional en `shared-specs/specs/hu05.feature` o archivo equivalente
- **FR-042**: Los Page Objects y selectores DEBEN corresponder a los componentes definidos en `shared-specs/frontend/components.json`
- **FR-043**: El comportamiento UI esperado DEBE validarse contra los contratos en `shared-specs/contracts/openapi/catalog.yaml` para mantener fidelidad al servicio de catálogo

## Automation Discipline

Cada historia de usuario se automatiza con respeto a principios estrictos de independencia y limpieza:

### Event Registration - Success Flow (@Before Hook)

```
@Before
1. POST /api/catalog/events → Crea evento con capacidad configurada
   Response: {"eventId": "evt-123", "availableSeats": 50}
2. Almacenar eventId en contexto de escenario para referencia
3. Opcionalmente: Esperar a que recursos de UI estén listos
```

### Event Registration - Success Flow (Escenario Positivo)

```
1. Navegar a EventPage
2. Ingresar nombre: "Concert Night 2026"
3. Ingresar descripción: "Amazing live concert event"
4. Ingresar fecha: fecha futura válida
5. Ingresar ubicación: "Concert Hall A"
6. Ingresar capacidad: "100"
7. Click en Submit
8. ASSERT: Mensaje de éxito visible en UI
9. Navegar a EventListPage
10. ASSERT: Evento aparece en lista con datos correctos
```

### Event Registration - Error Flow (@Before Hook)

```
@Before
1. POST /api/catalog/events → Crea evento de prueba
2. Almacenar eventId en contexto
```

### Event Registration - Error Flow (Escenario Negativo)

```
1. Navegar a EventPage
2. Intentar enviar formulario sin llenar nombre
3. ASSERT: Mensaje "Campo nombre es requerido" visible
4. ASSERT: Evento NO fue creado (ni en listado ni en DB)
5. Evento creado en @Before NO debe ser modificado
```

### Event Registration - Cleanup (@After Hook)

```
@After (ejecuta siempre, incluso con failures)
1. IF eventId existe en contexto:
   DELETE /api/catalog/events/{eventId}
2. Limpiar token de autenticación
3. Cerrar sesión (logout)
```

### Pruebas Independientes

- **Escenario 1 y 2 NO comparten datos**: Cada uno crea su propio evento en @Before
- **Ejecutar Escenario 1 sin Escenario 2**: Funciona completo, crea y limpia su evento
- **Ejecutar Escenario 2 sin Escenario 1**: Funciona completo, muestra error sin depender de eventos previos
- **Ejecutar ambos 10 veces**: Cada repetición limpia y recrea sus datos

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El framework DEBE ejecutar ambos escenarios (positivo y negativo) de forma independiente sin compartir estado, alcanzando 100% de independencia entre pruebas

- **SC-002**: Cada escenario DEBE completar ejecución (con o sin assertions pasadas) en menos de 30 segundos incluyendo setup y cleanup

- **SC-003**: El framework DEBE generar reportes HTML con capturas de pantalla automáticas, permitiendo a cualquier miembro del equipo entender fallos sin reexecución

- **SC-004**: El código de automatización DEBE tener 100% de adherencia a estándares POM:
  - Cero selectores hardcodeados en step definitions (todos en SelectorConstants)
  - Cero instanciación directa de Page Objects en steps (uso obligatorio de PageObjectFactory)
  - Cero lógica de negocio en Page Objects (solo interacción UI)

- **SC-005**: El framework DEBE tener máximo 2 niveles de dependencia entre clases:
  - Nivel 0: Constantes (SelectorConstants, UrlConstants, etc.)
  - Nivel 1: Page Objects (usan constantes)
  - Nivel 2: Step Definitions y Hooks (usan factories y pages)

- **SC-006**: Los escenarios DEBEN ser reutilizables sin modificación, alcanzándose cobertura de mínimo 2 casos de negocio (happy path y error handling)

- **SC-007**: La suite de pruebas completa DEBE ejecutarse en menos de 2 minutos, permitiendo feedback rápido en CI/CD

- **SC-008**: Todos los selectores UI DEBEN validarse contra `shared-specs/frontend/components.json` con coincidencia 100% en nombres y ubicaciones

- **SC-009**: Todos los endpoints API utilizados en hooks DEBEN corresponder a contratos en `shared-specs/contracts/openapi/catalog.yaml`

## Assumptions

- **A-001**: Se asume que existe un servidor de aplicación corriendo localmente en `http://localhost:3000` con la interfaz de registro de eventos accesible
- **A-002**: Se asume que el servicio `catalog` API está disponible en `http://localhost:8080/api/catalog` para crear/eliminar eventos en hooks
- **A-003**: Se asume que el navegador Chrome está instalado como navegador por defecto para ejecución local
- **A-004**: Se asume que existen credenciales de autenticación válidas disponibles como variables de entorno o en archivo de configuración
- **A-005**: Se asume que los elementos UI utilizarán atributos estables (id, name, data-testid) en lugar de solo clases CSS dinámicas
- **A-006**: Se asume que los tiempos de carga de página están entre 2-5 segundos en entornos normales
- **A-007**: Se asume que los hooks @Before/@After pueden realizar operaciones HTTP sin restricciones de CORS en entorno de desarrollo
- **A-008**: Se asume que la data creada en hooks no requiere validaciones complejas; la API sola es fuente de verdad
- **A-009**: Se asume que se mantendrá compatibilidad con Java 11+ como versión mínima de JDK
- **A-010**: Se asume que Gradlume que existen credenciales de autenticación válidas disponibles como variables de entorno o en archivo de configuración
- **A-005**: Se asume que los elementos UI utilizarán atributos estables (id, name, data-testid) en lugar de solo clases CSS dinámicas
- **A-006**: Se asume que los tiempos de carga de página están entre 2-5 segue versión 7.x+ está disponible en el ciclo de build
