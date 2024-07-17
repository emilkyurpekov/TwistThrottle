package twistthrottle.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import twistthrottle.models.entities.User;
import twistthrottle.services.UserServiceImpl;

@Controller
public class UserController {
    UserServiceImpl userService;
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Always prepare a new User object for the form
        model.addAttribute("user", new User());
        return "register";  // This view is your register.html
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return new ModelAndView("redirect:/login"); // Assuming you have a login page to redirect to
    }

}

