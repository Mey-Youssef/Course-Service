package tn.esprit.courseservice.Service.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.courseservice.Entity.Chapter;
import tn.esprit.courseservice.Entity.Course;
import tn.esprit.courseservice.Entity.CourseStudentProgress;
import tn.esprit.courseservice.Entity.Enrollement;
import tn.esprit.courseservice.Repository.ChapterRepository;
import tn.esprit.courseservice.Repository.CourseRepository;
import tn.esprit.courseservice.Repository.CourseStudentProgressRepository;
import tn.esprit.courseservice.Repository.EnrollementRepository;

@Service
public class CourseStudentProgressService {
    @Autowired
     CourseStudentProgressRepository courseStudentProgressRepository;
    @Autowired
    ChapterRepository chapterRepository;
    @Autowired
    EnrollementRepository enrollementRepository;
    @Autowired
    CourseRepository courseRepository;
    public boolean markChapterAsCompleted(int chapterId, int studentId, float studentScore) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chapitre introuvable"));

        if (studentScore < 60.0) {
            return false;
        }

        // Vérifier si le chapitre est bien lié à un cours
        Course course = chapter.getCourse();
        if (course == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Chapitre sans cours associé");
        }

        // Vérifier si l'étudiant est inscrit au cours
        Enrollement enrollement = enrollementRepository.findByStudentIdAndCourseId(studentId, course.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inscription non trouvée"));

        // Vérifier si le chapitre a déjà été complété par cet étudiant
        boolean alreadyCompleted = courseStudentProgressRepository.existsByEnrollementAndChapter(enrollement, chapter);
        if (!alreadyCompleted) {
            // Ajouter une nouvelle entrée de progression
            CourseStudentProgress progress = new CourseStudentProgress();
            progress.setEnrollement(enrollement);
            progress.setChapter(chapter);
            courseStudentProgressRepository.save(progress);
        }

        return true;
    }

    public double calculateStudentProgress(int courseId, int studentId) {
        // Vérifier si le cours existe
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cours introuvable"));

        // Vérifier si l'étudiant est inscrit au cours
        Enrollement enrollement = enrollementRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inscription non trouvée"));

        int totalChapters = course.getNbChapters();
        if (totalChapters == 0) return 0.0; // Aucun chapitre, progression 0%

        // Récupérer le nombre de chapitres complétés par l'étudiant
        int completedChapters = courseStudentProgressRepository.countByEnrollement(enrollement);

        // Calculer la progression en pourcentage
        return (double) completedChapters / totalChapters * 100;
    }



}
