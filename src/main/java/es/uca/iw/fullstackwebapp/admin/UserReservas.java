package es.uca.iw.fullstackwebapp.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import es.uca.iw.fullstackwebapp.MainLayout;
import es.uca.iw.fullstackwebapp.clase.Clase;
import es.uca.iw.fullstackwebapp.reserva.Reserva;
import es.uca.iw.fullstackwebapp.reserva.ReservaService;
import es.uca.iw.fullstackwebapp.reserva.EstadoReserva;
import es.uca.iw.fullstackwebapp.user.domain.User;
import es.uca.iw.fullstackwebapp.user.security.AuthenticatedUser;
import es.uca.iw.fullstackwebapp.user.services.EmailService;
import es.uca.iw.fullstackwebapp.user.services.UserManagementService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Scope;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringComponent
@Scope("prototype")
@RolesAllowed("ADMIN")
@Route(value = "userReservas/:id", layout = MainLayout.class)
@PageTitle("Reservas Usuario. Admin.")
public class UserReservas extends VerticalLayout implements BeforeEnterObserver {
    private UUID id;
    private final ReservaService reservaService;
    private final UserManagementService userService;
    private Grid<Reserva> grid = new Grid<>(Reserva.class);
    private Button saveButton = new Button("Guardar Cambios");
    private Binder<Reserva> binder = new BeanValidationBinder<>(Reserva.class);
    private Reserva selectedReserva;

    public UserReservas(ReservaService reservaService, AuthenticatedUser authenticatedUser, UserManagementService userService) {
        this.reservaService = reservaService;
        this.userService = userService;

        addClassName("admin-view");
        setSizeFull(); // Ocupa toda la altura disponible

        // Configurar Grid
        grid.removeAllColumns();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        grid.addColumn(reserva -> reserva.getClase().getHorario().format(formatter)).setHeader("Horario").setSortable(true);
        grid.addColumn(reserva -> reserva.getClase().getName()).setHeader("Clase").setSortable(true);
        grid.addColumn(reserva -> {
            Clase clase = reserva.getClase();
            return clase.getInstructor() != null
                    ? clase.getInstructor().getName() + " " + clase.getInstructor().getApellidos()
                    : "";
        }).setHeader("Instructor").setSortable(true);
        grid.addColumn(reserva -> reserva.getUsuario().getUsername()).setHeader("Usuario").setSortable(true);

        // Configuración del ComboBox para el estado
        ComboBox<EstadoReserva> estadoComboBox = new ComboBox<>();
        estadoComboBox.setItems(EstadoReserva.values());
        estadoComboBox.setItemLabelGenerator(EstadoReserva::name);

        // Configuración del editor
        grid.getEditor().setBinder(binder);
        binder.forField(estadoComboBox)
                .bind(Reserva::getEstado, Reserva::setEstado);

        Grid.Column<Reserva> estadoColumn = grid.addColumn(Reserva::getEstado)
                .setHeader("Estado")
                .setEditorComponent(estadoComboBox).setSortable(true);

        // Configuración del botón de guardar
        saveButton.addClickListener(e -> saveChanges());

        // Crear un HorizontalLayout para centrar el botón
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        buttonLayout.setWidthFull();
        buttonLayout.setPadding(true); // Añade un poco de espacio alrededor del botón

        // Agregar componentes al diseño
        add(grid);
        add(buttonLayout); // Añadir el botón al final
        setFlexGrow(1, grid); // Permitir que el grid crezca para ocupar el espacio disponible
        setAlignSelf(Alignment.END, buttonLayout); // Alinear el botón al final

        // Configuración del Grid y el Editor
        grid.getEditor().setBuffered(true);
        grid.addItemDoubleClickListener(event -> {
            grid.getEditor().editItem(event.getItem());
            selectedReserva = event.getItem(); // Guardar la reserva seleccionada
        });
    }

    private void saveChanges() {
        if (selectedReserva != null) {
            try {
                binder.writeBean(selectedReserva); // Escribir el valor en el bean
                reservaService.save(selectedReserva);

                // Obtener el usuario y la clase para enviar el correo
                User usuario = selectedReserva.getUsuario();
                EstadoReserva estadoReserva = selectedReserva.getEstado();
                Clase clase = selectedReserva.getClase();

                Notification.show("Cambios guardados.", 3000, Notification.Position.MIDDLE);
                grid.getEditor().cancel(); // Cancelar el editor después de guardar
                selectedReserva = null; // Limpiar la selección
            } catch (Exception e) {
                Notification.show("Error al guardar cambios: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        } else {
            Notification.show("No se ha seleccionado ninguna reserva.", 3000, Notification.Position.MIDDLE);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> idParameter = event.getRouteParameters().get("id");

        if (idParameter.isPresent()) {
            try {
                id = UUID.fromString(idParameter.get());
                User user = userService.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                List<Reserva> reservas = reservaService.getReservasPorUsuario(user);
                grid.setItems(reservas);
            } catch (IllegalArgumentException e) {
                Notification.show("ID de reserva no válido.");
            }
        } else {
            Notification.show("ID de reserva no proporcionado.");
        }
    }
}
