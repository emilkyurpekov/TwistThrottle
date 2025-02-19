package cart_service;

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
    public String addToCart(@RequestBody ProductDTO product, @RequestParam int quantity) {
        cart.add(new CartItem(product, quantity));
        return "Product added!";
    }

    @DeleteMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cart.removeIf(item -> item.getProduct().getId().equals(productId));
        return "Product removed!";
    }
}