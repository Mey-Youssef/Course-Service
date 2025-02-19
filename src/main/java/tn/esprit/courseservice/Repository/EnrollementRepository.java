package tn.esprit.courseservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.courseservice.Entity.Enrollement;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollementRepository extends JpaRepository<Enrollement,Integer> {
    boolean existsByStudentIdAndCourseId(int studentId, int courseId);
    Optional<Enrollement> findByStudentIdAndCourseId(int studentId, int courseId);
    List<Enrollement> findByStudentId(int studentId);
    List<Enrollement> findByCourseId(int courseId);
    long countByCourseId(int courseId);
}
