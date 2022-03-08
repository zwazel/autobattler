package dev.zwazel.autobattler.classes.utils.database;

import dev.zwazel.autobattler.classes.enums.UnitTypes;
import java.util.Date;

public interface UnitOnly {
  long getId();

  String getName();

  int getLevel();

  UnitTypes getUnitType();

  boolean isCustomNamesAllowed();

  Date getDateCollected();
}
