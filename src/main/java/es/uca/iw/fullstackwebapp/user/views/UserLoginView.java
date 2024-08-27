package es.uca.iw.fullstackwebapp.user.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;


//Al ser la unica vista con la etiqueta AnonymousAllowed, esta vista sera la primera que se muestre al usuario
//esta etiqueta hace que UNICAMENTE los usuarios no registrados tengan acceso.
//Los usuarios auteticados no podran acceder a esta vista
@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")

public class UserLoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();
    private final Button registerButton = new Button("Registrarse");

    public UserLoginView() {
        // Configurar la acción del formulario de inicio de sesión
        login.setAction("login");
        login.setForgotPasswordButtonVisible(false);
        login.addForgotPasswordListener(event -> {
            UI.getCurrent().navigate("reset-password");
        });

        // Configurar el botón de registro
        registerButton.addClickListener(e -> {
            UI.getCurrent().navigate("register");
        });

        // Crear un diseño vertical para el formulario y el botón de registro
        VerticalLayout loginLayout = new VerticalLayout(login, registerButton);
        loginLayout.setAlignItems(Alignment.CENTER);
        loginLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        loginLayout.setSizeUndefined();

        // Añadir el diseño del formulario al diseño principal
        add(loginLayout);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // Informar al usuario sobre un error de autenticación
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}