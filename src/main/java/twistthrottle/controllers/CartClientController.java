package twistthrottle.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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


@Controller
@RequestMapping("/cart")
public class CartClientController {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String CART_SERVICE_URL = "http://localhost:8081/api/cart";

    @GetMapping
    public String showCart(Model model) {

        try {
            CartItem[] cartItemsArray = restTemplate.getForObject(CART_SERVICE_URL, CartItem[].class);
            List<CartItem> cart = (cartItemsArray != null) ? Arrays.asList(cartItemsArray) : List.of(); // Handle potential null response
            model.addAttribute("cart", cart);
        } catch (Exception e) {
            System.err.println("Error fetching cart from cart service: " + e.getMessage());
            model.addAttribute("errorMessage", "Could not load cart contents.");
            model.addAttribute("cart", List.of());
        }


        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam String name,
                            @RequestParam double price,
                            @RequestParam int quantity) {

        ProductDTO product = new ProductDTO(productId, name, price, quantity);

        try {
            restTemplate.postForObject(
                    CART_SERVICE_URL + "/add?price=" + price + "&quantity=" + quantity,
                    product,
                    String.class
            );
        } catch (Exception e) {
            System.err.println("Error calling cart service /add endpoint: " + e.getMessage());
        }


        return "redirect:/cart";
    }
    @PostMapping("/checkout")
    public String confirmOrder(@RequestParam String shippingAddress, Model model, @AuthenticationPrincipal UserDetails userDetails) { // Use AuthenticationPrincipal

        if (userDetails == null) {
            return "redirect:/login";
        }
        String userEmail = userDetails.getUsername();

        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            model.addAttribute("errorMessage", "Shipping address is required.");
            CartItem[] cartItemsArray = restTemplate.getForObject(CART_SERVICE_URL, CartItem[].class);
            List<CartItem> cart = (cartItemsArray != null) ? Arrays.asList(cartItemsArray) : List.of();
            model.addAttribute("cart", cart);
            return "cart";
        }


        try{
            String response = restTemplate.postForObject(
                    CART_SERVICE_URL + "/checkout?shippingAddress=" + shippingAddress + "&userEmail=" + userEmail,
                    null,
                    String.class
            );
            model.addAttribute("message", response);

            return "order-confirmation";
        } catch(Exception e){
            System.err.println("Error during checkout POST to cart service: " + e.getMessage());
            model.addAttribute("errorMessage", "Checkout failed. Please try again.");
            CartItem[] cartItemsArray = restTemplate.getForObject(CART_SERVICE_URL, CartItem[].class);
            List<CartItem> cart = (cartItemsArray != null) ? Arrays.asList(cartItemsArray) : List.of();
            model.addAttribute("cart", cart);
            return "cart";
        }
    }



}