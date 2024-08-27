package es.uca.iw.fullstackwebapp.clase;


import org.springframework.stereotype.Service;
import es.uca.iw.fullstackwebapp.clase.ClaseRepository;

import java.util.List;

@Service
public class ClaseService {
    private final ClaseRepository claseRepository;

    public ClaseService(ClaseRepository claseRepository) {
        this.claseRepository = claseRepository;
    }

    public void saveClase(Clase clase) {
        claseRepository.save(clase);
    }
    public void deleteClase(Clase clase) {
        claseRepository.delete(clase);
    }

    public List<Clase> findAll() {
        return claseRepository.findAll();
    }
}
