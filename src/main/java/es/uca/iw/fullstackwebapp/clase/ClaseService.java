package es.uca.iw.fullstackwebapp.clase;


import org.springframework.stereotype.Service;
import es.uca.iw.fullstackwebapp.clase.ClaseRepository;
import org.springframework.transaction.annotation.Transactional;
import es.uca.iw.fullstackwebapp.instructor.Instructor;

import java.util.List;

@Service
public class ClaseService {
    private final ClaseRepository claseRepository;

    public ClaseService(ClaseRepository claseRepository) {
        this.claseRepository = claseRepository;
    }

    @Transactional
    public void saveClase(Clase clase) {
        if (clase.getInstructor() == null) {
            throw new IllegalArgumentException("Cada clase debe tener un instructor asignado.");
        }
        // Aquí va la lógica para guardar la clase (repository.save(clase), por ejemplo)
        claseRepository.save(clase);
    }
    public void deleteClase(Clase clase) {
        claseRepository.delete(clase);
    }

    public List<Clase> findAll() {
        return claseRepository.findAll();
    }
}
