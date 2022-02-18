package dev.zwazel.autobattler.classes.utils;

import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.model.FormationEntity;
import dev.zwazel.autobattler.classes.model.User;
import dev.zwazel.autobattler.classes.units.SimpleUnit;
import dev.zwazel.autobattler.classes.utils.database.FormationOnly;

import java.util.ArrayList;
import java.util.List;

public class FormationServiceTemplate {
    private ArrayList<SimpleUnit> units;

    public FormationServiceTemplate() {
    }

    public FormationServiceTemplate(ArrayList<SimpleUnit> units) {
        this.units = units;
    }

    public static List<FormationOnly> getFormationOnlyList(List<FormationEntity> formationEntities) {
        List<FormationOnly> formationOnlyList = new ArrayList<>();
        for (FormationEntity formationEntity : formationEntities) {
            formationOnlyList.add(new FormationOnly() {
                @Override
                public long getId() {
                    return formationEntity.getId();
                }

                @Override
                public String getFormationJson() {
                    return formationEntity.getFormationJson();
                }
            });
        }
        return formationOnlyList;
    }

    public FormationEntity getFormationEntity(User user) throws UnknownUnitType {
        Formation formation = getFormation(user);
        return new FormationEntity(formation);
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
