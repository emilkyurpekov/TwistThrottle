package cart_service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class Controller {

    private final List<CartItem> cart = new ArrayList<>();

    @GetMapping
    public List<CartItem> getCart() {
        return cart;
    }

    @PostMapping("/add")
    public String addToCart(@RequestBody ProductDTO product, @RequestParam double price, @RequestParam int quantity) {
        cart.add(new CartItem(product, price, quantity));
        return "Product added!";
    }

    @DeleteMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cart.removeIf(item -> item.getProduct().getProductId().equals(productId));
        return "Product removed!";
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCartItem(@RequestParam Long productId, @RequestParam int quantity) {
        if (quantity <= 0) {
            return ResponseEntity.badRequest().body("Quantity must be greater than zero.");
        }

        for (CartItem item : cart) {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(quantity);
                return ResponseEntity.ok("Quantity updated.");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in cart.");
    }
}