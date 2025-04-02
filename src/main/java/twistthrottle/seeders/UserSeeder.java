package twistthrottle.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import twistthrottle.models.entities.User;
import twistthrottle.models.entities.enums.RoleType;
import twistthrottle.services.UserService;

import java.util.HashSet;
import java.util.Set;

@Component
@Order(3)
public class UserSeeder implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public UserSeeder(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        seedAdminUser();
        seedRegularUser();
    }

    private void seedAdminUser() {
        String adminEmail = "admin@twistthrottle.com";
        if (!userService.existsByEmail(adminEmail)) {
            User adminUser = new User();
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setUsername("admin");
            adminUser.setEmail(adminEmail);
            adminUser.setPassword("password");

            User savedAdmin = userService.saveUser(adminUser);

            User fetchedAdmin = userService.findById(savedAdmin.getId()).orElse(null);
            if (fetchedAdmin != null) {
                Set<RoleType> adminRoles = new HashSet<>(fetchedAdmin.getRoles());
                adminRoles.add(RoleType.ADMIN);
                fetchedAdmin.setRoles(adminRoles);
                userService.saveUserWithoutEncoding(fetchedAdmin);
            }
        }
    }

    private void seedRegularUser() {
        String userEmail = "user@twistthrottle.com";
        if (!userService.existsByEmail(userEmail)) {
            User regularUser = new User();
            regularUser.setFirstName("Regular");
            regularUser.setLastName("User");
            regularUser.setUsername("user");
            regularUser.setEmail(userEmail);
            regularUser.setPassword("password");


            userService.saveUser(regularUser);
        }
    }
}