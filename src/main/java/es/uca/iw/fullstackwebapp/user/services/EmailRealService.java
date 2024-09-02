package es.uca.iw.fullstackwebapp.user.services;

import es.uca.iw.fullstackwebapp.user.domain.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

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


    @Override
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

    // Método para enviar notificaciones de estado de la reserva
    public boolean sendReservationStatusEmail(User user, String reservationStatus) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String subject = "Estado de su reserva";
        String body = "Estimado/a " + user.getUsername() + ",\n\n"
                + "El estado de su reserva ha cambiado: " + reservationStatus + ".\n"
                + "Gracias por utilizar nuestro servicio.";

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

    // Método para enviar recordatorios de clase
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
