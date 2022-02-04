package dev.zwazel.autobattler.demo;

import com.google.gson.*;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.units.Unit;
import dev.zwazel.autobattler.classes.utils.*;
import dev.zwazel.autobattler.classes.utils.database.FormationEntity;
import dev.zwazel.autobattler.classes.utils.database.repositories.FormationEntityRepository;
import dev.zwazel.autobattler.classes.utils.database.repositories.UserRepository;
import dev.zwazel.autobattler.classes.utils.database.repositories.UserRoleRepository;
import dev.zwazel.autobattler.classes.utils.map.Grid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static dev.zwazel.autobattler.classes.enums.Side.ENEMY;
import static dev.zwazel.autobattler.classes.enums.Side.FRIENDLY;

@RestController()
@RequestMapping("/init")
public class InitDBService {
    private final FormationEntityRepository formationEntityRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    private Grid grid = new Grid(10, 10);
    private ArrayList<Unit> friendlyUnitList = new ArrayList<>();
    private ArrayList<Unit> enemyUnitList = new ArrayList<>();
    private DemoBattler demoBattler = new DemoBattler();

    public InitDBService(UserRepository userRepository, FormationEntityRepository formationEntityRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.formationEntityRepository = formationEntityRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @GetMapping(path = "/initDB", produces = "text/plain")
    public String initDB() {
        try {
            User userLeft = getDataFromFormationPlan(FRIENDLY, "friendlyFormationBig.json");
            User userRight = getDataFromFormationPlan(ENEMY, "enemyFormationBig.json");

            userRepository.save(userLeft);
            userRepository.save(userRight);

            Formation userLeftFormation = new Formation(userLeft, friendlyUnitList);
            Formation userRightFormation = new Formation(userRight, enemyUnitList);

            formationEntityRepository.save(new FormationEntity(userLeftFormation, userLeft));
            formationEntityRepository.save(new FormationEntity(userRightFormation, userRight));

            userRoleRepository.save(new UserRole(EnumUserRole.ROLE_USER));
            userRoleRepository.save(new UserRole(EnumUserRole.ROLE_ADMIN));

            return "DB Initialized";
        } catch (URISyntaxException | FileNotFoundException | UnknownUnitType e) {
            e.printStackTrace();

            return "DB initialization failed";
        }
    }

    private User getDataFromFormationPlan(Side side, String fileName) throws URISyntaxException, FileNotFoundException, UnknownUnitType {
        GetFile getFile = new GetFile();
        File file = getFile.getFileFromResource(fileName);
        Reader reader = new FileReader(file);
        JsonElement jsonElement = JsonParser.parseReader(reader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("formation").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject unit = jsonArray.get(i).getAsJsonObject();
            Unit actualUnit = UnitTypeParser.getUnit(unit, demoBattler, side);
            grid.updateOccupiedGrid(actualUnit.getGridPosition(), actualUnit);
            switch (side) {
                case FRIENDLY -> friendlyUnitList.add(actualUnit);
                case ENEMY -> enemyUnitList.add(actualUnit);
            }
        }

        return new Gson().fromJson(jsonObject.get("user").getAsJsonObject(), User.class);
    }
}
