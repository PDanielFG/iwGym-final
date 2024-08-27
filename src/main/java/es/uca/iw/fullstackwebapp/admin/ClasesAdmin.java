package es.uca.iw.fullstackwebapp.admin;


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
import es.uca.iw.fullstackwebapp.clase.ClaseForm;
import es.uca.iw.fullstackwebapp.clase.ClaseService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Scope;

import java.time.format.DateTimeFormatter;
import es.uca.iw.fullstackwebapp.user.security.AuthenticatedUser;
import es.uca.iw.fullstackwebapp.clase.Clase;
import java.util.List;
import es.uca.iw.fullstackwebapp.instructor.Instructor;
import es.uca.iw.fullstackwebapp.instructor.InstructorService;





//Hecho fijandome en el tutorial de vaadin de github
//este metodo esta en el service
@SpringComponent
@Scope("prototype")
@RolesAllowed("ADMIN")
@Route(value = "clasesAdmin", layout = MainLayout.class)
@PageTitle("Clases Admin.")
public class ClasesAdmin extends VerticalLayout {
    Grid<Clase> grid = new Grid<>(Clase.class);
    TextField filterText = new TextField();
    ClaseForm form;
    private ClaseService service;
    private InstructorService instructorService;
    private AuthenticatedUser authenticatedUser;
    private Instructor instructor;

    public ClasesAdmin(ClaseService service, InstructorService instructorService, AuthenticatedUser authenticatedUser) {
        this.service = service;     //Me daba error porque lo tenia comentado. There was an exception while trying to navigate to '' with the root cause 'java.lang.NullPointerException:
        this.authenticatedUser = authenticatedUser;
        this.instructorService = instructorService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();      //Para que al recargar la vista o entrar por primera vez, el form aparezca cerrado
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

    //Varía con el github de vaadin de ejemplo
    private void configureGrid() {
        grid.removeAllColumns(); //me fallabapor esto
        grid.addClassNames("clase-grid");
        grid.setSizeFull();
        grid.addColumn(Clase::getName).setHeader("Nombre");
        grid.addColumn(Clase::getDescription).setHeader("Descripción");
        grid.addColumn(clase -> {
            if (clase.getHorario() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                return clase.getHorario().format(formatter); // Aplica el formato deseado
            } else {
                return ""; // Retorna una cadena vacía si el valor es null
            }
        }).setHeader("Horario");        grid.addColumn(Clase::getCapacidad).setHeader("Capacidad");

        // Configurar la columna para mostrar el nombre completo del instructor
        grid.addColumn(clase -> {
            Instructor instructor = clase.getInstructor();
            return instructor != null ? instructor.getName() + " " + instructor.getApellidos() : ""; // Concatenar nombre y apellidos
        }).setHeader("Instructor");


        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editClase(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name or nickname...");
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

    //Como se trata de la vista del administrador, listamos todas las clases, sin necesidad
    //de filtrar por usuario
    private void updateList() {
        // Obtener todas las clases sin filtrar por usuario logueado
        List<Clase> todasLasClases = service.findAll(); // Asegúrate de que este método devuelva todas las clases

        // Actualizar el grid con todas las clases obtenidas
        grid.setItems(todasLasClases);
    }
}
