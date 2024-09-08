package es.uca.iw.fullstackwebapp.clase;

import es.uca.iw.fullstackwebapp.reserva.Reserva;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import es.uca.iw.fullstackwebapp.instructor.Instructor;
import es.uca.iw.fullstackwebapp.user.domain.User;


@Entity
public class Clase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private LocalDateTime horario;
    private int capacidad;

    @ManyToOne
    @JoinColumn(name = "instructor")
    private Instructor instructor;

    //la parte de manyToOne debe de llamarse el atributo igual que en oneToMany
    //mappedby
    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas;

    public Clase() {
    }

    public Clase(String name, String description, LocalDateTime horario, int capacidad, Instructor instructor) {
        this.name = name;
        this.description = description;
        this.horario = horario;
        this.capacidad = capacidad;
        this.instructor = instructor;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getHorario() {
        return horario;
    }
    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }

    public int getCapacidad() {
        return capacidad;
    }
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
    public Instructor getInstructor() {
        return instructor;
    }
    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public int getNumeroDeReservas() {
        return reservas != null ? reservas.size() : 0;
    }
}
