package dev.zwazel.autobattler.classes.enums;

import dev.zwazel.autobattler.classes.abstractClasses.ScaleAttributeWithLevel;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;

public enum UnitTypes {
    MY_FIRST_UNIT("First unit", 10, 100, 1, false, true, "MY_FIRST_UNIT",
            ((health, level) -> ((int) (health + (health * ((level - 1) * 0.25))))),
            ((energy, level) -> ((int) (energy + (energy * ((level - 1) * 0.25))))));

    /**
     * the default name of every unit of this type. If no custom name is set, this name will be used.
     */
    private final String defaultName;

    /**
     * defines if the user can change the name of the unit
     */
    private final boolean customNamesAllowed;

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

    UnitTypes(String description, int baseHealth, int baseEnergy, int baseMoveSpeed, boolean canMoveDiagonally, boolean customNamesAllowed, String defaultName, ScaleAttributeWithLevel scaleHealth, ScaleAttributeWithLevel scaleEnergy) {
        this.description = description;
        this.baseHealth = baseHealth;
        this.baseEnergy = baseEnergy;
        this.baseMoveSpeed = baseMoveSpeed;
        this.canMoveDiagonally = canMoveDiagonally;
        this.customNamesAllowed = customNamesAllowed;
        this.defaultName = defaultName;
        this.scaleHealth = scaleHealth;
        this.scaleEnergy = scaleEnergy;
    }

    public static UnitTypes findUnitType(String unitTypeName) throws UnknownUnitType {
        if (unitTypeName == null) {
            throw new IllegalArgumentException("unitTypeName cannot be null");
        }

        for (UnitTypes type : UnitTypes.values()) {
            if (type.toString().equals(unitTypeName)) {
                return type;
            }
        }

        throw new UnknownUnitType(unitTypeName);
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

    public String getDefaultName() {
        return defaultName;
    }

    public boolean isCustomNamesAllowed() {
        return customNamesAllowed;
    }

    public int scaleHealth(int level) {
        return scaleHealth.scale(this.baseHealth, level);
    }

    public int scaleEnergy(int level) {
        return scaleEnergy.scale(this.baseEnergy, level);
    }
}
