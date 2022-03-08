package dev.zwazel.autobattler.classes.model;

import dev.zwazel.autobattler.classes.utils.EnumUserRole;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserRole {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;

  @Enumerated(EnumType.STRING) private EnumUserRole name;

  public UserRole(EnumUserRole name) { this.name = name; }

  public UserRole() {}

  @Override
  public String toString() {
    return "UserRole{"
        + "name=" + name + '}';
  }
}
