package dev.zwazel.autobattler.classes.Utils;

import java.util.HashMap;

public class Grid {
    private final HashMap<Vector, GridCell> grid = new HashMap<>();
    private final int width;
    private final int height;

    public Grid(Vector size) {
        width = size.getX();
        height = size.getY();
        for (int i = 0; i < size.getX(); i++) {
            for (int j = 0; j < size.getY(); j++) {
                grid.put(new Vector(i, j), new GridCell());
            }
        }
    }

    public Vector getGridSize() {
        return new Vector(width, height);
    }

    public HashMap<Vector, GridCell> getGrid() {
        return grid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
