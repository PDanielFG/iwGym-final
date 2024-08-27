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
    private String correo;
    private String telefono;

    public Instructor() {
    }

    public Instructor(String name, String apellidos, String correo, String telefono) {
        this.name = name;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Clase> getClases() {
        return clases;
    }

    public void setClases(List<Clase> clases) {
        this.clases = clases;
    }
}
