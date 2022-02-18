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

    @OneToMany
    Set<FormationUnitTable> formationUnits;

    @ManyToOne
    private User user;

    public FormationEntity(Formation formation, User user) {
        this.formationUnits = new HashSet<>();
        for(Unit unit : formation.getUnits()) {
            // TODO: 18.02.2022
        }

        this.user = user;
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

    @Override
    public String toString() {
        return "FormationEntity{" +
                "id=" + id +
                ", user=" + user +
                '}';
    }
}
