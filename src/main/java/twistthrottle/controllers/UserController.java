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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import twistthrottle.dtos.UserDTO;
import twistthrottle.models.entities.Motorcycle;
import twistthrottle.models.entities.User;
import twistthrottle.services.MotorcycleServiceImpl;
import twistthrottle.services.UserServiceImpl;

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
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("emailExists", "An account with this email already exists.");
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/login";
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
    public String showProfilePage(HttpSession session, Model model) {
        // Get the logged-in user from the session
        User user = (User) session.getAttribute("loggedInUser");

        // Check if the user is logged in
        boolean isLoggedIn = user != null;
        if (!isLoggedIn) {
            return "redirect:/login"; // Redirect to login page if not logged in
        }

        // Map User entity to UserDTO (manual conversion here, or use a mapper)
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());

        // Add UserDTO to the model
        model.addAttribute("user", userDTO);
        model.addAttribute("isLoggedIn", isLoggedIn); // Add login status to the model

        // Return the profile view
        return "profile";
    }

}

