package tn.esprit.courseservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.courseservice.Entity.Chapter;
import tn.esprit.courseservice.Entity.CourseStudentProgress;
import tn.esprit.courseservice.Entity.Enrollement;

@Repository
public interface CourseStudentProgressRepository extends JpaRepository<CourseStudentProgress, Integer> {
    boolean existsByEnrollementAndChapter(Enrollement enrollement, Chapter chapter);
    int countByEnrollement(Enrollement enrollement);

}
