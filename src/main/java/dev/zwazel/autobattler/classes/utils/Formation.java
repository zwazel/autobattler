package dev.zwazel.autobattler.classes.utils;

import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.model.UnitModel;
import dev.zwazel.autobattler.classes.model.User;

import java.util.ArrayList;
import java.util.Set;

public class Formation {
    private final User user;

    private final ArrayList<Unit> units;

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

    public Formation(User user, Set<UnitModel> units) {
        this(user, new ArrayList<>());
        for (UnitModel unitModel : units) {
            this.units.add(unitModel.getUnit());
        }
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "units=" + units +
                '}';
    }
}
