package dev.zwazel.autobattler.services;

import dev.zwazel.autobattler.classes.utils.User;
import dev.zwazel.autobattler.classes.utils.database.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/addFormation")
    public ResponseEntity<String> setFormationForUser() {
        return null;
    }

    @PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
    public User addUser(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }

    @GetMapping(path = "/get/{id}", produces = "application/json")
    public Optional<User> getUser(@PathVariable long id) {
        return userRepository.findById(id);
    }

    @GetMapping(path = "/getAll", produces = "application/json")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
