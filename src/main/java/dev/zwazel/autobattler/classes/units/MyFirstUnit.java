package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.BattlerGen2;
import dev.zwazel.autobattler.classes.Utils.Vector;
import dev.zwazel.autobattler.classes.Utils.json.ActionHistory;
import dev.zwazel.autobattler.classes.Utils.map.FindPath;
import dev.zwazel.autobattler.classes.Utils.map.Node;
import dev.zwazel.autobattler.classes.abilities.Ability;
import dev.zwazel.autobattler.classes.abilities.DefaultPunch;
import dev.zwazel.autobattler.classes.enums.Action;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.State;
import dev.zwazel.autobattler.classes.enums.UnitTypes;

import java.util.Random;

public class MyFirstUnit extends Unit {
    public MyFirstUnit(long id, int priority, int level, String name, Vector position, BattlerGen2 battler, Side side) {
        super(id, level, name, "First Unit", 100, 100, 'u', position, battler.getGrid().getGridSize(), 1, battler, side, priority, UnitTypes.MY_FIRST_UNIT);
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
    public Ability findSuitableAbility() {
        for (Ability ability : getAbilities()) {
            Unit target = findTargetUnit(ability.getTargetSide());
            if (ability.canBeUsed(target)) {
//                System.out.println("suitable ability = " + ability.getTitle());
                return ability;
            }
        }
        return null;
    }

    @Override
    public void moveTowards(Unit target) {
        if (target != null) {
            Node[] nodes = new FindPath().getNextMoveSteps(this.getGridPosition(), target.getGridPosition(), this.getBattler().getGrid(), this.getSpeed());
            if (nodes.length > 0) {
                for (Node node : nodes) {
                    move(node.getMyGridCell().getPosition(), false);
                }
            }
        }
    }

    @Override
    public void move(Vector direction, boolean checkIfOccupied) {
        if (!checkIfOccupied) {
            this.setGridPosition(direction);
        } else {
            if (!this.getBattler().placeOccupied(direction)) {
                this.setGridPosition(direction);
            }
        }
    }

    @Override
    public void moveRandom() {
        Random rand = new Random();
        int n = rand.nextInt(Vector.DIRECTION.values().length);
        Vector direction = Vector.DIRECTION.values()[n].getDirection();
        move(direction, true);
    }

    @Override
    public Unit findTargetUnit(Side side) {
        return getBattler().findClosestOther(this, side, true, false);
    }

    @Override
    public void die() {
        System.out.println("unit " + this.getName() + "(" + this.getID() + ")" + " died! (" + this.getSide() + ")" + " last hitter: " + this.getLastHitter().getName() + "(" + this.getLastHitter().getID() + ")");
        setMyState(State.DEAD);
    }

    @Override
    public ActionHistory run() {
        Action todoAction = null;
        Unit target = null;
        Ability suitableAbility = null;
        Vector targetPosition = null;
        if (this.getHealth() <= 0) {
            die();
            todoAction = Action.DIE;
        } else {

            for (Ability ability : getAbilities()) {
                ability.doRound();
            }

            suitableAbility = findSuitableAbility();

            todoAction = (suitableAbility == null) ? Action.CHASE : Action.USE_ABILITY;
            switch (todoAction) {
                case CHASE -> {
                    target = findTargetUnit(this.getSide().getOpposite());
                    moveTowards(target);
                    targetPosition = this.getGridPosition();
                }
                case USE_ABILITY -> {
                    target = findTargetUnit(suitableAbility.getTargetSide());
                    suitableAbility.use(target);
                }
                case RETREAT -> {

                }
            }
        }
        return new ActionHistory(todoAction, this, target, suitableAbility, targetPosition);
    }
}
