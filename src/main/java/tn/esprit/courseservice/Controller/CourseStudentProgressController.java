package tn.esprit.courseservice.Controller;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.courseservice.Service.Client.CourseStudentProgressService;

@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor

public class CourseStudentProgressController {
    @Autowired
     CourseStudentProgressService courseStudentProgressService;


    @PostMapping("/complete")
    public ResponseEntity<String> markChapterAsCompleted(
            @RequestParam int chapterId,
            @RequestParam int studentId,
            @RequestParam float studentScore) {

        boolean isCompleted = courseStudentProgressService.markChapterAsCompleted(chapterId, studentId, studentScore);

        if (isCompleted) {
            return ResponseEntity.ok("Chapitre marqué comme complété avec succès.");
        } else {
            return ResponseEntity.badRequest().body("Score insuffisant pour marquer le chapitre comme complété.");
        }
    }


    @GetMapping("/{courseId}/{studentId}")
    public ResponseEntity<Double> getStudentProgress(@PathVariable int courseId, @PathVariable int studentId) {
        double progress = courseStudentProgressService.calculateStudentProgress(courseId, studentId);
        return ResponseEntity.ok(progress);
    }
}
