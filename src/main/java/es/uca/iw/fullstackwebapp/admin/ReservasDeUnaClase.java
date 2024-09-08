package es.uca.iw.fullstackwebapp.admin;


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
import es.uca.iw.fullstackwebapp.clase.Clase;
import es.uca.iw.fullstackwebapp.clase.ClaseService;
import es.uca.iw.fullstackwebapp.user.security.AuthenticatedUser;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import com.vaadin.flow.component.textfield.TextField;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "reservasDeUnaClase", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@PageTitle("Clases que han sido reservadas por algun usuario")
public class ReservasDeUnaClase extends VerticalLayout {
    Grid<Clase> grid = new Grid<>(Clase.class);
    TextField filterText = new TextField();

    private ClaseService claseService;
    private AuthenticatedUser authenticatedUser;

    @Autowired
    public ReservasDeUnaClase(ClaseService claseService, AuthenticatedUser authenticatedUser) {
        this.claseService = claseService;
        this.authenticatedUser = authenticatedUser;
        addClassName("admin-view");
        setSizeFull();
        configureGrid();

        add(getToolbar(), grid);
        updateList();
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
                return clase.getHorario().format(formatter);
            } else {
                return "";
            }
        }).setHeader("Horario").setSortable(true);
        grid.addColumn(Clase::getCapacidad).setHeader("Capacidad").setSortable(true);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        // Redirige a la vista de reservas de la clase seleccionada
        grid.asSingleSelect().addValueChangeListener(event -> {
            Clase selectedClase = event.getValue();
            if (selectedClase != null) {
                getUI().ifPresent(ui -> ui.navigate("reservasClase/" + selectedClase.getId()));
            } else {
                Notification.show("Seleccione una clase para ver sus reservas.");
            }
        });
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
        List<Clase> todasLasClases = claseService.findAll();

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
}
