// cart_service/Controller.java
package cart_service;

import cart_service.entities.CartItem;
import cart_service.entities.Product; // Keep Product, it represents items IN THE CART
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class Controller {

    private final List<CartItem> cart = new ArrayList<>(); // In-memory cart

    @GetMapping
    public List<CartItem> getCart() {
        return cart;
    }

    @PostMapping("/add")
    public String addToCart(@RequestBody ProductDTO product) {
        cart.add(new CartItem(mapDtoToEntity(product), product.getPrice(), product.getQuantity()));
        return "Product added to cart!";
    }
    private ProductD mapDtoToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(product.getQuantity());
        return product;
    }

    @DeleteMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cart.removeIf(item -> item.getProduct().getId().equals(productId));
        return "Product removed from cart!";
    }

    @PostMapping("/update")
    public String updateCartItem(@RequestBody ProductDTO productDTO) {
        CartItem cartItem = findCartItem(productDTO.getId());
        if (cartItem != null) {
            cartItem.setQuantity(productDTO.getQuantity());
            return "Quantity updated!";
        } else {
            return "Error: Cart item not found.";
        }
    }

    private CartItem findCartItem(Long productId) {
        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(productId)) {
                return item;
            }
        }
        return null;
    }
    @PostMapping("/checkout")
    public List<CartItem> checkout() {
        return cart;
    }
}