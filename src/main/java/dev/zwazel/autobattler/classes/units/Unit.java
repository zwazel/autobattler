package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.Battler;
import dev.zwazel.autobattler.classes.ProcessingInstance;
import dev.zwazel.autobattler.classes.Utils.Vector;
import dev.zwazel.autobattler.classes.abilities.Ability;
import dev.zwazel.autobattler.classes.enums.Action;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.State;

import java.util.Arrays;

public abstract class Unit implements ProcessingInstance {
    private final long ID;
    private final char symbol;
    private final Side side;
    private int health;
    private int energy;
    private int level;
    private int priority;
    private String name;
    private String description;
    private Ability[] abilities = new Ability[0];
    private int baseDamage;
    private Vector gridPosition;
    private Vector gridSize;
    private int baseSpeed;
    private Battler battler;
    private Action todoAction;
    private Ability nextAbility;
    private Unit targetUnit;
    private State myState = State.ALIVE;

    public Unit(long id, int level, int baseDamage, String name, String description, int health, int energy, char symbol, Vector position, Vector gridSize, int baseSpeed, Battler battler, Side side, int priority) {
        this.ID = id;
        this.level = level;
        this.baseDamage = baseDamage;
        this.health = getLevelHealth(health, level - 1);
        this.energy = getLevelEnergy(energy, level - 1);
        this.name = name;
        this.description = description;
        this.symbol = symbol;
        this.gridPosition = position;
        this.gridSize = gridSize;
        this.baseSpeed = baseSpeed;
        this.battler = battler;
        this.side = side;
        this.priority = priority;
    }

    protected abstract int getLevelHealth(int health, int level);

    protected abstract int getLevelEnergy(int energy, int level);

    protected abstract Ability findSuitableAbility();

    protected abstract void moveTowards(Unit target);

    protected abstract void move(Vector direction);

    protected abstract void moveRandom();

    protected abstract Unit findTargetUnit();

    protected abstract void die();

    public void takeDamage(int damage) {
        health -= damage;
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

    public int getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
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

    public Vector getGridSize() {
        return gridSize;
    }

    public void setGridSize(Vector gridSize) {
        this.gridSize = gridSize;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(int baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public Battler getBattler() {
        return battler;
    }

    public void setBattler(Battler battler) {
        this.battler = battler;
    }

    public Side getSide() {
        return side;
    }

    public Action getTodoAction() {
        return todoAction;
    }

    public void setTodoAction(Action todoAction) {
        this.todoAction = todoAction;
    }

    public Ability getNextAbility() {
        return nextAbility;
    }

    public void setNextAbility(Ability nextAbility) {
        this.nextAbility = nextAbility;
    }

    public Unit getTargetUnit() {
        return targetUnit;
    }

    public void setTargetUnit(Unit targetUnit) {
        this.targetUnit = targetUnit;
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

    @Override
    public String toString() {
        return "Unit{" +
                "ID=" + ID +
                ", symbol=" + symbol +
                ", side=" + side +
                ", baseHealth=" + health +
                ", baseEnergy=" + energy +
                ", level=" + level +
                ", priority=" + priority +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", abilities=" + Arrays.toString(abilities) +
                ", baseDamage=" + baseDamage +
                ", gridPosition=" + gridPosition +
                ", gridSize=" + gridSize +
                ", baseSpeed=" + baseSpeed +
                ", battler=" + battler +
                ", todoAction=" + todoAction +
                ", nextAbility=" + nextAbility +
                ", targetUnit=" + targetUnit +
                ", myState=" + myState +
                '}';
    }
}
