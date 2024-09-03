package es.uca.iw.fullstackwebapp.user;

import es.uca.iw.fullstackwebapp.reserva.Reserva;
import es.uca.iw.fullstackwebapp.reserva.ReservaService;
import es.uca.iw.fullstackwebapp.user.domain.User;
import es.uca.iw.fullstackwebapp.user.services.EmailRealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private EmailRealService emailRealService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        // Verificar que el principal sea una instancia de UserDetails (esto puede variar según tu implementación)
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            // Obtener el usuario desde el servicio de usuario, si es necesario
            User user = (User) principal; // Asegúrate de tener el casting correcto

            // Obtener todas las reservas del usuario
            List<Reserva> reservas = reservaService.getReservasPorUsuario(user);

            // Enviar el correo con las reservas
            emailRealService.sendReservationsEmail(user, reservas);
        }
    }
}
