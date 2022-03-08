package dev.zwazel.autobattler.classes.utils;

import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.model.FormationEntity;
import dev.zwazel.autobattler.classes.model.User;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Formation {
  private final User user;

  private final ArrayList<Unit> units;

  public Formation(FormationEntity formationEntity) {
    this.user = formationEntity.getUser();
    this.units = new ArrayList<>();
    formationEntity.getFormationUnitTable().forEach(formationUnitEntity -> {
      try {
        this.units.add(formationUnitEntity.getUnit());
      } catch (UnknownUnitType e) {
        e.printStackTrace();
      }
    });
  }

  public Formation(User user, ArrayList<Unit> units) {
    this(user, units, true);
  }

  public Formation(User user, ArrayList<Unit> units, boolean copy) {
    this.user = user;

    if (copy) {
      this.units = new ArrayList<>();
      for (Unit unit : units) {
        this.units.add(unit.clone());
      }
    } else {
      this.units = units;
    }
  }

  public int getTotalLevels() {
    int totalLevels = 0;
    for (Unit unit : units) {
      totalLevels += unit.getLevel();
    }
    return totalLevels;
  }

  @Override
  public String toString() {
    return "Formation{"
        + "units=" + units + '}';
  }
}
