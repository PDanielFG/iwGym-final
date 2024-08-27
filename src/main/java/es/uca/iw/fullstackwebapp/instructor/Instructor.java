package es.uca.iw.fullstackwebapp.instructor;

import jakarta.persistence.*;
import es.uca.iw.fullstackwebapp.clase.Clase;
import java.util.List;



@Entity
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Clase> clases;

    private String name;
    private String apellidos;

    public Instructor() {
    }

    public Instructor(String name, String apellidos) {
        this.name = name;
        this.apellidos = apellidos;
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
}
