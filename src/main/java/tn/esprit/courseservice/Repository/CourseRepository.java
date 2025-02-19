package tn.esprit.courseservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.courseservice.Entity.Course;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByTutorId(int tutorId);
   // List<Course> findByTitleContainingIgnoreCase(String title);
    List<Course> findByRating(int rating);
    @Query("SELECT c FROM Course c WHERE LOWER(c.KeyWords) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Course> findByKeyword(@Param("keyword") String keyword);
    @Query("SELECT COALESCE(SUM(c.totalRating), 0), COALESCE(SUM(c.ratingCount), 0) FROM Course c WHERE c.tutorId = :tutorId")
    Optional<Object[]> findRatingSumAndCountByTutorId(@Param("tutorId") int tutorId);
}
