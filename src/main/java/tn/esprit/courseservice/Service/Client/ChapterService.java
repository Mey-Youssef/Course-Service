package tn.esprit.courseservice.Service.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.courseservice.Entity.Chapter;
import tn.esprit.courseservice.Entity.Course;
import tn.esprit.courseservice.Repository.ChapterRepository;
import tn.esprit.courseservice.Repository.CourseRepository;

import java.util.List;
import java.util.Map;

@Service
public class ChapterService {
    @Autowired
     ChapterRepository chapterRepository;
    @Autowired
    CourseRepository courseRepository;
    //Add chapter in a course
    // only the owner ofthe course can add modifiy , get , update , delete

    public Chapter addChapter(int courseId, int tutorId, Chapter chapter) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cours introuvable"));

        if (course.getTutorId() != tutorId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Seul le propriétaire du cours peut ajouter un chapitre");
        }

        chapter.setCourse(course);
        course.setNbChapters(course.getNbChapters() + 1);
        return chapterRepository.save(chapter);
    }
    public Chapter updateChapterPartially(int chapterId, int tutorId, Map<String, Object> updates) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chapitre introuvable"));

        if (chapter.getCourse().getTutorId() != tutorId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Seul le propriétaire du cours peut modifier ce chapitre");
        }

        updates.forEach((key, value) -> {
            switch (key) {
                case "title":
                    chapter.setTitle((String) value);
                    break;
                case "description":
                    chapter.setDescription((String) value);
                    break;
                case "open":
                    chapter.setOpen((Boolean) value);
                    break;
                default:
                    throw new IllegalArgumentException("Champ non reconnu : " + key);
            }
        });

        return chapterRepository.save(chapter);
    }

    public void deleteChapter(int chapterId, int tutorId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chapitre introuvable"));

        if (chapter.getCourse().getTutorId() != tutorId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Seul le propriétaire du cours peut supprimer ce chapitre");
        }

        chapterRepository.delete(chapter);
    }
    public Chapter getChapterById(int chapterId) {
        return chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chapitre introuvable"));
    }

    public List<Chapter> getChaptersByCourse(int courseId, int tutorId) {
        // Vérifier si le cours existe
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cours introuvable"));

        // Vérifier que l'utilisateur est bien le tuteur du cours
        if (course.getTutorId() != tutorId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé : Vous n'êtes pas le propriétaire de ce cours");
        }

        return chapterRepository.findByCourseId(courseId);
    }


}
