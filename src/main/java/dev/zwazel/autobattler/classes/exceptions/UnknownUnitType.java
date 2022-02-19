package dev.zwazel.autobattler.classes.exceptions;

import dev.zwazel.autobattler.classes.enums.UnitTypes;

public class UnknownUnitType extends Exception {
    public UnknownUnitType() {
        super("Unknown unit type");
    }

    public UnknownUnitType(String message) {
        super("Unknown unit type: " + message);
    }

    public UnknownUnitType(UnitTypes unitType) {
        super("Unknown unit type: " + unitType);
    }
}
