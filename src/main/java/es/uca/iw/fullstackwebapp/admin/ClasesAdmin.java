package es.uca.iw.fullstackwebapp.admin;

import com.vaadin.flow.component.dependency.CssImport;
import es.uca.iw.fullstackwebapp.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Scope;
import es.uca.iw.fullstackwebapp.clase.Clase;
import es.uca.iw.fullstackwebapp.clase.ClaseService;
import es.uca.iw.fullstackwebapp.instructor.Instructor;
import es.uca.iw.fullstackwebapp.instructor.InstructorService;
import es.uca.iw.fullstackwebapp.user.security.AuthenticatedUser;
import es.uca.iw.fullstackwebapp.reserva.ReservaService; // Importar ReservaService
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@Scope("prototype")
@RolesAllowed("ADMIN")
@Route(value = "clasesAdmin", layout = MainLayout.class)
@PageTitle("Clases Admin.")
@CssImport("./styles/dark-theme.css")
public class ClasesAdmin extends VerticalLayout {
    Grid<Clase> grid = new Grid<>(Clase.class);
    TextField filterText = new TextField();
    ClaseForm form;
    private ClaseService service;
    private InstructorService instructorService;
    private AuthenticatedUser authenticatedUser;
    private ReservaService reservaService; // Añadir ReservaService

    public ClasesAdmin(ClaseService service, InstructorService instructorService, AuthenticatedUser authenticatedUser, ReservaService reservaService) {
        this.service = service;
        this.authenticatedUser = authenticatedUser;
        this.instructorService = instructorService;
        this.reservaService = reservaService; // Inicializa ReservaService
        addClassName("admin-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new ClaseForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveClase);
        form.addDeleteListener(this::deleteClase);
        form.addCloseListener(e -> closeEditor());

        // Configura el ComboBox de instructores
        List<Instructor> instructors = instructorService.findAll(); // Obtén la lista de instructores
        form.setInstructors(instructors); // Configura el ComboBox en el formulario
    }

    private void saveClase(ClaseForm.SaveEvent event) {
        service.saveClase(event.getClase());
        updateList();
        closeEditor();
    }

    private void deleteClase(ClaseForm.DeleteEvent event) {
        service.deleteClase(event.getClase());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addClassNames("clase-grid");
        grid.setSizeFull();

        grid.addColumn(Clase::getName).setHeader("Nombre").setSortable(true);
        grid.addColumn(Clase::getDescription).setHeader("Descripción").setSortable(true);
        grid.addColumn(clase -> {
            if (clase.getHorario() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                return clase.getHorario().format(formatter); // Aplica el formato deseado
            } else {
                return "";
            }
        }).setHeader("Horario").setSortable(true);

        grid.addColumn(Clase::getCapacidad).setHeader("Capacidad").setSortable(true);

        // Configurar la columna para mostrar el nombre completo del instructor
        grid.addColumn(clase -> {
            Instructor instructor = clase.getInstructor();
            return instructor != null ? instructor.getName() + " " + instructor.getApellidos() : "";
        }).setHeader("Instructor").setSortable(true);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editClase(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filtrar por nombre o descripción...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addClaseButton = new Button("Añadir clase");
        addClaseButton.addClickListener(click -> addClase());

        var toolbar = new HorizontalLayout(filterText, addClaseButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editClase(Clase clase) {
        if (clase == null) {
            closeEditor();
        } else {
            form.setClase(clase);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setClase(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addClase() {
        grid.asSingleSelect().clear();
        editClase(new Clase());
    }

    private void updateList() {
        // Obtener todas las clases sin filtrar por usuario logueado
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

    // Método opcional para inscribir usuarios desde la vista de administración
    private void inscribirUsuario(String username, Clase clase) {
        try {
            reservaService.reserve(username, clase);
            updateList(); // Actualiza la lista para reflejar la nueva capacidad
        } catch (Exception e) {
            // Manejar excepciones, como mostrar una notificación de error al administrador
            e.printStackTrace();
        }
    }
}
