package dev.zwazel.autobattler.classes.units;

import dev.zwazel.autobattler.Battler;
import dev.zwazel.autobattler.classes.Utils.Vector;

import java.util.Random;

public class MyFirstUnit extends Unit {
    public MyFirstUnit(long id, int level, String name, Vector position, Vector gridSize, Battler battler) {
        super(id, level, 10, name, "First Unit", 100, 100, 'u', position, gridSize, 1, battler);
    }

    @Override
    public void move(Vector direction) {
        Vector temp = new Vector(this.getGridPosition());
        for (int i = 0; i < this.getBaseSpeed(); i++) {
            temp.add(direction);
            if (this.getBattler().placeOccupied(temp) && !temp.greaterThan(this.getGridSize()) && !temp.smallerThan(new Vector(0,0))) {
                this.setGridPosition(temp);
            } else {
                break;
            }
        }
    }

    @Override
    public void moveRandom() {
        Random rand = new Random();
        int n = rand.nextInt(Vector.DIRECTION.values().length);
        Vector direction = Vector.DIRECTION.values()[n].getDirection();
        System.out.println("direction of unit " + this.getID() + " = " + direction);
        move(direction);
    }
}
