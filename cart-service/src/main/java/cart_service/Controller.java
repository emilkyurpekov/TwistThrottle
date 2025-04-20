package cart_service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class Controller {

    private final CartService cartService;

    @Autowired
    public Controller(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<CartItem> getCart(HttpSession session) {
        return cartService.getCart(session);
    }

    @PostMapping("/add")
    public String addToCart(@RequestBody ProductDTO product, @RequestParam int quantity, HttpSession session) {
        try {
            cartService.addToCart(session, product, quantity);
            return "Product added!";
        } catch (Exception e) {
            System.err.println("Error in cart-service adding product: " + e.getMessage());
            return "Error adding product";
        }
    }

    @DeleteMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, HttpSession session) {
        cartService.removeFromCart(session, productId);
        return "Product removed!";
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCartItem(@RequestParam Long productId, @RequestParam int quantity, HttpSession session) {
        if (quantity <= 0) {
            return ResponseEntity.badRequest().body("Quantity must be greater than zero.");
        }
        try {
            cartService.updateItemQuantity(productId, quantity, session);
            return ResponseEntity.ok("Quantity updated.");
        } catch (Exception e) {
            System.err.println("Error updating cart item in cart-service: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in cart.");
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestParam String shippingAddress, HttpSession session) {
        System.out.println("Cart service checkout called for session " + session.getId() + " (Order creation happens in main app)");
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Shipping address is required.");
        }
        return ResponseEntity.ok("Cart ready for checkout confirmation. Shipping to " + shippingAddress);
    }

    @PostMapping("/clear")
    public ResponseEntity<String> clearCart(HttpSession session) {
        cartService.clearCart(session);
        return ResponseEntity.ok("Cart cleared");
    }
}