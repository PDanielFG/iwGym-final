package es.uca.iw.fullstackwebapp.clase;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import es.uca.iw.fullstackwebapp.instructor.Instructor;


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
    @JoinColumn(name = "clase_id")
    private Instructor instructor;

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
}
