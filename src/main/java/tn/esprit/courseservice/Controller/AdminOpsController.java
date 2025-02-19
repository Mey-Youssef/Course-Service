package tn.esprit.courseservice.Controller;

import com.netflix.discovery.converters.Auto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.courseservice.Configuration.AppConfig;
import tn.esprit.courseservice.Entity.Chapter;
import tn.esprit.courseservice.Entity.Course;
import tn.esprit.courseservice.Entity.Enrollement;
import tn.esprit.courseservice.Exception.UnauthorizedAccessException;
import tn.esprit.courseservice.Service.Client.ChapterService;
import tn.esprit.courseservice.Service.Client.CourseService;
import tn.esprit.courseservice.Service.Client.EnrollementService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/courses")
@RequiredArgsConstructor
public class AdminOpsController {
    @Autowired
    CourseService courseService;
    @Autowired
    ChapterService chapterService;
    @Autowired
    EnrollementService enrollementService;
    @Autowired
    AppConfig appConfig;

    private void validateAdmin(int adminId) {
        if (adminId != appConfig.getAdminId()) {
            throw new UnauthorizedAccessException("Accès refusé : vous n'êtes pas un administrateur.");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCourses(@RequestParam int adminId) {
        validateAdmin(adminId);
        List<Course> courses = courseService.getAllCoursesForAdmin(adminId);
        return ResponseEntity.ok(courses);
    }
    @GetMapping("/{courseId}")
    public ResponseEntity<?> getCourseById(@PathVariable int courseId, @RequestParam int adminId) {
        validateAdmin(adminId);
        Course course = courseService.getCourseByIdForAdmin(courseId, adminId);
        return ResponseEntity.ok(course);
    }
    @PatchMapping("/{courseId}")
    public ResponseEntity<?> updateCourse(@PathVariable int courseId,
                                          @RequestParam int adminId,
                                          @RequestBody Map<String, Object> updates) {
        validateAdmin(adminId);
        try {
            Course updatedCourse = courseService.updateCourseForAdmin(courseId, updates, adminId);
            return ResponseEntity.ok(updatedCourse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    // 4. Supprimer un cours (admin)
    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable int courseId, @RequestParam int adminId) {
        validateAdmin(adminId);
        courseService.deleteCourseForAdmin(courseId, adminId);
        return ResponseEntity.ok(Collections.singletonMap("message", "Cours supprimé avec succès."));
    }
   //  @GetMapping("/{courseId}/chapters")
    //    public ResponseEntity<?> getChapters(@PathVariable int courseId, @RequestParam int adminId) {
    //        validateAdmin(adminId);
    //        List<Chapter> chapters = chapterService.getChaptersByCourse(courseId);
    //        return ResponseEntity.ok(chapters);
    //    }
    // @PatchMapping("/{courseId}/chapters/{chapterId}")
    //    public ResponseEntity<?> updateChapter(@PathVariable int courseId,
    //                                           @PathVariable int chapterId,
    //                                           @RequestParam int adminId,
    //                                           @RequestBody Map<String, Object> updates) {
    //        validateAdmin(adminId);
    //        Chapter updatedChapter = chapterService.updateChapterForAdmin(courseId, chapterId, updates);
    //        return ResponseEntity.ok(updatedChapter);
    //    }
    //  @DeleteMapping("/{courseId}/chapters/{chapterId}")
    //    public ResponseEntity<?> deleteChapter(@PathVariable int courseId,
    //                                           @PathVariable int chapterId,
    //                                           @RequestParam int adminId) {
    //        validateAdmin(adminId);
    //        chapterService.deleteChapterForAdmin(courseId, chapterId);
    //        return ResponseEntity.ok(Collections.singletonMap("message", "Chapitre supprimé avec succès."));
    //    }


//  @GetMapping("/{courseId}/enrollments")
//    public ResponseEntity<?> getEnrollments(@PathVariable int courseId, @RequestParam int adminId) {
//        validateAdmin(adminId);
//        List<Enrollement> enrollements = enrollementService.getEnrollmentsByCourse(courseId);
//        return ResponseEntity.ok(enrollements);
//    }


    @GetMapping("/TutorCoursesBy")
    public ResponseEntity<?> getCoursesByTutor(@RequestParam int tutorId, @RequestParam int adminId) {
        try {
            validateAdmin(adminId);
            List<Course> courses = courseService.getCoursesByTutorId(tutorId);
            return ResponseEntity.ok(courses);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }
    @GetMapping("/student")
    public ResponseEntity<?> getEnrollmentsByStudent(@RequestParam int studentId, @RequestParam int adminId) {
        validateAdmin(adminId);
        try {
            List<Enrollement> enrollments = enrollementService.getEnrollmentsByStudent(studentId);
            return ResponseEntity.ok(enrollments);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }
    @GetMapping("/rating")
    public ResponseEntity<?> getTutorRating(@RequestParam int tutorId, @RequestParam int adminId) {
        try {
            validateAdmin(adminId);
            double rating = enrollementService.calculateTutorRating(tutorId);
            return ResponseEntity.ok(Collections.singletonMap("tutorRating", rating));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Erreur interne : " + e.getMessage()));
        }
    }

    @GetMapping("/enrollments")
    public ResponseEntity<?> getEnrollmentCounts(@RequestParam int tutorId, @RequestParam int adminId) {
        try {
            validateAdmin(adminId);
            Map<Integer, Long> enrollmentCounts = enrollementService.getEnrollmentCountForTutorCourses(tutorId);
            return ResponseEntity.ok(enrollmentCounts);
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("message", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Erreur interne : " + e.getMessage()));
        }
    }






}
