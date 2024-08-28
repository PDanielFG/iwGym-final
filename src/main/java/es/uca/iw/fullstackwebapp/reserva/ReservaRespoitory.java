package es.uca.iw.fullstackwebapp.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservaRespoitory extends JpaRepository<Reserva, Long> {
    Optional<Reserva> findById(Long id);

}
