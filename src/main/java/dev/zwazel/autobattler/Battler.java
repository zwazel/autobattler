package dev.zwazel.autobattler;

import dev.zwazel.autobattler.classes.Utils.Vector;
import dev.zwazel.autobattler.classes.enums.GamePhase;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.State;
import dev.zwazel.autobattler.classes.units.MyFirstUnit;
import dev.zwazel.autobattler.classes.units.Unit;

import java.util.ArrayList;

public class Battler {
    private final Vector gridSize = new Vector(9, 4);
    private final ArrayList<Unit> units;
    private final ArrayList<Unit> friendlyUnitList = new ArrayList<>();
    private final ArrayList<Unit> enemyUnitList = new ArrayList<>();
    private boolean fightFinished = false;
    private GamePhase gamePhase;
    private Side winningSide;

    public Battler() {
        int unitCounter = 0;
        units = new ArrayList<>();
        units.add(new MyFirstUnit(unitCounter++, 1, 1, "unit1", new Vector(1, 1), gridSize, this, Side.FRIENDLY));
        units.add(new MyFirstUnit(unitCounter++, 2, 1, "unit2", new Vector(2, 2), gridSize, this, Side.FRIENDLY));
        units.add(new MyFirstUnit(unitCounter++, 3, 1, "unit5", new Vector(1, 2), gridSize, this, Side.FRIENDLY));
        units.add(new MyFirstUnit(unitCounter++, 4, 1, "unit6", new Vector(2, 1), gridSize, this, Side.FRIENDLY));
        units.add(new MyFirstUnit(unitCounter++, 1, 1, "unit3", new Vector(5, 2), gridSize, this, Side.ENEMY));
        units.add(new MyFirstUnit(unitCounter++, 2, 1, "unit4", new Vector(9, 1), gridSize, this, Side.ENEMY));
        units.add(new MyFirstUnit(unitCounter++, 3, 1, "unit7", new Vector(9, 2), gridSize, this, Side.ENEMY));
        units.add(new MyFirstUnit(unitCounter++, 4, 1, "unit8", new Vector(6, 2), gridSize, this, Side.ENEMY));

        for (Unit unit : units) {
            if (unit.getSide() == Side.FRIENDLY) {
                friendlyUnitList.add(unit);
            } else {
                enemyUnitList.add(unit);
            }
        }

        drawBoard();
        int counter = 0;
        while (!fightFinished) {
            ArrayList<Unit> unitsToRemove = new ArrayList<>();
            for (Unit unit : units) {
                if (unit.getMyState() == State.DEAD) {
                    unitDied(unit);
                    unitsToRemove.add(unit);
                }
            }
            units.removeAll(unitsToRemove);

            int maxFriendlyCounter = friendlyUnitList.size() - 1;
            int maxEnemyCounter = enemyUnitList.size() - 1;
            int friendlyCounter = 0;
            int enemyCounter = 0;
            boolean cannotSwitchSide = false;
            boolean friendlies = true;
            System.out.println();
            System.out.println();
            System.out.println("ROUND " + counter);
            System.out.println("THINK");
            System.out.println("---------------------------------------------------------------------------");
            gamePhase = GamePhase.THINKING;
            for (int i = 0; i < units.size(); i++) {
                if (!cannotSwitchSide) {
                    boolean cantFriendlies = false;
                    boolean cantEnemies = false;
                    if (friendlyCounter > maxFriendlyCounter) {
                        cantFriendlies = true;
                        friendlies = false;
                    }
                    if (enemyCounter > maxEnemyCounter) {
                        cantEnemies = true;
                        friendlies = true;
                    }

                    if (cantEnemies || cantFriendlies) {
                        cannotSwitchSide = true;
                    }
                }

                // TODO: 27.12.2021 make sure it still works even if there are more friendlies than enemies or other way around
                // TODO: 17.01.2022 respect order and priority
                Unit unit;
                System.out.println("i = " + i);
                System.out.println("friendlyCounter = " + friendlyCounter);
                System.out.println("maxFriendlyCounter = " + maxFriendlyCounter);
                System.out.println("enemyCounter = " + enemyCounter);
                System.out.println("maxEnemyCounter = " + maxEnemyCounter);
                System.out.println("friendlies = " + friendlies);
                if (friendlies) {
                    unit = friendlyUnitList.get(friendlyCounter++);
                } else {
                    unit = enemyUnitList.get(enemyCounter++);
                }

                if (!cannotSwitchSide) {
                    friendlies = !friendlies;
                }

                unit.think();
            }

            System.out.println();
            System.out.println("DO");
            System.out.println("---------------------------------------------------------------------------");
            gamePhase = GamePhase.DOING;
            // TODO: 17.01.2022 respect order and priorities!
            for (Unit unit : units) {
                if (unit.getMyState() != State.DEAD)
                    unit.doWhatYouThoughtOf();
            }

            System.out.println();
            System.out.println("STATS");
            System.out.println("---------------------------------------------------------------------------");
            for (Unit unit : units) {
                System.out.println(unit.getID());
                System.out.println("\tHealth = " + unit.getBaseHealth());
                System.out.println("\tPos = " + unit.getGridPosition());
            }
            drawBoard();
            counter++;
        }

        System.out.println("Game ended after " + counter + " rounds!");
        System.out.println("Winner is: " + winningSide);
    }

    public static void main(String[] args) {
        new Battler();
    }

    public boolean placeOccupied(Vector toGo) {
        for (Unit unit : units) {
            if (unit.getGridPosition().equals(toGo)) {
                return true;
            }
        }
        return false;
    }

    public Unit findClosestOther(Unit unit) {
        Unit closestUnit = null;
        Side sideToCheck = (unit.getSide() == Side.FRIENDLY) ? Side.ENEMY : Side.FRIENDLY;
        Double shortestDistance = -1d;
        for (Unit unitChecking : units) {
            if (unitChecking != unit) {
                if (unitChecking.getSide() == sideToCheck && unitChecking.getMyState() != State.DEAD) {
                    Double temp = unit.getGridPosition().getDistanceFrom(unitChecking.getGridPosition());
                    if (shortestDistance < 0 || temp < shortestDistance) {
                        shortestDistance = temp;
                        closestUnit = unitChecking;
                    }
                }
            }
        }

        if (closestUnit != null) {
            System.out.println("closest unit to unit " + unit.getID() + " = " + closestUnit.getID() + " with distance = " + shortestDistance);
        } else {
            System.out.println("no closest unit to unit " + unit.getID());
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

    public void unitDied(Unit unit) {
        if (unit.getSide() == Side.ENEMY) {
            this.enemyUnitList.remove(unit);
        } else {
            this.friendlyUnitList.remove(unit);
        }

        if (enemyUnitList.size() == 0) {
            winningSide = Side.FRIENDLY;
            fightFinished = true;
        } else if (friendlyUnitList.size() == 0) {
            winningSide = Side.ENEMY;
            fightFinished = true;
        }
    }

    public Vector getGridSize() {
        return gridSize;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public GamePhase getCurrentState() {
        return gamePhase;
    }
}