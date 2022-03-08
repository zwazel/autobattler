package dev.zwazel.autobattler.classes.utils.database;

public interface UnitTypeWithInfo {
  String getName();

  String getDefaultName();

  boolean isCustomNamesAllowed();
}
