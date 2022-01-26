package dev.zwazel.autobattler.classes.enums;

public enum UnitTypes {
    MY_FIRST_UNIT;

    public static UnitTypes findUnitType(String name) {
        for (UnitTypes type : UnitTypes.values()) {
            if (type.toString().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
