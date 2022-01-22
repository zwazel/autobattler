package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.BattlerGen2;
import dev.zwazel.autobattler.classes.Obstacle;
import dev.zwazel.autobattler.classes.ProcessingInstance;
import dev.zwazel.autobattler.classes.Utils.Vector;
import dev.zwazel.autobattler.classes.abilities.Ability;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.State;

import java.util.Arrays;

public abstract class Unit implements ProcessingInstance, Obstacle {
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
    private int damage;
    private Vector gridPosition;
    private Vector gridSize;
    private int speed;
    private BattlerGen2 battler;
    private State myState = State.ALIVE;
    private Unit lastHitter;

    public Unit(long id, int level, int damage, String name, String description, int health, int energy, char symbol, Vector position, Vector gridSize, int speed, BattlerGen2 battler, Side side, int priority) {
        this.ID = id;
        this.level = level;
        this.damage = getLevelDamage(damage, level - 1);
        this.health = getLevelHealth(health, level - 1);
        this.energy = getLevelEnergy(energy, level - 1);
        this.name = name;
        this.description = description;
        this.symbol = symbol;
        this.gridPosition = position;
        this.gridSize = gridSize;
        this.speed = speed;
        this.battler = battler;
        this.side = side;
        this.priority = priority;
    }

    protected abstract int getLevelHealth(int health, int level);

    protected abstract int getLevelEnergy(int energy, int level);

    protected abstract int getLevelDamage(int damage, int level);

    protected abstract Ability findSuitableAbility();

    protected abstract void moveTowards(Unit target);

    protected abstract void move(Vector direction);

    protected abstract void moveRandom();

    protected abstract Unit findTargetUnit(Side side);

    protected abstract void die();

    public void takeDamage(int damage, Unit hitter) {
        health -= damage;
        this.lastHitter = hitter;
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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
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

    public Unit getLastHitter() {
        return lastHitter;
    }

    public void setLastHitter(Unit lastHitter) {
        this.lastHitter = lastHitter;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "ID=" + ID +
                ", symbol=" + symbol +
                ", side=" + side +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", health=" + health +
                ", damage=" + damage +
                ", energy=" + energy +
                ", speed=" + speed +
                ", priority=" + priority +
                ", abilities=" + Arrays.toString(abilities) +
                ", gridPosition=" + gridPosition +
                ", battler=" + battler +
                ", myState=" + myState +
                '}';
    }
}
