package twistthrottle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twistthrottle.models.entities.Category;
import twistthrottle.models.entities.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    List<Category> findAllById(Long categoryId);


    Optional<Category> findByName(String Name);

}
