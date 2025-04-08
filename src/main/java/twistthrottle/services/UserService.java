package twistthrottle.services;
import twistthrottle.models.entities.User;

import java.util.List;
import java.util.Optional;
public interface UserService {
    Optional<User> findById(Long id);
    User saveUser(User user);
    void delete(User user);
    User findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User saveUserWithoutEncoding(User user);
    List<User> findAllUsers();
    boolean changeUserPassword(String usernameOrEmail, String currentPassword, String newPassword);

}
