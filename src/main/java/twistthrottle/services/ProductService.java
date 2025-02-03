package twistthrottle.services;


import twistthrottle.models.entities.Category;
import twistthrottle.models.entities.Product;
import twistthrottle.models.entities.enums.productType;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {


    void updateProductStock(Long productId, int newStock);
    List<Product> findAllByProductType(productType productType);
    List<Product> findAll();
    List<Product> findAllByCategory(Category category);
    List<Product> findAllByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> findProductByName(String name);
    List<Product> findAllByOrderByPriceDesc();
    List<Product> findByCategoryId(Long categoryId);
}
