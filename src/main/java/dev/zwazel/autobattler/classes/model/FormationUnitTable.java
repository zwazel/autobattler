package dev.zwazel.autobattler.classes.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class FormationUnitTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "formation_unit_table", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "formation_entity_id")
    private FormationEntity formationEntity;

    @OneToMany
    private Set<UnitModel> unitModelSet;

    private int unitPriority;

    private int positionX;

    private int positionY;

    public FormationEntity getFormationEntity() {
        return formationEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
