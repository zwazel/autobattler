package dev.zwazel.autobattler.services;

import dev.zwazel.autobattler.BattlerGen2;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.UnitTypes;
import dev.zwazel.autobattler.classes.model.FormationEntity;
import dev.zwazel.autobattler.classes.model.User;
import dev.zwazel.autobattler.classes.utils.Formation;
import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.utils.battle.CreateFormations;
import dev.zwazel.autobattler.classes.utils.database.repositories.FormationEntityRepository;
import dev.zwazel.autobattler.classes.utils.database.repositories.UserRepository;
import dev.zwazel.autobattler.classes.utils.json.History;
import dev.zwazel.autobattler.classes.utils.json.HistoryToJson;
import dev.zwazel.autobattler.security.jwt.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController()
@RequestMapping("/api/battle")
public class BattleServices {
    private final Vector gridSize = new Vector(10, 10);
    private final Vector userGridSize = new Vector(3, 10);

    private final UserRepository userRepository;
    private final FormationEntityRepository formationEntityRepository;

    public BattleServices(UserRepository userRepository, FormationEntityRepository formationEntityRepository) {
        this.userRepository = userRepository;
        this.formationEntityRepository = formationEntityRepository;
    }

    @GetMapping(path = "/getGridSize/battle")
    public ResponseEntity<String> getGridSize() {
        // return the gridSize
        return ResponseEntity.ok(gridSize.toSize());
    }

    @GetMapping(path = "/getGridSize/user")
    public ResponseEntity<String> getUserGridSize() {
        // return the gridSize
        return ResponseEntity.ok(userGridSize.toSize());
    }

    @GetMapping(path = "/getFightHistory/{formationId}")
    public ResponseEntity<String> getFightHistory(@PathVariable long formationId, HttpServletRequest request) {
        JwtUtils jwtUtils = new JwtUtils("");
        String jwt = jwtUtils.getJwtFromCookies(request);
        boolean valid = jwtUtils.validateJwtToken(jwt);
        if (valid) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Optional<User> user1 = userRepository.findByUsername(username);

            // TODO: 08.02.2022 IMPLEMENT SOMETHING LIKE MATCHMAKING TO GET THE ENEMY USER
            Optional<User> user2 = userRepository.findByUsername(username);

            if (user1.isPresent() && user2.isPresent()) {
                Optional<FormationEntity> formationEntity = formationEntityRepository.findById(formationId);
                // TODO: 08.02.2022 FIND ENEMY FORMATION

                if (formationEntity.isPresent()) {
                    CreateFormations createFormations = new CreateFormations(userGridSize, true);

                    int minAmountUnits = 3;
                    int maxAmountUnits = (userGridSize.getX() + userGridSize.getY()) / 2;
                    int randomNumber = (int) (Math.random() * (maxAmountUnits - minAmountUnits + 1)) + minAmountUnits;
                    Formation formation = createFormations.createTestFormation(randomNumber, Side.ENEMY, 0, true, new UnitTypes[]{
                            UnitTypes.MY_FIRST_UNIT,
                    }, 1, 1);

                    FormationEntity formationEntityEnemyRandom = new FormationEntity(formation);

                    BattlerGen2 battler = new BattlerGen2(formationEntity.get(), formationEntityEnemyRandom, false, false, gridSize, true, false);
                    History history = battler.getHistory();

                    if (history == null) {
                        // return error, history doesn't exist
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("History doesn't exist");
                    } else {
                        return ResponseEntity.ok(HistoryToJson.toJson(history));
                    }
                } else {
                    // return error
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Formation doesn't exist");
                }
            } else {
                // return error
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist");
            }
        } else {
            // return error
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized, JWT is invalid!");
        }
    }
}
