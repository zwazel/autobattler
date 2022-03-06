package dev.zwazel.autobattler.classes.model;

import dev.zwazel.autobattler.classes.enums.UserLoginInfos;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(min = UserLoginInfos.MIN_USERNAME_LENGTH, max = UserLoginInfos.MAX_USERNAME_LENGTH)
    @Column(length = UserLoginInfos.MAX_USERNAME_LENGTH, unique = true)
    private String username;

    @NotNull
    @Size(min = UserLoginInfos.MIN_PASSWORD_LENGTH, max = UserLoginInfos.MAX_PASSWORD_LENGTH)
    @Column(length = UserLoginInfos.MAX_PASSWORD_LENGTH)
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
        lastLogin = accountCreated;
        this.units = new HashSet<>();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        accountCreated = new Date();
        lastLogin = accountCreated;
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
                "username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }
}
