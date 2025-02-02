package twistthrottle.services;

import twistthrottle.models.entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();
    Optional<Category> findByName(String name);
}
