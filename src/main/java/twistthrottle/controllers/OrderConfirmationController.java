package twistthrottle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
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

import java.util.List;

@Controller
public class OrderConfirmationController {

    private final OrderService orderService;
    private final UserServiceImpl userService;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String CART_SERVICE_URL = "http://localhost:8081/api/cart";

    @Autowired
    public OrderConfirmationController(OrderService orderService, UserServiceImpl userService) {
        this.orderService = orderService;
        this.userService = userService;
    }


    @PostMapping("/orderConfirmation")
    public String showOrderConfirmation(@RequestParam("shippingAddress") String shippingAddress,
                                        Model model,
                                        @AuthenticationPrincipal UserDetails userDetails) { // <-- Use AuthenticationPrincipal

        if (userDetails == null) {
            return "redirect:/login";
        }

        String usernameOrEmail = userDetails.getUsername();

        User loggedInUser = userService.findByEmail(usernameOrEmail)
                .orElseGet(() -> userService.findByUsername(usernameOrEmail));

        if (loggedInUser == null) {
            System.err.println("Could not find full user details in DB for order confirmation: " + usernameOrEmail);
            model.addAttribute("errorMessage", "Could not verify user details. Please try again.");

            return "cart";
        }

        System.out.println("User confirmed for order: " + loggedInUser.getUsername());

        ResponseEntity<List<CartItem>> cartResponse = restTemplate.exchange(
                CART_SERVICE_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CartItem>>() {}
        );
        List<CartItem> cartItems = cartResponse.getBody();

        if (cartItems == null || cartItems.isEmpty()) {
            model.addAttribute("message", "Your cart is empty. Cannot confirm order.");
            model.addAttribute("user", loggedInUser);
            return "cart";
        }

        try {
            Order order = orderService.createOrder(cartItems, shippingAddress, loggedInUser.getEmail());
            model.addAttribute("order", order);
            model.addAttribute("message", "Order confirmed successfully! Your order number is: " + order.getId());
            model.addAttribute("user", loggedInUser);
            model.addAttribute("shippingAddress", shippingAddress);
            return "order-confirmation";
        } catch (Exception e) {
            System.err.println("Error creating order for user " + loggedInUser.getEmail() + ": " + e.getMessage());
            model.addAttribute("message", "Error creating order: " + e.getMessage());
            model.addAttribute("user", loggedInUser);
            return "error";
        }
    }

}