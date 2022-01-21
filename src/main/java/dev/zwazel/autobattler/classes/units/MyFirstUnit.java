package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.BattlerGen2;
import dev.zwazel.autobattler.classes.Utils.Vector;
import dev.zwazel.autobattler.classes.abilities.Ability;
import dev.zwazel.autobattler.classes.abilities.DefaultPunch;
import dev.zwazel.autobattler.classes.enums.Action;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.State;

import java.util.Random;

import static dev.zwazel.autobattler.classes.enums.Side.ENEMY;

public class MyFirstUnit extends Unit {
    public MyFirstUnit(long id, int priority, int level, String name, Vector position, BattlerGen2 battler, Side side) {
        super(id, level, 10, name, "First Unit", 100, 100, 'u', position, battler.getGridSize(), 1, battler, side, priority);
        this.setAbilities(new Ability[]{new DefaultPunch(this)});
    }

    @Override
    protected int getLevelHealth(int health, int level) {
        return (int) (health + (health * (level * 0.25)));
    }

    @Override
    protected int getLevelEnergy(int energy, int level) {
        return (int) (energy + (energy * (level * 0.25)));
    }

    @Override
    protected int getLevelDamage(int damage, int level) {
        return (int) (damage + (damage * (level * 0.25)));
    }

    @Override
    public Ability findSuitableAbility() {
        for (Ability ability : getAbilities()) {
            Unit target = findTargetUnit(ability.getTargetSide());
            if (ability.canBeUsed(target)) {
                System.out.println("suitable ability = " + ability.getTitle());
                return ability;
            }
        }
        return null;
    }

    @Override
    public void moveTowards(Unit target) {
        if (target != null) {
            Vector dir = this.getGridPosition().directionTo(target.getGridPosition());
            System.out.println("direction " + this.getID() + " to " + target.getID() + " = " + dir);
            move(dir);
        }
    }

    @Override
    public void move(Vector direction) {
        System.out.println("unit " + this.getID() + " is moving, direction = " + direction + ":");
        Vector temp = new Vector(this.getGridPosition());
        for (int i = 0; i < this.getSpeed(); i++) {
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
    public Unit findTargetUnit(Side side) {
        return getBattler().findClosestOther(this, side, false);
    }

    @Override
    public void die() {
        System.out.println("unit " + this.getName() + " died! (" + this.getSide() + ")");
        setMyState(State.DEAD);
    }

    @Override
    public void run() {
        for (Ability ability : getAbilities()) {
            ability.doRound();
        }

        Ability suitableAbility = findSuitableAbility();

        Action todoAction = (suitableAbility == null) ? Action.CHASE : Action.USE_ABILITY;

        switch (todoAction) {
            case CHASE -> {
                moveTowards(findTargetUnit(ENEMY));
            }
            case USE_ABILITY -> {
                suitableAbility.use(findTargetUnit(suitableAbility.getTargetSide()));
            }
            case RETREAT -> {

            }
        }
    }
}
