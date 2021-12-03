package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.classes.abilities.Ability;
import dev.zwazel.autobattler.classes.enums.UsageType;

public abstract class Unit {
    private final UsageType MAX_HEALTH = UsageType.HEALTH;
    private final UsageType MAX_MANA = UsageType.MANA;
    private final UsageType MAX_STAMINA = UsageType.STAMINA;

    private final long ID;
    private String name;
    private String description;
    private Ability[] abilities = new Ability[0];

    public Unit(long id, String name, String description, int MAX_HEALTH, int MAX_MANA, int MAX_STAMINA, Ability[] abilities) {
        this(id, name, description, MAX_HEALTH, MAX_MANA, MAX_STAMINA);
        this.abilities = abilities;
    }

    public Unit(long id, String name, String description, int MAX_HEALTH, int MAX_MANA, int MAX_STAMINA) {
        this.ID = id;
        setMAX_HEALTH(MAX_HEALTH);
        setMAX_MANA(MAX_MANA);
        setMAX_STAMINA(MAX_STAMINA);
        this.name = name;
        this.description = description;
    }

    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UsageType getMAX_HEALTH() {
        return MAX_HEALTH;
    }

    public void setMAX_HEALTH(int MAX_HEALTH) {
        this.MAX_HEALTH.setAmount(MAX_HEALTH);
    }

    public UsageType getMAX_MANA() {
        return MAX_MANA;
    }

    public void setMAX_MANA(int MAX_MANA) {
        this.MAX_MANA.setAmount(MAX_MANA);
    }

    public UsageType getMAX_STAMINA() {
        return MAX_STAMINA;
    }

    public void setMAX_STAMINA(int MAX_STAMINA) {
        this.MAX_STAMINA.setAmount(MAX_STAMINA);
    }

    public Ability[] getAbilities() {
        return abilities;
    }

    public void setAbilities(Ability[] abilities) {
        this.abilities = abilities;
    }

    public void setAbility(Ability ability, int index) {
        this.abilities[index] = ability;
    }
}
