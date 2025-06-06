package es.uca.iw.fullstackwebapp.reserva;

import es.uca.iw.fullstackwebapp.clase.Clase;
import es.uca.iw.fullstackwebapp.user.domain.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaReserva;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estado;

    // Relación con la entidad Clase
    //la parte de manyToOne debe de llamarse el atributo igual que en oneToMany
    //mappedby
    @ManyToOne
    @JoinColumn(name = "clase_id")
    private Clase clase;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    public Reserva(String username, Long id) {
    }
    public Reserva() {
    }

    public Reserva(User usuario, Clase clase, EstadoReserva estado) {
        this.usuario = usuario;
        this.clase = clase;
        this.fechaReserva = LocalDateTime.now();    //fecha actual
        this.estado= EstadoReserva.PENDIENTE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public EstadoReserva getEstado() {
        return estado;
    }
    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }
}
