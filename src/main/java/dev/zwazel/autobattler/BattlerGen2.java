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
    private final ArrayList<Unit> units;
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
        units = new ArrayList<>();

        boolean friendlies = Math.random() < 0.5;
        int firstCounter = 0;
        int secondCounter = 0;
        for (int i = 0; i < friendlyUnitList.size() + enemyUnitList.size(); i++) {
            boolean friendlyDone = firstCounter >= friendlyUnitList.size();
            boolean enemyDone = secondCounter >= enemyUnitList.size();

            if (friendlies) {
                units.add(friendlyUnitList.get(firstCounter++));
            } else {
                units.add(enemyUnitList.get(secondCounter++));
            }

            friendlies = !friendlies;
            if (friendlyDone || enemyDone) {
                friendlies = !friendlies;
            }
        }

        for (Unit unit : units) {
            System.out.println(unit);
        }

        drawBoard();
        while (!fightFinished) {
            ListIterator<Unit> unitIterator = units.listIterator();
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

        drawBoard();
    }

    public static void main(String[] args) {
        new BattlerGen2();
    }

    public boolean placeOccupied(Vector toGo) {
        for (Unit unit : units) {
            if (unit.getGridPosition().equals(toGo)) {
                return true;
            }
        }
        return false;
    }

    public Unit findClosestOther(Unit unit, Side sideToCheck, boolean includeDead) {
        Unit closestUnit = null;
        Double shortestDistance = -1d;
        for (Unit unitChecking : units) {
            if (unitChecking != unit) {
                if (unitChecking.getSide() == sideToCheck) {
                    if (includeDead || unitChecking.getMyState() != State.DEAD) {
                        Double temp = unit.getGridPosition().getDistanceFrom(unitChecking.getGridPosition());
                        if (shortestDistance < 0 || temp < shortestDistance) {
                            shortestDistance = temp;
                            closestUnit = unitChecking;
                        }
                    }
                }
            }
        }

        if (closestUnit != null) {
//            System.out.println("closest unit to unit " + unit.getID() + " = " + closestUnit.getID() + " with distance = " + shortestDistance);
        } else {
//            System.out.println("no closest unit to unit " + unit.getID());
        }
        return closestUnit;
    }

    private void drawBoard() {
        ArrayList<Unit> placedUnits = new ArrayList<>();
        StringBuilder vertical = new StringBuilder();
        vertical.append("-".repeat((gridSize.getX() + 1) * 4 + 1));

        Vector gridPositionNow = new Vector(0, 0);

        for (int row = 0; row <= gridSize.getY(); row++) {
            System.out.println();
            System.out.println(vertical);

            for (int column = 0; column <= gridSize.getX(); column++) {
                String character = " ";
                gridPositionNow.setX(column);
                gridPositionNow.setY(row);
                for (Unit unit : units) {
                    if (!placedUnits.contains(unit) && unit.getGridPosition().equals(gridPositionNow)) {
                        placedUnits.add(unit);
                        character = "" + unit.getID();
                    }
                }

                System.out.print("|" + " " + character + " ");
            }
            System.out.print("|");
        }
        System.out.println();
        System.out.println(vertical);
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
                Unit actualUnit = UnitTypeParser.getUnit(unit, this, side);
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

    public Vector getGridSize() {
        return gridSize;
    }

    public ArrayList<Unit> getFriendlyUnitList() {
        return friendlyUnitList;
    }

    public ArrayList<Unit> getEnemyUnitList() {
        return enemyUnitList;
    }

    public boolean isFightFinished() {
        return fightFinished;
    }

    public void setFightFinished(boolean fightFinished) {
        this.fightFinished = fightFinished;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    public Side getWinningSide() {
        return winningSide;
    }

    public void setWinningSide(Side winningSide) {
        this.winningSide = winningSide;
    }
}
