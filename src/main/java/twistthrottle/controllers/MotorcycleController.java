package twistthrottle.controllers;

import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import twistthrottle.models.entities.Motorcycle;
import twistthrottle.models.entities.User;
import twistthrottle.services.MotorcycleServiceImpl;
@Controller
public class MotorcycleController {
    @Autowired
    private MotorcycleServiceImpl motorcycleService;

    @PostMapping("/addMotorcycle")
    public String addMotorcycle(@ModelAttribute Motorcycle motorcycle, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        try {
            motorcycle.setUser(loggedInUser);
            motorcycleService.save(motorcycle);
            redirectAttributes.addFlashAttribute("successMessage", "Motorcycle added successfully!");
            return "redirect:/profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add motorcycle.");
            return "redirect:/profile";
        }
    }
}
