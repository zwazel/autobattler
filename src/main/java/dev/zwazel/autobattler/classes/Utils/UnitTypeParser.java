package dev.zwazel.autobattler.classes.Utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.zwazel.autobattler.Battler;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.UnitTypes;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.units.MyFirstUnit;
import dev.zwazel.autobattler.classes.units.Unit;

public class UnitTypeParser {
    public static Unit getUnit(JsonObject unitJson, Battler battler, Side side) throws UnknownUnitType {
        JsonElement unitType = unitJson.get("type");
        UnitTypes type = UnitTypes.findUnitType(unitType.getAsString());
        if (type != null) {
            switch (type) {
                case MY_FIRST_UNIT -> {
                    return new MyFirstUnit(unitJson.get("id").getAsLong(), unitJson.get("priority").getAsInt(), unitJson.get("level").getAsInt(), unitJson.get("name").getAsString(), new Vector(unitJson.get("position").getAsJsonObject()), battler, side);
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
