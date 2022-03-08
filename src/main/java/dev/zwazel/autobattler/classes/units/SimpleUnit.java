package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.enums.UnitTypes;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.model.UnitModel;
import dev.zwazel.autobattler.classes.utils.UnitTypeParser;
import dev.zwazel.autobattler.classes.utils.Vector;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * Util class that works as a bridge to create a unit of a certain type.
 */
@Getter
@Setter
public class SimpleUnit {
  private long id;
  private int priority;
  private Integer level;
  private Vector position;
  private UnitTypes unitType;
  private String name;
  private Date dateCollected;

  public SimpleUnit() {}

  public SimpleUnit(UnitModel unitModel) {
    this.id = unitModel.getId();
    this.level = unitModel.getLevel();
    this.unitType = unitModel.getUnitType();
    this.name = unitModel.getName();
    this.dateCollected = unitModel.getDateCollected();
  }

  public SimpleUnit(UnitModel unitModel, int priority, Vector position) {
    this(unitModel);
    this.priority = priority;
    this.position = position;
  }

  public SimpleUnit(long id, int priority, int level, Vector position,
                    UnitTypes unitType, String name) {
    this.id = id;
    this.priority = priority;
    this.level = level;
    this.position = position;
    this.unitType = unitType;
    this.name = name;
  }

  public Unit getUnit() throws UnknownUnitType {
    return UnitTypeParser.getUnit(this);
  }

  public void setLevel(Integer level) {
    if (level != null && level > 0) {
      this.level = level;
    }
  }

  public void setUnitType(String unitType) throws UnknownUnitType {
    this.unitType = UnitTypes.findUnitType(unitType);
  }

  public void setUnitType(UnitTypes unitType) { this.unitType = unitType; }

  @Override
  public String toString() {
    return "SimpleUnit{"
        + "name='" + name + '\'' + ", position=" + position +
        ", priority=" + priority + ", id=" + id + ", level=" + level +
        ", unitType=" + ((unitType == null) ? "null" : unitType.name()) +
        ", dateCollected=" + dateCollected + '}';
  }
}
