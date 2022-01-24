package dev.zwazel.autobattler.classes.Utils;

import dev.zwazel.autobattler.classes.abilities.Ability;
import dev.zwazel.autobattler.classes.enums.Action;
import dev.zwazel.autobattler.classes.units.Unit;

public class ActionHistory {
    private final Action actionType;
    private final Unit user;
    private final Unit target;
    private final Ability ability;

    public ActionHistory(Action actionType, Unit user, Unit target, Ability ability) {
        this.actionType = actionType;
        this.user = user;
        this.target = target;
        this.ability = ability;
    }

    public Action getActionType() {
        return actionType;
    }

    public Unit getUser() {
        return user;
    }

    public Unit getTarget() {
        return target;
    }

    public Ability getAbility() {
        return ability;
    }

    @Override
    public String toString() {
        return "ActionHistory{" +
                "actionType=" + actionType +
                ", user=" + user +
                ", target=" + target +
                ", ability=" + ability +
                '}';
    }
}
