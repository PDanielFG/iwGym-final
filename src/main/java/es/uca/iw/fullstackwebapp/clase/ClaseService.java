package es.uca.iw.fullstackwebapp.clase;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaseService {
    private ClaseRepository claseRepository;

    public ClaseService(ClaseRepository claseRepository) {
        this.claseRepository = claseRepository;
    }

    public List<Clase> findAll() {
        return claseRepository.findAll();
    }

    public Clase saveClase(Clase clase) {
        return claseRepository.save(clase);
    }

    public void deleteClase(Clase clase) {
        claseRepository.delete(clase);
    }

    // Método para reducir la capacidad de la clase
    public void reducirCapacidad(Clase clase) {
        if (clase.getCapacidad() > 0) {
            clase.setCapacidad(clase.getCapacidad() - 1);
            saveClase(clase); // Guarda la clase con la nueva capacidad
        } else {
            throw new IllegalStateException("No hay capacidad disponible en esta clase.");
        }
    }
}
