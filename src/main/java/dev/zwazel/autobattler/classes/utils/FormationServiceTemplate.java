package dev.zwazel.autobattler.classes.utils;

import dev.zwazel.autobattler.classes.units.SimpleUnit;

import java.util.ArrayList;

public class FormationServiceTemplate {
    private ArrayList<SimpleUnit> units;

    public FormationServiceTemplate() {
    }

    public FormationServiceTemplate(ArrayList<SimpleUnit> units) {
        this.units = units;
    }

    public ArrayList<SimpleUnit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<SimpleUnit> units) {
        this.units = units;
    }
}
