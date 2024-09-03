package es.uca.iw.fullstackwebapp.user.services;

import es.uca.iw.fullstackwebapp.clase.Clase;
import es.uca.iw.fullstackwebapp.reserva.EstadoReserva;
import es.uca.iw.fullstackwebapp.reserva.Reserva;
import es.uca.iw.fullstackwebapp.user.domain.User;

import java.util.List;

public interface EmailService {
    boolean sendRegistrationEmail(User user);
    boolean sendReservationStatusEmail(User user, EstadoReserva estadoReserva, Clase clase);
    boolean modStatusReservationMail(User user, EstadoReserva estadoReserva, Clase clase);
    boolean sendClassReminderEmail(User user, String classDetails, String classDateTime);
    boolean sendReservationsEmail(User user, List<Reserva> reservas);
}
