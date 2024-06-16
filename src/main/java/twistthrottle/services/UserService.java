package twistthrottle.services;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import twistthrottle.models.entities.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long id);
    User save(User user );
    void delete(User user);
    User findByUsername(String username);
    User findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

}
