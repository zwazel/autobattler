package dev.zwazel.autobattler.classes.model;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addFormation(FormationEntity formation) {
        formations.add(formation);
    }

    public Set<FormationEntity> getFormations() {
        return formations;
    }

    public void setFormations(Set<FormationEntity> formations) {
        this.formations = formations;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getAccountCreated() {
        return accountCreated;
    }

    public void setAccountCreated(Date accountCreated) {
        this.accountCreated = accountCreated;
    }

    public void addUnit(UnitModel unit) {
        units.add(unit);
    }

    public Set<UnitModel> getUnits() {
        return units;
    }

    public void setUnits(Set<UnitModel> units) {
        this.units = units;
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
