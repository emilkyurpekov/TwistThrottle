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
    public String register() {
        return "register";
    }

    @GetMapping("/register-form")
    public String showRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }
   // @PostMapping("/register")
   // public ModelAndView registerUser(@ModelAttribute User user) {
  //      userService.saveUser(user);
  //      return new ModelAndView("redirect:/login");
//    }

}

