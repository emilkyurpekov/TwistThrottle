package twistthrottle.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import twistthrottle.dtos.CartItem;
import twistthrottle.dtos.OrderRequest;
import twistthrottle.dtos.ProductDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/cart")
public class CartClientController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String CART_SERVICE_URL = "http://localhost:8081/api/cart";

    @GetMapping
    public List<CartItem> showCart() {
        return Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(CART_SERVICE_URL, CartItem[].class)));

    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam String name, @RequestParam double price, @RequestParam int quantity) {
        ProductDTO product = new ProductDTO(productId, name, price);
        restTemplate.postForObject(CART_SERVICE_URL + "/add?quantity=" + quantity, product, String.class);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long productId) {
        restTemplate.delete(CART_SERVICE_URL + "/remove/" + productId);
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String confirmOrder(@RequestParam String shippingAddress, Model model) {
        OrderRequest orderRequest = new OrderRequest(shippingAddress);
        String response = restTemplate.postForObject(CART_SERVICE_URL + "/checkout", orderRequest, String.class);
        model.addAttribute("message", response);
        return "order-confirmation";
    }
}
