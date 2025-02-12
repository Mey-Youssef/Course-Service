package tn.esprit.courseservice.Service.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.courseservice.Entity.Course;
import tn.esprit.courseservice.Entity.Enrollement;
import tn.esprit.courseservice.Repository.CourseRepository;
import tn.esprit.courseservice.Repository.EnrollementRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollementService {
    @Autowired
     EnrollementRepository enrollementRepository;
    @Autowired
    CourseRepository courseRepository;
    public List<Enrollement> enrollStudent(int studentId, List<Integer> courseIds) {
        List<Enrollement> enrollements = new ArrayList<>();
        for (Integer courseId : courseIds) {

            if (enrollementRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'étudiant est déjà inscrit à ce cours: " + courseId);
            }

            Enrollement enrollement = new Enrollement();
            enrollement.setStudentId(studentId);
            enrollement.setCourseId(courseId); // On stocke seulement l'ID du cours
            enrollements.add(enrollementRepository.save(enrollement));
        }

        return enrollements;
    }


    public void unenrollStudent(int studentId, int courseId) {
        Enrollement enrollement = enrollementRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inscription non trouvée."));
        enrollementRepository.delete(enrollement);
    }
    public List<Integer> getEnrolledCourses(int studentId) {
        return enrollementRepository.findByStudentId(studentId)
                .stream()
                .map(Enrollement::getCourseId)
                .collect(Collectors.toList());
    }
    public void rateCourse(int studentId, int courseId, int rating) {
        // Vérifier que l'étudiant est bien inscrit à ce cours
        if (!enrollementRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "L'étudiant n'est pas inscrit à ce cours.");
        }

        // Récupérer le cours
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cours introuvable avec l'ID: " + courseId));

        // Mettre à jour la note du cours
        course.setRating(rating);

        courseRepository.save(course);
    }



}
