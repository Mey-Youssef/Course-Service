package tn.esprit.courseservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.courseservice.Entity.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
   // List<Course> findByTitleContainingIgnoreCase(String title);
    List<Course> findByRating(int rating);
    @Query("SELECT c FROM Course c WHERE LOWER(c.KeyWords) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Course> findByKeyword(@Param("keyword") String keyword);
}
