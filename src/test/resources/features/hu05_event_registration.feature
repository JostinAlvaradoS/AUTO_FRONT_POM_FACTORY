# language: es

Característica: Automatización de pruebas - Registro de Eventos
  Como administrador del sistema
  Quiero crear eventos en la plataforma
  Para que los usuarios puedan reservar asientos en el evento

  Antecedentes:
    Dado que el usuario administrador navega a la página de registro de eventos

  @hu05_positive @smoke
  Escenario: Registro exitoso de evento con todos los campos válidos
    Y el usuario ingresa un nombre de evento "Concierto de Rock 2024"
    Y el usuario ingresa una descripción "Concierto de rock en vivo con artistas reconocidos"
    Y el usuario ingresa una fecha de evento "2024-12-15"
    Y el usuario ingresa una ubicación "Estadio Nacional de Santiago"
    Y el usuario ingresa una capacidad de evento "5000"
    Cuando el usuario hace clic en el botón enviar
    Entonces el evento debe mostrarse en la lista de eventos
    Y debe mostrarse un mensaje de éxito


  @hu05_negative @regression
  Escenario: Validación de campos requeridos cuando están vacíos
    Cuando el usuario hace clic en el botón enviar sin llenar ningún campo
    Entonces debe mostrarse un mensaje de error para el campo nombre
    Y debe mostrarse un mensaje de error para el campo descripción
    Y debe mostrarse un mensaje de error para el campo fecha
    Y debe mostrarse un mensaje de error para el campo ubicación
    Y debe mostrarse un mensaje de error para el campo capacidad
