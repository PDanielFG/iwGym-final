package es.uca.iw.fullstackwebapp.admin;

import es.uca.iw.fullstackwebapp.clase.Clase;
import es.uca.iw.fullstackwebapp.reserva.Reserva;
import es.uca.iw.fullstackwebapp.reserva.ReservaService;
import es.uca.iw.fullstackwebapp.user.services.EmailRealService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReminderService {

    private final ReservaService reservaService;
    private final EmailRealService emailService;

    public ReminderService(ReservaService reservaService, EmailRealService emailService) {
        this.reservaService = reservaService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 * * * ?") // Ejecutar cada hora
    public void sendClassReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.plus(24, ChronoUnit.HOURS);

        List<Reserva> reservas = reservaService.findAll();
        for (Reserva reserva : reservas) {
            Clase clase = reserva.getClase();
            if (clase != null && clase.getHorario() != null) {
                LocalDateTime classStartTime = clase.getHorario();

                // Verificar si la clase empieza en las próximas 24 horas
                if (classStartTime.minus(24, ChronoUnit.HOURS).isBefore(now) &&
                        classStartTime.isAfter(now)) {

                    // Enviar el correo de recordatorio
                    emailService.sendClassReminderEmail(
                            reserva.getUsuario(),
                            "Clase: " + clase.getName() + "\nDescripción: " + clase.getDescription(),
                            classStartTime.toString()
                    );
                }
            }
        }
    }
}
