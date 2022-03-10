package dev.zwazel.autobattler.classes.model;

import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.enums.UnitTypes;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.units.SimpleUnit;
import dev.zwazel.autobattler.classes.utils.Vector;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@Entity
public class UnitModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Size(min = 1, max = 12)
    private String name;

    private int level;

    @Enumerated(EnumType.STRING)
    private UnitTypes unitType;

    private Date dateCollected;

    public UnitModel() {
        this.dateCollected = new Date();
    }

    public UnitModel(Unit unit) {
        this.id = unit.getID();
        this.name = unit.getName();
        this.level = unit.getLevel();
        this.unitType = unit.getType();
        this.dateCollected = new Date();
    }

    public UnitModel(String name, int level, UnitTypes unitType, User user) {
        this.name = name;
        this.level = level;
        this.unitType = unitType;
        this.user = user;
        this.dateCollected = new Date();
    }

    public UnitModel(int level, UnitTypes unitType, User user) {
        this(unitType.getDefaultName(), level, unitType, user);
    }

    public void setLevel(int level) {
        if (level > 0) {
            this.level = level;
        }
    }

    public Unit getUnit(int priority, Vector position) throws UnknownUnitType {
        return new SimpleUnit(this, priority, position).getUnit();
    }

    public boolean isCustomNamesAllowed() {
        return unitType.isCustomNamesAllowed();
    }

    @Override
    public String toString() {
        return "UnitModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", unitType=" + unitType +
                '}';
    }
}
