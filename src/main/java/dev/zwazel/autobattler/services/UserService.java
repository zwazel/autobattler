package dev.zwazel.autobattler.services;

import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.utils.FormationServiceTemplate;
import dev.zwazel.autobattler.classes.model.User;
import dev.zwazel.autobattler.classes.model.FormationEntity;
import dev.zwazel.autobattler.classes.utils.database.FormationOnly;
import dev.zwazel.autobattler.classes.utils.database.repositories.FormationEntityRepository;
import dev.zwazel.autobattler.classes.utils.database.repositories.UserRepository;
import dev.zwazel.autobattler.security.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserService {
    private final UserRepository userRepository;
    private final FormationEntityRepository formationEntityRepository;

    public UserService(UserRepository userRepository, FormationEntityRepository formationEntityRepository) {
        this.userRepository = userRepository;
        this.formationEntityRepository = formationEntityRepository;
    }

    private Optional<User> getUserWithJWT(UserRepository userRepository, HttpServletRequest request) {
        JwtUtils jwtUtils = new JwtUtils("");
        String jwt = jwtUtils.getJwtFromCookies(request);
        boolean valid = jwtUtils.validateJwtToken(jwt);
        if (valid) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            return userRepository.findByUsername(username);
        }
        return Optional.empty();
    }

    @GetMapping(path = "/getAllFormations", produces = "application/json")
    public Iterable<FormationOnly> getAllFormations(HttpServletRequest request) {
        Optional<User> userOptional = getUserWithJWT(userRepository, request);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return formationEntityRepository.findAllByUserIdOrderById(user.getId());
        }
        return null;
    }

    @PostMapping(path = "/addFormation")
    public ResponseEntity<String> setFormationForUser(@RequestBody FormationServiceTemplate formationServiceTemplate, HttpServletRequest request) {
        System.out.println("formationServiceTemplate = " + formationServiceTemplate);

        Optional<User> userOptional = getUserWithJWT(userRepository, request);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            try {
                FormationEntity formationEntity = formationServiceTemplate.getFormationEntity(user);
                boolean formationAlreadyExists = formationEntityRepository.existsByFormationJson(formationEntity.getFormationJson());

                if (!formationAlreadyExists) {
                    user.addFormation(formationEntity);
                    userRepository.save(user);
                    return ResponseEntity.ok("Formation added");
                } else {
                    return ResponseEntity.badRequest().body("Formation already exists");
                }
            } catch (UnknownUnitType e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().body("User not found");
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
