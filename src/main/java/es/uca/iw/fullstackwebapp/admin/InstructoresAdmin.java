package es.uca.iw.fullstackwebapp.admin;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.fullstackwebapp.MainLayout;
import es.uca.iw.fullstackwebapp.instructor.InstructorService;
import es.uca.iw.fullstackwebapp.user.security.AuthenticatedUser;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Scope;
import java.time.format.DateTimeFormatter;
import java.util.List;

import es.uca.iw.fullstackwebapp.instructor.Instructor;
import es.uca.iw.fullstackwebapp.instructor.InstructorForm;

@SpringComponent
@Scope("prototype")
@RolesAllowed("ADMIN")
@Route(value = "instructoresAdmin", layout = MainLayout.class)
@PageTitle("Instructores Admin.")
public class InstructoresAdmin extends VerticalLayout {
    Grid<Instructor> grid = new Grid<>(Instructor.class);
    TextField filterText = new TextField();
    InstructorForm form;
    private InstructorService service;
    private AuthenticatedUser authenticatedUser;

    public InstructoresAdmin(InstructorService service, AuthenticatedUser authenticatedUser) {
        this.service = service;     //Me daba error porque lo tenia comentado. There was an exception while trying to navigate to '' with the root cause 'java.lang.NullPointerException: Cannot invoke "es.uca.iw.fullstackwebapp.allegado.AllegadoService.findAllAllegados(String)" because "this.service" is null'
        this.authenticatedUser = authenticatedUser;
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
        form = new InstructorForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveInstructor);
        form.addDeleteListener(this::deleteInstructor);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveInstructor(InstructorForm.SaveEvent event) {
        service.saveInstructor(event.getInstructor());
        updateList();
        closeEditor();
    }

    private void deleteInstructor(InstructorForm.DeleteEvent event) {
        service.deleteInstructor(event.getInstructor());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.removeAllColumns();    //importante esta linea
        grid.addClassNames("instructor-grid");
        grid.setSizeFull();

        //uso de formateadores, por eso uso el metodo addcolumn
        grid.addColumn(instructor -> instructor.getName() + " " + instructor.getApellidos())
                .setHeader("Nombre Completo");
        grid.addColumn(Instructor::getCorreo).setHeader("Correo electrónico");
        grid.addColumn(Instructor::getTelefono).setHeader("Número de teléfono");
      //  grid.addColumn(Instructor::getClases).setHeader("Clases");  //Añadimos las clases a las que esta acosiado, pero no la metemos en el form de antes logicamente



        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editInstructor(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name or nickname...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addAllegadoButton = new Button("Añadir instructor");
        addAllegadoButton.addClickListener(click -> addInstructor());

        var toolbar = new HorizontalLayout(filterText, addAllegadoButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editInstructor(Instructor instructor) {
        if (instructor == null) {
            closeEditor();
        } else {
            form.setClase(instructor);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setClase(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addInstructor() {
        grid.asSingleSelect().clear();
        editInstructor(new Instructor());
    }

    //Como se trata de la vista del administrador, listamos todas las instructores, sin necesidad
    //de filtrar por usuario
    private void updateList() {
        // Obtener todas los instructores totales
        List<Instructor> todosInstructores = service.findAll(); // Asegúrate de que este método devuelva todas las clases

        // Actualizar el grid con todas las clases obtenidas
        grid.setItems(todosInstructores);
    }
}
