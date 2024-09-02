package es.uca.iw.fullstackwebapp.reserva;

import es.uca.iw.fullstackwebapp.clase.Clase;
import org.springframework.stereotype.Service;
import es.uca.iw.fullstackwebapp.user.domain.User;
import es.uca.iw.fullstackwebapp.user.services.UserManagementService;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    private ReservaRespoitory reservaRepository;
    private UserManagementService userService;

    public ReservaService(ReservaRespoitory reservaRepository, UserManagementService userService) {
        this.reservaRepository = reservaRepository;
        this.userService = userService;
    }

    public Optional<Reserva> findById(Long id) {
        return reservaRepository.findById(id);
    }

    public void save(Reserva reserva) {
        reservaRepository.save(reserva);
    }

    public void reserve(String username, Clase clase) {
        // Verificar que la clase existe
        if (clase == null || clase.getId() == null) {
            throw new IllegalArgumentException("La clase no existe");
        }

        // Obtener el usuario por su nombre de usuario
        User usuario = userService.findByUsername(username);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        // Crear una nueva reserva
        Reserva reserva = new Reserva(usuario, clase, EstadoReserva.PENDIENTE);

        // Guardar la reserva en la base de datos
        reservaRepository.save(reserva);
    }

    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    public List<Reserva> getReservasPorUsuario(User usuario) {
        return reservaRepository.findByUsuario(usuario);
    }

}
