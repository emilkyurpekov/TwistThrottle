package twistthrottle.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String showCart(Model model, HttpServletRequest request) {
        List<CartItem> cart = Arrays.asList(Objects.requireNonNull(
                restTemplate.getForObject(CART_SERVICE_URL, CartItem[].class)));
        model.addAttribute("cart", cart);
        Boolean isLoggedIn = (request.getSession().getAttribute("loggedInUser") != null);
        model.addAttribute("isLoggedIn", isLoggedIn);
        if (!isLoggedIn) {
            return "login";
        }
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam String name,
                            @RequestParam double price,
                            @RequestParam int quantity) {
        ProductDTO product = new ProductDTO(productId, name, price, quantity);
        restTemplate.postForObject(CART_SERVICE_URL + "/add", product, String.class);
        return "redirect:/cart";
    }
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long productId) {
        restTemplate.delete(CART_SERVICE_URL + "/remove/" + productId);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCartItem(@RequestParam Long productId,
                                 @RequestParam int quantity, @RequestParam String name, @RequestParam double price,
                                 HttpSession session) {
        ProductDTO productDTO = new ProductDTO(productId,name,price,quantity);

        restTemplate.postForObject(CART_SERVICE_URL + "/update", productDTO, String.class);
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