package dev.zwazel.autobattler;

import dev.zwazel.autobattler.classes.Utils.Vector;
import dev.zwazel.autobattler.classes.enums.GamePhase;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.units.Unit;

import java.util.ArrayList;

public class BattlerGen2 {
    private final Vector gridSize = new Vector(9,9);
    private final ArrayList<Unit> friendlyUnitList;
    private final ArrayList<Unit> enemyUnitList;
    private boolean fightFinished = false;
    private GamePhase gamePhase;
    private Side winningSide;

    public BattlerGen2() {
        friendlyUnitList = new ArrayList<>();
        enemyUnitList = new ArrayList<>();
    }

    private void getDataFromFormationPlan(ArrayList<Unit> units) {

    }

    public static void main(String[] args) {

    }
}
