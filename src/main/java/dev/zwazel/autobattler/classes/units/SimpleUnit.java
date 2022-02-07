package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.classes.enums.UnitTypes;
import dev.zwazel.autobattler.classes.utils.Vector;

public class SimpleUnit {
    private String name;
    private Vector position;
    private UnitTypes unitType;

    public SimpleUnit() {
    }

    public SimpleUnit(String name, Vector position, UnitTypes unitType) {
        this.name = name;
        this.position = position;
        this.unitType = unitType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public UnitTypes getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitTypes unitType) {
        this.unitType = unitType;
    }
}
