package twistthrottle.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twistthrottle.models.entities.Product;
import twistthrottle.repositories.MotorcycleRepository;
import twistthrottle.repositories.ProductRepository;
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
}
