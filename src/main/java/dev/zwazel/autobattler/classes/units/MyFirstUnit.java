package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.BattlerGen2;
import dev.zwazel.autobattler.classes.abilities.Ability;
import dev.zwazel.autobattler.classes.abilities.DefaultPunch;
import dev.zwazel.autobattler.classes.enums.Action;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.UnitTypes;
import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.utils.json.ActionHistory;
import dev.zwazel.autobattler.classes.utils.map.FindPath;
import dev.zwazel.autobattler.classes.utils.map.Node;

import java.util.Random;

public class MyFirstUnit extends Unit {
    public MyFirstUnit(long id, int priority, int level, String name, Vector position) {
        super(id, priority, level, name, "First Unit", 10, 100, 'u', position, 1, UnitTypes.MY_FIRST_UNIT);
    }

    public MyFirstUnit(long id, int priority, int level, String name, Vector position, BattlerGen2 battler, Side side) {
        super(id, level, name, "First Unit", 10, 100, 'u', position, battler.getGrid().getGridSize(), 1, battler, side, priority, UnitTypes.MY_FIRST_UNIT);
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
//                
                return ability;
            }
        }
        return null;
    }

    @Override
    public Vector[] moveTowards(Unit target) {
        if (target != null) {
            Node[] nodes = new FindPath().getNextMoveSteps(this.getGridPosition(), target.getGridPosition(), this.getBattler().getGrid(), this.getSpeed());
            if (nodes.length > 0) {
                Vector[] vectors = new Vector[nodes.length];
                for (int i = 0; i < nodes.length; i++) {
                    Node node = nodes[i];
                    vectors[i] = node.getMyGridCell().getPosition();
                    move(node.getMyGridCell().getPosition(), false);
                }
                return vectors;
            }
        }
        return new Vector[0];
    }

    @Override
    public boolean move(Vector direction, boolean checkIfOccupied) {
        if (!checkIfOccupied) {
//            
            this.setGridPosition(direction);
            return true;
        } else {
            if (new FindPath().isReachable(this.getGridPosition(), direction, this.getBattler().getGrid())) {
                this.setGridPosition(direction);
                return true;
            }
        }
        return false;
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
    public void takeDamage(Ability ability) {
        this.setHealth(this.getHealth() - ability.getOutPutAmount());
        if (this.getHealth() <= 0) {
            die(ability);
        }
    }

    @Override
    public ActionHistory run() {
        Action todoAction = null;
        Unit[] targets = new Unit[0];
        Ability suitableAbility = null;
        Vector[] targetPositions = new Vector[0];

        for (Ability ability : getAbilities()) {
            ability.doRound();
        }

        suitableAbility = findSuitableAbility();

        todoAction = (suitableAbility == null) ? Action.CHASE : Action.USE_ABILITY;
        switch (todoAction) {
            case CHASE -> {
                Unit target = findTargetUnit(this.getSide().getOpposite());
                if (target != null) {
                    targets = new Unit[]{target};
                    targetPositions = moveTowards(targets[0]);
                } else {
                    targets = new Unit[0];
                }
            }
            case USE_ABILITY -> {
                targets = new Unit[]{findTargetUnit(suitableAbility.getTargetSide())};
                suitableAbility.use(targets[0]);
            }
            case RETREAT -> {

            }
        }

        return new ActionHistory(todoAction, this, targets, suitableAbility, targetPositions);
    }
}
