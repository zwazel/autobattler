package dev.zwazel.autobattler.classes.exceptions;

public class UnknownUnitType extends Exception {
    public UnknownUnitType() {
        super("Unknown unit type");
    }

    public UnknownUnitType(String message) {
        super("Unknown unit type: " + message);
    }
}
