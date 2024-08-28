package es.uca.iw.fullstackwebapp.admin;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import es.uca.iw.fullstackwebapp.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
@RolesAllowed("ADMIN")
@Route(value = "userReservas/:id", layout = MainLayout.class)
@PageTitle("Reservas Usuario. Admin.")
public class UserReservas extends VerticalLayout implements BeforeEnterObserver {

    public UserReservas() {
        add("hola usuario");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

    }
}
