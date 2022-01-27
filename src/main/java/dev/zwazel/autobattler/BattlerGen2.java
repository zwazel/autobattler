package dev.zwazel.autobattler;

import com.google.gson.*;
import dev.zwazel.autobattler.classes.Utils.*;
import dev.zwazel.autobattler.classes.Utils.json.Export;
import dev.zwazel.autobattler.classes.Utils.json.History;
import dev.zwazel.autobattler.classes.Utils.map.*;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.State;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.units.PlaceboUnit;
import dev.zwazel.autobattler.classes.units.Unit;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

import static dev.zwazel.autobattler.classes.enums.Side.ENEMY;
import static dev.zwazel.autobattler.classes.enums.Side.FRIENDLY;

public class BattlerGen2 {
    private final Grid grid = new Grid(new Vector(10, 10));
    private final ArrayList<Unit> friendlyUnitList;
    private final ArrayList<Unit> enemyUnitList;
    private User friendlyUser;
    private User enemyUser;
    private ArrayList<Unit> units;
    private History history;
    private boolean fightFinished = false;
    private Side winningSide;

    public BattlerGen2() {
        friendlyUnitList = new ArrayList<>();
        enemyUnitList = new ArrayList<>();

//        getDataFromFormationPlan(FRIENDLY, "friendlyFormation.json");
//        getDataFromFormationPlan(ENEMY, "enemyFormation.json");
        try {
            friendlyUser = getDataFromFormationPlan(FRIENDLY, "friendlyFormationBig.json");
            enemyUser = getDataFromFormationPlan(ENEMY, "enemyFormationBig.json");

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

            history = new History(new Formation(friendlyUser, new ArrayList<>(friendlyUnitList)), new Formation(enemyUser, new ArrayList<>(enemyUnitList)), this);

            drawBoard();
            int roundCounter = 0;
            while (!fightFinished) {
                ListIterator<Unit> unitIterator = units.listIterator();
                while (unitIterator.hasNext()) {
                    Unit unit = unitIterator.next();
                    Vector posBefore = unit.getGridPosition();
                    if (unit.getMyState() != State.ALIVE) {
                        if (unit.getSide() == FRIENDLY) {
                            friendlyUnitList.remove(unit);
                        } else if (unit.getSide() == ENEMY) {
                            enemyUnitList.remove(unit);
                        }
                        grid.updateOccupiedGrid(posBefore, null);
                        unitIterator.remove();
                    } else {
                        history.addActionHistory(unit.run());
                        // TODO: 27.01.2022 uncomment once I figure out how to get to the closest around a unit
                        grid.updateOccupiedGrid(posBefore, null);
                        grid.updateOccupiedGrid(unit.getGridPosition(), unit);
                    }
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

            System.out.println("fight done after " + roundCounter + "!");
            System.out.println("winningSide = " + winningSide);
            System.out.println("surviving units: ");
            for (Unit unit : units) {
                System.out.println(unit);
            }

            drawBoard();

            try {
                new Export().export(history);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (URISyntaxException | FileNotFoundException | UnknownUnitType e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BattlerGen2();
    }

    public boolean placeOccupied(Vector toGo) {
        GridCell cell = grid.getGridCells()[toGo.getX()][toGo.getY()];
        return cell.getCurrentObstacle() != null;
    }

    public Unit findClosestOther(Unit unit, Side sideToCheck, boolean checkIfReachable, boolean includeDead) {
        Unit closestUnit = null;
        Double shortestDistance = -1d;
        for (Unit unitChecking : units) {
            if (unitChecking != unit) {
                if (unitChecking.getSide() == sideToCheck) {
                    if (includeDead || unitChecking.getMyState() != State.DEAD) {
                        if (checkIfReachable) {
                            boolean done = false;
                            GridGraph graph = new GridGraph(grid);
                            Node targetNode = graph.getNodes()[unitChecking.getGridPosition().getX()][unitChecking.getGridPosition().getY()];
                            LinkedList<Node> neighbors = targetNode.getMyNeighbors();
                            System.out.println("checking if " + unit.getName() + " ("+unit.getID()+") can reach " + unitChecking.getName() + " ("+unitChecking.getID()+") at pos " + unitChecking.getGridPosition());
                            System.out.println("neighbors.size() = " + neighbors.size());
                            int counter = 1;
                            for(Node node : neighbors) {
                                Vector vector = node.getMyGridCell().getPosition();
                                System.out.println("current neighbour : " + vector);
                                if (isReachable(unit.getGridPosition(), vector)) {
                                    done = true;
                                    System.out.println(unit.getName() + " ("+unit.getID()+") can reach " + unitChecking.getName() + " ("+unitChecking.getID()+") at close pos " + vector);
                                    closestUnit = new PlaceboUnit(vector, grid.getGridSize());
                                    break;
                                } else {
                                    System.out.println("neighbour " + counter + " can't be reached");
                                }
                                counter++;
                            }
                            if(!done) {
                                System.out.println(unit.getName() + " ("+unit.getID()+") can't reach " + unitChecking.getName() + " ("+unitChecking.getID()+")");
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
        return closestUnit;
    }

    public boolean isReachable(Vector start, Vector end) {
        FindPath path = new FindPath();
        return (path.findPath(start, end, new GridGraph(grid)) != null);
    }

    private void drawBoard() {
        ArrayList<Unit> placedUnits = new ArrayList<>();
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
                // TODO: 27.01.2022 once place occupied works, just take the current grid cell and then take the unit from there, if it has one!
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
            // TODO: 27.01.2022 uncomment once we figure out how to get to closest point besides it
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
