package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.classes.Utils.Vector;

import java.util.Random;

public class MyFirstUnit extends Unit {
    public MyFirstUnit(long id, int level, String name, Vector position, Vector gridSize) {
        super(id, level, 10, name, "First Unit", 100, 100, 'u', position, gridSize, 1);
    }

    @Override
    public boolean move(Vector direction) {
        Vector temp = this.getGridPosition();
        direction.multiply(this.getBaseSpeed());
        temp.add(direction);
        if(!temp.greaterThan(this.getGridSize())) {
            this.getGridPosition().add(temp);
        }

        return false;
    }

    @Override
    public boolean moveRandom() {
        Random rand = new Random();
        int n = rand.nextInt(Vector.DIRECTION.values().length);
        return move(Vector.DIRECTION.values()[n].getDirection());
    }
}
