package es.uca.iw.fullstackwebapp.admin;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import es.uca.iw.fullstackwebapp.MainLayout;
import es.uca.iw.fullstackwebapp.reserva.ReservaService;
import es.uca.iw.fullstackwebapp.user.domain.User;
import es.uca.iw.fullstackwebapp.user.security.AuthenticatedUser;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Scope;

import java.util.Optional;
import java.util.UUID;

import es.uca.iw.fullstackwebapp.reserva.Reserva;

@SpringComponent
@Scope("prototype")
@RolesAllowed("ADMIN")
@Route(value = "userReservas/:id", layout = MainLayout.class)
@PageTitle("Reservas Usuario. Admin.")
public class UserReservas extends VerticalLayout implements BeforeEnterObserver {
    private final ReservaService reservaService;
    private UUID id;  // Cambiado de UUID a Long
    private Reserva reserva;
    private final AuthenticatedUser authenticatedUser;

    public UserReservas(ReservaService reservaService, AuthenticatedUser authenticatedUser) {
        this.reservaService = reservaService;
        this.authenticatedUser = authenticatedUser;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> idParameter = event.getRouteParameters().get("id");

        if (idParameter.isPresent()) {
            try {
                id = UUID.fromString(idParameter.get());
                //cargarDatosReserva(id);
            } catch (IllegalArgumentException e) {
                Notification.show("ID de reserva no válido.");
                // event.forwardTo(ReservaAdmin.class);
            }
        } else {
            Notification.show("ID de reserva no proporcionado.");
            // event.forwardTo(ReservaAdmin.class);
        }
    }

   /* private void cargarDatosReserva(UUID id) {  // Cambiado de UUID a Long
        Optional<Reserva> optionalReserva = reservaService.findById(id);

        if (optionalReserva.isPresent()) {
            reserva = optionalReserva.get();

            Optional<User> optionalUsuario = authenticatedUser.get();
            if (optionalUsuario.isEmpty() || !reserva.getUsuario().equals(optionalUsuario.get())) {
                Notification.show("No tiene permisos para editar esta reserva.");
                // getUI().ifPresent(ui -> ui.navigate(ReservaAdmin.class));
                return;
            }

            // Aquí puedes añadir la lógica para mostrar los detalles de la reserva
        } else {
            Notification.show("Reserva no encontrada.");
            // getUI().ifPresent(ui -> ui.navigate(ReservaAdmin.class));
        }
    }*/
}
