package dev.zwazel.autobattler.classes.model;

import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.utils.Formation;
import dev.zwazel.autobattler.classes.utils.json.HistoryToJson;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EnableAutoConfiguration
@Entity
@Table(name = "formation")
public class FormationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<FormationUnitTable> formationUnitTable;

    public FormationEntity(Formation formation) {
        this.user = formation.getUser();

        this.formationUnitTable = new HashSet<>();
        for (Unit unit : formation.getUnits()) {
            FormationUnitTable formationUnitTable = new FormationUnitTable(unit);
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

    public String getFormationJson() {
        return HistoryToJson.formationToJson(new Formation(this));
    }

    public int getAmountUnits() {
        return formationUnitTable.size();
    }

    public int getMinLevel() {
        int minLevel = Integer.MAX_VALUE;
        for (FormationUnitTable formationUnitTable : this.formationUnitTable) {
            if (formationUnitTable.getUnitModel().getLevel() < minLevel) {
                minLevel = formationUnitTable.getUnitModel().getLevel();
            }
        }
        return minLevel;
    }

    public int getMaxLevel() {
        int maxLevel = Integer.MIN_VALUE;
        for (FormationUnitTable formationUnitTable : this.formationUnitTable) {
            if (formationUnitTable.getUnitModel().getLevel() > maxLevel) {
                maxLevel = formationUnitTable.getUnitModel().getLevel();
            }
        }
        return maxLevel;
    }

    public float getAverageLevel() {
        int totalLevel = 0;
        for (FormationUnitTable formationUnitTable : this.formationUnitTable) {
            totalLevel += formationUnitTable.getUnitModel().getLevel();
        }
        return (float) totalLevel / this.formationUnitTable.size();
    }

    public int getTotalLevel() {
        int totalLevel = 0;
        for (FormationUnitTable formationUnitTable : this.formationUnitTable) {
            totalLevel += formationUnitTable.getUnitModel().getLevel();
        }
        return totalLevel;
    }

    @Override
    public String toString() {
        return "FormationEntity{" +
                "id=" + id +
                ", user=" + user +
                ", formationUnitTable=" + formationUnitTable +
                '}';
    }
}
