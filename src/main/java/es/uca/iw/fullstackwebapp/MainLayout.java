package es.uca.iw.fullstackwebapp;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import es.uca.iw.fullstackwebapp.admin.ClasesAdmin;
import es.uca.iw.fullstackwebapp.admin.InstructoresAdmin;
import es.uca.iw.fullstackwebapp.admin.ReservasDeUnaClase;
import es.uca.iw.fullstackwebapp.user.domain.User;
import es.uca.iw.fullstackwebapp.user.security.AuthenticatedUser;
import es.uca.iw.fullstackwebapp.user.views.ClasesListView;
import es.uca.iw.fullstackwebapp.user.views.ReservaListView;
import es.uca.iw.fullstackwebapp.user.views.UserHomeView;
import org.vaadin.lineawesome.LineAwesomeIcon;
import es.uca.iw.fullstackwebapp.admin.ReservaAdmin;


import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("IwGymUca");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        // Agregar elementos visibles para todos los usuarios
        if (accessChecker.hasAccess(UserHomeView.class)) {
            nav.addItem(new SideNavItem("Home", UserHomeView.class, LineAwesomeIcon.HOME_SOLID.create()));
        }

        if (accessChecker.hasAccess(ClasesListView.class)) {
            nav.addItem(new SideNavItem("Clases Disponibles", ClasesListView.class, VaadinIcon.EXIT.create()));
        }

        if (accessChecker.hasAccess(ReservaListView.class)) {
            nav.addItem(new SideNavItem("Mis Reservas", ReservaListView.class, VaadinIcon.OPEN_BOOK.create()));
        }

        // Verificar si el usuario es admin y agregar elementos de administración si es necesario
        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent() && accessChecker.hasAccess(ClasesAdmin.class)) {
            nav.addItem(new SideNavItem("ADMIN. Clases", ClasesAdmin.class, LineAwesomeIcon.USER_SHIELD_SOLID.create()));
        }
        if (maybeUser.isPresent() && accessChecker.hasAccess(InstructoresAdmin.class)) {
            nav.addItem(new SideNavItem("ADMIN. Instructores", InstructoresAdmin.class, LineAwesomeIcon.USER_SHIELD_SOLID.create()));
        }
        if (maybeUser.isPresent() && accessChecker.hasAccess(ReservaAdmin.class)) {
            nav.addItem(new SideNavItem("ADMIN. Reservas", ReservaAdmin.class, LineAwesomeIcon.USER_SHIELD_SOLID.create()));
        }
        if (maybeUser.isPresent() && accessChecker.hasAccess(ReservasDeUnaClase.class)) {
            nav.addItem(new SideNavItem("ADMIN. Reservas de una clase", ReservasDeUnaClase.class, LineAwesomeIcon.USER_SHIELD_SOLID.create()));
        }
 
        return nav;
    }


    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getUsername());
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getUsername());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
