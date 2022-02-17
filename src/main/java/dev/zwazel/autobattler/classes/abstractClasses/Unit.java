package dev.zwazel.autobattler.classes.abstractClasses;

import dev.zwazel.autobattler.BattlerGen2;
import dev.zwazel.autobattler.classes.Obstacle;
import dev.zwazel.autobattler.classes.enums.Action;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.State;
import dev.zwazel.autobattler.classes.enums.UnitTypes;
import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.utils.json.ActionHistory;
import dev.zwazel.autobattler.classes.utils.map.FindPath;
import dev.zwazel.autobattler.classes.utils.map.Node;

import java.util.Arrays;

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
     * the description of the unit
     */
    private String description;

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
     * tells if the unit can move diagonally or not
     */
    private boolean canMoveDiagonally;

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
        this.ID = id;
        this.level = level;
        this.health = type.scaleHealth(level);
        this.energy = type.scaleEnergy(level);
        this.name = name;
        this.description = type.getDescription();
        this.symbol = symbol;
        this.gridPosition = position;
        this.speed = type.getBaseMoveSpeed();
        this.priority = priority;
        this.type = type;
        this.canMoveDiagonally = type.isCanMoveDiagonally();
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
            Node[] nodes = new FindPath().getNextMoveSteps(gridPosition, target.getGridPosition(), battler.getGrid(), speed, canMoveDiagonally);
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
        Vector direction = Vector.getRandomDirection(canMoveDiagonally);
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
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public Ability[] getAbilities() {
        return abilities;
    }

    public void setAbilities(Ability[] abilities) {
        this.abilities = abilities;
    }

    public void setAbility(Ability ability, int index) {
        this.abilities[index] = ability;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public char getSymbol() {
        return symbol;
    }

    public Vector getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(Vector gridPosition) {
        this.gridPosition = gridPosition;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public BattlerGen2 getBattler() {
        return battler;
    }

    public void setBattler(BattlerGen2 battler) {
        this.battler = battler;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public State getMyState() {
        return myState;
    }

    public void setMyState(State myState) {
        this.myState = myState;
    }

    public UnitTypes getType() {
        return type;
    }

    public boolean isCanMoveDiagonally() {
        return canMoveDiagonally;
    }

    public void setCanMoveDiagonally(boolean canMoveDiagonally) {
        this.canMoveDiagonally = canMoveDiagonally;
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
            Unit clone = (Unit) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
