package es.uca.iw.fullstackwebapp.user.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import es.uca.iw.fullstackwebapp.MainLayout;
import jakarta.annotation.security.PermitAll;

@PageTitle("Inicio")
@PermitAll
@Route(value = "", layout = MainLayout.class)
public class UserHomeView extends VerticalLayout {

    public UserHomeView() {
        addClassName("home-view");

        // Título Principal
        H1 title = new H1("Bienvenid@ a nuestra aplicación de gestión de clases");
        title.getStyle().set("text-align", "center");
        add(title);

        // Introducción
        add(new Paragraph("¡Gracias por visitarnos! Esta aplicación está diseñada para facilitarte la gestión de tus actividades en el gimnasio. Aquí podrás encontrar información sobre nuestras clases, reservar tus horarios preferidos, y mucho más."));

        // Quiénes somos
        H2 quienesSomosTitle = new H2("¿Quiénes somos?");
        add(quienesSomosTitle);
        add(new Paragraph("Somos un gimnasio comprometido con tu bienestar y salud. Nuestro objetivo es ofrecerte una experiencia completa y personalizada a través de una amplia gama de clases y servicios. Nuestro equipo está formado por instructores altamente capacitados que te ayudarán a alcanzar tus metas." +
                " Además, nuestro panel de administración permite una gestión eficaz de usuarios, clases e instructores, garantizando una experiencia fluida y eficiente."));

        // Qué hacemos
        H2 queHacemosTitle = new H2("¿Qué hacemos?");
        add(queHacemosTitle);
        add(new Paragraph("Nuestra aplicación te permite gestionar diferentes aspectos de tu experiencia en el gimnasio:"));

        add(new Paragraph("Una vez estes registrado podras elegir entre una gran cantidad de clases que ofertamos para ti, según tus necesidades y gustos. Muy sencillo, tan solo pulsando sobre el boton 'Reservar'."));
        add(new Paragraph("Además, cada día actualizamos y sacamos nuevas clases, cada una de ellas con un instructor especializado, perfectamente cualificado en la materia"));
        add(new Paragraph("¿Eres olvidadizo/a? No te preocupes, con nuestro sistema de recordatorios y notificaciones vía email, estarás al tanto de cualquier novedad. Modificación del horario, estado de tus reservas de clase, resumen de todas tus reservas etc..."));
        add(new Paragraph("En IwGymUca estamos por y para tí, por eso estamos continuamente mejorando, y contratando a nuevo personal, cada vez que demos de alta a un instructor nuevo, podrás verlo en nuestros menús de clases."));
        add(new Paragraph("Nuestros administradores de sistema están en todo, pudiendo ver, comprobar y por supuesto confirmar las reservas que realices, o recomendandote otras, de nuevos deportes, para tí, o que encajen bien para lograr tus metas fitness"));

        // Contacto
        H2 contactoTitle = new H2("Contacto");
        add(contactoTitle);
        add(new Paragraph("Para más información o asistencia, no dudes en ponerte en contacto con nosotros a través de nuestro correo electrónico:"));
        Anchor contactEmail = new Anchor("mailto:support@yourgym.com", "iw.gym.uca@gmail.com");
        add(contactEmail);

        // Enlaces a otras vistas
        H2 explorarTitle = new H2("Explora más opciones, no lo dudes, y comienza viendo todas nuestras clases");
        add(explorarTitle);
        add(new RouterLink("Ver Clases Disponibles", ClasesListView.class));
        //add(new RouterLink("Mi Perfil", UserProfileView.class));

        // Estilo opcional
        getStyle().set("padding", "20px");
    }
}
