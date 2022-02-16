package dev.zwazel.autobattler.classes.utils;

import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.units.SimpleUnit;
import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.utils.database.FormationEntity;
import dev.zwazel.autobattler.classes.utils.json.HistoryToJson;

import java.util.ArrayList;

public class FormationServiceTemplate {
    private ArrayList<SimpleUnit> units;

    public FormationServiceTemplate() {
    }

    public FormationServiceTemplate(ArrayList<SimpleUnit> units) {
        this.units = units;
    }

    public FormationEntity getFormationEntity(User user) throws UnknownUnitType {
        Formation formation = getFormation(user);
        FormationEntity formationEntity = new FormationEntity();
        String formationjson = HistoryToJson.formationToJson(formation);

        formationEntity.setFormationJson(formationjson);
        formationEntity.setUser(user);
        return formationEntity;
    }

    public Formation getFormation(User user) throws UnknownUnitType {
        ArrayList<Unit> units = new ArrayList<>();
        for (SimpleUnit simpleUnit : this.units) {
            units.add(simpleUnit.getUnit());
        }

        return new Formation(user, units);
    }

    public ArrayList<SimpleUnit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<SimpleUnit> units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return "FormationServiceTemplate{" +
                "units=" + units +
                '}';
    }
}
