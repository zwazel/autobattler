package dev.zwazel.autobattler.classes.Utils;

import dev.zwazel.autobattler.classes.units.Unit;

import java.util.ArrayList;

public class Formation {
    private final ArrayList<Unit> units;

    public Formation(ArrayList<Unit> units) {
        this.units = units;
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
