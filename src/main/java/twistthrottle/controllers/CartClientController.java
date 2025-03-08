package twistthrottle.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
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
        if(!isLoggedIn){
            return "login";
        }
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
    @PostMapping("/update")
    public String updateCartItem(@RequestParam Long productId,
                                 @RequestParam int quantity,
                                 HttpSession session,
                                 Model model) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // Or APPLICATION_JSON if you switch

            // No request body needed for URL parameters
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);


            restTemplate.exchange(
                    CART_SERVICE_URL + "/update?productId=" + productId + "&quantity=" + quantity,
                    HttpMethod.PUT, // Use PUT
                    requestEntity,
                    String.class
            );
        } catch (HttpClientErrorException e) {

            model.addAttribute("errorMessage", "Error updating cart: " + e.getResponseBodyAsString());
            List<CartItem> cart = Arrays.asList(Objects.requireNonNull(
                    restTemplate.getForObject(CART_SERVICE_URL, CartItem[].class)));
            model.addAttribute("cart", cart);
            return "cart";
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred.");
            List<CartItem> cart = Arrays.asList(Objects.requireNonNull(
                    restTemplate.getForObject(CART_SERVICE_URL, CartItem[].class)));
            model.addAttribute("cart", cart);
            return "cart";
        }

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