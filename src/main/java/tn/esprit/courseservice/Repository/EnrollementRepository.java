package tn.esprit.courseservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.courseservice.Entity.Enrollement;

@Repository
public interface EnrollementRepository extends JpaRepository<Enrollement,Integer> {
}
