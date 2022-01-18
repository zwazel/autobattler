package dev.zwazel.autobattler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.zwazel.autobattler.classes.Utils.UnitTypeParser;
import dev.zwazel.autobattler.classes.Utils.Vector;
import dev.zwazel.autobattler.classes.enums.GamePhase;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.units.Unit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class BattlerGen2 {
    private final Vector gridSize = new Vector(9, 9);
    private final ArrayList<Unit> friendlyUnitList;
    private final ArrayList<Unit> enemyUnitList;
    private final boolean fightFinished = false;
    private GamePhase gamePhase;
    private Side winningSide;

    public BattlerGen2() {
        friendlyUnitList = new ArrayList<>();
        enemyUnitList = new ArrayList<>();

        try {
            File file = getFileFromResource("friendlyFormation.json");
            Reader reader = new FileReader(file);
            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            jsonArray = jsonArray.get(1).getAsJsonObject().getAsJsonObject().get("formation").getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject unit = jsonArray.get(i).getAsJsonObject();
                Unit actualUnit = UnitTypeParser.getUnit(unit, new Battler(false), Side.FRIENDLY);
                friendlyUnitList.add(actualUnit);
            }
        } catch (URISyntaxException | FileNotFoundException | UnknownUnitType e) {
            e.printStackTrace();
        }

        for(Unit unit : friendlyUnitList) {
            System.out.println("unit = " + unit);
        }
    }

    public static void main(String[] args) {
        new BattlerGen2();
    }

    private void getDataFromFormationPlan(JsonObject jsonObject, ArrayList<Unit> units) {

    }

    /*
        The resource URL is not working in the JAR
        If we try to access a file that is inside a JAR,
        It throws NoSuchFileException (linux), InvalidPathException (Windows)

        Resource URL Sample: file:java-io.jar!/json/file1.json
     */
    private File getFileFromResource(String fileName) throws URISyntaxException {

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {

            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());

            return new File(resource.toURI());
        }

    }
}
