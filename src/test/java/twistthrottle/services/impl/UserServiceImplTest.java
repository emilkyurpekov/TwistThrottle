package twistthrottle.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import twistthrottle.models.entities.Motorcycle;
import twistthrottle.models.entities.User;
import twistthrottle.repositories.MotorcycleRepository;
import twistthrottle.repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MotorcycleRepository motorcycleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private Motorcycle testMotorcycle;
    private final Long userId = 1L;
    private final String username = "testuser";
    private final String email = "test@example.com";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(userId);
        testUser.setUsername(username);
        testUser.setEmail(email);
        testUser.setPassword("password");

        testMotorcycle = new Motorcycle();
    }

    @Test
    void findById_WhenUserExists_ShouldReturnOptionalUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findById(userId);

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldReturnEmptyOptional() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(userId);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void saveUser_ShouldCallRepositorySaveAndReturnUser() {
        when(userRepository.save(testUser)).thenReturn(testUser);

        User savedUser = userService.saveUser(testUser);

        assertNotNull(savedUser);
        assertEquals(testUser, savedUser);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        doNothing().when(userRepository).delete(testUser);

        userService.delete(testUser);

        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void findByUsername_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findByUsername(username)).thenReturn(testUser);

        User result = userService.findByUsername(username);

        assertNotNull(result);
        assertEquals(testUser, result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void findByUsername_WhenUserDoesNotExist_ShouldReturnNull() {
        when(userRepository.findByUsername(username)).thenReturn(null);

        User result = userService.findByUsername(username);

        assertNull(result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void findByEmail_WhenUserExists_ShouldReturnOptionalUser() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findByEmail_WhenUserDoesNotExist_ShouldReturnEmptyOptional() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail(email);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void existsByUsername_WhenUsernameExists_ShouldReturnTrue() {
        when(userRepository.existsByUsername(username)).thenReturn(true);
//
        Boolean result = userService.existsByUsername(username);

        assertTrue(result);
        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void existsByUsername_WhenUsernameDoesNotExist_ShouldReturnFalse() {
        when(userRepository.existsByUsername(username)).thenReturn(false);

        Boolean result = userService.existsByUsername(username);

        assertFalse(result);
        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void existsByEmail_WhenEmailExists_ShouldReturnTrue() {
        when(userRepository.existsByEmail(email)).thenReturn(true);

        Boolean result = userService.existsByEmail(email);

        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void existsByEmail_WhenEmailDoesNotExist_ShouldReturnFalse() {
        when(userRepository.existsByEmail(email)).thenReturn(false);

        Boolean result = userService.existsByEmail(email);

        assertFalse(result);
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void saveMotorcycle_ShouldCallRepositorySave() {
        userService.save(testMotorcycle);

        verify(motorcycleRepository, times(1)).save(testMotorcycle);
    }
}