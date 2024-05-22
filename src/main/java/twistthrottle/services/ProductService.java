package twistthrottle.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twistthrottle.models.entities.Product;
import twistthrottle.repositories.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProductsSortedByPriceDesc(){
        return productRepository.findAllByOrderByPriceDesc();
    }
}
