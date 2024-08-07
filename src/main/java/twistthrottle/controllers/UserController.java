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
import twistthrottle.models.entities.User;
import twistthrottle.services.UserServiceImpl;

@Controller
public class UserController {
    @Autowired
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
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
            return "register";  // Assuming your registration page view name is "register"
        }
        // Continue with registration if the email doesn't exist
        userService.saveUser(user);
        return "redirect:/registrationSuccess";  // Redirect to a success page after registration
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
            return "redirect:/login"; // Redirect to login page if user is not logged in
        }
        model.addAttribute("user", user);
        return "profile"; // Return the profile view
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // This removes all session attributes and effectively logs the user out
        }
        return "redirect:/home"; // Redirects to the login page after logout
    }
}

