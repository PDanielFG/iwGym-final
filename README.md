# Pedro Daniel Fernández Guerrero

# Proyecto final para la asignatura Ingeniería Web, Universidad de Cádiz, convocatoria de Septiembre 2024.

# Sistema de Gestión de Reservas de Clases en un Gimnasio

# Diagrama de clases del sistema
![mcd](https://github.com/user-attachments/assets/027ff4d2-80dd-4214-92f1-544757fe21bc)

# Diagrama C4 Context
![c4Context](https://github.com/user-attachments/assets/d19cd42e-9d7c-4896-8d61-d38a5400e102)

# Diagrama C4 Container 
![c4Container](https://github.com/user-attachments/assets/837b815f-6d2b-4e9f-98df-b0f1601513ea)

# Requisitos funcionales del sistema
Registrar usuario.

Autenticar usuario.

Enviar correo de autenticación.

Iniciar sesión.

Cerrar sesión.

Reservar clase.

Enviar correo de estado de la reserva.

Enviar correo de actualización de la reserva.

Enviar correo con todas las reservas de un usuario.

Gestionar clases, solo administrador (añadir, eliminar, editar)

Gestionar reservas de un usuario, solo administrador (modificar el estado: PENDIENTE, CONFIRMADA, CANCELADA)

Gestionar instructores, solo administrador (añadir, eliminar, editar)

Asingar instructor a una actividad.


# Requisitos no funcionales del sistema
Seguridad: Autenticación en dos pasos, necesidad de estar con la sesión iniciada para navegar o modificar la url, Spring Boot Security y Bean validations.

Mantenibilidad: Estructura de carpetas uniforme, clara y concisa, para identificar rapidamente el archivo que deseemos. Seguimos metodologia de la nomenclatura para variables, metodos, nombre de archivos...

Fiabilidad: Tratamiento de errores mediante el uso de las excepciones de Java, uso de optionals para evitar null-pointer exceptions y abundante uso de las etiquetas de Spring.

Eficiencia: Relaciones necesarias, ayudandonos de las etiquetas de Spring, por ejemplo para las relaciones entre clases. @OneToMany, @ManyToOne.

Portabilidad: Uso de estandares, lenguajes de programación portables como Java que contiene su propia maquina virtual JVM, APIS para la asincronía y fluidez...

Operabilidad: El sistema informa al usuario de lo que necesita antes de ejecutar una acción erronea que conlleve un error/excepción.

Compatibilidad: Conectividad mediante emails (SMTP), Spring permite enviar correos electronicos de una forma muy cómoda y sencilla desde la propia aplicación y uso de REST (funcionalidad muy útil)

