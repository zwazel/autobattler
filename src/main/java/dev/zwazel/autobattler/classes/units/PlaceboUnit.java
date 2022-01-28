package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.classes.Utils.Vector;
import dev.zwazel.autobattler.classes.Utils.json.ActionHistory;
import dev.zwazel.autobattler.classes.abilities.Ability;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.UnitTypes;

/**
 * this unit doesnt really exist, its used to find a path.... yes
 */
public class PlaceboUnit extends Unit {
    public PlaceboUnit(Vector position, Vector gridSize) {
        super(-1, -1, "I SHOULDNT BE HERE I AM PLACEBO!!", "", -1, -1, '-', position, gridSize, -1, null, Side.ENEMY, -1, UnitTypes.NOT_EXISTENT);
    }

    @Override
    public ActionHistory run() {
        return null;
    }

    @Override
    protected int getLevelHealth(int health, int level) {
        return 0;
    }

    @Override
    protected int getLevelEnergy(int energy, int level) {
        return 0;
    }

    @Override
    protected Ability findSuitableAbility() {
        return null;
    }

    @Override
    protected Vector[] moveTowards(Unit target) {
        return new Vector[0];
    }

    @Override
    protected boolean move(Vector direction, boolean checkIfOccupied) {
        return false;
    }

    @Override
    protected void moveRandom() {

    }

    @Override
    protected Unit findTargetUnit(Side side) {
        return null;
    }

    @Override
    public void takeDamage(Ability ability) {

    }
}
