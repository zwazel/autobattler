package dev.zwazel.autobattler.classes.utils.database;

import dev.zwazel.autobattler.classes.utils.Formation;
import dev.zwazel.autobattler.classes.utils.User;
import dev.zwazel.autobattler.classes.utils.json.HistoryToJson;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;

@EnableAutoConfiguration
@Entity
@Table(name = "formation")
public class FormationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "formation")
    private String formationJson;

    @ManyToOne
    private User user;

    public FormationEntity(String formationJson, User user) {
        this.formationJson = formationJson;
        this.user = user;
    }

    public FormationEntity(Formation formation, User user) {
        this.formationJson = HistoryToJson.formationToJson(formation);
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

    public String getFormationJson() {
        return formationJson;
    }

    public void setFormationJson(String formationJson) {
        this.formationJson = formationJson;
    }
}
