package twistthrottle.services;

import twistthrottle.models.entities.User;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public Boolean existsByUsername(String username) {
        return null;
    }

    @Override
    public Boolean existsByEmail(String email) {
        return null;
    }
}
