package twistthrottle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twistthrottle.models.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findAllByProductType(String productType);
    List<Product> findByCategory(String category);
    List<Product> findAllByPriceBetween(int minPrice, int maxPrice);
    List<Product> findProductByNameContaining(String name);
    List<Product> findByCompatibleModels(String model);
}
