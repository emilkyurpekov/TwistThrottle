package twistthrottle.services;


import twistthrottle.models.entities.Product;

import java.util.List;

public interface ProductService {

    void updateProductStock(Long productId, int newStock);
    List<Product> findAllByProductType(String productType);
    List<Product> findByCategory(String category);
    List<Product> findAllByPriceBetween(int minPrice, int maxPrice);
    List<Product> findProductByNameContaining(String name);
    List<Product> findByCompatibleModels(String model);
    List<Product> findAllByOrderByPriceDesc();
}
