package dev.zwazel.autobattler.classes.model;

import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.utils.Formation;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EnableAutoConfiguration
@Entity
@Table(name = "formation")
public class FormationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private User user;

    @OneToMany
    private Set<FormationUnitTable> formationUnitTable;

    public FormationEntity(Formation formation) {
        this.user = formation.getUser();

        this.formationUnitTable = new HashSet<>();
        for (Unit unit : formation.getUnits()) {
            FormationUnitTable formationUnitTable = new FormationUnitTable(this, unit);
            this.formationUnitTable.add(formationUnitTable);
        }
    }

    public FormationEntity() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<FormationUnitTable> getFormationUnitTable() {
        return formationUnitTable;
    }

    public void setFormationUnitTable(Set<FormationUnitTable> formationUnitTable) {
        this.formationUnitTable = formationUnitTable;
    }

    @Override
    public String toString() {
        return "FormationEntity{" +
                "id=" + id +
                ", user=" + user +
                '}';
    }
}
