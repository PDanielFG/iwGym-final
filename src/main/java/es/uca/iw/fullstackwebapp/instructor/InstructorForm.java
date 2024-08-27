package es.uca.iw.fullstackwebapp.instructor;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.shared.Registration;
import es.uca.iw.fullstackwebapp.clase.Clase;
import es.uca.iw.fullstackwebapp.user.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class InstructorForm extends FormLayout {
    Binder<Instructor> binder = new Binder<>(Instructor.class);
    TextField name = new TextField("Name");
    TextField apellidos = new TextField("Apellidos");
    TextField correo = new TextField("Correo");
    TextField telefono = new TextField("Telefono");
    private Instructor instructor;

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(com|es)$";


    public InstructorForm(){
        addClassName("clase-form");
        binder.bindInstanceFields(this);
        add(name, apellidos, correo, telefono, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, instructor)));
        close.addClickListener(click -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(save, delete, close);
    }

    public void setClase(Instructor instructor) {
        this.instructor = instructor;
        binder.readBean(instructor);
    }

    private void validateAndSave() {
        // Obtener el usuario actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                User currentUser = (User) principal;
                String correoValue = correo.getValue();
                String nameValue = name.getValue();
                String apellidosValue = apellidos.getValue();
                String telefonoValue = telefono.getValue();

                // Asignar los valores del formulario al objeto
                instructor.setName(name.getValue());
                instructor.setApellidos(apellidos.getValue());

                // Verificar que todos los campos estén rellenos
                if (nameValue.isEmpty() || apellidosValue.isEmpty() || correoValue.isEmpty() || telefonoValue.isEmpty()) {
                    Notification.show("Todos los campos son obligatorios.", 3000, Notification.Position.MIDDLE);
                    return; // Salir del método si algún campo está vacío
                }

                if (!correoValue.matches(EMAIL_REGEX)) {
                    Notification.show("Correo no válido", 3000, Notification.Position.MIDDLE);
                    return;
                }
                instructor.setCorreo(correo.getValue());
                instructor.setTelefono(telefono.getValue());

                // Disparar el evento de guardar
                fireEvent(new SaveEvent(this, instructor));
            }
        }

    }

    public static abstract class InstructorFormEvent extends ComponentEvent<InstructorForm> {
        private Instructor instructor;

        protected InstructorFormEvent(InstructorForm source, Instructor instructor) {
            super(source, false);
            this.instructor = instructor;
        }

        public Instructor getInstructor() {
            return instructor;
        }
    }


    public static class SaveEvent extends InstructorFormEvent {
        SaveEvent(InstructorForm source, Instructor instructor) {
            super(source, instructor);
        }
    }

    public static class DeleteEvent extends InstructorFormEvent {
        DeleteEvent(InstructorForm source, Instructor instructor){
            super(source, instructor);
        }
    }

    public static class CloseEvent extends InstructorFormEvent {
        CloseEvent(InstructorForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addSaveListener(ComponentEventListener<T> listener) {
        return addListener(SaveEvent.class, (ComponentEventListener) listener);
    }

    public <T extends ComponentEvent<?>> Registration addDeleteListener(ComponentEventListener<T> listener) {
        return addListener(DeleteEvent.class, (ComponentEventListener) listener);
    }

    public <T extends ComponentEvent<?>> Registration addCloseListener(ComponentEventListener<T> listener) {
        return addListener(CloseEvent.class, (ComponentEventListener) listener);
    }

}


