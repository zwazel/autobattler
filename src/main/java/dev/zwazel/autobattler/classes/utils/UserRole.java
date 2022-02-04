package dev.zwazel.autobattler.classes.utils;

import javax.persistence.*;

@Entity
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    private EnumUserRole name;

    public UserRole(EnumUserRole name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EnumUserRole getName() {
        return name;
    }

    public void setName(EnumUserRole name) {
        this.name = name;
    }
}
