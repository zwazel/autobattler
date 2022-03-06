package dev.zwazel.autobattler.classes.model;

import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.utils.Vector;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class FormationUnitTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "formation_unit_table", nullable = false)
    private Long id;

    @ManyToOne
    private UnitModel unitModel;

    private int unitPriority;

    private int positionX;

    private int positionY;

    @ManyToOne(cascade = CascadeType.ALL)
    private FormationEntity formation;

    public FormationUnitTable() {
    }

    public FormationUnitTable(Unit unit, FormationEntity formation) {
        this.unitModel = unit.getUnitModel();
        this.unitPriority = unit.getPriority();
        this.positionX = unit.getGridPosition().getX();
        this.positionY = unit.getGridPosition().getY();
        this.formation = formation;
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

    public Vector getUnitPosition() {
        return new Vector(positionX, positionY);
    }

    public void setUnitPosition(Vector position) {
        this.positionX = position.getX();
        this.positionY = position.getY();
    }

    public void update(FormationUnitTable formationUnitTable) {
        this.unitPriority = formationUnitTable.getUnitPriority();
        this.positionX = formationUnitTable.getPositionX();
        this.positionY = formationUnitTable.getPositionY();
    }

    @Override
    public String toString() {
        return "FormationUnitTable{" +
                "id=" + id +
                ", unitModel=" + unitModel +
                ", unitPriority=" + unitPriority +
                ", positionX=" + positionX +
                ", positionY=" + positionY +
                '}';
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

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public FormationEntity getFormation() {
        return formation;
    }

    public void setFormation(FormationEntity formation) {
        this.formation = formation;
    }
}
