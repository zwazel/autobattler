package dev.zwazel.autobattler.classes.utils.database;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import dev.zwazel.autobattler.classes.utils.User;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

@Entity
@Table(name = "formation")
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class)
        ,
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class FormationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Type(type = "jsonb")
    @Column(name = "formation", columnDefinition = "jsonb")
    private String formationJson;

    @ManyToOne
    private User user;

    public FormationEntity(String formationJson, User user) {
        this.formationJson = formationJson;
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
