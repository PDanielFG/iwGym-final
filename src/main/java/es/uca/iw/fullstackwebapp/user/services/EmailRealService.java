package es.uca.iw.fullstackwebapp.user.services;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import es.uca.iw.fullstackwebapp.clase.Clase;
import es.uca.iw.fullstackwebapp.reserva.EstadoReserva;
import es.uca.iw.fullstackwebapp.reserva.Reserva;
import es.uca.iw.fullstackwebapp.user.domain.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailRealService implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String defaultMail;

    @Value("${server.port}")
    private int serverPort;

    public EmailRealService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private String getServerUrl() {
        // Generate the server URL
        String serverUrl = "http://";
        serverUrl += InetAddress.getLoopbackAddress().getHostAddress();
        serverUrl += ":" + serverPort + "/";
        return serverUrl;
    }

    public boolean sendRegistrationEmail(User user) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String subject = "Bienvenido";
        String body = "Por favor, verifique su cuenta. "
                + "Haga click en " + getServerUrl() + "useractivation "
                + "e introduzca el siguiente su correo y el código: "
                + user.getRegisterCode();

        try {
            helper.setFrom(defaultMail);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(body);
            this.mailSender.send(message);
        } catch (MailException | MessagingException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean sendReservationStatusEmail(User user, EstadoReserva estadoReserva, Clase clase) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        // Definir el formato para la fecha y hora
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        String subject = "Estado de su reserva";
        String body = "Estimado/a " + user.getUsername() + ",\n\n"
                + "El estado de su reserva es: " + estadoReserva.name() + ".\n\n"
                + "Detalles de la clase:\n"
                + "Nombre: " + clase.getName() + "\n"
                + "Descripción: " + clase.getDescription() + "\n"
                + "Horario: " + (clase.getHorario() != null ? clase.getHorario().format(formatter) : "No especificado") + "\n"
                + "Capacidad: " + clase.getCapacidad() + "\n\n"
                + "Gracias por utilizar nuestro servicio. ¡Nos vemos pronto!\n"
                + "¡Saludos de todo el equipo de IwGymUca!";

        try {
            helper.setFrom(defaultMail);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(body);
            this.mailSender.send(message);
        } catch (MailException | MessagingException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean modStatusReservationMail(User user, EstadoReserva estadoReserva, Clase clase) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String subject = "Actualización de Estado de Reserva";
        String body = "Estimado/a " + user.getUsername() + ",\n\n"
                + "El estado de su reserva ha sido actualizado a: " + estadoReserva.name() + ".\n\n"
                + "Detalles de la clase:\n"
                + "Nombre: " + clase.getName() + "\n"
                + "Descripción: " + clase.getDescription() + "\n"
                + "Horario: " + (clase.getHorario() != null ? clase.getHorario().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "No especificado") + "\n"
                + "Capacidad: " + clase.getCapacidad() + "\n\n"
                + "Gracias por utilizar nuestro servicio. ¡Nos vemos pronto!\n"
                + "¡Saludos de todo el equipo de IwGymUca!";

        try {
            helper.setFrom(defaultMail);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(body);
            mailSender.send(message);
            System.out.println("Correo enviado a: " + user.getEmail());  // Mensaje de depuración
            return true;
        } catch (MailException | MessagingException ex) {
            ex.printStackTrace();
            System.err.println("Error al enviar el correo: " + ex.getMessage());  // Mensaje de error
            return false;
        }
    }


    public boolean sendReservationsEmail(User user, List<Reserva> reservas) {
        // Verificar si hay reservas; si no hay, mostrar notificación y no enviar el correo
        if (reservas == null || reservas.isEmpty()) {
            // Asegúrate de que la notificación se muestre en el hilo de UI
            UI currentUI = UI.getCurrent();
            if (currentUI != null) {
                currentUI.access(() -> {
                    Notification.show("No hay reservas para enviar", 3000, Notification.Position.MIDDLE);
                });
            } else {
                // Si no hay un contexto de UI, podrías registrar un log o manejarlo de otra manera.
                System.out.println("No hay reservas para enviar y no hay UI disponible.");
            }
            return false; // Retornar false si no hay reservas
        }

        // Crear el mensaje de correo
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String subject = "Tus reservas";
        StringBuilder body = new StringBuilder();
        body.append("Estimado/a ").append(user.getUsername()).append(",\n\n")
                .append("Aquí están todas tus reservas:\n\n");

        // Formateador de fecha para el formato dd/MM/yyyy HH:mm
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Reserva reserva : reservas) {
            // Formatea la fecha y hora de la clase
            String formattedDate = reserva.getClase().getHorario().format(formatter);

            body.append("Clase: ").append(reserva.getClase().getName()).append("\n")
                    .append("Descripción: ").append(reserva.getClase().getDescription()).append("\n")
                    .append("Fecha y hora: ").append(formattedDate).append("\n")
                    .append("Estado: ").append(reserva.getEstado()).append("\n\n");
        }

        body.append("¡Gracias por utilizar nuestro servicio!");

        try {
            helper.setFrom(defaultMail);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(body.toString());
            mailSender.send(message);
            return true; // Retornar true si el correo se envía correctamente
        } catch (MailException | MessagingException ex) {
            ex.printStackTrace();
            return false; // Retornar false si ocurre algún error
        }
    }




    public boolean sendClassReminderEmail(User user, String classDetails, String classDateTime) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String subject = "Recordatorio de clase";
        String body = "Estimado/a " + user.getUsername() + ",\n\n"
                + "Este es un recordatorio de su próxima clase:\n"
                + "Detalles: " + classDetails + "\n"
                + "Fecha y hora: " + classDateTime + "\n\n"
                + "¡Nos vemos pronto!";

        try {
            helper.setFrom(defaultMail);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(body);
            this.mailSender.send(message);
        } catch (MailException | MessagingException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }
}
