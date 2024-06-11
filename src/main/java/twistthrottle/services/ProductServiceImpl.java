package twistthrottle.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twistthrottle.models.entities.Product;
import twistthrottle.repositories.ProductRepository;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    @Transactional
    public void updateProductStock(Long productId, int newStock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Product not found"));
        if (newStock < 0) {
            throw new IllegalArgumentException("Stock can't be negative");
        }
        product.setStock(newStock);
        productRepository.save(product);
    }

    @Override
    public List<Product> findAllByProductType(String productType) {
        return productRepository.findAllByProductType(productType);
    }

    @Override
    public List<Product> findByCategory(String category) {
        return null;
    }

    @Override
    public List<Product> findAllByPriceBetween(int minPrice, int maxPrice) {
        return null;
    }

    @Override
    public List<Product> findProductByNameContaining(String name) {
        return null;
    }

    @Override
    public List<Product> findByCompatibleModels(String model) {
        return null;
    }

    @Override
    public List<Product> findAllByOrderByPriceDesc() {
        return null;
    }

}
