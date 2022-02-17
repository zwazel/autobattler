package dev.zwazel.autobattler.classes.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.zwazel.autobattler.BattlerGen2;
import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.UnitTypes;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.units.MyFirstUnit;
import dev.zwazel.autobattler.classes.units.SimpleUnit;

public class UnitTypeParser {
    public static Unit getUnit(SimpleUnit unit) throws UnknownUnitType {
        UnitTypes type = UnitTypes.findUnitType(unit.getUnitType());

        if (type != null) {
            switch (type) {
                case MY_FIRST_UNIT -> {
                    return new MyFirstUnit(unit.getId(), unit.getPriority(), 1, unit.getPosition(), unit.getName());
                }
                default -> {
                    throw new UnknownUnitType();
                }
            }
        } else {
            throw new UnknownUnitType();
        }
    }

    public static Unit getUnit(JsonObject unitJson, BattlerGen2 battler, Side side) throws UnknownUnitType {
        JsonElement unitType = unitJson.get("type");
        UnitTypes type = UnitTypes.findUnitType(unitType.getAsString());
        if (type != null) {
            switch (type) {
                case MY_FIRST_UNIT -> {
                    return new MyFirstUnit(unitJson.get("id").getAsLong(), unitJson.get("priority").getAsInt(),
                            unitJson.get("level").getAsInt(), new Vector(unitJson.get("position").getAsJsonObject()),
                            side, battler, unitJson.get("name").getAsString());
                }
                default -> {
                    throw new UnknownUnitType();
                }
            }
        } else {
            throw new UnknownUnitType();
        }
    }
}
