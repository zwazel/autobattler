package dev.zwazel.autobattler.services;

import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.model.FormationEntity;
import dev.zwazel.autobattler.classes.model.UnitModel;
import dev.zwazel.autobattler.classes.model.User;
import dev.zwazel.autobattler.classes.units.SimpleUnit;
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
@RequestMapping("/api/authenticated/user")
public class UserService {
    private final UserRepository userRepository;
    private final FormationEntityRepository formationEntityRepository;
    private final UnitModelRepository unitModelRepository;
    private final JwtUtils jwtUtils;

    public UserService(UserRepository userRepository, FormationEntityRepository formationEntityRepository, UnitModelRepository unitModelRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.formationEntityRepository = formationEntityRepository;
        this.unitModelRepository = unitModelRepository;
        this.jwtUtils = jwtUtils;
    }

    private Optional<User> getUserWithJWT(UserRepository userRepository, HttpServletRequest request) {
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

    @PostMapping(path = "/updateUnit", produces = "application/json")
    public ResponseEntity<UnitOnly> updateUnit(@RequestBody SimpleUnit simpleUnit, HttpServletRequest request) {
        Optional<User> userOptional = getUserWithJWT(userRepository, request);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<UnitModel> unitModelOptional = unitModelRepository.findById(simpleUnit.getId());
            if (unitModelOptional.isPresent()) {
                UnitModel unitModel = unitModelOptional.get();
                unitModel.setLevel(simpleUnit.getLevel());
                unitModel.setName(simpleUnit.getName());
                unitModel.setUser(user);
                unitModelRepository.save(unitModel);
                return ResponseEntity.ok(FormationServiceTemplate.getUnitOnly(unitModel));
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(path="/addUnit", produces = "application/json")
    public ResponseEntity<UnitOnly> addUnit(@RequestBody SimpleUnit simpleUnit, HttpServletRequest request) {
        Optional<User> userOptional = getUserWithJWT(userRepository, request);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UnitModel unitModel = new UnitModel();
            unitModel.setLevel(simpleUnit.getLevel());
            unitModel.setName(simpleUnit.getName());
            unitModel.setUser(user);
            unitModelRepository.save(unitModel);
            return ResponseEntity.ok(FormationServiceTemplate.getUnitOnly(unitModel));
        }
        return ResponseEntity.badRequest().build();
    }

    // TODO: 19.02.2022 - SET A LIMIT OF HOW MANY FORMATIONS A USER CAN HAVE
    // TODO: 19.02.2022 - SET A LIMIT OF HOW MANY UNITS PER FORMATION A USER CAN HAVE
    @PostMapping(path = "/addFormation")
    public ResponseEntity<String> setFormationForUser(@RequestBody FormationServiceTemplate formationServiceTemplate, HttpServletRequest request) {
        Optional<User> userOptional = getUserWithJWT(userRepository, request);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            try {
                FormationEntity formationEntity = formationServiceTemplate.getFormationEntity(user, unitModelRepository);

                // this might not be the most performant way to check if a formation already exists, but my brain cant figure out how to do it better right now. so this has to do for now. And I mean i'll limit the amount of formations a user can have to a smaller number, so it won't take too long!
                boolean formationAlreadyExists = false;
                List<FormationEntity> formationEntities = formationEntityRepository.findAllByUserOrderById(user);
                for (FormationEntity formationEntityToCheck : formationEntities) {
                    if (formationEntityToCheck.getFormationJson().equals(formationEntity.getFormationJson())) {
                        formationAlreadyExists = true;
                        break;
                    }
                }

                if (!formationAlreadyExists) {
                    user.addFormation(formationEntity);

                    System.out.println("formation has been added");

                    userRepository.save(user);
                    return ResponseEntity.ok("Formation added");
                } else {
                    System.err.println("formation already exists");
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

    @GetMapping(path = "/get/{username}", produces = "application/json")
    public Optional<User> getUserByName(@PathVariable String username) {
        return userRepository.findByUsername(username);
    }

    @GetMapping(path = "/getAll", produces = "application/json")
    public List<UserOnly> getAllUsers() {
        return userRepository.findAllUserOnly();
    }
}
