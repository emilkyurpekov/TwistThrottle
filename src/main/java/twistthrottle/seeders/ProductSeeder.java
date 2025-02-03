package twistthrottle.seeders;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import twistthrottle.models.entities.Category;
import twistthrottle.models.entities.Product;
import twistthrottle.models.entities.enums.productType;
import twistthrottle.repositories.CategoryRepository;
import twistthrottle.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ProductSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductSeeder(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            Category exhaustSystems = categoryRepository.findByName("Exhaust Systems")
                    .orElseThrow(() -> new RuntimeException("Category 'Exhaust Systems' not found"));
            Category engineComponents = categoryRepository.findByName("Engine Components")
                    .orElseThrow(() -> new RuntimeException("Category 'Engine Components' not found"));
            Category accessories = categoryRepository.findByName("Accessories")
                    .orElseThrow(() -> new RuntimeException("Category 'Accessories' not found"));

            Product product1 = new Product();
            product1.setName("High-Performance Muffler");
            product1.setProductType(productType.PARTS);
            product1.setPrice(BigDecimal.valueOf(199.99));
            product1.setStock(50);
            product1.setCategory(exhaustSystems);

            Product product2 = new Product();
            product2.setName("Turbocharger Kit");
            product2.setProductType(productType.PARTS);
            product2.setPrice(BigDecimal.valueOf(499.99));
            product2.setStock(30);
            product2.setCategory(engineComponents);

            Product product3 = new Product();
            product3.setName("Motorcycle Cover");
            product3.setProductType(productType.ACCESSORIES);
            product3.setPrice(BigDecimal.valueOf(29.99));
            product3.setStock(100);
            product3.setCategory(accessories);

            Product product4 = new Product();
            product4.setName("Akrapovic Honda exhaust");
            product4.setProductType(productType.PARTS);
            product4.setPrice(BigDecimal.valueOf(1200.00));
            product4.setStock(20);
            product4.setCategory(exhaustSystems);

            Product product5 = new Product();
            product5.setName("Shifter");
            product5.setProductType(productType.PARTS);
            product5.setPrice(BigDecimal.valueOf(300.00));
            product5.setStock(50);
            product5.setCategory(accessories);


            Product product6 = new Product();
            product6.setName("Clutch");
            product6.setProductType(productType.PARTS);
            product6.setPrice(BigDecimal.valueOf(400.00));
            product6.setStock(50);
            product6.setCategory(accessories);

            Product product7 = new Product();
            product7.setName("Akrapovic BMW exhaust");
            product7.setProductType(productType.PARTS);
            product7.setPrice(BigDecimal.valueOf(1200.00));
            product7.setStock(20);
            product7.setCategory(exhaustSystems);

            Product product8 = new Product();
            product8.setName("Spark plugs");
            product8.setProductType(productType.PARTS);
            product8.setPrice(BigDecimal.valueOf(20.00));
            product8.setStock(20);
            product8.setCategory(engineComponents);

            Product product9 = new Product();
            product9.setName("Oil Filter");
            product9.setProductType(productType.PARTS);
            product9.setPrice(BigDecimal.valueOf(200.00));
            product9.setStock(20);
            product9.setCategory(engineComponents);



            productRepository.saveAll(List.of(product1, product2, product3));

            System.out.println("Products have been seeded!");
        } else {
            System.out.println("Products already exist. Skipping seeding.");
        }
    }
}
