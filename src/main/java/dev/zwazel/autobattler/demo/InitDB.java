package dev.zwazel.autobattler.demo;

import com.google.gson.*;
import dev.zwazel.autobattler.classes.Utils.GetFile;
import dev.zwazel.autobattler.classes.Utils.UnitTypeParser;
import dev.zwazel.autobattler.classes.Utils.User;
import dev.zwazel.autobattler.classes.Utils.database.repositories.UserRepository;
import dev.zwazel.autobattler.classes.Utils.map.Grid;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.units.Unit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static dev.zwazel.autobattler.classes.enums.Side.ENEMY;
import static dev.zwazel.autobattler.classes.enums.Side.FRIENDLY;

public class InitDB {
    private Grid grid = new Grid(10, 10);
    private ArrayList<Unit> friendlyUnitList = new ArrayList<>();
    private ArrayList<Unit> enemyUnitList = new ArrayList<>();
    private DemoBattler demoBattler = new DemoBattler();
    private UserRepository userRepository;

    public InitDB() {
        try {
            User userLeft = getDataFromFormationPlan(FRIENDLY, "friendlyFormation.json");
            User userRight = getDataFromFormationPlan(ENEMY, "enemyFormation.json");

            userRepository.save(userLeft);
            userRepository.save(userRight);
        } catch (URISyntaxException | FileNotFoundException | UnknownUnitType e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        InitDB initDB = new InitDB();
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
