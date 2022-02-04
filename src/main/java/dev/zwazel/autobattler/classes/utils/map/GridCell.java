package dev.zwazel.autobattler.classes.utils.map;

import dev.zwazel.autobattler.classes.Obstacle;
import dev.zwazel.autobattler.classes.utils.Vector;

public class GridCell {
    private final Vector position;
    private Obstacle currentObstacle = null;

    public GridCell(Vector vector) {
        this.position = vector;
    }

    public GridCell(int x, int y) {
        this.position = new Vector(x, y);
    }

    public Obstacle getCurrentObstacle() {
        return currentObstacle;
    }

    public void setCurrentObstacle(Obstacle currentObstacle) {
        this.currentObstacle = currentObstacle;
    }

    public Vector getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "GridCell{" +
                "position=" + position +
                '}';
    }
}
