# AUTO_FRONT_POM_FACTORY

Proyecto de automatización UI para la Ticketing Platform utilizando el patrón **Page Object Model (POM)** con **Page Factory**.

## 🛠️ Tecnologías
- **Java 17**
- **Serenity BDD**
- **Gradle**
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
