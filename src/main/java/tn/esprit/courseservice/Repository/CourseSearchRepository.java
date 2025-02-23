package tn.esprit.courseservice.Repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.courseservice.Entity.Course;

import java.util.List;
@Repository
public interface CourseSearchRepository extends ElasticsearchRepository<Course, Integer> {
    // Rechercher des cours par nom (insensible à la casse)
    List<Course> findByNameContainingIgnoreCase(String name);

    // Rechercher dans la description ou les mots-clés
    //List<Course> findByDescriptionContainingOrKeyWordsContainingIgnoreCase(String description, String keyWords);
}
