package es.uca.iw.fullstackwebapp.admin;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import es.uca.iw.fullstackwebapp.MainLayout;


@PageTitle("admin view")
@Route(value = "adminview", layout = MainLayout.class)
@Component // Required for unit testing
@Scope("prototype") // Required for IT testing
@RolesAllowed("ADMIN")

public class AdminView extends VerticalLayout{
    public AdminView() {
        add("admin view");
    }
}
