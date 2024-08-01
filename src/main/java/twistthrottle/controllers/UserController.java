package twistthrottle.controllers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
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
    public ModelAndView registerUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return new ModelAndView("redirect:/login");
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

