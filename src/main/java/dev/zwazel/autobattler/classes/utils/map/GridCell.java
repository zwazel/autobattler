package dev.zwazel.autobattler.classes.utils.map;

import dev.zwazel.autobattler.classes.Obstacle;
import dev.zwazel.autobattler.classes.utils.Vector;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GridCell {
    private final Vector position;
    private Obstacle currentObstacle = null;

    public GridCell(Vector vector) {
        this.position = vector;
    }

    public GridCell(int x, int y) {
        this.position = new Vector(x, y);
    }

    @Override
    public String toString() {
        return "GridCell{" +
                "position=" + position +
                '}';
    }
}
