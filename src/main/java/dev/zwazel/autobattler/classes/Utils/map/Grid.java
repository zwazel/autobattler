package dev.zwazel.autobattler.classes.Utils.map;

import dev.zwazel.autobattler.classes.Obstacle;
import dev.zwazel.autobattler.classes.Utils.Vector;

import java.util.Arrays;

public class Grid {
    private final GridCell[][] gridCells;
    private final int width;
    private final int height;

    public Grid(int x, int y) {
        this(new Vector(x, y));
    }

    public Grid(Vector size) {
        width = size.getX();
        height = size.getY();
        gridCells = new GridCell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                gridCells[i][j] = new GridCell(i, j);
            }
        }
    }

    public void updateOccupiedGrid(Vector vector, Obstacle obstacle) {
        gridCells[vector.getX()][vector.getY()].setCurrentObstacle(obstacle);
    }

    public void updateOccupiedGrid(int x, int y, Obstacle obstacle) {
        gridCells[x][y].setCurrentObstacle(obstacle);
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
