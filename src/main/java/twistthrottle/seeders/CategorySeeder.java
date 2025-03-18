package twistthrottle.seeders;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import twistthrottle.models.entities.Category;
import twistthrottle.models.entities.enums.categoryType;
import twistthrottle.repositories.CategoryRepository;

@Component
public class CategorySeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategorySeeder(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            Category exhaustSystems = new Category(
                    "Exhaust Systems",
                    categoryType.EXHAUST_SYSTEMS,
                    "Components related to exhaust systems."
            );
            Category engineComponents = new Category(
                    "Engine Components",
                    categoryType.ENGINE_COMPONENTS,
                    "Essential parts for engine systems."
            );
            Category accessories = new Category(
                    "Accessories",
                    categoryType.ACCESSORIES,
                    "Various motorcycle accessories."
            );

            categoryRepository.save(exhaustSystems);
            categoryRepository.save(engineComponents);
            categoryRepository.save(accessories);
        }
    }
}
