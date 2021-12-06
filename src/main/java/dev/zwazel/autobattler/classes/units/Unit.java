package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.Battler;
import dev.zwazel.autobattler.classes.Utils.Vector;
import dev.zwazel.autobattler.classes.abilities.Ability;
import dev.zwazel.autobattler.classes.enums.Side;

import java.util.Arrays;

public abstract class Unit {
    private final long ID;
    private final char symbol;
    private final Side side;
    private int baseHealth;
    private int baseEnergy;
    private int level;
    private String name;
    private String description;
    private Ability[] abilities = new Ability[0];
    private int baseDamage;
    private Vector gridPosition;
    private Vector gridSize;
    private int baseSpeed;
    private Battler battler;

    public Unit(long id, int level, int baseDamage, String name, String description, int baseHealth, int baseEnergy, char symbol, Ability[] abilities, Vector position, Vector gridSize, int baseSpeed, Battler battler, Side side) {
        this(id, level, baseDamage, name, description, baseHealth, baseEnergy, symbol, position, gridSize, baseSpeed, battler, side);
        this.abilities = abilities;
    }

    public Unit(long id, int level, int baseDamage, String name, String description, int baseHealth, int baseEnergy, char symbol, Vector position, Vector gridSize, int baseSpeed, Battler battler, Side side) {
        this.ID = id;
        this.level = level;
        this.baseDamage = baseDamage;
        this.baseHealth = baseHealth;
        this.baseEnergy = baseEnergy;
        this.name = name;
        this.description = description;
        this.symbol = symbol;
        this.gridPosition = position;
        this.gridSize = gridSize;
        this.baseSpeed = baseSpeed;
        this.battler = battler;
        this.side = side;
    }

    public abstract void move(Vector direction);

    public abstract void moveRandom();

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

    public int getBaseHealth() {
        return baseHealth;
    }

    public void setBaseHealth(int baseHealth) {
        this.baseHealth = baseHealth;
    }

    public int getBaseEnergy() {
        return baseEnergy;
    }

    public void setBaseEnergy(int baseEnergy) {
        this.baseEnergy = baseEnergy;
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

    @Override
    public String toString() {
        return "Unit{" +
                "ID=" + ID +
                ", symbol=" + symbol +
                ", baseHealth=" + baseHealth +
                ", baseEnergy=" + baseEnergy +
                ", level=" + level +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", abilities=" + Arrays.toString(abilities) +
                ", baseDamage=" + baseDamage +
                ", gridPosition=" + gridPosition +
                ", gridSize=" + gridSize +
                ", baseSpeed=" + baseSpeed +
                ", battler=" + battler +
                ", side=" + side +
                '}';
    }
}
