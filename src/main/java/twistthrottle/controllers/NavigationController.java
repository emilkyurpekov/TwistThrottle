package twistthrottle.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request) {
        Boolean isLoggedIn = (request.getSession().getAttribute("loggedInUser") != null); // Make sure 'user' is what you actually set in session on login
        model.addAttribute("isLoggedIn", isLoggedIn);
        return "home";
    }
    @GetMapping("/login")
    public  String login(){
        return "login";
    }
    @GetMapping("/about")
    public String about(){
        return "about";
    }
}