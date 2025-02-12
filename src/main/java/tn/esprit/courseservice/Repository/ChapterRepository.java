package tn.esprit.courseservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.courseservice.Entity.Chapter;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter,Integer> {
    List<Chapter> findByCourseId(int courseId);

}

