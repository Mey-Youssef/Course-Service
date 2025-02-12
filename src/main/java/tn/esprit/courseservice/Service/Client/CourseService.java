package tn.esprit.courseservice.Service.Client;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.courseservice.Entity.Course;
import tn.esprit.courseservice.Entity.Theme;
import tn.esprit.courseservice.Exception.UnauthorizedAccessException;
import tn.esprit.courseservice.Repository.CourseRepository;
import tn.esprit.courseservice.Repository.ThemeRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    ThemeRepository themeRepository;


    // Méthode de validation du propriétaire du cours
    private Course validateCourseOwnership(int courseId, int tutorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Cours introuvable avec l'ID : " + courseId));

        if (course.getTutorId() != tutorId) {
            throw new UnauthorizedAccessException("Accès refusé : vous n'êtes pas le propriétaire de ce cours !");
        }
        return course;
    }
    public Course addCourseWithTheme(Course course, String themeName, String themeDescription, Integer tutorId) {
        // 1️⃣ Vérification de l'ID du tuteur
        if (tutorId == null) {
            throw new IllegalArgumentException("L'ID du tuteur ne doit pas être nul.");
        }

        // 2️⃣ Affecter l'ID du tuteur au cours
        course.setTutorId(tutorId);

        // 3️⃣ Vérifier si le thème existe déjà, sinon le créer
        Theme theme = themeRepository.findByName(themeName)
                .orElseGet(() -> {
                    Theme newTheme = new Theme();
                    newTheme.setName(themeName);
                    newTheme.setDescription(themeDescription);
                    return themeRepository.save(newTheme);
                });

        course.setTheme(theme);


        return courseRepository.save(course);
    }

    //Assign course to a theme
    public Course assignCourseToTheme(int courseId, int themeId, int tutorId) {
        Course course = validateCourseOwnership(courseId, tutorId);
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException("Thème introuvable avec l'ID : " + themeId));

        course.setTheme(theme);
        return courseRepository.save(course);
    }
    public Course updateCoursePartially(int courseId, Map<String, Object> updates, int tutorId) {
        Course existingCourse = validateCourseOwnership(courseId, tutorId);

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    existingCourse.setName((String) value);
                    break;
                case "description":
                    existingCourse.setDescription((String) value);
                    break;
                case "image":
                    existingCourse.setImage((String) value);
                    break;
                case "difficulty":
                    existingCourse.setDifficulty((String) value);
                    break;
                case "prerequisite":
                    existingCourse.setPrerequisite((String) value);
                    break;
                case "keyWords":
                    existingCourse.setKeyWords((String) value);
                    break;
                case "nbChapters":
                    existingCourse.setNbChapters((Integer) value);
                    break;
                default:
                    throw new IllegalArgumentException("Champ non reconnu : " + key);
            }
        });

        return courseRepository.save(existingCourse);
    }

    public List<Course> getCoursesByTutorId(int tutorId) {
        List<Course> courses = courseRepository.findByTutorId(tutorId);
        if (courses.isEmpty()) {
            throw new EntityNotFoundException("Aucun cours trouvé pour le tuteur avec l'ID : " + tutorId);
        }
        return courses;
    }
    public void deleteCourse(int courseId, int tutorId) {
        Course course = validateCourseOwnership(courseId, tutorId);
        courseRepository.delete(course);
    }




}