package dev.zwazel.autobattler.services;

import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.model.FormationEntity;
import dev.zwazel.autobattler.classes.model.FormationUnitTable;
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
import dev.zwazel.autobattler.classes.utils.rest.FormationIdOnly;
import dev.zwazel.autobattler.security.jwt.JwtUtils;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${zwazel.app.maximumAmountUnitsPerUser}")
    private int MAXIMUM_AMOUNT_UNITS;

    @Value("${zwazel.app.maximumAmountFormationsPerUser}")
    private int MAXIMUM_AMOUNT_FORMATIONS;

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

    @PostMapping(path = "/addUnit", produces = "application/json")
    public ResponseEntity<UnitOnly> addUnit(@RequestBody SimpleUnit simpleUnit, HttpServletRequest request) {
        Optional<User> userOptional = getUserWithJWT(userRepository, request);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Long amountUnits = unitModelRepository.countByUser(user);
            if (amountUnits < MAXIMUM_AMOUNT_UNITS) {
                UnitModel unitModel = new UnitModel();
                unitModel.setLevel(simpleUnit.getLevel());
                unitModel.setName(simpleUnit.getName());
                unitModel.setUnitType(simpleUnit.getUnitType());
                unitModel.setUser(user);
                unitModelRepository.save(unitModel);
                return ResponseEntity.ok(FormationServiceTemplate.getUnitOnly(unitModel));
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(path = "/deleteFormation")
    public ResponseEntity<?> deleteFormationFromUser(@RequestBody FormationIdOnly formationID, HttpServletRequest request) {
        Optional<User> userOptional = getUserWithJWT(userRepository, request);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Optional<FormationEntity> formationEntityOptional = formationEntityRepository.findById(formationID.formationID);
            if (formationEntityOptional.isPresent()) {
                FormationEntity formationEntity = formationEntityOptional.get();
                if (formationEntity.getUser().getId() == user.getId()) {
                    formationEntityRepository.delete(formationEntity);

                    return ResponseEntity.ok().build();
                }
            }
        }
        return ResponseEntity.badRequest().build();
    }

    // TODO: 19.02.2022 - SET A LIMIT OF HOW MANY FORMATIONS A USER CAN HAVE
    // TODO: 19.02.2022 - SET A LIMIT OF HOW MANY UNITS PER FORMATION A USER CAN HAVE
    @PostMapping(path = "/addFormation")
    public ResponseEntity<?> setFormationForUser(@RequestBody FormationServiceTemplate formationServiceTemplate, HttpServletRequest request) {

        Optional<User> userOptional = getUserWithJWT(userRepository, request);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Long amountFormations = formationEntityRepository.countByUser(user);
            if (amountFormations < MAXIMUM_AMOUNT_FORMATIONS) {

                // new formation
                if (formationServiceTemplate.getId() == null) {
                    try {
                        FormationEntity formationEntity = formationServiceTemplate.getFormationEntity(user, unitModelRepository);

                        if (!formationAlreadyExists(formationEntity, user)) {
                            user.addFormation(formationEntity);

                            userRepository.save(user);

                            return ResponseEntity.ok(FormationServiceTemplate.getFormationOnly(formationEntity));
                        } else {
                            System.err.println("formation already exists");
                            return ResponseEntity.badRequest().body("Formation already exists");
                        }
                    } catch (UnknownUnitType | NotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    // update formation
                    Optional<FormationEntity> formationEntityOptional = formationEntityRepository.findById(formationServiceTemplate.getId());
                    if (formationEntityOptional.isPresent()) {
                        FormationEntity formationEntity = formationEntityOptional.get();
                        if (formationEntity.getUser().getId() == user.getId()) {
                            // add every unit not in the formation
                            try {
                                FormationEntity newFormationEntity = formationServiceTemplate.getFormationEntity(user, unitModelRepository);
                                for (FormationUnitTable unitNew : newFormationEntity.getFormationUnitTable()) {
                                    boolean found = false;
                                    for (FormationUnitTable unitOld : formationEntity.getFormationUnitTable()) {
                                        if (unitNew.getUnit().getID() == unitOld.getUnit().getID()) {
                                            found = true;
                                            unitOld.update(unitNew);
                                            break;
                                        }
                                    }
                                    if (!found) {
                                        formationEntity.addFormationUnitTable(unitNew);
                                    }
                                }

                                if (!formationAlreadyExists(formationEntity, user)) {
                                    formationEntityRepository.save(formationEntity);
                                    return ResponseEntity.ok(FormationServiceTemplate.getFormationOnly(formationEntity));
                                } else {
                                    System.err.println("formation already exists");
                                    return ResponseEntity.badRequest().body("Formation already exists");
                                }

                            } catch (UnknownUnitType | NotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        return ResponseEntity.badRequest().body("Formation not found");
                    }
                }
            } else {
                return ResponseEntity.badRequest().body("You can't have more than " + MAXIMUM_AMOUNT_FORMATIONS + " formations");
            }
        }
        return ResponseEntity.badRequest().body("User not found");
    }

    private boolean formationAlreadyExists(FormationEntity formation, User user) {
        boolean formationAlreadyExists = true;
        for (FormationUnitTable formationUnitTable : formation.getFormationUnitTable()) {
            List<FormationEntity> formationEntitySame = formationEntityRepository.findAllByFormationUnitTableAndUser(formationUnitTable, user);
            if (formationEntitySame.size() <= 0) {
                formationAlreadyExists = false;
                break;
            }
        }
        return formationAlreadyExists;
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
