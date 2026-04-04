# language: es
@ListaEspera
Característica: Sistema de Lista de Espera Inteligente — UI
  Como usuario interesado en un evento agotado
  Quiero unirme a la lista de espera desde la interfaz web
  Para recibir una notificación cuando un asiento esté disponible

  @RegistroExitoso
  Escenario: Registro exitoso en lista de espera
    Dado que el evento "Concierto Rock 2026" está agotado y el usuario navega a su página
    Cuando hace clic en "Join the Waitlist", ingresa "jostin@example.com" y confirma
    Entonces la UI muestra "You're on the list!"
    Y el usuario recibe su posición en la cola

  @TicketsDisponibles
  Escenario: Intento de registro con tickets disponibles
    Dado que el evento "Concierto Rock 2026" tiene tickets disponibles
    Cuando el usuario navega a la página del evento
    Entonces el botón "Join the Waitlist" no es visible en la UI

  @RegistroDuplicado
  Escenario: Registro duplicado en la misma lista
    Dado que "jostin@example.com" ya está registrado en la lista del evento desde la UI
    Cuando intenta registrarse nuevamente desde el modal de waitlist
    Entonces la UI muestra el mensaje de conflicto retornado por la API

  @AsignacionAutomatica
  Escenario: Asignación automática al expirar una reserva
    Dado que "jostin@example.com" es el primero en la lista de espera del evento
    Cuando el tiempo de pago inicial caduca
    Entonces la API confirma que la entrada fue asignada automáticamente
    Y actualiza el estado de la entrada a Asignado
    Y envía un correo con el enlace de pago con validez de 30 minutos

  @LiberacionConSiguiente
  Escenario: Liberación por inacción con siguiente en cola
    Dado que "jostin@example.com" fue asignado y no pagó en 30 minutos
    Y "segundo@example.com" es el siguiente en la lista de espera
    Cuando el sistema detecta la inacción
    Entonces el sistema marca la entrada de "jostin@example.com" como Expirado
    Y reasigna el asiento directamente a "segundo@example.com" sin liberarlo al pool general
    Y envía correo de pago a "segundo@example.com" con validez de 30 minutos

  @LiberacionSinCola
  Escenario: Liberación por inacción con cola vacía
    Dado que "jostin@example.com" fue asignado y no pagó en 30 minutos
    Y no hay más usuarios en la lista de espera del evento
    Cuando el sistema detecta la inacción
    Entonces el sistema cancela la orden y libera el asiento al pool general
