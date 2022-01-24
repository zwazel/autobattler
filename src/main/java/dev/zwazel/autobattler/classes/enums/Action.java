package dev.zwazel.autobattler.classes.enums;

public enum Action {
    CHASE,
    USE_ABILITY,
    RETREAT,
    DIE;

    Action() {
    }

    @Override
    public String toString() {
        return "Action{" + this.name() + "}";
    }
}
