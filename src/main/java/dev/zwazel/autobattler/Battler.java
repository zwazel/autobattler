package dev.zwazel.autobattler;

import dev.zwazel.autobattler.classes.Utils.Vector;
import dev.zwazel.autobattler.classes.enums.CurrentState;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.units.MyFirstUnit;
import dev.zwazel.autobattler.classes.units.Unit;

import java.util.ArrayList;

public class Battler {
    private final Vector gridSize = new Vector(9, 4);
    private final ArrayList<Unit> units;
    private final ArrayList<Unit> friendlyUnitList = new ArrayList<>();
    private final ArrayList<Unit> enemyUnitList = new ArrayList<>();
    private boolean fightFinished = false;
    private CurrentState currentState;
    private Side winningSide;

    public Battler() {
        int unitCounter = 0;
        units = new ArrayList<>();
        units.add(new MyFirstUnit(unitCounter++, 1, 1, "unit1", new Vector(1, 1), gridSize, this, Side.FRIENDLY));
        units.add(new MyFirstUnit(unitCounter++, 2, 1, "unit2", new Vector(2, 2), gridSize, this, Side.FRIENDLY));
        units.add(new MyFirstUnit(unitCounter++, 1, 1, "unit3", new Vector(5, 2), gridSize, this, Side.ENEMY));
        units.add(new MyFirstUnit(unitCounter++, 2, 1, "unit4", new Vector(9, 1), gridSize, this, Side.ENEMY));

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
            currentState = CurrentState.THINKING;
            for (int i = 0; i < units.size(); i++) {
                // TODO: 27.12.2021 make sure it still works even if there are more friendlies than enemies or other way around
                Unit unit;
                System.out.println("i = " + i);
                System.out.println("friendlyCounter = " + friendlyCounter);
                System.out.println("enemyCounter = " + enemyCounter);
                System.out.println("friendlies = " + friendlies);
                if (friendlies) {
                    unit = friendlyUnitList.get(friendlyCounter++);
                } else {
                    unit = enemyUnitList.get(enemyCounter++);
                }

                if (!cannotSwitchSide) {
                    friendlies = !friendlies;
                }

                if (!cannotSwitchSide) {
                    cannotSwitchSide = friendlyCounter > maxFriendlyCounter || enemyCounter > maxEnemyCounter;
                }

                unit.think();
            }

            System.out.println();
            System.out.println("DO");
            System.out.println("---------------------------------------------------------------------------");
            currentState = CurrentState.DOING;
            for (Unit unit : units) {
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
                if (unitChecking.getSide() == sideToCheck) {
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

        units.remove(unit);

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

    public CurrentState getCurrentState() {
        return currentState;
    }
}