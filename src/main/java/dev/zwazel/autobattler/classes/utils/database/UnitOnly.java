package dev.zwazel.autobattler.classes.utils.database;

import dev.zwazel.autobattler.classes.enums.UnitTypes;

public interface UnitOnly {
    long getId();

    String getName();

    int getLevel();

    UnitTypes getUnitType();

    boolean isCustomNamesAllowed();
}
