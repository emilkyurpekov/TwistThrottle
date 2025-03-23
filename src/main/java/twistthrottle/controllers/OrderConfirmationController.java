package twistthrottle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.client.RestTemplate;
import twistthrottle.dtos.CartItem;
import twistthrottle.models.entities.Order;
import twistthrottle.models.entities.User;
import twistthrottle.services.OrderService;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;

@Controller
public class OrderConfirmationController {

    private final OrderService orderService;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String CART_SERVICE_URL = "http://localhost:8081/api/cart";

    @Autowired
    public OrderConfirmationController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/orderConfirmation")
    public String showOrderConfirmation(@RequestParam("shippingAddress") String shippingAddress, Model model, HttpSession session) {

        System.out.println("Entering orderConfirmation. Session ID: " + session.getId());
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            System.out.println("loggedInUser is null!");
            return "redirect:/login";
        }
        System.out.println("loggedInUser is: " + loggedInUser.getUsername());

        ResponseEntity<List<CartItem>> cartResponse = restTemplate.exchange(
                CART_SERVICE_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CartItem>>() {
                }
        );

        List<CartItem> cartItems = cartResponse.getBody();

        if (cartItems == null || cartItems.isEmpty()) {
            model.addAttribute("message", "Your cart is empty.");
            return "redirect:/cart";
        }


        try {
            Order order = orderService.createOrder(cartItems, shippingAddress, loggedInUser.getEmail());
            model.addAttribute("order", order);
            model.addAttribute("message", "Order confirmed successfully! Your order number is: " + order.getId());
        } catch (Exception e) {
            model.addAttribute("message", "Error creating order: " + e.getMessage());
            return "error-page";




        }
        model.addAttribute("user", loggedInUser);
        model.addAttribute("shippingAddress", shippingAddress);
        model.addAttribute("cart", cartItems);
        return "order-confirmation";
    }
}