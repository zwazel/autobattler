package dev.zwazel.autobattler.classes.Utils;

import dev.zwazel.autobattler.classes.units.Unit;

import java.util.ArrayList;

public class Formation {
    private final User user;
    private final ArrayList<Unit> units;

    public Formation(User user, ArrayList<Unit> units) {
        this.user = user;
        this.units = units;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "units=" + units +
                '}';
    }
}
