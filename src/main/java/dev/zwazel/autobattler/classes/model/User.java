package dev.zwazel.autobattler.classes.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EnableAutoConfiguration
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<FormationEntity> formations;

    @ManyToMany
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<UserRole> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UnitModel> units;

    private Date lastLogin;

    private Date accountCreated;

    public User() {
        accountCreated = new Date();
        this.units = new HashSet<>();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        accountCreated = new Date();
        this.units = new HashSet<>();
    }

    public void addFormation(FormationEntity formation) {
        formations.add(formation);
    }

    public void addUnit(UnitModel unit) {
        units.add(unit);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
