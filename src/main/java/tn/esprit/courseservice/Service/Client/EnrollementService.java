package tn.esprit.courseservice.Service.Client;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.courseservice.Entity.Course;
import tn.esprit.courseservice.Entity.Enrollement;
import tn.esprit.courseservice.Repository.CourseRepository;
import tn.esprit.courseservice.Repository.EnrollementRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EnrollementService {
    @Autowired
     EnrollementRepository enrollementRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    CourseService courseService;
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

        // Mettre à jour les champs de notation :
        course.setTotalRating(course.getTotalRating() + rating);
        course.setRatingCount(course.getRatingCount() + 1);
        // Mettre à jour le rating calculé à partir des valeurs agrégées
        course.setRating(course.calculateAverageRating());

        courseRepository.save(course);
    }

    //    public Map<Integer, Long> getEnrollmentCountForTutorCourses(int tutorId) {
//        List<Course> courses = getCoursesByTutorId(tutorId);
//        Map<Integer, Long> enrollmentCounts = new HashMap<>();
//        for (Course course : courses) {
//            long count = enrollementRepository.countByCourseId(course.getId());
//            enrollmentCounts.put(course.getId(), count);
//        }
//        return enrollmentCounts;
//    }
//    public List<Enrollement> getEnrollmentsByCourse(int courseId) {
//        List<Enrollement> enrollments = enrollementRepository.findByCourseId(courseId);
//        if(enrollments.isEmpty()){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune inscription trouvée pour le cours avec l'ID: " + courseId);
//        }
//        return enrollments;
//    }
public List<Enrollement> getEnrollmentsByStudent(int studentId) {
    List<Enrollement> enrollments = enrollementRepository.findByStudentId(studentId);
    if (enrollments.isEmpty()) {
        throw new EntityNotFoundException("Aucune inscription trouvée pour l'étudiant avec l'ID : " + studentId);
    }
    return enrollments;
}
    public List<Enrollement> getEnrollmentsByCourse(int courseId) {
        List<Enrollement> enrollments = enrollementRepository.findByCourseId(courseId);
        if (enrollments.isEmpty()) {
            throw new EntityNotFoundException("Aucune inscription trouvée pour le cours avec l'ID : " + courseId);
        }
        return enrollments;
    }
    // 2. Retourner le nombre d'étudiants inscrits pour chaque cours d'un tuteur
    public Map<Integer, Long> getEnrollmentCountForTutorCourses(int tutorId) {
        List<Course> courses = courseService.getCoursesByTutorId(tutorId);
        Map<Integer, Long> enrollmentCounts = new HashMap<>();
        for (Course course : courses) {
            long count = enrollementRepository.countByCourseId(course.getId());
            enrollmentCounts.put(course.getId(), count);
        }
        return enrollmentCounts;
    }


    public double calculateTutorRating(int tutorId) {
        Object[] result = courseRepository.findRatingSumAndCountByTutorId(tutorId)
                .orElseThrow(() -> new EntityNotFoundException("Aucun cours trouvé pour le tuteur avec l'ID : " + tutorId));

        Number sumRating = (Number) result[0];
        Number sumCount = (Number) result[1];

        if (sumCount.longValue() == 0) {
            return 0.0;
        }
        return (double) sumRating.longValue() / sumCount.longValue();
    }




}
