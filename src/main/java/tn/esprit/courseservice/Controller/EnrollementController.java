package tn.esprit.courseservice.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.courseservice.Service.Client.EnrollementService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/enrollements")
public class EnrollementController {
    @Autowired
    EnrollementService enrollementService;
        @PostMapping("/enroll")
        public ResponseEntity<?> enrollStudent(@RequestParam int studentId, @RequestBody List<Integer> courseIds) {
            return ResponseEntity.ok(enrollementService.enrollStudent(studentId, courseIds));
        }

    @DeleteMapping("/unenroll")
    public ResponseEntity<?> unenrollStudent(@RequestParam int studentId, @RequestParam int courseId) {
        enrollementService.unenrollStudent(studentId, courseId);
        return ResponseEntity.ok(Collections.singletonMap("message", "Désinscription réussie."));
    }

    @GetMapping("/courses")
    public ResponseEntity<?> getEnrolledCourses(@RequestParam int studentId) {
        return ResponseEntity.ok(enrollementService.getEnrolledCourses(studentId));
    }

    @PostMapping("/rate")
    public ResponseEntity<?> rateCourse(@RequestParam int studentId, @RequestParam int courseId, @RequestParam int rating) {
        enrollementService.rateCourse(studentId, courseId, rating);
        return ResponseEntity.ok(Collections.singletonMap("message", "Note ajoutée avec succès."));
    }
}
