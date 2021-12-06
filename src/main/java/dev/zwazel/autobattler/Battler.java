package dev.zwazel.autobattler;

import dev.zwazel.autobattler.classes.units.MyFirstUnit;
import dev.zwazel.autobattler.classes.units.Unit;

import java.util.ArrayList;

public class Battler {
    private final int BOARD_SIZE = 10;

    public Battler() {
        int unitCounter = 0;
        ArrayList<Unit> units = new ArrayList<>();
        units.add(new MyFirstUnit(unitCounter++, 1, "unit1"));
        units.add(new MyFirstUnit(unitCounter++, 1, "unit2"));
        units.add(new MyFirstUnit(unitCounter++, 1, "unit3"));

        drawBoard();
    }

    public static void main(String[] args) {
        new Battler();
    }

    private void drawBoard() {
        StringBuilder vertical = new StringBuilder();
        vertical.append("-".repeat(BOARD_SIZE * 4 + 1));

        for (int row = 0; row < BOARD_SIZE; row++) {
            System.out.println();
            System.out.println(vertical);

            for (int column = 0; column < BOARD_SIZE; column++) {
                System.out.print("|" + " " + " " + " ");
            }
            System.out.print("|");
        }
        System.out.println();
        System.out.println(vertical);
    }
}