package twistthrottle.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import twistthrottle.models.entities.Product;
import twistthrottle.services.ProductServiceImpl;

import java.util.List;

@Controller
public class NavigationController {
    private final ProductServiceImpl productService;

    public NavigationController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request) {
        Boolean isLoggedIn = (request.getSession().getAttribute("loggedInUser") != null);
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
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/home";
    }
    @GetMapping("/adminlogin")
    public String adminLogin() {
        return "adminlogin";
    }
     @GetMapping("/products")
    public String getProducts(Model model, HttpServletRequest request) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
         Boolean isLoggedIn = (request.getSession().getAttribute("loggedInUser") != null);
         model.addAttribute("isLoggedIn", isLoggedIn);
        return "products";
    }
}