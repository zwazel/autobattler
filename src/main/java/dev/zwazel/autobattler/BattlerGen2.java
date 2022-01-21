package dev.zwazel.autobattler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.zwazel.autobattler.classes.Utils.UnitTypeParser;
import dev.zwazel.autobattler.classes.Utils.Vector;
import dev.zwazel.autobattler.classes.enums.GamePhase;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.State;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.units.Unit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ListIterator;

import static dev.zwazel.autobattler.classes.enums.Side.ENEMY;
import static dev.zwazel.autobattler.classes.enums.Side.FRIENDLY;

public class BattlerGen2 {
    private final Vector gridSize = new Vector(9, 9);
    private final ArrayList<Unit> friendlyUnitList;
    private final ArrayList<Unit> enemyUnitList;
    private boolean fightFinished = false;
    private GamePhase gamePhase;
    private Side winningSide;

    public BattlerGen2() {
        friendlyUnitList = new ArrayList<>();
        enemyUnitList = new ArrayList<>();

        getDataFromFormationPlan(FRIENDLY, "friendlyFormation.json");
        getDataFromFormationPlan(ENEMY, "enemyFormation.json");

        friendlyUnitList.sort(Comparator.comparingInt(Unit::getPriority));
        enemyUnitList.sort(Comparator.comparingInt(Unit::getPriority));
        ArrayList<Unit> orderedList = new ArrayList<>();

        boolean friendlies = Math.random() < 0.5;
        int firstCounter = 0;
        int secondCounter = 0;
        for (int i = 0; i < friendlyUnitList.size() + enemyUnitList.size(); i++) {
            boolean friendlyDone = firstCounter >= friendlyUnitList.size();
            boolean enemyDone = secondCounter >= enemyUnitList.size();
            System.out.println("friendlies = " + friendlies);
            System.out.println("i = " + i);
            System.out.println("secondCounter = " + secondCounter);
            System.out.println("firstCounter = " + firstCounter);

            if (friendlies) {
                orderedList.add(friendlyUnitList.get(firstCounter++));
            } else {
                orderedList.add(enemyUnitList.get(secondCounter++));
            }

            friendlies = !friendlies;
            if (friendlyDone || enemyDone) {
                friendlies = !friendlies;
                System.out.println("no " + ((friendlyDone) ? "friendlies" : "enemies"));
            }
        }

        for (Unit unit : orderedList) {
            System.out.println(unit);
        }

        while (!fightFinished) {
            ListIterator<Unit> unitIterator = orderedList.listIterator();
            while (unitIterator.hasNext()) {
                Unit unit = unitIterator.next();
                if (unit.getMyState() != State.ALIVE) {
                    if (unit.getSide() == FRIENDLY) {
                        friendlyUnitList.remove(unit);
                    } else if (unit.getSide() == ENEMY) {
                        enemyUnitList.remove(unit);
                    }

                    unitIterator.remove();
                } else {
                    unit.run();
                }
            }

            if (friendlyUnitList.size() <= 0) {
                winningSide = Side.ENEMY;
                fightFinished = true;
            } else if (enemyUnitList.size() <= 0) {
                winningSide = FRIENDLY;
                fightFinished = true;
            }
        }

        System.out.println("fight done!");
        System.out.println("winningSide = " + winningSide);
    }

    public static void main(String[] args) {
        new BattlerGen2();
    }

    private void getDataFromFormationPlan(Side side, String fileName) {
        try {
            File file = getFileFromResource(fileName);
            Reader reader = new FileReader(file);
            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            jsonArray = jsonArray.get(1).getAsJsonObject().getAsJsonObject().get("formation").getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject unit = jsonArray.get(i).getAsJsonObject();
                Unit actualUnit = UnitTypeParser.getUnit(unit, new Battler(false), side);
                switch (side) {
                    case FRIENDLY -> {
                        friendlyUnitList.add(actualUnit);
                    }
                    case ENEMY -> {
                        enemyUnitList.add(actualUnit);
                    }
                }
            }
        } catch (URISyntaxException | FileNotFoundException | UnknownUnitType e) {
            e.printStackTrace();
        }
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
