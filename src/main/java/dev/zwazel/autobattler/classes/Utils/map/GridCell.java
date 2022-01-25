package dev.zwazel.autobattler.classes.Utils.map;

import dev.zwazel.autobattler.classes.Obstacle;

public class GridCell {
    private Obstacle currentObstacle = null;

    public GridCell() {

    }

    public GridCell(Obstacle obstacle) {
        this.currentObstacle = obstacle;
    }

    public Obstacle getCurrentObstacle() {
        return currentObstacle;
    }

    public void setCurrentObstacle(Obstacle currentObstacle) {
        this.currentObstacle = currentObstacle;
    }

    @Override
    public String toString() {
        return "GridCell{" +
                "currentObstacle=" + currentObstacle +
                '}';
    }
}
