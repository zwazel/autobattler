package dev.zwazel.autobattler.classes.utils.json;

import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.abilities.Ability;
import dev.zwazel.autobattler.classes.enums.Action;
import dev.zwazel.autobattler.classes.units.Unit;

import java.util.Arrays;

public record ActionHistory(Action actionType, Unit user, Unit[] targets, Ability ability,
                            Vector[] positions) {

    @Override
    public String toString() {
        return "ActionHistory{" +
                "actionType=" + actionType +
                ", user=" + user +
                ", target=" + Arrays.toString(targets) +
                ", ability=" + ability +
                ", position=" + Arrays.toString(positions) +
                '}';
    }
}
