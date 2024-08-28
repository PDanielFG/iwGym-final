package es.uca.iw.fullstackwebapp.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import es.uca.iw.fullstackwebapp.user.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservaRespoitory extends JpaRepository<Reserva, Long> {
    Optional<Reserva> findById(Long id);

    List<Reserva> findByUsuario(User usuario);
}
