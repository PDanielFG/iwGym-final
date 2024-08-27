package es.uca.iw.fullstackwebapp.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRespoitory extends JpaRepository<Reserva, Long> {

}
