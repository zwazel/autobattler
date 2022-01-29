package dev.zwazel.autobattler.classes.Utils;

import dev.zwazel.autobattler.classes.units.Unit;

import java.util.ArrayList;

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
