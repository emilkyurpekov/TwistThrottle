package twistthrottle;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import twistthrottle.models.entities.Category;
import twistthrottle.models.entities.Product;
import twistthrottle.models.entities.enums.productType;
import twistthrottle.repositories.ProductRepository;
import twistthrottle.services.ProductServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductServiceImpl productServiceImpl; // Autowire the implementation class

    @Test
    void updateProductStock_ValidProductId_ValidStock() {
        Long productId = 1L;
        int newStock = 50;

        Product product = new Product();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productServiceImpl.updateProductStock(productId, newStock);

        assertEquals(newStock, product.getStock());
    }

    @Test
    void updateProductStock_InvalidProductId() {
        Long productId = 1L;
        int newStock = 50;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> productServiceImpl.updateProductStock(productId, newStock));
    }

    @Test
    void updateProductStock_NegativeStock() {
        Long productId = 1L;
        int newStock = -50;

        assertThrows(IllegalStateException.class, () -> productServiceImpl.updateProductStock(productId, newStock));
    }

    @Test
    void findAllByProductType_ValidProductType() {
        productType productType = twistthrottle.models.entities.enums.productType.PARTS;

        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());

        when(productRepository.findAllByProductType(productType)).thenReturn(products);

        assertEquals(products, productServiceImpl.findAllByProductType(productType));
    }

    @Test
    void findAllByCategory_ValidCategory() {
        Category category = new Category();
        category.setId(1L);

        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());

        when(productRepository.findAllByCategory(category)).thenReturn(products);

        assertEquals(products, productServiceImpl.findAllByCategory(category));
    }

    @Test
    void findAllByPriceBetween_ValidRange() {
        int minPrice = 100;
        int maxPrice = 200;

        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());

        when(productRepository.findAllByPriceBetween(minPrice, maxPrice)).thenReturn(products);

        assertEquals(products, productServiceImpl.findAllByPriceBetween(minPrice, maxPrice));
    }

    @Test
    void findProductByName_ValidName() {
        String name = "Product1";

        List<Product> products = new ArrayList<>();
        products.add(new Product());

        when(productRepository.findProductByName(name)).thenReturn(products);

        assertEquals(products, productServiceImpl.findProductByName(name));
    }
}
