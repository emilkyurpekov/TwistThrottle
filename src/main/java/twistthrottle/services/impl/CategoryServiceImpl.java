package twistthrottle.services.impl;

import org.springframework.stereotype.Service;
import twistthrottle.models.entities.Category;
import twistthrottle.repositories.CategoryRepository;
import twistthrottle.services.CategoryService;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
