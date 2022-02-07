package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.classes.utils.Vector;

public class SimpleUnit {
    private String name;
    private Vector position;
    private String unitType;

    public SimpleUnit() {
    }

    public SimpleUnit(String name, Vector position, String unitType) {
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

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    @Override
    public String toString() {
        return "SimpleUnit{" +
                "name='" + name + '\'' +
                ", position=" + position +
                ", unitType='" + unitType + '\'' +
                '}';
    }
}
