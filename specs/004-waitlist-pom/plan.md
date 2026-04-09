# Implementation Plan: Sistema de Lista de Espera — UI Automation (POM)

**Branch**: `004-waitlist-pom` | **Date**: 2026-04-03 | **Spec**: [spec.md](./spec.md)

## Summary
Automatización UI del WaitlistModal (Next.js) para los 6 escenarios de lista de espera usando POM + Factory. Los escenarios 1-3 son UI puros. Los escenarios 4-6 se verifican via API (RestAssured) desde los step definitions.

## Technical Context
| Ítem | Valor |
|---|---|
| **Frontend URL** | `http://localhost:3000` |
| **Event Page** | `/events/{eventId}` |
| **Waitlist API** | `http://localhost:5006` |
| **Driver** | Chrome (WebDriverManager) |
| **New Page Object** | `WaitlistPage extends BasePage` |

## Constitution Check
- [x] **POM Pattern**: `WaitlistPage` extiende `BasePage`, selectores en `SelectorConstants`.
- [x] **Factory Pattern**: Acceso vía `PageObjectFactory.getWaitlistPage()`.
- [x] **Constants**: Selectores, URLs y test data centralizados en sus respectivas clases.
- [x] **Hook Lifecycle**: `@Before` crea evento agotado via API, `@After` limpia.
- [x] **Hybrid Testing**: Escenarios 4-6 usan RestAssured directo (sin UI) para verificar estado backend.

## New Files
```
specs/004-waitlist-pom/
├── spec.md | plan.md | tasks.md

src/test/resources/features/
└── waitlist.feature

src/test/java/com/automation/hu05/
├── pages/WaitlistPage.java
├── step_definitions/WaitlistSteps.java
├── hooks/WaitlistHooks.java
└── runners/RunWaitlistTests.java
```

## Edited Files
```
constants/SelectorConstants.java   ← sección Waitlist
constants/UrlConstants.java        ← EVENT_DETAIL_PAGE
constants/TestDataConstants.java   ← WAITLIST_EMAIL, WAITLIST_SECOND_EMAIL
factories/PageObjectFactory.java   ← getWaitlistPage()
```
