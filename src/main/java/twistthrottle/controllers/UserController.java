package twistthrottle.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import twistthrottle.models.entities.User;

@Controller
public class UserController {

    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        User user = new User();  // Assuming you have a default constructor
        model.addAttribute("user", user);
        return "register";  // Name of the Thymeleaf template
    }

   // @PostMapping("/register")
   // public ModelAndView registerUser(@RequestParam("username") String username,
                               //      @RequestParam("email") String email,
                                  //   @RequestParam("password") String password) {
        // Assuming you have a service to handle registration
        //userService.registerNewUser(username, email, password);
      //  return new ModelAndView("redirect:/home");  // Redirect after successful registration
   // }
}
