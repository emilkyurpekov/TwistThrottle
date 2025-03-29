package twistthrottle.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- Import PasswordEncoder
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twistthrottle.models.entities.Motorcycle;
import twistthrottle.models.entities.User;
import twistthrottle.models.entities.enums.RoleType; // <-- Import RoleType Enum
import twistthrottle.repositories.MotorcycleRepository;
import twistthrottle.repositories.UserRepository;
import twistthrottle.services.UserService;

import java.util.HashSet; // <-- Import HashSet
import java.util.List;
import java.util.Optional;
import java.util.Set; // <-- Import Set

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MotorcycleRepository motorcycleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired

    public UserServiceImpl(UserRepository userRepository,
                           MotorcycleRepository motorcycleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.motorcycleRepository = motorcycleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Set<RoleType> roles = new HashSet<>();
            roles.add(RoleType.USER);
            user.setRoles(roles);
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User saveUserWithoutEncoding(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            User existingUser = userRepository.findById(user.getId()).orElse(null);
            if (existingUser != null) {
                user.setRoles(existingUser.getRoles());
            } else {
                Set<RoleType> roles = new HashSet<>();
                roles.add(RoleType.USER);
                user.setRoles(roles);
            }
        }
        return userRepository.save(user);
    }


    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void save(Motorcycle motorcycle) {
        motorcycleRepository.save(motorcycle);
    }
}