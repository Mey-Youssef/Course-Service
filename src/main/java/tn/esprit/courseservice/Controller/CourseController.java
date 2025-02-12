package tn.esprit.courseservice.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.courseservice.Entity.Course;
import tn.esprit.courseservice.Service.Client.CourseService;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    @Autowired
     CourseService courseService;
    @PostMapping("/addWithTheme")
    public ResponseEntity<Course> addCourseWithTheme(
            @RequestBody Course course,
            @RequestParam String themeName,
            @RequestParam String themeDescription,
            @RequestParam Integer tutorId) {
        try {
            Course savedCourse = courseService.addCourseWithTheme(course, themeName, themeDescription, tutorId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
        } catch(Exception e) {
            // En production, vous pouvez améliorer la gestion des erreurs en utilisant un ControllerAdvice.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
