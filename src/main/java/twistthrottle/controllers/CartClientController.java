package twistthrottle.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import twistthrottle.dtos.CartItem;
import twistthrottle.dtos.ProductDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/cart")
public class CartClientController {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String CART_SERVICE_URL = "http://localhost:8081/api/cart";

    @GetMapping
    public String showCart(Model model) {
        List<CartItem> cart = Arrays.asList(Objects.requireNonNull(
                restTemplate.getForObject(CART_SERVICE_URL, CartItem[].class)));
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam String name,
                            @RequestParam double price,
                            @RequestParam int quantity) {
        ProductDTO product = new ProductDTO(productId, name, price,quantity);
        restTemplate.postForObject(
                CART_SERVICE_URL + "/add?price=" + price + "&quantity=" + quantity,
                product,
                String.class
        );
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long productId) {
        restTemplate.delete(CART_SERVICE_URL + "/remove/" + productId);
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String confirmOrder(@RequestParam String shippingAddress, Model model) {
        String response = restTemplate.postForObject(
                CART_SERVICE_URL + "/checkout?shippingAddress=" + shippingAddress,
                null,
                String.class
        );
        model.addAttribute("message", response);
        return "order-confirmation";
    }
}
