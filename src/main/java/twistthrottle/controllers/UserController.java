package twistthrottle.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import twistthrottle.dtos.UserDTO;
import twistthrottle.models.entities.Order;
import twistthrottle.models.entities.User;
import twistthrottle.services.impl.MotorcycleServiceImpl;
import twistthrottle.services.impl.OrderServiceImpl;
import twistthrottle.services.impl.UserServiceImpl;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private final UserServiceImpl userService;
    private final MotorcycleServiceImpl motorcycleService;
    private final OrderServiceImpl orderService;
    public UserController(UserServiceImpl userService, MotorcycleServiceImpl motorcycleService, OrderServiceImpl orderService) {
        this.userService = userService;

        this.motorcycleService = motorcycleService;
        this.orderService = orderService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("emailExists", "An account with this email already exists.");
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String showProfilePage(@AuthenticationPrincipal UserDetails userDetails, Model model) { // <-- Notice the change here

        if (userDetails == null) {
            return "redirect:/login";
        }

        String usernameOrEmail = userDetails.getUsername();

        User user = userService.findByEmail(usernameOrEmail)
                .orElseGet(() -> userService.findByUsername(usernameOrEmail));

        if (user == null) {
            System.err.println("Could not find full user details in DB for: " + usernameOrEmail);
            return "redirect:/login?error=user_details_not_found";
        }

        List<Order> userOrders = orderService.getOrdersByUser(user.getId());

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());

        model.addAttribute("orders", userOrders);
        model.addAttribute("user", userDTO);

        return "profile";
    }
}

