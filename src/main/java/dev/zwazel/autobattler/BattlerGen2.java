package dev.zwazel.autobattler;

import com.google.gson.*;
import dev.zwazel.autobattler.classes.Obstacle;
import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.enums.Action;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.State;
import dev.zwazel.autobattler.classes.enums.UnitTypes;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.model.FormationEntity;
import dev.zwazel.autobattler.classes.model.User;
import dev.zwazel.autobattler.classes.units.MyFirstUnit;
import dev.zwazel.autobattler.classes.utils.Formation;
import dev.zwazel.autobattler.classes.utils.GetFile;
import dev.zwazel.autobattler.classes.utils.UnitTypeParser;
import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.utils.battle.CreateFormations;
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
    private ArrayList<Unit> units;
    private History history;
    private boolean fightFinished = false;
    private Side winningSide;

    public BattlerGen2(Vector gridSize) {
        grid = new Grid(gridSize);
        friendlyUnitList = new ArrayList<>();
        enemyUnitList = new ArrayList<>();
    }

    public BattlerGen2(FormationEntity formationLeft, FormationEntity formationRight, boolean createJson, boolean runWithGUI, Vector gridSize, boolean mirrorEnemy, boolean drawBoard) {
        friendlyUnitList = new ArrayList<>();
        enemyUnitList = new ArrayList<>();
        grid = new Grid(gridSize);

        Formation _formationLeft = new Formation(formationLeft.getUser(), formationLeft.getFormationUnits());
        Formation _formationRight = new Formation(formationRight.getUser(), getUnits());

        try {
            User friendlyUser = formationLeft.getUser();
            User enemyUser = formationRight.getUser();

            getFormationFromJson(FRIENDLY, formationLeft.getFormationJson(), mirrorEnemy);
            getFormationFromJson(ENEMY, formationRight.getFormationJson(), mirrorEnemy);

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

            System.out.println(units);

            history = new History(new Formation(friendlyUser, new ArrayList<>(friendlyUnitList)), new Formation(enemyUser, new ArrayList<>(enemyUnitList)), this);

            if (runWithGUI) {
                GUI gui = new GUI(this, 50);
            } else {
                if (drawBoard) {
                    drawBoard();
                }
                while (!fightFinished) {
                    ListIterator<Unit> unitIterator = units.listIterator();
                    while (unitIterator.hasNext()) {
                        doTurn(unitIterator, true);
                    }
                    if (drawBoard) {
                        drawBoard();
                    }
                }

                if (createJson) {
                    try {
                        new Export().export(history);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Vector gridSize = new Vector(10, 10);

        CreateFormations createFormations = new CreateFormations(gridSize, false);

        int amountUnitsLeft = 3;
        int amountUnitsRight = amountUnitsLeft;
        Formation left = createFormations.createTestFormation(amountUnitsLeft, FRIENDLY, 0, true, new UnitTypes[]{
                UnitTypes.MY_FIRST_UNIT,
        }, 1, 10);
        Formation right = createFormations.createTestFormation(amountUnitsRight, ENEMY, amountUnitsLeft, true, new UnitTypes[]{
                UnitTypes.MY_FIRST_UNIT,
        }, 1, 10);
        User userLeft = left.getUser();
        User userRight = right.getUser();

        new BattlerGen2(new FormationEntity(left, userLeft), new FormationEntity(right, userRight), false, true, gridSize, false, true);
    }

    private void mirrorSide(Unit unit) {
        unit.setGridPosition(new Vector(grid.getGridSize().getX() - 1 - unit.getGridPosition().getX(), unit.getGridPosition().getY()));
    }

    // TODO: 28.01.2022 use a pathfinding like algorithm that goes from current node of unit and checks all the neighbours if there is someone, the first one found is considered the closest one
    public Unit findClosestOther(Unit unit, Side sideToCheck, boolean checkIfReachable, boolean includeDead) {
        Unit closestUnit = null;
        Double shortestDistance = -1d;

        ArrayList<Unit> unitsToCheck = (sideToCheck == FRIENDLY) ? friendlyUnitList : enemyUnitList;

        for (Unit unitChecking : unitsToCheck) {
            if (unitChecking != unit) {
                if (includeDead || unitChecking.getMyState() != State.DEAD) {
                    if (checkIfReachable) {
                        FindPath path = new FindPath();
                        if (!path.isReachable(unit.getGridPosition(), unitChecking.getGridPosition(), grid)) {
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

        return closestUnit;
    }

    public void doTurn(Iterator<Unit> unitIterator, boolean createHistory) {
        if (unitIterator.hasNext()) {
            Unit unit = unitIterator.next();
            doTurn(unitIterator, unit, createHistory);
        } else {
            throw new RuntimeException("No units left");
        }
    }

    public ActionHistory doTurn(Iterator<Unit> unitIterator, Unit unit, boolean createHistory) {
        ActionHistory actionHistory;
        Vector posBefore = unit.getGridPosition();
        // TODO: 27.01.2022 update the way units die, think about how it should work!
        if (unit.getMyState() != State.ALIVE) {
            if (unit.getSide() == FRIENDLY) {
                friendlyUnitList.remove(unit);
            } else if (unit.getSide() == ENEMY) {
                enemyUnitList.remove(unit);
            }
            grid.updateOccupiedGrid(posBefore, null);
            actionHistory = new ActionHistory(Action.DIE, unit, new Unit[0], null, new Vector[]{unit.getGridPosition()});
            if (createHistory) {
                history.addActionHistory(actionHistory);
            }
            unitIterator.remove();
        } else {
            actionHistory = unit.process();
            if (createHistory) {
                history.addActionHistory(actionHistory);
            }
            grid.updateOccupiedGrid(posBefore, null);
            grid.updateOccupiedGrid(unit.getGridPosition(), unit);
        }

        if (friendlyUnitList.size() <= 0) {
            winningSide = Side.ENEMY;
            fightFinished = true;
        } else if (enemyUnitList.size() <= 0) {
            winningSide = FRIENDLY;
            fightFinished = true;
        }

        return actionHistory;
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
                        character = "" + ((MyFirstUnit) obstacle).getSymbol();
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

    private void getFormationFromJson(Side side, String json, boolean mirrorEnemy) throws UnknownUnitType {
        JsonElement jsonElement = JsonParser.parseString(json);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        getUnitsFromJsonAndAssignThemToSide(side, jsonArray, mirrorEnemy);
    }

    private void getUnitsFromJsonAndAssignThemToSide(Side side, JsonArray jsonArray, boolean mirrorEnemy) throws UnknownUnitType {
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject unit = jsonArray.get(i).getAsJsonObject();
            Unit actualUnit = UnitTypeParser.getUnit(unit, this, side);
            if (side == ENEMY && mirrorEnemy) {
                mirrorSide(actualUnit);
            }

            grid.updateOccupiedGrid(actualUnit.getGridPosition(), actualUnit);
            switch (side) {
                case FRIENDLY -> friendlyUnitList.add(actualUnit);
                case ENEMY -> enemyUnitList.add(actualUnit);
            }
        }
    }

    private User getFormationPlanFromFile(Side side, String fileName, boolean mirrorEnemy) throws URISyntaxException, FileNotFoundException, UnknownUnitType {
        GetFile getFile = new GetFile();
        File file = getFile.getFileFromResource(fileName);
        Reader reader = new FileReader(file);
        JsonElement jsonElement = JsonParser.parseReader(reader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("formation").getAsJsonArray();
        getUnitsFromJsonAndAssignThemToSide(side, jsonArray, mirrorEnemy);

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
