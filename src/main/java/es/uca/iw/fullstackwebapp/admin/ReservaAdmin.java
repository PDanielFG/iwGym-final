package es.uca.iw.fullstackwebapp.admin;


import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import es.uca.iw.fullstackwebapp.MainLayout;
import es.uca.iw.fullstackwebapp.user.domain.User;
import es.uca.iw.fullstackwebapp.user.security.AuthenticatedUser;
import es.uca.iw.fullstackwebapp.user.services.UserManagementService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Scope;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import java.util.List;
import java.util.Optional;
import es.uca.iw.fullstackwebapp.reserva.Reserva;


@SpringComponent
@Scope("prototype")
@RolesAllowed("ADMIN")
@Route(value = "reservasAdmin", layout = MainLayout.class)
@PageTitle("Reservas Admin.")
public class ReservaAdmin extends VerticalLayout {
    //Haremos un listado de Usuarios, que serán los que tengan alguna reserva hecha
    //Al clickar en un user nos mostrará un listado de sus reservas y desde alli podremos modifcar el estado
    Grid<User> grid = new Grid<>(User.class);
    TextField filterText = new TextField();
    private UserManagementService service;
    private AuthenticatedUser authenticatedUser;
    private Reserva reserva;
    private Long id;


    public ReservaAdmin(UserManagementService service, AuthenticatedUser authenticatedUser) {
        this.service = service;     //Me daba error porque lo tenia comentado. There was an exception while trying to navigate to '' with the root cause 'java.lang.NullPointerException:
        this.authenticatedUser = authenticatedUser;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        add(getToolbar(), getContent());
        updateList();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }



    private void configureGrid() {
        grid.removeAllColumns();  // Limpia todas las columnas
        grid.addClassNames("instructor-grid");
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.addColumn(User::getUsername).setHeader("Nombre de usuario");
        grid.addColumn(User::getEmail).setHeader("Correo electrónico");
        grid.addColumn(user -> user.getReservas().size()).setHeader("Número de reservas");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> {
            User selectedUser = event.getValue();
            if (selectedUser != null) {
                // Navega a la vista de reservas del usuario usando su UUID
                getUI().ifPresent(ui -> ui.navigate("userReservas/" + selectedUser.getId()));
            } else {
                Notification.show("Seleccione un usuario para ver sus reservas.");
            }
        });
    }


    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name or nickname...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        var toolbar = new HorizontalLayout(filterText);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    //Como se trata de la vista del administrador, listamos todas las instructores, sin necesidad
    //de filtrar por usuario
    private void updateList() {
        // Obtener todas los instructores totales
        List<User> todosInstructores = service.findAll(); // Asegúrate de que este método devuelva todas las clases

        // Actualizar el grid con todas las clases obtenidas
        grid.setItems(todosInstructores);
    }
}