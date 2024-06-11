package twistthrottle.repositories;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twistthrottle.models.entities.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findById(Long id);
    Product save(Product product);
    void delete(Product product);
    List<Product> findAllByProductType(String productType);
    List<Product> findAllByCategory(String category);
    List<Product> findAllByPriceBetween(int minPrice, int maxPrice);
    List<Product> findProductByNameContaining(String name);
    List<Product> findByCompatibleModels(String model);
    List<Product> findAllByOrderByPriceDesc();
}
