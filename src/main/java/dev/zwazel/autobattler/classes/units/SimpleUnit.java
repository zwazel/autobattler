package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.enums.UnitTypes;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.utils.UnitTypeParser;
import dev.zwazel.autobattler.classes.utils.Vector;

/**
 * Util class that works as a bridge to create a unit of a certain type.
 */
public class SimpleUnit {
    private long id;
    private int priority;
    private Integer level;
    private Vector position;
    private UnitTypes unitType;
    private String name;

    public SimpleUnit() {
    }

    public SimpleUnit(long id, int priority, int level, Vector position, String unitType, String name) throws UnknownUnitType {
        this.id = id;
        this.priority = priority;
        this.level = level;
        this.position = position;
        this.unitType = UnitTypes.findUnitType(unitType);
        this.name = name;
    }

    public SimpleUnit(long id, int priority, int level, Vector position, UnitTypes unitType, String name) {
        this.id = id;
        this.priority = priority;
        this.level = level;
        this.position = position;
        this.unitType = unitType;
        this.name = name;
    }

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

    public UnitTypes getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) throws UnknownUnitType {
        this.unitType = UnitTypes.findUnitType(unitType);
    }

    public void setUnitType(UnitTypes unitType) {
        this.unitType = unitType;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "SimpleUnit{" +
                "name='" + name + '\'' +
                ", position=" + position +
                ", priority=" + priority +
                ", id=" + id +
                '}';
    }
}
