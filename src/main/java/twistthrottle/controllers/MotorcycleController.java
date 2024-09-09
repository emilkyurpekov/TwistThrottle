package twistthrottle.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import twistthrottle.models.entities.Motorcycle;
import twistthrottle.models.entities.User;
import twistthrottle.services.MotorcycleServiceImpl;

import java.util.List;

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
    @GetMapping("/motorcycles")
    public ResponseEntity<List<Motorcycle>> getUserMotorcycles(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Motorcycle> motorcycles = motorcycleService.findByUser(user);
        return ResponseEntity.ok(motorcycles);
    }
}
