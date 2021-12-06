package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.classes.abilities.Ability;

public class MyFirstUnit extends Unit {
    public MyFirstUnit(long id, int level, String name) {
        super(id, level, 10, name, "First Unit", 100, 100, 'u');
    }
}
