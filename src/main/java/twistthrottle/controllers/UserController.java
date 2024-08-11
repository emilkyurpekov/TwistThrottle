package twistthrottle.controllers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import twistthrottle.models.entities.Motorcycle;
import twistthrottle.models.entities.User;
import twistthrottle.services.MotorcycleServiceImpl;
import twistthrottle.services.UserServiceImpl;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private final UserServiceImpl userService;
    private final MotorcycleServiceImpl motorcycleService;
    public UserController(UserServiceImpl userService, MotorcycleServiceImpl motorcycleService) {
        this.userService = userService;
        this.motorcycleService = motorcycleService;
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("emailExists", "An account with this email already exists.");
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/registrationSuccess";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        User user = userService.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/home";
        } else {
            model.addAttribute("loginError", "Invalid username or password");
            return "login";
        }
    }
    @GetMapping("/profile")
    public String showUserProfile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // Make sure this redirect is working correctly.
        }
        model.addAttribute("user", user);
        // Assuming you have additional data like motorcycles
        model.addAttribute("motorcycles", motorcycleService.getMotorcyclesByUser(user));
        return "profile"; // Ensure 'profile.html' is correctly located under /src/main/resources/templates/
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/home";
    }
}

