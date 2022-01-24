package dev.zwazel.autobattler.classes.Utils;

import dev.zwazel.autobattler.classes.abilities.Ability;
import dev.zwazel.autobattler.classes.enums.Action;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.units.Unit;

public record ActionHistory(Action actionType,
                            Unit user,
                            Unit target,
                            Ability ability,
                            Vector position) {

    @Override
    public Action actionType() {
        return actionType;
    }

    @Override
    public Unit user() {
        return user;
    }

    @Override
    public Unit target() {
        return target;
    }

    @Override
    public Ability ability() {
        return ability;
    }

    @Override
    public Vector position() {
        return position;
    }

    @Override
    public String toString() {
        return "ActionHistory{" +
                "actionType=" + actionType +
                ", user=" + user +
                ", target=" + target +
                ", ability=" + ability +
                ", position=" + position +
                '}';
    }
}
