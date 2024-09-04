package es.uca.iw.fullstackwebapp.user.views;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import es.uca.iw.fullstackwebapp.MainLayout;
import es.uca.iw.fullstackwebapp.reserva.Reserva;
import es.uca.iw.fullstackwebapp.reserva.ReservaService;
import es.uca.iw.fullstackwebapp.user.domain.User;
import es.uca.iw.fullstackwebapp.user.security.AuthenticatedUser;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import es.uca.iw.fullstackwebapp.clase.Clase;
import com.vaadin.flow.component.textfield.TextField;



@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "reservas", layout = MainLayout.class)
@PageTitle("Mis Reservas")
public class ReservaListView extends VerticalLayout {
    Grid<Reserva> grid = new Grid<>(Reserva.class);
    TextField filterText = new TextField();

    private ReservaService reservaService;
    private AuthenticatedUser authenticatedUser;

    @Autowired
    public ReservaListView(ReservaService reservaService, AuthenticatedUser authenticatedUser) {
        this.reservaService = reservaService;
        this.authenticatedUser = authenticatedUser;
        addClassName("list-view");
        setSizeFull();
        configureGrid();


        // Crear y configurar el VerticalLayout para el encabezado
        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.setAlignItems(Alignment.CENTER);
        headerLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        H1 title = new H1("Tus reservas, "+authenticatedUser.get().get().getUsername());
        headerLayout.add(title);

        add(headerLayout, grid);
        updateList();
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addClassNames("clase-grid");
        grid.setSizeFull();

        //Con los modificadores y formateadores de addColumn, vamos a mostras en el grid
        //fecha de la reserva, clase asociada a clase_id, y el instructor de dicha clase
        //aunque el registro de reserva sea fehca, clase_id y usuario_id
        //Fecha y hora
        grid.addColumn(reserva -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return reserva.getFechaReserva().format(formatter);
        }).setHeader("Fecha de Reserva");

        //clase asociada al clase_id
        grid.addColumn(reserva -> reserva.getClase().getName())
                .setHeader("Clase");

        //Instructor de la clase
        grid.addColumn(reserva -> {
            Clase clase = reserva.getClase();
            return clase.getInstructor() != null
                    ? clase.getInstructor().getName() + " " + clase.getInstructor().getApellidos()
                    : "";
        }).setHeader("Instructor");

        //Estado de la reserva
        grid.addColumn(reserva -> reserva.getEstado()).setHeader("Estado");


        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    /*private Component getToolbar() {
        filterText.setPlaceholder("Filtrar por nombre o descripción...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        var toolbar = new HorizontalLayout(filterText);
        toolbar.addClassName("toolbar");
        return toolbar;
    }*/

    private void updateList() {
        // Obtener el usuario autenticado
        Optional<User> currentUserOpt = authenticatedUser.get();

        if (currentUserOpt.isPresent()) {
            es.uca.iw.fullstackwebapp.user.domain.User currentUser = currentUserOpt.get();
            // Obtener las reservas del usuario autenticado
            List<Reserva> reservasDelUsuario = reservaService.getReservasPorUsuario(currentUser);

            // Resetting the grid's items explicitly with the correct class type
            grid.setItems(reservasDelUsuario.stream().map(Reserva.class::cast).toList());
        } else {
            // Mostrar una notificación si no hay un usuario autenticado (caso muy raro)
            Notification.show("No se encontró el usuario autenticado.", 3000, Notification.Position.MIDDLE);
        }
    }

}
