package twistthrottle.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import twistthrottle.dtos.CartItem;
import twistthrottle.models.entities.Order;
import twistthrottle.models.entities.User;
import twistthrottle.services.OrderService;
import twistthrottle.services.impl.UserServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class OrderConfirmationController {

    private final OrderService orderService;
    private final UserServiceImpl userService;
    private final RestTemplate restTemplate;
    private static final String CART_SERVICE_URL = "http://localhost:8081/api/cart";

    @Autowired
    public OrderConfirmationController(OrderService orderService, UserServiceImpl userService, RestTemplate restTemplate) {
        this.orderService = orderService;
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/orderConfirmation")
    public String showOrderConfirmation(@RequestParam("shippingAddress") String shippingAddress,
                                        @RequestParam("cardNumber") String cardNumber,
                                        Model model,
                                        @AuthenticationPrincipal UserDetails userDetails,
                                        HttpServletRequest request) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        String usernameOrEmail = userDetails.getUsername();
        User loggedInUser = userService.findByEmail(usernameOrEmail)
                .orElseGet(() -> userService.findByUsername(usernameOrEmail));

        HttpHeaders headers = new HttpHeaders();
        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null) {
            headers.set("Cookie", cookieHeader);
        }
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        List<CartItem> cartItems = Collections.emptyList();
        try {
            ResponseEntity<CartItem[]> cartResponse = restTemplate.exchange(
                    CART_SERVICE_URL,
                    HttpMethod.GET,
                    requestEntity,
                    CartItem[].class);

            CartItem[] cartItemsArray = cartResponse.getBody();
            if (cartItemsArray != null) {
                cartItems = Arrays.asList(cartItemsArray);
            }

        } catch (Exception e) {
            System.err.println("Error fetching cart items during order confirmation: " + e.getMessage());
            model.addAttribute("errorMessage", "Could not load cart contents. Please try again.");
            model.addAttribute("user", loggedInUser);
            model.addAttribute("cart", cartItems);
            return "cart";
        }

        if (loggedInUser == null) {
            System.err.println("Could not find full user details in DB for order confirmation: " + usernameOrEmail);
            model.addAttribute("errorMessage", "Could not verify user details. Please try again.");
            model.addAttribute("cart", cartItems);
            return "cart";
        }

        if (cartItems.isEmpty()) {
            model.addAttribute("message", "Your cart is empty. Cannot confirm order.");
            model.addAttribute("user", loggedInUser);
            model.addAttribute("cart", cartItems);
            return "cart";
        }

        try {
            Order order = orderService.createOrder(cartItems, shippingAddress, cardNumber, loggedInUser.getEmail(), cookieHeader);

            model.addAttribute("order", order);
            model.addAttribute("message", "Order confirmed successfully! Your order number is: " + order.getId());
            model.addAttribute("user", loggedInUser);
            model.addAttribute("shippingAddress", shippingAddress);
            return "order-confirmation";

        } catch (IllegalStateException | IllegalArgumentException e) {
            System.err.println("Validation error creating order for user " + loggedInUser.getEmail() + ": " + e.getMessage());
            model.addAttribute("errorMessage", "Could not place order: " + e.getMessage());
            model.addAttribute("user", loggedInUser);
            model.addAttribute("cart", cartItems);
            return "cart";

        } catch (Exception e) {
            System.err.println("Unexpected error creating order for user " + loggedInUser.getEmail() + ": " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("message", "Error creating order. Please contact support.");
            model.addAttribute("user", loggedInUser);
            return "error";
        }
    }
}