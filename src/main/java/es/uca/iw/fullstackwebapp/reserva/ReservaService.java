package es.uca.iw.fullstackwebapp.reserva;

import es.uca.iw.fullstackwebapp.clase.Clase;
import es.uca.iw.fullstackwebapp.clase.ClaseService; // Importa el servicio de clase
import es.uca.iw.fullstackwebapp.user.domain.User;
import es.uca.iw.fullstackwebapp.user.services.UserManagementService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    private ReservaRespoitory reservaRepository;
    private UserManagementService userService;
    private ClaseService claseService; // Agrega ClaseService como dependencia

    public ReservaService(ReservaRespoitory reservaRepository, UserManagementService userService, ClaseService claseService) {
        this.reservaRepository = reservaRepository;
        this.userService = userService;
        this.claseService = claseService; // Inicializa ClaseService
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

        // Verificar si hay capacidad disponible en la clase
        if (clase.getCapacidad() <= 0) {
            throw new IllegalStateException("No hay capacidad disponible en esta clase.");
        }

        // Crear una nueva reserva
        Reserva reserva = new Reserva(usuario, clase, EstadoReserva.PENDIENTE);

        // Guardar la reserva en la base de datos
        reservaRepository.save(reserva);

        // Disminuir la capacidad de la clase usando el servicio de clase
        claseService.reducirCapacidad(clase);
    }

    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    public List<Reserva> getReservasPorUsuario(User usuario) {
        return reservaRepository.findByUsuario(usuario);
    }

    public Reserva findByUser(User user) {
        return (Reserva) reservaRepository.findByUsuario(user);
    }

    public List<Reserva> findReservasByClaseId(Long claseId) {
        return reservaRepository.findByClaseId(claseId);
    }
}
