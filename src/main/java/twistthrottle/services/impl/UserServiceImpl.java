package twistthrottle.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twistthrottle.dtos.UserDTO;
import twistthrottle.models.entities.Motorcycle;
import twistthrottle.models.entities.User;
import twistthrottle.repositories.MotorcycleRepository;
import twistthrottle.repositories.UserRepository;
import twistthrottle.services.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MotorcycleRepository motorcycleRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, MotorcycleRepository motorcycleRepository) {
        this.userRepository = userRepository;
        this.motorcycleRepository = motorcycleRepository;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);  // Save the user to the database
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
    public User findByEmail(String email) {
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
    
    public void save(Motorcycle motorcycle) {
        motorcycleRepository.save(motorcycle);
    }

}
