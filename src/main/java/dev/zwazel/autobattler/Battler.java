package dev.zwazel.autobattler;

import dev.zwazel.autobattler.classes.Grid;
import dev.zwazel.autobattler.classes.units.MyFirstUnit;
import dev.zwazel.autobattler.classes.units.Unit;

import java.util.ArrayList;

public class Battler {
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 5;
    private ArrayList<Unit> units;

    public Battler() {
        int unitCounter = 0;
        units = new ArrayList<>();
        units.add(new MyFirstUnit(unitCounter++, 1, "unit1", new Grid(0, 0)));
        units.add(new MyFirstUnit(unitCounter++, 1, "unit2", new Grid(1, 1)));
        units.add(new MyFirstUnit(unitCounter++, 1, "unit3", new Grid(9, 4)));

        drawBoard();
    }

    public static void main(String[] args) {
        new Battler();
    }

    private void drawBoard() {
        ArrayList<Unit> placedUnits = new ArrayList<>();
        StringBuilder vertical = new StringBuilder();
        vertical.append("-".repeat(BOARD_WIDTH * 4 + 1));

        int counter = 0;
        Grid gridPositionNow = new Grid(0, 0);

        for (int row = 0; row < BOARD_HEIGHT; row++) {
            System.out.println();
            System.out.println(vertical);

            for (int column = 0; column < BOARD_WIDTH; column++) {
                char character = ' ';
                gridPositionNow.setX(column);
                gridPositionNow.setY(row);
                for (Unit unit : units) {
                    if (!placedUnits.contains(unit) && unit.getGridPosition().equals(gridPositionNow)) {
                        placedUnits.add(unit);
                        character = unit.getSymbol();
                    }
                }

                System.out.print("|" + " " + character + " ");
            }
            System.out.print("|");
        }
        System.out.println();
        System.out.println(vertical);
        System.out.println("gridPositionNow = " + gridPositionNow);
    }
}