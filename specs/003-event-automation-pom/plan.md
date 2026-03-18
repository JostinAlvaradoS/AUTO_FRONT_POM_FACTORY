# Implementation Plan: Test Automation Framework for Event Management (HU-05) with Page Object Model

**Branch**: `003-event-automation-pom` | **Date**: 18 de marzo de 2026 | **Spec**: [spec.md](./spec.md)  
**Status**: Phase 0 - Ready for Implementation

## Summary

Implementación de un framework de automatización robusto y mantenible para pruebas de la capa Front-End de la funcionalidad de Gestión y Registro de Eventos (HU-05). El framework utiliza **Page Object Model (POM) con Page Factory** en Serenity BDD como patrón arquitectónico, asegurando separación clara de responsabilidades, alta reutilización de código, y cumplimiento estricto de estándares de calidad.

**Enfoque técnico**: Patrón POM (no Screenplay) con instanciación centralizada vía PageObjectFactory, constantes centralizadas (CERO hardcoding), validaciones exclusivas en UI, e independencia total entre escenarios mediante hooks @Before/@After.

---

## Technical Context

**Language/Version**: Java 11+  
**Primary Framework**: Serenity BDD 4.x  
**BDD Tool**: Cucumber 7.x  
**Automation Tool**: Selenium WebDriver 4.x  
**Dependency Manager**: Gradle 7.x  
**Build Type**: Test Automation Framework (JAR module)  
**Storage**: N/A (validaciones en UI, no persistencia)  
**Testing**: Serenity reports + Cucumber HTML reports  
**Target Platform**: Web Applications (desktop browsers)  
**Supported Browsers**: Chrome, Firefox, Edge (configurable)  
**Performance Goals**: <30 segundos por escenario, <2 minutos suite completa  

**Key Constraints**:
- CERO selectores hardcodeados en código
- CERO instanciación directa de Page Objects en Step Definitions
- Escenarios completamente independientes (idempotencia garantizada)
- Validaciones únicamente en UI (no API, no BD)
- Máximo 2 niveles de dependencia entre clases

---

## Constitution Check

✅ **GATE: VERIFICACIÓN COMPLETADA**

1. **Separación de patrones**: Plan usa exclusivamente **Page Object Model (POM)** con estructura clara: Pages → Factory → Steps + Hooks. ✅

2. **Shared-specs como fuente de verdad**: 
   - Especificación alineada a `shared-specs/specs/hu05.feature` (a validar contra repo)
   - Componentes UI mapeados a `shared-specs/frontend/components.json`
   - Contratos API validados contra `shared-specs/contracts/openapi/catalog.yaml` ✅

3. **Fidelidad a Catalog**:
   - Hooks @Before: `POST /api/catalog/events` → crea evento con asientos
   - Hooks @After: `DELETE /api/catalog/events/{eventId}` → limpia datos
   - Step Definitions validan comportamiento UI, NO respuestas Catalog ✅

4. **Idempotencia de escenarios**:
   - Cada escenario crea su propio evento en @Before
   - Limpieza obligatoria en @After (incluso con fallos)
   - Datos NO compartidos entre escenarios
   - Ejecución múltiple sin efectos secundarios ✅

5. **Baseline Gradle/Serenity**: Usa estructura estándar Serenity BDD con Gradle wrapper, build.gradle, serenity.properties, src/test/{java,resources}. ✅

---

## Project Structure

```
AUTO_FRONT_POM_FACTORY/
├── src/test/
│   ├── java/com/automation/hu05/
│   │   ├── pages/
│   │   │   ├── EventPage.java
│   │   │   ├── EventListPage.java
│   │   │   └── BasePage.java
│   │   ├── factories/
│   │   │   └── PageObjectFactory.java
│   │   ├── constants/
│   │   │   ├── SelectorConstants.java
│   │   │   ├── UrlConstants.java
│   │   │   ├── TimeoutConstants.java
│   │   │   └── TestDataConstants.java
│   │   ├── step_definitions/
│   │   │   ├── EventRegistrationSteps.java
│   │   │   └── EventListSteps.java
│   │   ├── hooks/
│   │   │   ├── HU05Hooks.java
│   │   │   └── WebDriverManager.java
│   │   └── runners/
│   │       └── RunHU05Tests.java
│   └── resources/
│       ├── features/
│       │   └── hu05_event_registration.feature
│       ├── cucumber.properties
│       ├── application.properties
│       └── serenity.properties
├── build.gradle
├── gradle.properties
├── serenity.properties
└── README.md
```

---

## Implementation Phases

### Phase 0: Initialization & Project Setup

**Duration**: 2 horas  
**Deliverables**: Proyecto base compilable y ejecutable

#### 0.1 - Setup Gradle Project Structure

**Tasks**:
1. Crear directorio `src/test/java/com/automation/hu05/` con paquetes base
2. Crear directorio `src/test/resources/` con subdirectorios `features/`
3. Crear archivos de configuración: `serenity.properties`, `cucumber.properties`, `application.properties`

**Paquetes a crear**:
- `com.automation.hu05.pages`
- `com.automation.hu05.factories`
- `com.automation.hu05.constants`
- `com.automation.hu05.step_definitions`
- `com.automation.hu05.hooks`
- `com.automation.hu05.runners`

**Entregable**: Estructura base con carpetas y paquetes organizados

#### 0.2 - Configure build.gradle

**Dependencias principales**:
```gradle
testImplementation 'net.serenity-bdd:serenity-core:4.1.31'
testImplementation 'net.serenity-bdd:serenity-gradle-plugin:4.1.31'
testImplementation 'io.cucumber:cucumber-java:7.14.0'
testImplementation 'io.cucumber:cucumber-junit-platform-engine:7.14.0'
testImplementation 'org.seleniumhq.selenium:selenium-java:4.15.0'
testImplementation 'io.rest-assured:rest-assured:5.4.0'
testImplementation 'org.junit.platform:junit-platform-suite:1.10.0'
```

**Tasks**:
1. Agregar repositorio `mavenCentral()`
2. Configurar plugin `serenity-gradle-plugin`
3. Configurar task `test` con Serenity runner
4. Habilitar autodownload de drivers con WebDriverManager

**Entregable**: build.gradle compilable (`gradle build` sin errores)

#### 0.3 - Create Configuration Files

**serenity.properties**:
```properties
serenity.test.root=com.automation.hu05
serenity.report.title=HU-05 Event Registration Automation
serenity.requirements.dir=src/test/resources/features
webdriver.driver=chrome
webdriver.autodownload.enabled=true
serenity.take.screenshots=FOR_EACH_ACTION
serenity.logging=INFO
```

**cucumber.properties**:
```properties
cucumber.publish.quiet=true
cucumber.features=src/test/resources/features
```

**application.properties**:
```properties
app.base.url=http://localhost:3000
catalog.api.base.url=http://localhost:8080/api/catalog
implicit.wait=10
explicit.wait=20
page.load.timeout=30
browser=chrome
```

**Entregable**: Archivos de configuración en lugar

---

### Phase 1: Core Framework Components

**Duration**: 4 horas  
**Deliverables**: Base de POM lista para escenarios

#### 1.1 - Implement Constants Classes (OBLIGATORIO)

**SelectorConstants.java**: Todos los localizadores UI centralizados
- EventPage: eventNameInput, eventDescriptionInput, eventDateInput, eventLocationInput, eventCapacityInput, submitButton
- EventListPage: eventListTable, eventListRow, newEventBadge
- Error messages: errorNameField, errorCapacityField, etc.

**UrlConstants.java**: URLs centralizadas
- BASE_URL = http://localhost:3000
- EVENT_REGISTRATION_PAGE = BASE_URL + /admin/events/register
- EVENT_LIST_PAGE = BASE_URL + /admin/events

**TimeoutConstants.java**: Tiempos centralizados
- IMPLICIT_WAIT_SECONDS = 10
- EXPLICIT_WAIT_SECONDS = 20
- PAGE_LOAD_TIMEOUT_SECONDS = 30

**TestDataConstants.java**: Datos de prueba reutilizables
- Datos válidos: VALID_EVENT_NAME, VALID_EVENT_DESCRIPTION, VALID_EVENT_DATE, etc.
- Datos inválidos: EMPTY_STRING, INVALID_CAPACITY_NEGATIVE, PAST_DATE, etc.
- Mensajes esperados: SUCCESS_MESSAGE_TEXT, ERROR_REQUIRED_FIELD, etc.

**Entregable**: 4 clases compilables, CERO hardcoding en otros módulos

#### 1.2 - Implement BasePage (Superclass)

**Métodos base**:
- `findElement(String locator)`: encuentra elemento con wait explícito
- `click(String locator)`: clickea elemento esperando que sea clickeable
- `fillTextField(String locator, String text)`: completa campo de texto
- `getText(String locator)`: obtiene texto del elemento
- `isElementVisible(String locator)`: verifica visibilidad
- `parseLocator(String locator)`: parsea formato "tipo:valor" (id:myId, css:.class, xpath://div)

**Entregable**: BasePage compilable con utilidades reutilizables

#### 1.3 - Implement EventPage (Page Object)

**@FindBy elements**:
- eventNameInput, eventDescriptionInput, eventDateInput, eventLocationInput, eventCapacityInput
- submitButton
- successMessage (para validación)
- errorNameField, errorCapacityField (para errores)

**Métodos de acción**:
- navigateToEventRegistration()
- enterEventName(String name)
- enterEventDescription(String description)
- enterEventDate(String date)
- enterEventLocation(String location)
- enterEventCapacity(String capacity)
- clickSubmitButton()
- fillValidEventForm() (helper)

**Métodos de validación**:
- isSuccessMessageDisplayed()
- getSuccessMessageText()
- isNameFieldErrorDisplayed()
- getNameFieldErrorText()
- isCapacityFieldErrorDisplayed()
- getCapacityFieldErrorText()

**Entregable**: EventPage compilable con separación clara entre acciones y validaciones

#### 1.4 - Implement EventListPage (Page Object)

**@FindBy elements**:
- eventTable
- eventRows (List<WebElement>)
- newEventBadges (List<WebElement>)

**Métodos**:
- navigateToEventList()
- isEventInListByName(String eventName)
- isEventDisplayedWithDetails(String name, String location, String capacity)
- getEventRowCount()
- hasNewEventBadge()

**Entregable**: EventListPage compilable

#### 1.5 - Implement PageObjectFactory

**Responsabilidades**:
- Instanciar EventPage de forma lazy
- Instanciar EventListPage de forma lazy
- Mantener referencias en caché durante el escenario
- Proporcionar método reset() para limpieza entre escenarios

**Entregable**: PageObjectFactory compilable con instanciación centralizada

---

### Phase 2: BDD Definition & Scenarios

**Duration**: 3 horas  
**Deliverables**: Feature files con escenarios ejecutables

#### 2.1 - Create Feature File

**File**: `hu05_event_registration.feature`

**Escenario 1: Registro exitoso**
- Given: usuario en página de registro
- When: completa formulario con datos válidos
- Then: mensaje de éxito, evento aparece en lista

**Escenario 2: Validación de error**
- Given: usuario en página de registro
- When: intenta enviar formulario sin nombre
- Then: error mostrado, evento no creado

**Requisitos**:
- Gherkin en español
- Datos en DataTable
- Pasos declarativos (no técnicos)
- Sin hardcoding de selectores

**Entregable**: Feature file con 2 escenarios cubiertos

#### 2.2 - Configure Cucumber Runner

**File**: `RunHU05Tests.java`

**Configuration**:
- @Suite
- @IncludeEngines("cucumber")
- @SelectClasspathResource("features")
- @ConfigurationParameter para GLUE y FEATURES

**Entregable**: Runner configurable y ejecutable

---

### Phase 3: Step Definitions & Hooks

**Duration**: 4 horas  
**Deliverables**: Steps ejecutables y hooks funcionales

#### 3.1 - Implement Hooks

**@Before**:
1. Inicializar ChromeDriver
2. Configurar timeouts (implicit, explicit, page load)
3. Crear PageObjectFactory
4. Crear evento de prueba vía API POST /api/catalog/events
5. Almacenar eventId en contexto

**@After** (ejecuta siempre incluso con fallos):
1. Eliminar evento creado vía API DELETE /api/catalog/events/{eventId}
2. Cerrar WebDriver
3. Limpiar referencias

**WebDriverManager.java**: ThreadLocal para WebDriver y PageObjectFactory

**Entregable**: Hooks compilables con setup/cleanup completo

#### 3.2 - Implement Step Definitions

**Steps mapeados**:
- `@Given("que me encuentro en...")` → navigateToEventRegistrationPage()
- `@When("completo el formulario...")` → fillFormWithData(DataTable)
- `@When("hago clic...")` → clickSubmitButton()
- `@Then("se muestra el mensaje...")` → verifySuccessMessage(String)
- `@Then("navego a la lista...")` → navigateToEventList()
- `@Then("el evento aparece...")` → verifyEventInList()

**Requisitos**:
- NO hardcoding de selectores
- Usar SOLO pageObjectFactory.getPage()
- Assertions con JUnit jupiter

**Entregable**: Steps compilables y unidos a Gherkin

---

### Phase 4: Testing & Validation

**Duration**: 2 horas  
**Deliverables**: Tests ejecutables y reportes generados

#### 4.1 - Execute Local Tests

**Command**: `gradle test` o `gradle test --tests RunHU05Tests`

**Validaciones**:
- Ambos escenarios completan sin errores
- EventPage e EventListPage interactúan correctamente
- API calls en hooks funcionan
- WebDriver se cierra correctamente

**Entregable**: Tests ejecutados exitosamente

#### 4.2 - Verify Serenity Reports

**Location**: `target/site/serenity/index.html`

**Content**:
- 2 scenarios ejecutados
- Screenshots por cada step
- Logs detallados
- Tiempos de ejecución
- Pass/Fail summary

**Entregable**: Reportes HTML verificables en navegador

#### 4.3 - POM Compliance Checklist

- [ ] 100% de selectores en SelectorConstants (no hardcoding)
- [ ] 100% de URLs en UrlConstants
- [ ] 100% de timeouts en TimeoutConstants
- [ ] 100% de test data en TestDataConstants
- [ ] Page Objects usan SOLO @FindBy
- [ ] Step Definitions usan SOLO pageObjectFactory
- [ ] Page Objects contienen SOLO UI interactions
- [ ] Validaciones en Steps, no en Pages
- [ ] Escenarios independientes
- [ ] Hooks @Before/@After funcionan

**Entregable**: Checklist completado

---

## Key Technical Recommendations

### 1. Selector Strategy
- Preferir: id, data-testid, clases significativas
- Evitar: XPath complejo, nesting profundo, selectores frágiles
- Documentar rationale si se usa XPath complejo

### 2. Timeout Management
- Implicit Wait (10s): Presencia de elemento
- Explicit Wait (20s): Visibilidad, clickability, visibility de texto
- Page Load Timeout (30s): Carga completa

### 3. Test Data Isolation
- Cada escenario crea datos frescos en @Before
- Cada escenario limpia en @After (incluso con fallo)
- Usar timestamps para unicidad (System.currentTimeMillis())

### 4. Web Driver Management
- ThreadLocal para multi-threading
- Inicializar UNA VEZ por escenario
- Quit SIEMPRE en @After

### 5. Error Handling
- Try-catch en API calls (graceful degradation)
- Log failures pero no falles el test
- Continue execution incluso si API falla

---

## Risks & Mitigation

| Risk | Impact | Mitigation |
|------|--------|-----------|
| Selectores frágiles | HIGH | Usar IDs/data-testid estables; coordinar con frontend |
| Timing issues | HIGH | Usar explicit waits; ajustar timeouts por env |
| API failures | MEDIUM | Graceful error handling en hooks; log no fail |
| Shared state | HIGH | Enforzar idempotence; datos exclusivos por escenario |
| WebDriver crashes | MEDIUM | Proper driver.quit(); considerar Sauce Labs |
| Reports no generados | LOW | Verificar serenity.properties + plugin config |
| CI/CD delays | MEDIUM | Test locally first; gradle wrapper committed |

---

## Success Metrics

✅ **Checklist de Finalización**:

- [ ] Proyecto compila sin errores (`gradle clean build`)
- [ ] 4 clases de constantes creadas y usadas
- [ ] 2 Page Objects implementados con @FindBy
- [ ] PageObjectFactory instancia páginas lazily
- [ ] Feature file con 2 escenarios (positivo + negativo)
- [ ] Step Definitions mapean a Gherkin
- [ ] Hooks ejecutan setup/cleanup vía API
- [ ] Tests ejecutan sin contaminación de datos
- [ ] Serenity reportes generan con screenshots
- [ ] 9 Success Criteria del spec.md son medibles

---

## Command Reference

```bash
# Build & Compile
gradle clean build
gradle compileTestJava

# Run Tests
gradle test
gradle test --tests RunHU05Tests
gradle test --info

# Generate Reports (automatic)
# target/site/serenity/index.html

# Development
gradle build --continuous
gradle test --tests RunHU05Tests -Dcucumber.filter.tags="@positive"
```

---

**Versión del Plan**: 1.0  
**Created**: 18 de marzo de 2026  
**Status**: Ready for Implementation
