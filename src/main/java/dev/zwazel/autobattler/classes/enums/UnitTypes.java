package dev.zwazel.autobattler.classes.enums;

import dev.zwazel.autobattler.classes.units.MyFirstUnit;

public enum UnitTypes {
    MY_FIRST_UNIT(MyFirstUnit.class);

    private final Class<MyFirstUnit> unitType;

    UnitTypes(Class<MyFirstUnit> unitType) {
        this.unitType = unitType;
    }

    public static UnitTypes findUnitType(String name) {
        for (UnitTypes type : UnitTypes.values()) {
            if (type.toString().equals(name)) {
                return type;
            }
        }
        return null;
    }

    public Class<MyFirstUnit> getUnitType() {
        return unitType;
    }
}
