package es.uca.iw.fullstackwebapp.admin;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import es.uca.iw.fullstackwebapp.MainLayout;
import es.uca.iw.fullstackwebapp.clase.Clase;
import es.uca.iw.fullstackwebapp.reserva.ReservaService;
import es.uca.iw.fullstackwebapp.user.domain.User;
import es.uca.iw.fullstackwebapp.user.security.AuthenticatedUser;
import es.uca.iw.fullstackwebapp.user.services.UserManagementService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Scope;

import java.util.Optional;
import java.util.UUID;
import java.util.List;


import es.uca.iw.fullstackwebapp.reserva.Reserva;

@SpringComponent
@Scope("prototype")
@RolesAllowed("ADMIN")
@Route(value = "userReservas/:id", layout = MainLayout.class)
@PageTitle("Reservas Usuario. Admin.")
public class UserReservas extends VerticalLayout implements BeforeEnterObserver {
    private UUID id;  // Cambiado de UUID a Long
    private Reserva reserva;
    private final AuthenticatedUser authenticatedUser;
    private final ReservaService reservaService;
    private final UserManagementService userService;

    private Grid<Reserva> grid = new Grid<>(Reserva.class);


    public UserReservas(ReservaService reservaService, AuthenticatedUser authenticatedUser, UserManagementService userService) {
        this.reservaService = reservaService;
        this.authenticatedUser = authenticatedUser;
        this.userService = userService;

        grid.removeAllColumns();
        // Configurar el Grid
        grid.addColumn(Reserva::getFechaReserva).setHeader("Fecha de Reserva");
        //clase asociada al clase_id
        grid.addColumn(reserva -> reserva.getClase().getName())
                .setHeader("Clase");

        //Instructor asociado al clase_id
        grid.addColumn(reserva -> {
            Clase clase = reserva.getClase();
            return clase.getInstructor() != null
                    ? clase.getInstructor().getName() + " " + clase.getInstructor().getApellidos()
                    : "";
        }).setHeader("Instructor");

        grid.addColumn(reserva -> reserva.getUsuario().getUsername()).setHeader("Usuario");
        grid.addColumn(Reserva::getEstado).setHeader("Estado");

        //Anchura automatica
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        add(grid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> idParameter = event.getRouteParameters().get("id");

        if (idParameter.isPresent()) {
            try {
                id = UUID.fromString(idParameter.get());
                User user = userService.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                List<Reserva> reservas = reservaService.getReservasPorUsuario(user);
                grid.setItems(reservas); // Establecer las reservas en el Grid
            } catch (IllegalArgumentException e) {
                Notification.show("ID de reserva no válido.");
                // event.forwardTo(ReservaAdmin.class);
            }
        } else {
            Notification.show("ID de reserva no proporcionado.");
            // event.forwardTo(ReservaAdmin.class);
        }
    }


}
