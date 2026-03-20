# language: es

Característica: Automatización de pruebas - Catálogo Admin
  Como administrador del sistema
  Quiero gestionar el catálogo de eventos
  Para que los usuarios puedan ver y comprar entradas

  @positive
  Escenario: Registro exitoso de un evento
    Dado que soy un administrador autenticado
    Y me encuentro en el módulo de gestión de eventos
    Cuando registro un nuevo evento con información válida
    Entonces debería visualizar un mensaje de confirmación de registro exitoso
    Y el evento debería aparecer en el listado de eventos

  @negative
  Escenario: Validación de nombre obligatorio en el registro de eventos
    Dado que soy un administrador autenticado
    Y me encuentro en el módulo de gestión de eventos
    Cuando intento registrar un evento sin nombre
    Entonces debería visualizar un mensaje indicando que el nombre es obligatorio
