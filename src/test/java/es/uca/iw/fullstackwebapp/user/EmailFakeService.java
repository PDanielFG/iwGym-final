package es.uca.iw.fullstackwebapp.user;

import es.uca.iw.fullstackwebapp.user.domain.User;
import es.uca.iw.fullstackwebapp.user.services.EmailService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import es.uca.iw.fullstackwebapp.reserva.EstadoReserva;
import es.uca.iw.fullstackwebapp.clase.Clase;


@Service
@Primary
public class EmailFakeService implements EmailService {

    @Override
    public boolean sendRegistrationEmail(User user) {
        String subject = "Welcome";
        String body = "You should activate your account. "
                + "Go to http://localhost:8080/useractivation "
                + "and introduce your email and the following code: "
                + user.getRegisterCode();

        try {
            System.out.println("From: app (testing)");
            System.out.println("To: " + user.getEmail());
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);

            int secondsToSleep = 5;
            Thread.sleep(secondsToSleep * 1000);
            System.out.println("Email send simulation done!");
            return true;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean sendReservationStatusEmail(User user, EstadoReserva estadoReserva, Clase clase) {
        // Definir el asunto y el cuerpo del correo
        String subject = "Estado de su reserva";
        String body = "Estimado/a " + user.getUsername() + ",\n\n"
                + "El estado de su reserva ha cambiado a: " + estadoReserva.name() + ".\n\n"
                + "Detalles de la clase:\n"
                + "Nombre: " + clase.getName() + "\n"
                + "Descripción: " + clase.getDescription() + "\n"
                + "Horario: " + (clase.getHorario() != null ? clase.getHorario().toString() : "No especificado") + "\n"
                + "Capacidad: " + clase.getCapacidad() + "\n\n"
                + "Gracias por utilizar nuestro servicio. ¡Nos vemos pronto!\n"
                + "¡Saludos de todo el equipo de IwGymUca!";

        // Simulación del envío del correo
        try {
            System.out.println("From: app (testing)");
            System.out.println("To: " + user.getEmail());
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);

            // Simular un retraso en el envío del correo
            int secondsToSleep = 5;
            Thread.sleep(secondsToSleep * 1000);
            System.out.println("Email send simulation done!");
            return true;
        } catch (InterruptedException ie) {
            // Manejo de la interrupción del hilo
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean modStatusReservationMail(User user, EstadoReserva estadoReserva, Clase clase) {
        // Implementación simulada para pruebas
        System.out.println("Simulación de envío de correo a: " + user.getEmail());
        System.out.println("Estado de reserva: " + estadoReserva.name());
        System.out.println("Detalles de clase: " + clase.getName());
        return true;
    }


    @Override
    public boolean sendClassReminderEmail(User user, String classDetails, String classDateTime) {
        String subject = "Class Reminder";
        String body = "This is a reminder for your upcoming class:\n"
                + "Details: " + classDetails + "\n"
                + "Date and Time: " + classDateTime;

        try {
            System.out.println("From: app (testing)");
            System.out.println("To: " + user.getEmail());
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);

            int secondsToSleep = 5;
            Thread.sleep(secondsToSleep * 1000);
            System.out.println("Email send simulation done!");
            return true;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return false;
        }
    }


}
