package tn.esprit.courseservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.courseservice.Entity.Theme;

import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme,Integer> {
    Optional<Theme> findByName(String name);
}
