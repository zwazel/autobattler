package dev.zwazel.autobattler.classes.abstractClasses;

import dev.zwazel.autobattler.BattlerGen2;
import dev.zwazel.autobattler.classes.Obstacle;
import dev.zwazel.autobattler.classes.enums.Action;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.State;
import dev.zwazel.autobattler.classes.enums.UnitTypes;
import dev.zwazel.autobattler.classes.model.UnitModel;
import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.utils.json.ActionHistory;
import dev.zwazel.autobattler.classes.utils.map.FindPath;
import dev.zwazel.autobattler.classes.utils.map.Node;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
@Builder
public abstract class Unit implements Obstacle, Cloneable {
    /**
     * The unit's unique ID.
     */
    private final long ID;

    /**
     * the symbol of the unit.
     */
    private final char symbol;

    /**
     * the type of the unit
     */
    private final UnitTypes type;

    /**
     * the side of the unit
     */
    private Side side;

    /**
     * the health of the unit
     */
    private int health;

    /**
     * the energy of the unit
     */
    private int energy;

    /**
     * the level of the unit
     */
    private int level;

    /**
     * the priority of the unit, used to sort the units in the queue and to determine the order of the units
     */
    private int priority;

    /**
     * the name of the unit
     */
    private String name;

    /**
     * the different abilities of the unit
     */
    private Ability[] abilities = new Ability[0];

    /**
     * the position of the unit
     */
    private Vector gridPosition;

    /**
     * the speed of the unit, tells how many tiles the unit can move in one turn
     */
    private int speed;

    /**
     * the battlerGen2 object
     */
    private BattlerGen2 battler;

    /**
     * the current state of the unit
     */
    private State myState = State.ALIVE;

    /**
     * constructor to use when saving formation
     *
     * @param id       the id of the unit
     * @param level    the level of the unit
     * @param name     the name of the unit
     * @param symbol   the symbol of the unit, used for printing in the console (in theory, actually it has never been used yet lol)
     * @param position the position of the unit
     * @param priority the priority of the unit, used for sorting
     * @param type     the type of the unit
     */
    public Unit(long id, int priority, int level, UnitTypes type, char symbol, Vector position, String name) {
        this(id, priority, level, type, symbol, position);
        if (type.isCustomNamesAllowed()) {
            this.name = name;
        } else {
            this.name = type.getDefaultName();
        }
    }

    /**
     * constructor to use when the unit needs to be used in fight
     *
     * @param id       the id of the unit
     * @param level    the level of the unit
     * @param name     the name of the unit
     * @param symbol   the symbol of the unit, used for printing in the console (in theory, actually it has never been used yet lol)
     * @param position the position of the unit
     * @param battler  the battler that the unit belongs to
     * @param side     the side of the unit
     * @param priority the priority of the unit, used for sorting
     * @param type     the type of the unit
     */
    public Unit(long id, int priority, int level, UnitTypes type, char symbol, Vector position, Side side, BattlerGen2 battler, String name) {
        this(id, priority, level, type, symbol, position, name);
        this.battler = battler;
        this.side = side;
    }

    public Unit(long id, int priority, int level, UnitTypes type, char symbol, Vector position, Side side, BattlerGen2 battler) {
        this(id, priority, level, type, symbol, position);
        this.battler = battler;
        this.side = side;
    }

    public Unit(long id, int priority, int level, UnitTypes type, char symbol, Vector position) {
        this.ID = id;
        this.level = level;
        this.health = type.scaleHealth(level);
        this.energy = type.scaleEnergy(level);
        this.symbol = symbol;
        this.gridPosition = position;
        this.speed = type.scaleMoveSpeed(level);
        this.priority = priority;
        this.type = type;
        if (name == null) {
            this.name = type.getDefaultName();
        }
    }

    public UnitModel getUnitModel() {
        return new UnitModel(this);
    }

    /**
     * use said ability on the target
     *
     * @param ability the ability to use
     * @param target  the target of the ability
     */
    protected void useAbility(Ability ability, Unit target) {
        ability.processUse(target);
    }

    /**
     * method called by the battler to tell this unit to do something
     *
     * @return the action that the unit did
     */
    public ActionHistory process() {
        return run();
    }

    /**
     * in this method, the unit will decide what to do.
     *
     * @return the action that the unit did. for example if the unit uses an ability.
     */
    protected abstract ActionHistory run();

    protected abstract Ability findSuitableAbility();

    protected Vector[] moveTowards(Unit target) {
        if (target != null) {
            Node[] nodes = new FindPath().getNextMoveSteps(gridPosition, target.getGridPosition(), battler.getGrid(), speed, type.isCanMoveDiagonally());
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

    // TODO: 17.02.2022 use speed to move as much as possible. get a random destination within reach and move there by using the move method
    protected boolean move(Vector direction, boolean checkIfOccupied) {
        if (!checkIfOccupied) {
            this.setGridPosition(direction);
            return true;
        } else {
            if (!new FindPath().isOccupied(direction, battler.getGrid())) {
                this.setGridPosition(direction);
                return true;
            }
        }
        return false;
    }

    protected void moveRandom() {
        Vector direction = Vector.getRandomDirection(type.isCanMoveDiagonally());
        move(direction, true);
    }

    protected abstract Unit findTargetUnit(Side side);

    public abstract void takeDamage(Ability ability);

    public ActionHistory die(Ability ability) {
        setMyState(State.DEAD);

        return new ActionHistory(Action.DIE, this, new Unit[0], null, new Vector[]{this.getGridPosition()});
    }

    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (this.type.isCustomNamesAllowed()) {
            this.name = name;
        }
    }

    @Override
    public String toString() {
        return "Unit{" +
                "ID=" + ID +
                ", symbol=" + symbol +
                ", name='" + name + '\'' +
                ", priority=" + priority +
                ", side=" + side +
                ", level=" + level +
                ", health=" + health +
                ", energy=" + energy +
                ", speed=" + speed +
                ", myState=" + myState +
                ", type=" + type +
                ", gridPosition=" + gridPosition +
                ", abilities=" + Arrays.toString(abilities) +
                '}';
    }

    @Override
    public Unit clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (Unit) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Unit unit = (Unit) o;

        if (getID() != unit.getID()) return false;
        if (getSymbol() != unit.getSymbol()) return false;
        if (getHealth() != unit.getHealth()) return false;
        if (getEnergy() != unit.getEnergy()) return false;
        if (getLevel() != unit.getLevel()) return false;
        if (getPriority() != unit.getPriority()) return false;
        if (getSpeed() != unit.getSpeed()) return false;
        if (getType() != unit.getType()) return false;
        if (getSide() != unit.getSide()) return false;
        if (!getName().equals(unit.getName())) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(getAbilities(), unit.getAbilities())) return false;
        if (getGridPosition() != null ? !getGridPosition().equals(unit.getGridPosition()) : unit.getGridPosition() != null)
            return false;
        if (getBattler() != null ? !getBattler().equals(unit.getBattler()) : unit.getBattler() != null) return false;
        return getMyState() == unit.getMyState();
    }

    @Override
    public int hashCode() {
        int result = (int) (getID() ^ (getID() >>> 32));
        result = 31 * result + (int) getSymbol();
        result = 31 * result + getType().hashCode();
        result = 31 * result + (getSide() != null ? getSide().hashCode() : 0);
        result = 31 * result + getHealth();
        result = 31 * result + getEnergy();
        result = 31 * result + getLevel();
        result = 31 * result + getPriority();
        result = 31 * result + getName().hashCode();
        result = 31 * result + Arrays.hashCode(getAbilities());
        result = 31 * result + (getGridPosition() != null ? getGridPosition().hashCode() : 0);
        result = 31 * result + getSpeed();
        result = 31 * result + (getBattler() != null ? getBattler().hashCode() : 0);
        result = 31 * result + (getMyState() != null ? getMyState().hashCode() : 0);
        return result;
    }
}
