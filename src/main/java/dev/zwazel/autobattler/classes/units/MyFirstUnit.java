package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.Battler;
import dev.zwazel.autobattler.classes.Utils.Vector;
import dev.zwazel.autobattler.classes.abilities.Ability;
import dev.zwazel.autobattler.classes.abilities.DefaultPunch;
import dev.zwazel.autobattler.classes.enums.Action;
import dev.zwazel.autobattler.classes.enums.Side;

import java.util.Random;

public class MyFirstUnit extends Unit {
    public MyFirstUnit(long id, int level, String name, Vector position, Vector gridSize, Battler battler, Side side) {
        super(id, level, 10, name, "First Unit", 100, 100, 'u', position, gridSize, 1, battler, side);
        this.setAbilities(new Ability[]{new DefaultPunch(this)});
    }

    @Override
    public Ability findSuitableAbility() {
        for (Ability ability : getAbilities()) {
            if (ability.canBeUsed() && ability.isInRange(this, getBattler().findClosestOther(this))) {
                return ability;
            }
        }
        return null;
    }

    @Override
    public void move(Vector direction) {
        Vector temp = new Vector(this.getGridPosition());
        for (int i = 0; i < this.getBaseSpeed(); i++) {
            temp.add(direction);
            if (!this.getBattler().placeOccupied(temp) && !temp.greaterThan(this.getGridSize()) && !temp.smallerThan(new Vector(0, 0))) {
                this.setGridPosition(temp);
            } else {
                break;
            }
        }
    }

    @Override
    public void moveRandom() {
        Random rand = new Random();
        int n = rand.nextInt(Vector.DIRECTION.values().length);
        Vector direction = Vector.DIRECTION.values()[n].getDirection();
        move(direction);
    }

    @Override
    public void think() {
        setNextAbility(findSuitableAbility());
        if (getNextAbility() != null) {
            this.setTodoAction(Action.USE_ABILITY);
        } else {
            this.setTodoAction(Action.CHASE);
        }
    }

    @Override
    public void doWhatYouThoughtOf() {
        if (this.getTodoAction() != null) {
            switch (this.getTodoAction()) {
                case CHASE -> {

                }
                case USE_ABILITY -> {
                    getNextAbility().use(getBattler().findClosestOther(this));
                }
                case RETREAT -> {

                }
            }
        }
    }
}
