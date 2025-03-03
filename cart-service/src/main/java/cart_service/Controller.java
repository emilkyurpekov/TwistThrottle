package cart_service;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class Controller {
    private final CartService cartService;
    private final List<CartItem> cart = new ArrayList<>();

    public Controller(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<CartItem> getCart() {
        return cart;
    }

    @PostMapping("/add")
    public String addToCart(@RequestBody ProductDTO product,@RequestParam double price, @RequestParam int quantity) {
        cart.add(new CartItem(product, price, quantity));
        return "Product added!";
    }

    @DeleteMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cart.removeIf(item -> item.getProduct().getId().equals(productId));
        return "Product removed!";
    }
    @PostMapping("/update")
    public void updateCartItem(@RequestParam Long productId,
                               @RequestParam int quantity,
                               HttpSession session) {
        cartService.updateItemQuantity(productId, quantity, session);
    }
}
