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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "formation_unit_table", nullable = false)
    private Long id;

    @ManyToOne
    private UnitModel unitModel;

    private int unitPriority;

    private int positionX;

    private int positionY;

    public FormationUnitTable() {
    }

    public FormationUnitTable(Unit unit) {
        this.unitModel = unit.getUnitModel();
        this.unitPriority = unit.getPriority();
        this.positionX = unit.getGridPosition().getX();
        this.positionY = unit.getGridPosition().getY();
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
}
