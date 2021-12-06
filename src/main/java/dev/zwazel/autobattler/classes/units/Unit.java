package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.classes.abilities.Ability;

import java.util.Arrays;

public abstract class Unit {
    private final long ID;
    private int baseHealth;
    private int baseEnergy;
    private int level;
    private String name;
    private String description;
    private Ability[] abilities = new Ability[0];
    private int baseDamage;
    private char symbol;

    public Unit(long id, int level, int baseDamage, String name, String description, int baseHealth, int baseEnergy, char symbol, Ability[] abilities) {
        this(id, level, baseDamage, name, description, baseHealth, baseEnergy, symbol);
        this.abilities = abilities;
    }

    public Unit(long id, int level, int baseDamage, String name, String description, int baseHealth, int baseEnergy, char symbol) {
        this.ID = id;
        this.level = level;
        this.baseDamage = baseDamage;
        this.baseHealth = baseHealth;
        this.baseEnergy = baseEnergy;
        this.name = name;
        this.description = description;
        this.symbol = symbol;
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

    @Override
    public String toString() {
        return "Unit{" +
                "ID=" + ID +
                ", MAX_HEALTH=" + baseHealth +
                ", MAX_ENERGY=" + baseEnergy +
                ", level=" + level +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", abilities=" + Arrays.toString(abilities) +
                '}';
    }
}
