package dev.zwazel.autobattler.classes.model;

import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.enums.UnitTypes;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.units.SimpleUnit;
import dev.zwazel.autobattler.classes.utils.Vector;

import javax.persistence.*;
import java.util.Set;

@Entity
public class UnitModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    private Set<FormationUnitTable> formationUnitTables;

    private String name;

    private int level;

    @Enumerated(EnumType.STRING)
    private UnitTypes unitType;

    public UnitModel() {
    }

    public UnitModel(Unit unit) {
        this.id = unit.getID();
        this.name = unit.getName();
        this.level = unit.getLevel();
        this.unitType = unit.getType();
    }

    public Unit getUnit(int priority, Vector position) throws UnknownUnitType {
        return new SimpleUnit(this, priority, position).getUnit();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public UnitTypes getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitTypes unitType) {
        this.unitType = unitType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
