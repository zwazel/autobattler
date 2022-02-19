package dev.zwazel.autobattler.services;

import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.model.FormationEntity;
import dev.zwazel.autobattler.classes.model.UnitModel;
import dev.zwazel.autobattler.classes.model.User;
import dev.zwazel.autobattler.classes.utils.FormationServiceTemplate;
import dev.zwazel.autobattler.classes.utils.database.FormationOnly;
import dev.zwazel.autobattler.classes.utils.database.UnitOnly;
import dev.zwazel.autobattler.classes.utils.database.UserOnly;
import dev.zwazel.autobattler.classes.utils.database.repositories.FormationEntityRepository;
import dev.zwazel.autobattler.classes.utils.database.repositories.UnitModelRepository;
import dev.zwazel.autobattler.classes.utils.database.repositories.UserRepository;
import dev.zwazel.autobattler.security.jwt.JwtUtils;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserService {
    private final UserRepository userRepository;
    private final FormationEntityRepository formationEntityRepository;
    private final UnitModelRepository unitModelRepository;

    public UserService(UserRepository userRepository, FormationEntityRepository formationEntityRepository, UnitModelRepository unitModelRepository) {
        this.userRepository = userRepository;
        this.formationEntityRepository = formationEntityRepository;
        this.unitModelRepository = unitModelRepository;
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
    public List<FormationOnly> getAllFormations(HttpServletRequest request) {
        Optional<User> userOptional = getUserWithJWT(userRepository, request);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<FormationEntity> formationEntities = formationEntityRepository.findAllByUserOrderById(user);
            return FormationServiceTemplate.getFormationOnlyList(formationEntities);
        }
        return null;
    }

    @GetMapping(path = "/getAllUnits", produces = "application/json")
    public List<UnitOnly> getAllUnits(HttpServletRequest request) {
        Optional<User> userOptional = getUserWithJWT(userRepository, request);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<UnitModel> unitModels = unitModelRepository.findAllByUserOrderByLevel(user);
            return FormationServiceTemplate.getUnitOnlyList(unitModels);
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
                FormationEntity formationEntity = formationServiceTemplate.getFormationEntity(user, unitModelRepository);
                // TODO: 18.02.2022 CHECK IF FORMATION ALREADY EXISTS!!!!
                boolean formationAlreadyExists = false;

                if (!formationAlreadyExists) {
                    user.addFormation(formationEntity);

                    System.out.println("formationEntity = " + formationEntity);

                    userRepository.save(user);
                    return ResponseEntity.ok("Formation added");
                } else {
                    return ResponseEntity.badRequest().body("Formation already exists");
                }
            } catch (UnknownUnitType | NotFoundException e) {
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
    public List<UserOnly> getAllUsers() {
        return userRepository.findAllUserOnly();
    }
}
