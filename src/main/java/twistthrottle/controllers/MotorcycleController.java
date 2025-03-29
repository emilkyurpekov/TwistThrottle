package twistthrottle.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import twistthrottle.models.entities.Motorcycle;
import twistthrottle.models.entities.User;
import twistthrottle.services.impl.MotorcycleServiceImpl;
import twistthrottle.services.impl.UserServiceImpl;

import java.util.List;

@Controller
public class MotorcycleController {
    @Autowired
    private MotorcycleServiceImpl motorcycleService;
    @Autowired
    private final UserServiceImpl userService;

    public MotorcycleController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/addMotorcycle")
    public String addMotorcycle(@ModelAttribute Motorcycle motorcycle,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        String usernameOrEmail = userDetails.getUsername();

        User loggedInUser = userService.findByEmail(usernameOrEmail)
                .orElseGet(() -> userService.findByUsername(usernameOrEmail));

        if (loggedInUser == null) {
            System.err.println("Could not find full user details in DB for addMotorcycle: " + usernameOrEmail);
            redirectAttributes.addFlashAttribute("errorMessage", "Could not find user details.");
            return "redirect:/profile";
        }


        try {
            motorcycle.setUser(loggedInUser);
            motorcycleService.save(motorcycle);
            redirectAttributes.addFlashAttribute("successMessage", "Motorcycle added successfully!");
            return "redirect:/profile";
        } catch (Exception e) {
            System.err.println("Error saving motorcycle for user " + usernameOrEmail + ": " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add motorcycle.");
            return "redirect:/profile";
        }
    }
    @GetMapping("/motorcycles")
    public ResponseEntity<List<Motorcycle>> getUserMotorcycles(@AuthenticationPrincipal UserDetails userDetails) { // <-- Use @AuthenticationPrincipal

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String usernameOrEmail = userDetails.getUsername();


        User loggedInUser = userService.findByEmail(usernameOrEmail)
                .orElseGet(() -> userService.findByUsername(usernameOrEmail));

        if (loggedInUser == null) {
            System.err.println("Could not find full user details in DB for getUserMotorcycles: " + usernameOrEmail);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        List<Motorcycle> motorcycles = motorcycleService.findByUser(loggedInUser);
        return ResponseEntity.ok(motorcycles);
    }
}
