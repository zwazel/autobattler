package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.utils.UnitTypeParser;
import dev.zwazel.autobattler.classes.utils.Vector;

public class SimpleUnit {
    private String name;
    private Vector position;
    private String unitType;
    private int priority;
    private long id;

    public Unit getUnit() throws UnknownUnitType {
        return UnitTypeParser.getUnit(this);
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SimpleUnit{" +
                "name='" + name + '\'' +
                ", position=" + position +
                ", unitType='" + unitType + '\'' +
                ", priority=" + priority +
                ", id=" + id +
                '}';
    }
}
