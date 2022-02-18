package dev.zwazel.autobattler.classes.enums;

import dev.zwazel.autobattler.classes.abstractClasses.ScaleAttributeWithLevel;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;

public enum UnitTypes {
    MY_FIRST_UNIT("First unit", 10, 10, 1, false,
            true, "MY_FIRST_UNIT", 1,
            ((health, level) -> ((int) (health + (health * ((level - 1) * 0.25))))),
            ((energy, level) -> ((int) (energy + (energy * ((level - 1) * 0.25))))),
            ((moveSpeed, level) -> (moveSpeed))
    );

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
     * the default name of every unit of this type. If no custom name is set, this name will be used.
     */
    private final String defaultName;

    /**
     * defines if the user can change the name of the unit
     */
    private final boolean customNamesAllowed;

    /**
     * how many slots in the formation this unit takes up
     */
    private final int slotSize;

    /**
     * the health scale function, which takes the base health and the level and returns the scaled health
     */
    private final ScaleAttributeWithLevel scaleHealth;

    /**
     * the energy scale function, which takes the base energy and the level and returns the scaled energy
     */
    private final ScaleAttributeWithLevel scaleEnergy;

    /**
     * the move speed scale function, which takes the base move speed and the level and returns the scaled move speed
     */
    private final ScaleAttributeWithLevel scaleMoveSpeed;

    /**
     * default, placeholder constructor! to quickly create a new unit type, not suitable if more customizations are needed
     *
     * @param description the description of the unit
     */
    UnitTypes(String description) {
        this.description = description;
        this.baseHealth = 10;
        this.baseEnergy = 10;
        this.baseMoveSpeed = 1;
        this.canMoveDiagonally = false;
        this.customNamesAllowed = true;
        this.defaultName = this.name();
        this.slotSize = 1;
        this.scaleHealth = ((health, level) -> health + ((level - 1) * 10)); // add 10 health per level, and make it so that the unit has its base health on level 1
        this.scaleEnergy = ((energy, level) -> energy + ((level - 1) * 10)); // add 10 energy per level, and make it so that the unit has its base energy on level 1
        this.scaleMoveSpeed = ((moveSpeed, level) -> moveSpeed); // add 0 move speed per level, always keep it on 0!
    }

    UnitTypes(String description, int baseHealth, int baseEnergy, int baseMoveSpeed, boolean canMoveDiagonally, boolean customNamesAllowed, String defaultName, int slotSize, ScaleAttributeWithLevel scaleHealth, ScaleAttributeWithLevel scaleEnergy, ScaleAttributeWithLevel scaleMoveSpeed) {
        this.description = description;
        this.baseHealth = baseHealth;
        this.baseEnergy = baseEnergy;
        this.baseMoveSpeed = baseMoveSpeed;
        this.canMoveDiagonally = canMoveDiagonally;
        this.customNamesAllowed = customNamesAllowed;
        this.defaultName = defaultName;
        this.slotSize = slotSize;
        this.scaleHealth = scaleHealth;
        this.scaleEnergy = scaleEnergy;
        this.scaleMoveSpeed = scaleMoveSpeed;
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

    public int getSlotSize() {
        return slotSize;
    }

    public int scaleHealth(int level) {
        return scaleHealth.scale(this.baseHealth, level);
    }

    public int scaleEnergy(int level) {
        return scaleEnergy.scale(this.baseEnergy, level);
    }

    public int scaleMoveSpeed(int level) {
        return scaleMoveSpeed.scale(this.baseMoveSpeed, level);
    }
}
