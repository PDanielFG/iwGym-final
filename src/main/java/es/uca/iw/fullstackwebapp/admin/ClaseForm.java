package es.uca.iw.fullstackwebapp.admin;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.shared.Registration;
import es.uca.iw.fullstackwebapp.clase.Clase;
import es.uca.iw.fullstackwebapp.instructor.Instructor;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.button.ButtonVariant;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import es.uca.iw.fullstackwebapp.user.domain.User;
import com.vaadin.flow.component.notification.Notification;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.vaadin.flow.component.datetimepicker.DateTimePicker;

public class ClaseForm extends FormLayout {
    Binder<Clase> binder = new BeanValidationBinder<>(Clase.class);

    TextField name = new TextField("Name");
    TextField description = new TextField("Description");
    DateTimePicker horario = new DateTimePicker("Horario");
    TextField capacidad = new TextField("Capacidad");
    ComboBox<Instructor> instructor = new ComboBox<>("Instructor");
    private Clase clase;

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public ClaseForm() {
        addClassName("admin-view");
        binder.bindInstanceFields(this);
        add(name, description, horario, capacidad, instructor, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, clase)));
        close.addClickListener(click -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(save, delete, close);
    }

    public void setClase(Clase clase) {
        this.clase = clase;
        binder.readBean(clase);
    }

    public void setInstructors(List<Instructor> instructors) {
        instructor.setItems(instructors);
        instructor.setItemLabelGenerator(Instructor::getName);
    }

    private void validateAndSave() {
        // Obtener el usuario actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                User currentUser = (User) principal;

                // Asignar los valores del formulario al objeto
                clase.setName(name.getValue());
                clase.setDescription(description.getValue());

                // Validar horario
                LocalDateTime selectedHorario = horario.getValue();
                if (selectedHorario != null) {
                    if (selectedHorario.isBefore(LocalDateTime.now())) {
                        Notification.show("El horario no puede ser anterior a la fecha y hora actual.", 3000, Notification.Position.MIDDLE);
                        return;
                    }

                    LocalTime startTime = LocalTime.of(8, 0);
                    LocalTime endTime = LocalTime.of(23, 0);
                    LocalTime selectedTime = selectedHorario.toLocalTime();

                    if (selectedTime.isBefore(startTime) || selectedTime.isAfter(endTime)) {
                        Notification.show("La hora debe estar entre las 8:00 AM y las 11:00 PM.", 3000, Notification.Position.MIDDLE);
                        return;
                    }

                    clase.setHorario(selectedHorario);
                } else {
                    Notification.show("El campo 'Horario' es obligatorio.", 3000, Notification.Position.MIDDLE);
                    return;
                }

                // Validar capacidad
                try {
                    int capacidadValue = Integer.parseInt(capacidad.getValue());
                    if (capacidadValue < 5) {
                        Notification.show("La capacidad mínima de la clase es de 5 personas.", 3000, Notification.Position.MIDDLE);
                        return;
                    }
                    clase.setCapacidad(capacidadValue);
                } catch (NumberFormatException e) {
                    Notification.show("La capacidad debe ser un número entero válido.", 3000, Notification.Position.MIDDLE);
                    return;
                }

                // Validar que se haya seleccionado un instructor
                if (instructor.getValue() == null) {
                    Notification.show("Es obligatorio asignar un instructor a la clase.", 3000, Notification.Position.MIDDLE);
                    return;
                }
                clase.setInstructor(instructor.getValue());

                // Disparar el evento de guardar
                fireEvent(new SaveEvent(this, clase));
            }
        }
    }

    // Eventos y registro de listeners se mantienen igual
    public static abstract class ClaseFormEvent extends ComponentEvent<ClaseForm> {
        private Clase clase;

        protected ClaseFormEvent(ClaseForm source, Clase clase) {
            super(source, false);
            this.clase = clase;
        }

        public Clase getClase() {
            return clase;
        }
    }

    public static class SaveEvent extends ClaseFormEvent {
        SaveEvent(ClaseForm source, Clase clase) {
            super(source, clase);
        }
    }

    public static class DeleteEvent extends ClaseFormEvent {
        DeleteEvent(ClaseForm source, Clase clase) {
            super(source, clase);
        }
    }

    public static class CloseEvent extends ClaseFormEvent {
        CloseEvent(ClaseForm source) {
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
