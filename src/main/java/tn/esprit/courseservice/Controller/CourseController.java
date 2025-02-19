package tn.esprit.courseservice.Controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.courseservice.Entity.Course;
import tn.esprit.courseservice.Exception.UnauthorizedAccessException;
import tn.esprit.courseservice.Service.Client.CourseService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/{courseId}/assignTheme")
        public ResponseEntity<Course> assignCourseToTheme(
                @PathVariable int courseId,
                @RequestParam int themeId,
               @RequestParam int tutorId) { 
            try {
               Course updatedCourse = courseService.assignCourseToTheme(courseId, themeId, tutorId);
                return ResponseEntity.ok(updatedCourse);
            } catch (EntityNotFoundException e) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } catch (UnauthorizedAccessException e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
          } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
       }
    @PatchMapping("/{courseId}/update")
    public ResponseEntity<Object> updateCoursePartially(
            @PathVariable int courseId,
            @RequestBody Map<String, Object> updates,
            @RequestParam int tutorId) {
        try {
            Course updatedCourse = courseService.updateCoursePartially(courseId, updates, tutorId);
            return ResponseEntity.ok(updatedCourse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Cours introuvable avec l'ID : " + courseId));
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("message", "Accès refusé : vous n'êtes pas autorisé à modifier ce cours."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Une erreur interne est survenue : " + e.getMessage()));
        }
    }


    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<Object> getCoursesByTutor(@PathVariable int tutorId) {
        try {
            List<Course> courses = courseService.getCoursesByTutorId(tutorId);
            return ResponseEntity.ok(courses);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Erreur interne : " + e.getMessage()));
        }
    }
    @DeleteMapping("/{courseId}/delete")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable int courseId,
            @RequestParam int tutorId) {
        try {
            courseService.deleteCourse(courseId, tutorId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
