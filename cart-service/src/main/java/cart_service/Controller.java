package cart_service;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class Controller {
    private final CartService cartService;  // You might not be using this yet, but that's fine
    private final List<CartItem> cart = new ArrayList<>();

    public Controller(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<CartItem> getCart() {
        return cart;
    }

    @PostMapping("/add")
    public String addToCart(@RequestBody ProductDTO product) {
        cart.add(new CartItem(product, product.getPrice(), product.getQuantity()));
        return "Product added!";
    }

    @DeleteMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cart.removeIf(item -> item.getProduct().getId().equals(productId));
        return "Product removed!";
    }
    private CartItem findCartItem(Long productId) {
        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(productId)) {
                return item;
            }
        }
        return null;
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
}