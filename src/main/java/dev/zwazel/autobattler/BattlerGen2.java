package dev.zwazel.autobattler;

import com.google.gson.*;
import dev.zwazel.autobattler.classes.Obstacle;
import dev.zwazel.autobattler.classes.enums.Action;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.State;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.units.MyFirstUnit;
import dev.zwazel.autobattler.classes.units.Unit;
import dev.zwazel.autobattler.classes.utils.*;
import dev.zwazel.autobattler.classes.utils.json.ActionHistory;
import dev.zwazel.autobattler.classes.utils.json.Export;
import dev.zwazel.autobattler.classes.utils.json.History;
import dev.zwazel.autobattler.classes.utils.map.FindPath;
import dev.zwazel.autobattler.classes.utils.map.Grid;
import dev.zwazel.autobattler.classes.utils.map.GridCell;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;

import static dev.zwazel.autobattler.classes.enums.Side.ENEMY;
import static dev.zwazel.autobattler.classes.enums.Side.FRIENDLY;

public class BattlerGen2 {
    private final Grid grid;
    private final ArrayList<Unit> friendlyUnitList;
    private final ArrayList<Unit> enemyUnitList;
    private User friendlyUser;
    private User enemyUser;
    private ArrayList<Unit> units;
    private History history;
    private boolean fightFinished = false;
    private Side winningSide;

    public BattlerGen2() {
        grid = new Grid(new Vector(10, 10));
        friendlyUnitList = new ArrayList<>();
        enemyUnitList = new ArrayList<>();
    }

    public BattlerGen2(boolean createJson, boolean runWithGUI, Vector gridSize) {
        friendlyUnitList = new ArrayList<>();
        enemyUnitList = new ArrayList<>();
        grid = new Grid(gridSize);

        try {
//            getDataFromFormationPlan(FRIENDLY, "friendlyFormation.json");
//            getDataFromFormationPlan(ENEMY, "enemyFormation.json");
            friendlyUser = getDataFromFormationPlan(FRIENDLY, "friendlyFormationBig.json");
            enemyUser = getDataFromFormationPlan(ENEMY, "enemyFormationBig.json");

            friendlyUnitList.sort(Comparator.comparingInt(Unit::getPriority));
            enemyUnitList.sort(Comparator.comparingInt(Unit::getPriority));
            units = new ArrayList<>();

            boolean friendlies = Math.random() < 0.5;
//            boolean friendlies = false;
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

            history = new History(new Formation(friendlyUser, new ArrayList<>(friendlyUnitList)), new Formation(enemyUser, new ArrayList<>(enemyUnitList)), this);
            int roundCounter = 0;

            if (runWithGUI) {
                GUI gui = new GUI(this, 50);
            } else {
                drawBoard();
                while (!fightFinished) {
                    ListIterator<Unit> unitIterator = units.listIterator();
                    while (unitIterator.hasNext()) {
                        doTurn(unitIterator, true);
                    }

                    if (friendlyUnitList.size() <= 0) {
                        winningSide = Side.ENEMY;
                        fightFinished = true;
                    } else if (enemyUnitList.size() <= 0) {
                        winningSide = FRIENDLY;
                        fightFinished = true;
                    }
                    drawBoard();
                    roundCounter++;
                }

                System.out.println("fight done after " + (roundCounter) + " turns!");
                System.out.println("winningSide = " + winningSide);

                if (createJson) {
                    try {
                        new Export().export(history);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (URISyntaxException | FileNotFoundException | UnknownUnitType e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BattlerGen2(false, true, new Vector(10, 10));
    }

    // TODO: 28.01.2022 use a pathfinding like algorithm that goes from current node of unit and checks all the neighbours if there is someone, the first one found is considered the closest one
    public Unit findClosestOther(Unit unit, Side sideToCheck, boolean checkIfReachable, boolean includeDead) {
        Unit closestUnit = null;
        Double shortestDistance = -1d;
        System.out.println(unit.getName() + "(" + unit.getID() + ")" + " searches closest other");
        for (Unit unitChecking : units) {
            if (unitChecking != unit) {
                if (unitChecking.getSide() == sideToCheck) {
                    if (includeDead || unitChecking.getMyState() != State.DEAD) {
                        if (checkIfReachable) {
                            FindPath path = new FindPath();
                            Vector vector = path.findClosestNearbyNode(grid, unit.getGridPosition(), unitChecking.getGridPosition());
                            if (vector == null) {
                                continue;
                            }
                        }
                        Double temp = unit.getGridPosition().getDistanceFrom(unitChecking.getGridPosition());
                        if (shortestDistance < 0 || temp < shortestDistance) {
                            shortestDistance = temp;
                            closestUnit = unitChecking;
                        }
                    }
                }
            }
        }
        System.out.println("closestUnit = " + closestUnit);
        return closestUnit;
    }

    public void doTurn(Iterator<Unit> unitIterator, boolean createHistory) {
        Unit unit = unitIterator.next();
        doTurn(unitIterator, unit, createHistory);
    }

    public ActionHistory doTurn(Iterator<Unit> unitIterator, Unit unit, boolean createHistory) {
        Vector posBefore = unit.getGridPosition();
        // TODO: 27.01.2022 update the way units die, think about how it should work!
        if (unit.getMyState() != State.ALIVE) {
            if (unit.getSide() == FRIENDLY) {
                friendlyUnitList.remove(unit);
            } else if (unit.getSide() == ENEMY) {
                enemyUnitList.remove(unit);
            }
            grid.updateOccupiedGrid(posBefore, null);
            if (createHistory) {
                history.addActionHistory(new ActionHistory(Action.DIE, unit, new Unit[0], null, new Vector[]{unit.getGridPosition()}));
            }
            unitIterator.remove();
        } else {
            ActionHistory actionHistory = unit.run();
            if (createHistory) {
                history.addActionHistory(actionHistory);
            }
            grid.updateOccupiedGrid(posBefore, null);
            grid.updateOccupiedGrid(unit.getGridPosition(), unit);
            return actionHistory;
        }
        return null;
    }

    private void drawBoard() {
        StringBuilder vertical = new StringBuilder();
        vertical.append("-".repeat((grid.getWidth()) * 4 + 1));

        Vector gridPositionNow = new Vector(0, 0);

        for (int row = 0; row < grid.getHeight(); row++) {
            System.out.println();
            System.out.println(vertical);

            for (int column = 0; column < grid.getWidth(); column++) {
                String character = " ";
                gridPositionNow.setX(column);
                gridPositionNow.setY(row);

                GridCell cell = grid.getGridCells()[gridPositionNow.getX()][gridPositionNow.getY()];
                Obstacle obstacle = cell.getCurrentObstacle();
                if (obstacle != null) {
                    if (obstacle.getClass() == MyFirstUnit.class) {
                        character = "" + ((MyFirstUnit) obstacle).getID();
                    } else {
                        character = "/";
                    }
                }

                System.out.print("|" + " " + character + " ");
            }
            System.out.print("|");
        }
        System.out.println();
        System.out.println(vertical);
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
            Unit actualUnit = UnitTypeParser.getUnit(unit, this, side);
            grid.updateOccupiedGrid(actualUnit.getGridPosition(), actualUnit);
            switch (side) {
                case FRIENDLY -> friendlyUnitList.add(actualUnit);
                case ENEMY -> enemyUnitList.add(actualUnit);
            }
        }

        return new Gson().fromJson(jsonObject.get("user").getAsJsonObject(), User.class);
    }

    public Grid getGrid() {
        return grid;
    }

    public ArrayList<Unit> getUnits() {
        return units;
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

    public Side getWinningSide() {
        return winningSide;
    }

    public void setWinningSide(Side winningSide) {
        this.winningSide = winningSide;
    }

    public History getHistory() {
        return history;
    }
}
