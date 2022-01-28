package dev.zwazel.autobattler.classes.Utils;

import dev.zwazel.autobattler.classes.units.Unit;

import java.util.ArrayList;

public record Formation(User user,
                        ArrayList<Unit> units) {

    @Override
    public String toString() {
        return "Formation{" +
                "units=" + units +
                '}';
    }
}
