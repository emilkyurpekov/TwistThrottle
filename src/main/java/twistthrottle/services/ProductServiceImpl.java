package twistthrottle.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twistthrottle.models.entities.Category;
import twistthrottle.models.entities.Product;
import twistthrottle.models.entities.enums.productType;
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
    public void updateProductStock(Long productId, int newStock)throws IllegalStateException, IllegalArgumentException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Product not found"));
        if (newStock < 0) {
            throw new IllegalArgumentException("Stock can't be negative");
        }
        product.setStock(newStock);
        productRepository.save(product);
    }

    @Override
    public List<Product> findAllByProductType(productType productType) {
        return productRepository.findAllByProductType(productType);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findAllByCategory(Category category) {
        return productRepository.findAllByCategory(category);
    }

    @Override
    public List<Product> findAllByPriceBetween(int minPrice, int maxPrice) {
        return productRepository.findAllByPriceBetween(minPrice,maxPrice);
    }

    @Override
    public List<Product> findProductByName(String name) {
       return productRepository.findProductByName(name);
    }
    @Override
    public List<Product> findAllByOrderByPriceDesc() {
        return productRepository.findAllByOrderByPriceDesc();
    }

}
