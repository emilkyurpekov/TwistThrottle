package twistthrottle.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @GetMapping("/register")
    public String register() {
        return "register";
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
