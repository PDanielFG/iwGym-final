package es.uca.iw.fullstackwebapp.instructor;


import org.springframework.stereotype.Service;
import es.uca.iw.fullstackwebapp.instructor.InstructorRepository;

import java.util.List;

@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }


    public void saveInstructor(Instructor instructor) {
        instructorRepository.save(instructor);
    }
    public void deleteInstructor(Instructor instructor) {
        instructorRepository.delete(instructor);
    }

    public List<Instructor> findAll() {
        return instructorRepository.findAll();
    }
}
