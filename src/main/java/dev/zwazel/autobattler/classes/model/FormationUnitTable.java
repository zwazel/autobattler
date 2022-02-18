package dev.zwazel.autobattler.classes.model;

import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.utils.Vector;

import javax.persistence.*;

@Entity
public class FormationUnitTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "formation_unit_table", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "formation_entity_id")
    private FormationEntity formationEntity;

    @ManyToOne
    private UnitModel unitModel;

    private int unitPriority;

    private int positionX;

    private int positionY;

    public FormationUnitTable() {
    }

    public FormationUnitTable(FormationEntity formationEntity, Unit unit) {
        this.formationEntity = formationEntity;

        this.unitModel = unit.getUnitModel();
        this.unitPriority = unit.getPriority();
        this.positionX = unit.getGridPosition().getX();
        this.positionY = unit.getGridPosition().getY();
    }

    public FormationEntity getFormationEntity() {
        return formationEntity;
    }

    public void setFormationEntity(FormationEntity formationEntity) {
        this.formationEntity = formationEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Unit getUnit() throws UnknownUnitType {
        return unitModel.getUnit(this.getUnitPriority(), this.getUnitPosition());
    }

    public UnitModel getUnitModel() {
        return unitModel;
    }

    public void setUnitModel(UnitModel unitModel) {
        this.unitModel = unitModel;
    }

    public int getUnitPriority() {
        return unitPriority;
    }

    public void setUnitPriority(int unitPriority) {
        this.unitPriority = unitPriority;
    }

    public Vector getUnitPosition() {
        return new Vector(positionX, positionY);
    }

    public void setUnitPosition(Vector position) {
        this.positionX = position.getX();
        this.positionY = position.getY();
    }
}
