package twistthrottle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import twistthrottle.models.entities.User;
import twistthrottle.models.entities.enums.RoleType;
import twistthrottle.services.UserService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/users-list";
    }
    @GetMapping("/users/{userId}/edit")
    public String showEditUserRolesForm(@PathVariable Long userId, Model model) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId)); // Handle if user doesn't exist

        RoleType[] allRoles = RoleType.values();

        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);

        Set<String> userRoleNames = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
        model.addAttribute("userRoles", userRoleNames);


        return "admin/user-edit-roles";
    }

}