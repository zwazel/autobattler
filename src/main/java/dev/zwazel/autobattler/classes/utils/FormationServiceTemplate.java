package dev.zwazel.autobattler.classes.utils;

import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.enums.UnitTypes;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.model.FormationEntity;
import dev.zwazel.autobattler.classes.model.UnitModel;
import dev.zwazel.autobattler.classes.model.User;
import dev.zwazel.autobattler.classes.units.SimpleUnit;
import dev.zwazel.autobattler.classes.utils.database.FormationOnly;
import dev.zwazel.autobattler.classes.utils.database.UnitOnly;
import dev.zwazel.autobattler.classes.utils.database.repositories.UnitModelRepository;
import javassist.NotFoundException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class FormationServiceTemplate {
    private ArrayList<SimpleUnit> units;

    public FormationServiceTemplate() {
    }

    public FormationServiceTemplate(ArrayList<SimpleUnit> units) {
        this.units = units;
    }

    public static List<UnitOnly> getUnitOnlyList(List<UnitModel> unitModels) {
        List<UnitOnly> unitOnlyList = new ArrayList<>();
        for (UnitModel unitModel : unitModels) {
            unitOnlyList.add(new UnitOnly() {
                @Override
                public long getId() {
                    return unitModel.getId();
                }

                @Override
                public String getName() {
                    return unitModel.getName();
                }

                @Override
                public int getLevel() {
                    return unitModel.getLevel();
                }

                @Override
                public UnitTypes getUnitType() {
                    return unitModel.getUnitType();
                }

                @Override
                public boolean isCustomNamesAllowed() {
                    return unitModel.isCustomNamesAllowed();
                }
            });
        }
        return unitOnlyList;
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

    public FormationEntity getFormationEntity(User user, UnitModelRepository unitModelRepository) throws UnknownUnitType, NotFoundException {
        for (SimpleUnit simpleUnit : this.units) {
            Optional<UnitModel> unitModel = unitModelRepository.findById(simpleUnit.getId());
            if (unitModel.isPresent()) {
                simpleUnit.setUnitType(unitModel.get().getUnitType());

                if (!unitModel.get().getName().equals(simpleUnit.getName())) {
                    unitModel.get().setName(simpleUnit.getName());
                    unitModelRepository.save(unitModel.get());
                }
            } else {
                throw new NotFoundException("UnitModel with id " + simpleUnit.getId() + " not found");
            }
        }

        Formation formation = getFormation(user);
        return new FormationEntity(formation);
    }

    public Formation getFormation(User user) throws UnknownUnitType {
        ArrayList<Unit> units = new ArrayList<>();
        for (SimpleUnit simpleUnit : this.units) {
            if (simpleUnit.getUnitType() == null) {
                throw new UnknownUnitType(simpleUnit.getUnitType());
            }
            units.add(simpleUnit.getUnit());
        }

        return new Formation(user, units);
    }

    @Override
    public String toString() {
        return "FormationServiceTemplate{" +
                "units=" + units +
                '}';
    }
}
