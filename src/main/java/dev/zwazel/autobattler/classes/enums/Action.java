package dev.zwazel.autobattler.classes.enums;

import dev.zwazel.autobattler.classes.abilities.Ability;
import dev.zwazel.autobattler.classes.units.Unit;

public enum Action {
    CHASE,
    USE_ABILITY,
    RETREAT,
    DIE;

    Action() {
    }

    @Override
    public String toString() {
        return "Action{"+this.name()+"}";
    }
}
