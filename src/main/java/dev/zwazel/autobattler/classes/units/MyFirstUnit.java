package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.classes.abilities.Ability;

public class MyFirstUnit extends Unit {
    public MyFirstUnit(long id, String name, String description, int MAX_HEALTH, int MAX_MANA, int MAX_STAMINA, Ability[] abilities) {
        super(id, name, description, MAX_HEALTH, MAX_MANA, MAX_STAMINA, abilities);
    }

    public MyFirstUnit(long id, String name, String description, int MAX_HEALTH, int MAX_MANA, int MAX_STAMINA) {
        super(id, name, description, MAX_HEALTH, MAX_MANA, MAX_STAMINA);
    }
}
