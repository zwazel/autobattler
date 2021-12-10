package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.Battler;
import dev.zwazel.autobattler.classes.Utils.Vector;
import dev.zwazel.autobattler.classes.abilities.Ability;
import dev.zwazel.autobattler.classes.abilities.DefaultPunch;
import dev.zwazel.autobattler.classes.enums.Action;
import dev.zwazel.autobattler.classes.enums.CurrentState;
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
            Unit target = findTargetUnit();
            if (ability.canBeUsed(target)) {
                System.out.println("suitable ability = " + ability.getTitle());
                return ability;
            }
        }
        return null;
    }

    @Override
    public void moveTowards(Unit target) {
        Vector dir = this.getGridPosition().directionTo(target.getGridPosition());
        System.out.println("direction " + this.getID() + " to " + target.getID() + " = " + dir);
        move(dir);
    }

    @Override
    public void move(Vector direction) {
        System.out.println("unit " + this.getID() + " is moving, direction = " + direction + ":");
        Vector temp = new Vector(this.getGridPosition());
        for (int i = 0; i < this.getBaseSpeed(); i++) {
            temp.add(direction);
            System.out.println("\ttemp = " + temp);
            boolean placeOccupied = this.getBattler().placeOccupied(temp);
            System.out.println("\tplaceOccupied = " + placeOccupied);
            boolean canChangeDir = direction.getX() != 0 && direction.getY() != 0;
            System.out.println("\tcanChangeDir = " + canChangeDir);
            int counter = 0;
            int tempDir = 0;
            while (canChangeDir && placeOccupied) {
                if (counter == 0) {
                    tempDir = temp.getY();
                    temp.setY(this.getGridPosition().getY());
                } else {
                    temp.setY(tempDir);
                    temp.setX(this.getGridPosition().getX());
                    canChangeDir = false;
                }
                System.out.println("\ttemp after checking other direction = " + temp);
                placeOccupied = this.getBattler().placeOccupied(temp);
                counter++;
            }
            if (!placeOccupied && !temp.greaterThan(this.getGridSize()) && !temp.smallerThan(new Vector(0, 0))) {
                System.out.println("\tpos not occupied, moving to " + temp);
                this.setGridPosition(temp);
            } else {
                System.out.println("\tunit couldnt move!");
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
        System.out.println("unit " + this.getID() + " todo = " + this.getTodoAction());
    }

    @Override
    public void doWhatYouThoughtOf() {
        for (Ability ability : getAbilities()) {
            ability.doRound();
        }

        if (this.getTodoAction() != null) {
            switch (this.getTodoAction()) {
                case CHASE -> {
                    // TODO: 10.12.2021 THE CHECKING IF THE PLACE IS OCCUPIED MUST BE DONE WHILE THINKING AND NOT WHILE DOING!!
                    moveTowards(findTargetUnit());
                }
                case USE_ABILITY -> {
                    getNextAbility().use(findTargetUnit());
                }
                case RETREAT -> {

                }
            }
        }
    }

    @Override
    public Unit findTargetUnit() {
        if (this.getBattler().getCurrentState() == CurrentState.THINKING || this.getTargetUnit() == null) {
            setTargetUnit(getBattler().findClosestOther(this));
        }
        return getTargetUnit();
    }
}
