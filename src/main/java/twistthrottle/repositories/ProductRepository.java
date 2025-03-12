package twistthrottle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import twistthrottle.dtos.ProductDTO;
import twistthrottle.models.entities.Category;
import twistthrottle.models.entities.Product;
import twistthrottle.models.entities.enums.productType;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findById(Long id);
    Product save(Product product);
    void delete(Product product);
    List<Product> findAllByProductType(productType productType);
    List<Product> findAllByCategory(Category category);
    List<Product> findAllByPriceBetween(int price, int price2);
    List<Product> findProductByName(String name);
    List<Product> findAllByOrderByPriceDesc();
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findProductById(Long id);
    ProductDTO findProductDTOById(@Param("id") Long id);
}
