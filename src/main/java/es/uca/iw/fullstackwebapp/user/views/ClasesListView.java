package es.uca.iw.fullstackwebapp.user.views;

import es.uca.iw.fullstackwebapp.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import es.uca.iw.fullstackwebapp.clase.ClaseService;
import es.uca.iw.fullstackwebapp.reserva.EstadoReserva;
import es.uca.iw.fullstackwebapp.reserva.ReservaService;
import es.uca.iw.fullstackwebapp.user.domain.User;
import es.uca.iw.fullstackwebapp.user.services.EmailService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.time.format.DateTimeFormatter;
import es.uca.iw.fullstackwebapp.user.security.AuthenticatedUser;
import es.uca.iw.fullstackwebapp.clase.Clase;
import java.util.List;
import java.util.stream.Collectors;
import es.uca.iw.fullstackwebapp.instructor.Instructor;

@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "clases", layout = MainLayout.class)
@PageTitle("Clases GYM.")
public class ClasesListView extends VerticalLayout {
    Grid<Clase> grid = new Grid<>(Clase.class);
    TextField filterText = new TextField();

    private ClaseService service;
    private ReservaService reservaService;
    private EmailService emailService;
    private AuthenticatedUser authenticatedUser;

    @Autowired
    public ClasesListView(ClaseService service, ReservaService reservaService, EmailService emailService, AuthenticatedUser authenticatedUser) {
        this.service = service;
        this.reservaService = reservaService;
        this.emailService = emailService;
        this.authenticatedUser = authenticatedUser;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        add(getToolbar(), grid);
        updateList();
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addClassNames("clase-grid");
        grid.setSizeFull(); // Importante para que el grid ocupe todo el espacio disponible

        // Configuración de columnas con flexibilidad equitativa
        grid.addColumn(Clase::getName)
                .setHeader("Nombre")
                .setSortable(true)
                .setFlexGrow(1); // Misma flexibilidad para todas las columnas

        grid.addColumn(Clase::getDescription)
                .setHeader("Descripción")
                .setSortable(true)
                .setFlexGrow(1); // Misma flexibilidad

        grid.addColumn(clase -> {
                    if (clase.getHorario() != null) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                        return clase.getHorario().format(formatter);
                    } else {
                        return "";
                    }
                }).setHeader("Horario")
                .setSortable(true)
                .setFlexGrow(1); // Misma flexibilidad

        grid.addColumn(Clase::getCapacidad)
                .setHeader("Capacidad")
                .setSortable(true)
                .setFlexGrow(1); // Misma flexibilidad

        grid.addColumn(clase -> {
                    Instructor instructor = clase.getInstructor();
                    return instructor != null ? instructor.getName() + " " + instructor.getApellidos() : "";
                }).setHeader("Instructor")
                .setSortable(true)
                .setFlexGrow(1); // Misma flexibilidad

        // Columna de acciones (puedes optar por no darle flexibilidad si no quieres que crezca)
        grid.addComponentColumn(clase -> {
                    Button reserveButton = new Button("Reservar");
                    reserveButton.addClickListener(click -> reservarClase(clase));
                    return reserveButton;
                }).setHeader("Acciones")
                .setFlexGrow(1); // Misma flexibilidad

        // Asegurarse de que el autoajuste de ancho está activado para que se respete el tamaño disponible
        grid.getColumns().forEach(col -> col.setAutoWidth(false)); // Desactiva el autoancho si lo tenías activado
    }


    private Component getToolbar() {
        filterText.setPlaceholder("Filtrar por nombre o descripción...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        var toolbar = new HorizontalLayout(filterText);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        // Obtener todas las clases
        List<Clase> todasLasClases = service.findAll();

        // Obtener el texto del filtro
        String filtro = filterText.getValue().trim().toLowerCase();

        // Filtrar las clases por nombre o descripción
        List<Clase> clasesFiltradas = todasLasClases.stream()
                .filter(clase -> clase.getName().toLowerCase().contains(filtro) ||
                        clase.getDescription().toLowerCase().contains(filtro))
                .collect(Collectors.toList());

        // Actualizar el grid con las clases filtradas
        grid.setItems(clasesFiltradas);
    }

    private void reservarClase(Clase clase) {
        // Obtener el usuario actual
        User user = authenticatedUser.getUser(); // Ajusta según tu implementación
        String username = user.getUsername();

        try {
            // Llamar al servicio de reserva para crear la reserva
            reservaService.reserve(username, clase);

            // Definir el estado de la reserva
            EstadoReserva estadoReserva = EstadoReserva.PENDIENTE;

            // Enviar notificación por correo con el estado de la reserva y los detalles de la clase
            emailService.sendReservationStatusEmail(user, estadoReserva, clase);

            // Actualizar la vista o mostrar un mensaje de éxito
            Notification.show("Clase reservada exitosamente.\n Para más información acerca de la reserva, compruebe su correo electrónico", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            // Manejo de errores, mostrar mensaje de error
            Notification.show("Error al reservar la clase: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

}