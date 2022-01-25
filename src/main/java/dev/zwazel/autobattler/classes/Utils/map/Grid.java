package dev.zwazel.autobattler.classes.Utils.map;

import dev.zwazel.autobattler.classes.Utils.Vector;

import java.util.Arrays;

public class Grid {
    private final GridCell[][] gridCells;
    private final int width;
    private final int height;

    public Grid(Vector size) {
        width = size.getX();
        height = size.getY();
        gridCells = new GridCell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                gridCells[i][j] = new GridCell(i, j);
            }
        }

        System.out.println(new GridGraph(this));
    }

    public Vector getGridSize() {
        return new Vector(width, height);
    }

    public GridCell[][] getGridCells() {
        return gridCells;
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
                "gridCells=" + Arrays.deepToString(gridCells) +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
