package dev.zwazel.autobattler.classes.enums;

public enum Side {
    FRIENDLY,
    ENEMY;

    public Side getOpposite() {
        return (this == FRIENDLY) ? ENEMY : FRIENDLY;
    }
}
