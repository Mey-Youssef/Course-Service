package tn.esprit.courseservice.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.courseservice.Entity.Course;
import tn.esprit.courseservice.Service.Client.CourseSearchService;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor

public class CourseSearchController {
    @Autowired
    CourseSearchService courseSearchService;

    @GetMapping("/coursesByName")
    public ResponseEntity<List<Course>> searchCoursesByName(@RequestParam String query) {
        List<Course> courses = courseSearchService.searchCoursesByName(query);
        return ResponseEntity.ok(courses);
        // @GetMapping("/coursesByDescription")
        //    public ResponseEntity<List<Course>> searchCoursesByDescription(@RequestParam String query) {
        //        List<Course> courses = courseSearchService.searchCoursesByDescriptionOrKeywords(query);
        //        return ResponseEntity.ok(courses);
        //    }
    }
}
