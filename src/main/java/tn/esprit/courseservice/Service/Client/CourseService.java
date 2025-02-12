package tn.esprit.courseservice.Service.Client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.courseservice.Entity.Course;
import tn.esprit.courseservice.Entity.Theme;
import tn.esprit.courseservice.Repository.CourseRepository;
import tn.esprit.courseservice.Repository.ThemeRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    ThemeRepository themeRepository;


    public Course addCourseWithTheme(Course course, String themeName, String themeDescription, Integer tutorId) {
        // Validation de l'ID du tuteur
        if (tutorId == null) {
            throw new IllegalArgumentException("L'ID du tuteur ne doit pas être nul.");
        }
        // Affecter l'ID du tuteur au cours
        course.setTutorId(tutorId);

        // Déclaration de la variable pour le thème
        Theme theme;

        // Vérifier si le thème existe déjà, sinon le créer
        try {
            Optional<Theme> existingTheme = themeRepository.findByName(themeName);
            if (existingTheme.isPresent()) {
                theme = existingTheme.get();
            } else {
                theme = new Theme();
                theme.setName(themeName);
                theme.setDescription(themeDescription);
                theme = themeRepository.save(theme);
            }
        } catch(Exception e) {
            throw new RuntimeException("Erreur lors de la récupération ou de la sauvegarde du thème : " + e.getMessage(), e);
        }

        // Affecter le thème au cours
        course.setTheme(theme);

        // Sauvegarder le cours en gérant les exceptions
        try {
            Course savedCourse = courseRepository.save(course);
            return savedCourse;
        } catch(Exception e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du cours : " + e.getMessage(), e);
        }
    }
    //Assign course to a theme
    public Course assignCourseToTheme(int courseId, int themeId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Optional<Theme> optionalTheme = themeRepository.findById(themeId);

        if (optionalCourse.isEmpty()) {
            throw new IllegalArgumentException("Cours introuvable !");
        }

        if (optionalTheme.isEmpty()) {
            throw new IllegalArgumentException("Thème introuvable !");
        }

        Course course = optionalCourse.get();
        Theme theme = optionalTheme.get();

        course.setTheme(theme);
        return courseRepository.save(course);
    }


}
