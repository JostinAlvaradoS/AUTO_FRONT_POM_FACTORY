# AUTO_FRONT_POM_FACTORY - Frontend Automation (POM)

Este proyecto contiene las pruebas de frontend del catálogo utilizando el patrón **Page Object Model (POM)** y **Factory** con Serenity BDD.

## Requisitos Previos

### Entorno de Aplicación (SUT)

Antes de ejecutar las pruebas, asegúrate de tener el sistema en funcionamiento:

1.  **Clonar el repositorio**: `git clone [URL_REPOSITORIO_TICKETING_PROJECT]`
2.  **Levantar Backend (Docker)**: `docker-compose up -d` desde la raíz de `ticketing_project`.
3.  **Levantar Frontend**: Navega a `ticketing_project/frontend` y ejecuta:
    ```bash
    pnpm install && pnpm dev
    # o npm install && npm run dev
    ```

### Herramientas
- **Java 17** o superior
- **Serenity BDD**
- **Gradle** (usar `./gradlew`)
- **Google Chrome y ChromeDriver**
- **JUnit 5**
- **Cucumber**

## 🎯 Objetivo
Validar el flujo de reserva de asientos mediante una implementación tradicional de POM usando la anotación `@FindBy`.

## 📜 Especificaciones de Negocio
Este repositorio consume las specs centralizadas mediante un submódulo en `shared-specs/`.
- Ver historias de usuario: [hu01-reserva-asiento.feature](shared-specs/specs/001-ticketing-mvp/hu01-reserva-asiento.feature)

## 🚀 Ejecución
Para ejecutar las pruebas y generar el reporte:
```bash
./gradlew clean test aggregate
```
Los reportes se generan en `target/site/serenity/index.html`.
