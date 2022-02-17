package dev.zwazel.autobattler.classes.enums;

import dev.zwazel.autobattler.classes.abstractClasses.ScaleAttributeWithLevel;

public enum UnitTypes {
    MY_FIRST_UNIT("First unit", 10, 100, 1, false,
            ((health, level) -> ((int) (health + (health * ((level - 1) * 0.25))))),
            ((energy, level) -> ((int) (energy + (energy * ((level - 1) * 0.25))))));

    /**
     * the description of the unit
     */
    private final String description;

    /**
     * the base health of the unit, which will be scaled with the level
     */
    private final int baseHealth;

    /**
     * the base energy of the unit, which will be scaled with the level
     */
    private final int baseEnergy;

    /**
     * the movement speed of the unit, which says how many squares it can move per turn
     */
    private final int baseMoveSpeed;

    /**
     * whether the unit can move diagonally or not
     */
    private final boolean canMoveDiagonally;

    /**
     * the health scale function, which takes the base health and the level and returns the scaled health
     */
    private final ScaleAttributeWithLevel scaleHealth;

    /**
     * the energy scale function, which takes the base energy and the level and returns the scaled energy
     */
    private final ScaleAttributeWithLevel scaleEnergy;

    UnitTypes(String description, int baseHealth, int baseEnergy, int baseMoveSpeed, boolean canMoveDiagonally, ScaleAttributeWithLevel scaleHealth, ScaleAttributeWithLevel scaleEnergy) {
        this.description = description;
        this.baseHealth = baseHealth;
        this.baseEnergy = baseEnergy;
        this.baseMoveSpeed = baseMoveSpeed;
        this.canMoveDiagonally = canMoveDiagonally;
        this.scaleHealth = scaleHealth;
        this.scaleEnergy = scaleEnergy;
    }

    public static UnitTypes findUnitType(String name) {
        for (UnitTypes type : UnitTypes.values()) {
            if (type.toString().equals(name)) {
                return type;
            }
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public int getBaseMoveSpeed() {
        return baseMoveSpeed;
    }

    public boolean isCanMoveDiagonally() {
        return canMoveDiagonally;
    }

    public int scaleHealth(int level) {
        return scaleHealth.scale(this.baseHealth, level);
    }

    public int scaleEnergy(int level) {
        return scaleEnergy.scale(this.baseEnergy, level);
    }
}
