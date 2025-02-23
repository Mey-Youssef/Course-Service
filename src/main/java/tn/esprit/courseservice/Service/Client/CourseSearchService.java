package tn.esprit.courseservice.Service.Client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.courseservice.Entity.Course;
import tn.esprit.courseservice.Repository.CourseSearchRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseSearchService {
    @Autowired
    CourseSearchRepository courseSearchRepository;
    public List<Course> searchCoursesByName(String query) {
        return courseSearchRepository.findByNameContainingIgnoreCase(query);
    }

   // public List<Course> searchCoursesByDescriptionOrKeywords(String query) {
    //        return courseSearchRepository.findByDescriptionContainingOrKeyWordsContainingIgnoreCase(query, query);
    //    }
}
