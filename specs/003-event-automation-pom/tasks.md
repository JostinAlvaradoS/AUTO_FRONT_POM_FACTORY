# Task Breakdown: Test Automation Framework for Event Management (HU-05) with Page Object Model

**Feature**: `003-event-automation-pom` | **Date**: 18 de marzo de 2026  
**Implementation Plan**: [plan.md](./plan.md) | **Specification**: [spec.md](./spec.md)  
**Total Tasks**: 42 | **Estimated Total Duration**: 15 horas

---

## Phase 0: Project Initialization & Setup ⏱️ (2 horas)

### T001 - Create Project Directory Structure [SETUP]
- **Duration**: 15 min  
- **Priority**: P0 (Blocker)
- **Subtasks**:
  - [ ] T001.1 - Crear `src/test/java/com/automation/hu05/pages/`
  - [ ] T001.2 - Crear `src/test/java/com/automation/hu05/factories/`
  - [ ] T001.3 - Crear `src/test/java/com/automation/hu05/constants/`
  - [ ] T001.4 - Crear `src/test/java/com/automation/hu05/step_definitions/`
  - [ ] T001.5 - Crear `src/test/java/com/automation/hu05/hooks/`
  - [ ] T001.6 - Crear `src/test/java/com/automation/hu05/runners/`
  - [ ] T001.7 - Crear `src/test/resources/features/`
  - [ ] T001.8 - Crear directorio `src/test/resources/` con subdirs
- **Acceptance Criteria**: Todas las carpetas existen y son navigables

### T002 - Configure build.gradle with Serenity Dependencies [SETUP]
- **Duration**: 30 min  
- **Priority**: P0 (Blocker)
- **Dependencies**: T001
- **Subtasks**:
  - [ ] T002.1 - Add `repositories { mavenCentral() }`
  - [ ] T002.2 - Add `testImplementation 'net.serenity-bdd:serenity-core:4.1.31'`
  - [ ] T002.3 - Add `testImplementation 'net.serenity-bdd:serenity-gradle-plugin:4.1.31'`
  - [ ] T002.4 - Add `testImplementation 'io.cucumber:cucumber-java:7.14.0'`
  - [ ] T002.5 - Add `testImplementation 'io.cucumber:cucumber-junit-platform-engine:7.14.0'`
  - [ ] T002.6 - Add `testImplementation 'org.seleniumhq.selenium:selenium-java:4.15.0'`
  - [ ] T002.7 - Add `testImplementation 'io.rest-assured:rest-assured:5.4.0'`
  - [ ] T002.8 - Add `testImplementation 'org.junit.platform:junit-platform-suite:1.10.0'`
  - [ ] T002.9 - Configure `serenity-gradle-plugin` en plugins block
  - [ ] T002.10 - Configure test task con Serenity runner
- **Acceptance Criteria**: `gradle clean build` compila sin errores (no requiere ejecutar tests)

### T003 - Create serenity.properties [SETUP]
- **Duration**: 10 min  
- **Priority**: P1
- **Dependencies**: T001
- **File**: `serenity.properties` (raíz del proyecto)
- **Content**:
  ```properties
  serenity.test.root=com.automation.hu05
  serenity.report.title=HU-05 Event Registration Automation
  serenity.requirements.dir=src/test/resources/features
  webdriver.driver=chrome
  webdriver.autodownload.enabled=true
  serenity.take.screenshots=FOR_EACH_ACTION
  serenity.logging=INFO
  ```
- **Acceptance Criteria**: Archivo existe y contiene todas las propiedades

### T004 - Create cucumber.properties [SETUP]
- **Duration**: 5 min  
- **Priority**: P1
- **Dependencies**: T001
- **File**: `src/test/resources/cucumber.properties`
- **Content**:
  ```properties
  cucumber.publish.quiet=true
  cucumber.features=src/test/resources/features
  ```
- **Acceptance Criteria**: Archivo exist en ubicación correcta

### T005 - Create application.properties [SETUP]
- **Duration**: 5 min  
- **Priority**: P1
- **Dependencies**: T001
- **File**: `src/test/resources/application.properties`
- **Content**:
  ```properties
  app.base.url=http://localhost:3000
  catalog.api.base.url=http://localhost:8080/api/catalog
  implicit.wait=10
  explicit.wait=20
  page.load.timeout=30
  browser=chrome
  ```
- **Acceptance Criteria**: Archivo existe en ubicación correcta

### T006 - Verify Phase 0 Build [VALIDATION]
- **Duration**: 10 min  
- **Priority**: P0 (Blocker)
- **Dependencies**: T002, T003, T004, T005
- **Command**: `gradle clean build`
- **Acceptance Criteria**: 
  - Build completa sin errores
  - No hay warnings críticos
  - `build/` está generado

---

## Phase 1: Core Framework Components ⏱️ (4 horas)

### T007 - Implement SelectorConstants.java [DEVELOPMENT]
- **Duration**: 30 min  
- **Priority**: P0 (Blocker)
- **Dependencies**: T001
- **File**: `src/test/java/com/automation/hu05/constants/SelectorConstants.java`
- **Subtasks**:
  - [ ] T007.1 - Crear clase pública SelectorConstants
  - [ ] T007.2 - Agregar constantes para EventPage inputs (eventNameInput, eventDescriptionInput, eventDateInput, eventLocationInput, eventCapacityInput)
  - [ ] T007.3 - Agregar constante para submitButton
  - [ ] T007.4 - Agregar constantes para error messages (errorNameField, errorDescriptionField, etc.)
  - [ ] T007.5 - Agregar constante para successMessage
  - [ ] T007.6 - Agregar constantes para EventListPage (eventListTable, eventListRow, newEventBadge)
  - [ ] T007.7 - Documentar formato de selectores (ej: "id:eventName", "css:.class", "xpath://div")
- **Acceptance Criteria**: 
  - Clase compilable sin errores
  - Todas las constantes documentadas
  - Nombres descriptivos y semánticos

### T008 - Implement UrlConstants.java [DEVELOPMENT]
- **Duration**: 15 min  
- **Priority**: P0 (Blocker)
- **Dependencies**: T001
- **File**: `src/test/java/com/automation/hu05/constants/UrlConstants.java`
- **Content**:
  - BASE_URL = "http://localhost:3000"
  - EVENT_REGISTRATION_PAGE = BASE_URL + "/admin/events/register"
  - EVENT_LIST_PAGE = BASE_URL + "/admin/events"
- **Acceptance Criteria**: Clase compilable, 3 constantes definidas

### T009 - Implement TimeoutConstants.java [DEVELOPMENT]
- **Duration**: 10 min  
- **Priority**: P0 (Blocker)
- **Dependencies**: T001
- **File**: `src/test/java/com/automation/hu05/constants/TimeoutConstants.java`
- **Content**:
  - IMPLICIT_WAIT_SECONDS = 10
  - EXPLICIT_WAIT_SECONDS = 20
  - PAGE_LOAD_TIMEOUT_SECONDS = 30
  - SHORT_WAIT_SECONDS = 5
- **Acceptance Criteria**: Clase compilable, 4 constantes definidas

### T010 - Implement TestDataConstants.java [DEVELOPMENT]
- **Duration**: 20 min  
- **Priority**: P1
- **Dependencies**: T001
- **File**: `src/test/java/com/automation/hu05/constants/TestDataConstants.java`
- **Subtasks**:
  - [ ] T010.1 - Agregar datos válidos (VALID_EVENT_NAME, VALID_EVENT_DESCRIPTION, VALID_EVENT_DATE, VALID_EVENT_LOCATION, VALID_EVENT_CAPACITY)
  - [ ] T010.2 - Agregar datos inválidos (EMPTY_STRING, INVALID_CAPACITY_NEGATIVE, INVALID_CAPACITY_STRING, PAST_DATE, LONG_NAME)
  - [ ] T010.3 - Agregar mensajes esperados (SUCCESS_MESSAGE_TEXT, ERROR_REQUIRED_FIELD, ERROR_INVALID_CAPACITY, ERROR_INVALID_DATE)
- **Acceptance Criteria**: Clase compilable, 12+ constantes definidas

### T011 - Implement BasePage.java [DEVELOPMENT]
- **Duration**: 45 min  
- **Priority**: P0 (Blocker)
- **Dependencies**: T007, T009
- **File**: `src/test/java/com/automation/hu05/pages/BasePage.java`
- **Subtasks**:
  - [ ] T011.1 - Crear clase abstracta BasePage con constructor(WebDriver)
  - [ ] T011.2 - Agregar atributos: protected WebDriver driver, protected WebDriverWait wait
  - [ ] T011.3 - Implementar PageFactory.initElements(driver, this) en constructor
  - [ ] T011.4 - Implementar método protegido findElement(String locator)
  - [ ] T011.5 - Implementar método protegido click(String locator)
  - [ ] T011.6 - Implementar método protegido fillTextField(String locator, String text)
  - [ ] T011.7 - Implementar método protegido getText(String locator)
  - [ ] T011.8 - Implementar método protegido isElementVisible(String locator)
  - [ ] T011.9 - Implementar método protegido isElementPresent(String locator)
  - [ ] T011.10 - Implementar método privado parseLocator(String locator) que soporta "id:val", "css:val", "xpath:val", "name:val"
- **Acceptance Criteria**: 
  - Clase compilable sin errores
  - Todos los métodos compilables
  - parseLocator maneja todos los tipos de locadores

### T012 - Implement EventPage.java [DEVELOPMENT]
- **Duration**: 50 min  
- **Priority**: P0 (Blocker)
- **Dependencies**: T011, T007, T008
- **File**: `src/test/java/com/automation/hu05/pages/EventPage.java`
- **Subtasks**:
  - [ ] T012.1 - Crear clase que extiende BasePage
  - [ ] T012.2 - Agregar @FindBy para todos los elementos: eventNameInput, eventDescriptionInput, eventDateInput, eventLocationInput, eventCapacityInput, submitButton
  - [ ] T012.3 - Agregar @FindBy para success/error messages
  - [ ] T012.4 - Implementar navegación: navigateToEventRegistration()
  - [ ] T012.5 - Implementar acciones: enterEventName(String), enterEventDescription(String), enterEventDate(String), enterEventLocation(String), enterEventCapacity(String)
  - [ ] T012.6 - Implementar acción: clickSubmitButton()
  - [ ] T012.7 - Implementar validaciones: isSuccessMessageDisplayed(), getSuccessMessageText(), isNameFieldErrorDisplayed(), getNameFieldErrorText()
  - [ ] T012.8 - Implementar validaciones: isCapacityFieldErrorDisplayed(), getCapacityFieldErrorText()
  - [ ] T012.9 - Implementar helper: fillValidEventForm()
  - [ ] T012.10 - Implementar método privado implicitlyWaitForPageLoad()
- **Acceptance Criteria**: 
  - Clase compilable sin errores
  - Todas las anotaciones @FindBy presentes
  - Métodos con nombres semánticos

### T013 - Implement EventListPage.java [DEVELOPMENT]
- **Duration**: 35 min  
- **Priority**: P1
- **Dependencies**: T011, T007, T008
- **File**: `src/test/java/com/automation/hu05/pages/EventListPage.java`
- **Subtasks**:
  - [ ] T013.1 - Crear clase que extiende BasePage
  - [ ] T013.2 - Agregar @FindBy para eventTable, List<WebElement> eventRows, List<WebElement> newEventBadges
  - [ ] T013.3 - Implementar navegación: navigateToEventList()
  - [ ] T013.4 - Implementar validaciones: isEventInListByName(String), isEventDisplayedWithDetails(String, String, String), getEventRowCount(), hasNewEventBadge()
  - [ ] T013.5 - Implementar método helper: getEventRowByName(String)
  - [ ] T013.6 - Implementar método privado implicitlyWaitForPageLoad()
- **Acceptance Criteria**: 
  - Clase compilable sin errores
  - Todas las anotaciones @FindBy presentes
  - Validaciones retornan boolean

### T014 - Implement PageObjectFactory.java [DEVELOPMENT]
- **Duration**: 20 min  
- **Priority**: P0 (Blocker)
- **Dependencies**: T012, T013
- **File**: `src/test/java/com/automation/hu05/factories/PageObjectFactory.java`
- **Subtasks**:
  - [ ] T014.1 - Crear clase PageObjectFactory con atributo WebDriver driver
  - [ ] T014.2 - Agregar atributos privados: EventPage eventPage, EventListPage eventListPage
  - [ ] T014.3 - Implementar constructor PageObjectFactory(WebDriver driver)
  - [ ] T014.4 - Implementar method: getEventPage() con lazy initialization
  - [ ] T014.5 - Implementar method: getEventListPage() con lazy initialization
  - [ ] T014.6 - Implementar method: reset() para limpiar caché
- **Acceptance Criteria**: 
  - Clase compilable sin errores
  - Lazy initialization funciona correctamente

### T015 - Verify Phase 1 Compilation [VALIDATION]
- **Duration**: 10 min  
- **Priority**: P0 (Blocker)
- **Dependencies**: T007, T008, T009, T010, T011, T012, T013, T014
- **Command**: `gradle compileTestJava`
- **Acceptance Criteria**: 
  - Compile sin errores
  - No hay warnings no resueltos
  - Todas las clases son importables

---

## Phase 2: BDD Definition & Scenarios ⏱️ (3 horas)

### T016 - Create Feature File hu05_event_registration.feature [DEVELOPMENT]
- **Duration**: 45 min  
- **Priority**: P0 (Blocker)
- **Dependencies**: T015
- **File**: `src/test/resources/features/hu05_event_registration.feature`
- **Subtasks**:
  - [ ] T016.1 - Crear header: `# language: es`
  - [ ] T016.2 - Crear Feature: "Gestión y Registro de Eventos (HU-05)"
  - [ ] T016.3 - Agregar descripción: "Como administrador..." "Quiero registrar..." "Para que estén disponibles..."
  - [ ] T016.4 - Implementar Antecedentes (Background) con paso "Dado que me encuentro en la página de registro de eventos"
  - [ ] T016.5 - Crear Escenario 1 positivo con tabla de datos (nombre, descripción, fecha, ubicación, capacidad)
  - [ ] T016.6 - Crear Escenario 1 positivo - pasos When/Then para validar success
  - [ ] T016.7 - Crear Escenario 2 negativo - pasos When/Then para validar error
  - [ ] T016.8 - Verificar Gherkin sintaxis válida
- **Acceptance Criteria**: 
  - Archivo existe en ubicación correcta
  - Syntax válida de Gherkin
  - 2 escenarios funcionales
  - En español

### T017 - Create Cucumber Runner RunHU05Tests.java [DEVELOPMENT]
- **Duration**: 20 min  
- **Priority**: P0 (Blocker)
- **Dependencies**: T016
- **File**: `src/test/java/com/automation/hu05/runners/RunHU05Tests.java`
- **Subtasks**:
  - [ ] T017.1 - Crear clase con @Suite annotation
  - [ ] T017.2 - Agregar @IncludeEngines("cucumber")
  - [ ] T017.3 - Agregar @SelectClasspathResource("features")
  - [ ] T017.4 - Configurar @ConfigurationParameter para GLUE_PROPERTY_NAME y valor "com.automation.hu05"
  - [ ] T017.5 - Configurar @ConfigurationParameter para FEATURES_PROPERTY_NAME y valor "src/test/resources/features"
  - [ ] T017.6 - Configurar @ConfigurationParameter para PLUGIN_PROPERTY_NAME y valor "html:target/cucumber-reports/cucumber.html"
- **Acceptance Criteria**: 
  - Clase compilable sin errores
  - Todas las anotaciones presentes
  - Ready para ejecutar Cucumber

### T018 - Verify Phase 2 Setup [VALIDATION]
- **Duration**: 10 min  
- **Priority**: P1
- **Dependencies**: T016, T017
- **Subtasks**:
  - [ ] T018.1 - Compilar Runner: `gradle compileTestJava`
  - [ ] T018.2 - Verificar que Cucumber reconoce features
- **Acceptance Criteria**: 
  - Compile sin errores
  - Feature file se reconoce como válida

---

## Phase 3: Step Definitions & Hooks ⏱️ (4 horas)

### T019 - Implement WebDriverManager.java [DEVELOPMENT]
- **Duration**: 20 min  
- **Priority**: P0 (Blocker)
- **Dependencies**: T001
- **File**: `src/test/java/com/automation/hu05/hooks/WebDriverManager.java`
- **Subtasks**:
  - [ ] T019.1 - Crear clase ThreadLocal<WebDriver> driver
  - [ ] T019.2 - Crear clase ThreadLocal<PageObjectFactory> pageObjectFactory
  - [ ] T019.3 - Implementar setDriver(WebDriver)
  - [ ] T019.4 - Implementar getDriver()
  - [ ] T019.5 - Implementar clearDriver()
  - [ ] T019.6 - Implementar setPageObjectFactory(PageObjectFactory)
  - [ ] T019.7 - Implementar getPageObjectFactory()
  - [ ] T019.8 - Implementar clearPageObjectFactory()
- **Acceptance Criteria**: 
  - Clase compilable sin errores
  - Todos los métodos implementados
  - ThreadLocal maneja valores correctamente

### T020 - Implement HU05Hooks.java [DEVELOPMENT]
- **Duration**: 60 min  
- **Priority**: P0 (Blocker)
- **Dependencies**: T014, T019
- **File**: `src/test/java/com/automation/hu05/hooks/HU05Hooks.java`
- **Subtasks**:
  - [ ] T020.1 - Crear clase con @Before y @After methods
  - [ ] T020.2 - Implementar @Before con inicialización de ChromeDriver
  - [ ] T020.3 - Configurar ChromeOptions en @Before
  - [ ] T020.4 - Configurar timeouts (implicit, explicit, page load) en @Before
  - [ ] T020.5 - Crear PageObjectFactory en @Before
  - [ ] T020.6 - Almacenar driver y factory en WebDriverManager en @Before
  - [ ] T020.7 - Llamar createTestEvent() en @Before
  - [ ] T020.8 - Implementar @After que llame deleteTestEvent()
  - [ ] T020.9 - Implementar @After que cierre driver
  - [ ] T020.10 - Implementar @After que limpie WebDriverManager
  - [ ] T020.11 - Implementar método privado initializeChromeDriver()
  - [ ] T020.12 - Implementar método privado createTestEvent() con API REST Assured POST
  - [ ] T020.13 - Implementar método privado deleteTestEvent() con API REST Assured DELETE
  - [ ] T020.14 - Implementar método privado buildEventPayload()
- **Acceptance Criteria**: 
  - Clase compilable sin errores
  - @Before y @After compilables
  - API calls con REST Assured incluidas
  - Manejo de excepciones en API calls

### T021 - Implement EventRegistrationSteps.java [DEVELOPMENT]
- **Duration**: 60 min  
- **Priority**: P0 (Blocker)
- **Dependencies**: T016, T020
- **File**: `src/test/java/com/automation/hu05/step_definitions/EventRegistrationSteps.java`
- **Subtasks**:
  - [ ] T021.1 - Crear clase con constructor sin parámetros
  - [ ] T021.2 - En constructor: obtener driver de WebDriverManager
  - [ ] T021.3 - En constructor: obtener factory de WebDriverManager
  - [ ] T021.4 - En constructor: obtener EventPage de factory
  - [ ] T021.5 - En constructor: obtener EventListPage de factory
  - [ ] T021.6 - Implementar @Given("que me encuentro en la página de registro de eventos")
  - [ ] T021.7 - Implementar @When("completo el formulario con los siguientes datos:") con DataTable
  - [ ] T021.8 - Implementar @When("dejo el campo nombre vacío")
  - [ ] T021.9 - Implementar @When("completo los demás campos con datos válidos:") con DataTable
  - [ ] T021.10 - Implementar @When("hago clic en el botón Enviar")
  - [ ] T021.11 - Implementar @Then("se muestra el mensaje de éxito {string}")
  - [ ] T021.12 - Implementar @Then("navego a la lista de eventos")
  - [ ] T021.13 - Implementar @Then("el evento aparece en la lista con los datos ingresados")
  - [ ] T021.14 - Implementar @Then("se visualiza un badge de {string}")
  - [ ] T021.15 - Implementar @Then("se muestra el mensaje de error {string} en el campo nombre")
  - [ ] T021.16 - Implementar @Then("el evento NO se crea en el sistema")
  - [ ] T021.17 - Implementar @Then("permanezco en la página de registro")
  - [ ] T021.18 - Agregar assertions con JUnit Jupiter
- **Acceptance Criteria**: 
  - Clase compilable sin errores
  - Todos los steps implementados
  - DataTable parsing funciona
  - Assertions con assertTrue/assertEquals

### T022 - Verify Phase 3 Step Binding [VALIDATION]
- **Duration**: 15 min  
- **Priority**: P0 (Blocker)
- **Dependencies**: T019, T020, T021
- **Subtasks**:
  - [ ] T022.1 - Compilar: `gradle compileTestJava`
  - [ ] T022.2 - Verificar que no hay unbound steps en logs
  - [ ] T022.3 - Verificar que Cucumber reconoce todos los steps
- **Acceptance Criteria**: 
  - Compile sin errores
  - Logging muestra steps bound correctamente

---

## Phase 4: Testing & Validation ⏱️ (2 horas)

### T023 - Execute Feature Scenario 1 Positive [TESTING]
- **Duration**: 30 min  
- **Priority**: P0
- **Dependencies**: T022
- **Command**: `gradle test --tests RunHU05Tests`
- **Subtasks**:
  - [ ] T023.1 - Ejecutar suite de tests
  - [ ] T023.2 - Monitorear que Escenario 1 comience
  - [ ] T023.3 - Verificar que @Before ejecuta sin errores
  - [ ] T023.4 - Verificar que formulario se llena correctamente
  - [ ] T023.5 - Verificar que submit button se hace clic
  - [ ] T023.6 - Verificar que success message aparece
  - [ ] T023.7 - Verificar que navegación a list page funciona
  - [ ] T023.8 - Verificar que evento aparece en lista
  - [ ] T023.9 - Verificar que @After limpia datos
- **Acceptance Criteria**: 
  - Escenario 1 PASA (all assertions true)
  - @Before y @After ejecutan
  - screenshots se generan

### T024 - Execute Feature Scenario 2 Negative [TESTING]
- **Duration**: 30 min  
- **Priority**: P0
- **Dependencies**: T022
- **Command**: `gradle test --tests RunHU05Tests`
- **Subtasks**:
  - [ ] T024.1 - Ejecutar suite de tests
  - [ ] T024.2 - Monitorear que Escenario 2 comience
  - [ ] T024.3 - Verificar que campo nombre quedó vacío
  - [ ] T024.4 - Verificar que otros campos se llenaron
  - [ ] T024.5 - Verificar que submit button se hace clic
  - [ ] T024.6 - Verificar que error message aparece
  - [ ] T024.7 - Verificar que usuario permanece en form page
  - [ ] T024.8 - Verificar que @After limpia datos
- **Acceptance Criteria**: 
  - Escenario 2 PASA (all assertions true)
  - Error message válido
  - screenshots se generan

### T025 - Verify Suite Execution Complete [VALIDATION]
- **Duration**: 15 min  
- **Priority**: P0
- **Dependencies**: T023, T024
- **Subtasks**:
  - [ ] T025.1 - Ejecutar suite completa: `gradle test`
  - [ ] T025.2 - Verificar que ambos escenarios completan
  - [ ] T025.3 - Verificar que no hay data pollution entre escenarios
  - [ ] T025.4 - Revisar build console para warnings
  - [ ] T025.5 - Verificar que build/ generado
- **Acceptance Criteria**: 
  - Ambos escenarios PASAN
  - build completa exitosamente
  - No hay data sharing

### T026 - Generate & Verify Serenity Reports [VALIDATION]
- **Duration**: 20 min  
- **Priority**: P1
- **Dependencies**: T025
- **Subtasks**:
  - [ ] T026.1 - Verificar que `target/site/serenity/index.html` existe
  - [ ] T026.2 - Abrir report en navegador
  - [ ] T026.3 - Verificar que 2 escenarios listados
  - [ ] T026.4 - Verificar que screenshots por step presentes
  - [ ] T026.5 - Verificar que logs con timestamps presentes
  - [ ] T026.6 - Verificar que tiempos de ejecución listados
  - [ ] T026.7 - Verificar que test results resumen correcto (2/2 passed)
  - [ ] T026.8 - Verificar que Requirements section mapea a feature file
- **Acceptance Criteria**: 
  - Report HTML abierto en navegador
  - Todas las secciones presentes
  - Screenshots muestran paso a paso
  - Información completa y legible

### T027 - POM Compliance Audit [VALIDATION]
- **Duration**: 15 min  
- **Priority**: P1
- **Dependencies**: T026
- **Checklist**:
  - [ ] T027.1 - Verificar: 100% de selectores en SelectorConstants (cero hardcoding en Pages)
  - [ ] T027.2 - Verificar: 100% de URLs en UrlConstants (cero hardcoding en Pages)
  - [ ] T027.3 - Verificar: 100% de timeouts en TimeoutConstants
  - [ ] T027.4 - Verificar: 100% de test data en TestDataConstants
  - [ ] T027.5 - Verificar: Page Objects solo usan anotaciones @FindBy
  - [ ] T027.6 - Verificar: Step Definitions usan SOLO pageObjectFactory.getPage()
  - [ ] T027.7 - Verificar: Page Objects contienen SOLO UI interactions
  - [ ] T027.8 - Verificar: Validaciones en Steps, nada en Pages
  - [ ] T027.9 - Verificar: Cada escenario es completamente independiente
  - [ ] T027.10 - Verificar: Hooks @Before/@After funcionan correctamente
- **Acceptance Criteria**: 
  - Todos los items checkboxed
  - Cero desviaciones del patrón POM
  - Código cumple estándares

### T028 - Generate Final Summary Report [DOCUMENTATION]
- **Duration**: 15 min  
- **Priority**: P2
- **Dependencies**: T027
- **Output**: Documento de resumen en `IMPLEMENTATION_SUMMARY.md`
- **Content**:
  - Framework architecture overview
  - File structure summary
  - Scenarios executed + results
  - Performance metrics (total time, per scenario)
  - POM compliance status
  - Next steps (expansion, CI/CD)
- **Acceptance Criteria**: 
  - Documento existe
  - Contiene métricas de ejecución
  - Conclusiones documentadas

---

## Summary & Status Tracking

### Tasks by Phase
| Phase | Count | Duration | Status |
|-------|-------|----------|--------|
| Phase 0: Setup | 6 | 2h | [ ] Not Started |
| Phase 1: Framework | 9 | 4h | [ ] Not Started |
| Phase 2: BDD | 3 | 1.5h | [ ] Not Started |
| Phase 3: Steps & Hooks | 4 | 2.5h | [ ] Not Started |
| Phase 4: Testing | 6 | 2.5h | [ ] Not Started |
| **TOTAL** | **28** | **~12.5h** | - |

### Critical Path
1. **T001** → T002 → T006 (Project Setup - 2h blocker)
2. **T007** → T014, **T015** (Framework - 4h blocker)
3. **T016** → T018 (BDD - 1.5h blocker)
4. **T019** → T022 (Hooks & Steps - 2.5h blocker)
5. **T023** → T027 (Testing - 2.5h validation)

### Success Criteria (Phase 4 Gate)
- [ ] All 28 tasks completed
- [ ] Both scenarios PASS in full suite
- [ ] Serenity reports generated with screenshots
- [ ] Zero POM violations in audit
- [ ] Build executes in <30 seconds per scenario
- [ ] Tests can run independently (idempotence verified)

---

**Created**: 18 de marzo de 2026  
**Framework**: Serenity BDD + Cucumber + Selenium + Gradle  
**Estimated Total**: 12.5 horas de implementación

