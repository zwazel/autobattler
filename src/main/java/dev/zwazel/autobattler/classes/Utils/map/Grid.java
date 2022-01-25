package dev.zwazel.autobattler.classes.Utils.map;

import dev.zwazel.autobattler.classes.Utils.Vector;

import java.util.HashMap;

public class Grid {
    private final HashMap<Long, GridCell> grid = new HashMap<>();
    private final int width;
    private final int height;

    public Grid(Vector size) {
        width = size.getX();
        height = size.getY();
        for (int i = 0; i < size.getX(); i++) {
            for (int j = 0; j < size.getY(); j++) {
                grid.put(new Vector(i, j).toNumberId(), new GridCell());
            }
        }
    }

    public Vector getGridSize() {
        return new Vector(width, height);
    }

    public HashMap<Long, GridCell> getGrid() {
        return grid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Grid{" +
                "grid=" + grid +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
