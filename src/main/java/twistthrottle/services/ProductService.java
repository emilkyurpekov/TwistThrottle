package twistthrottle.services;




public interface ProductService {

    void updateProductStock(Long productId, int newStock);
}
