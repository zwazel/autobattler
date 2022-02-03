package dev.zwazel.autobattler.demo;

import dev.zwazel.autobattler.classes.units.SimpleWall;
import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.utils.map.FindPath;
import dev.zwazel.autobattler.classes.utils.map.Grid;
import dev.zwazel.autobattler.classes.utils.map.GridGraph;
import dev.zwazel.autobattler.classes.utils.map.Node;

public class TestPath {
    public static void main(String[] args) {
        Grid grid = new Grid(new Vector(5, 5));

        grid.getGridCells()[1][0].setCurrentObstacle(new SimpleWall());
        grid.getGridCells()[1][1].setCurrentObstacle(new SimpleWall());
        grid.getGridCells()[1][2].setCurrentObstacle(new SimpleWall());
        grid.getGridCells()[1][3].setCurrentObstacle(new SimpleWall());
//        grid.getGridCells()[1][4].setCurrentObstacle(new SimpleWall());
        grid.getGridCells()[3][3].setCurrentObstacle(new SimpleWall());
        grid.getGridCells()[3][4].setCurrentObstacle(new SimpleWall());
//        grid.getGridCells()[4][3].setCurrentObstacle(new SimpleWall());

        Vector start = new Vector(0, 0);
        Vector end = new Vector(4, 4);

        FindPath findPath = new FindPath();
        Node[] path = findPath.findPath(start, end, new GridGraph(grid));

        System.out.println("PATH");
        if (path.length <= 0) {
            System.out.println("no path found");
        }
        System.out.println("PATH VISUALIZED");
        drawGrid(grid, path, start, end);
    }

    private static void drawGrid(Grid grid, Node[] path, Vector start, Vector end) {
        StringBuilder vertical = new StringBuilder();
        vertical.append("-".repeat((grid.getWidth()) * 4 + 1));

        Vector gridPositionNow = new Vector(0, 0);

        for (int row = 0; row < grid.getHeight(); row++) {
            System.out.println();
            System.out.println(vertical);

            gridPositionNow.setY(row);
            for (int column = 0; column < grid.getWidth(); column++) {
                String character = " ";
                gridPositionNow.setX(column);

                if (gridPositionNow.equals(start)) {
                    character = "O";
                } else if (gridPositionNow.equals(end)) {
                    character = "X";
                } else {
                    for (Node node : path) {
                        Vector nodePos = node.getMyGridCell().getPosition();
                        if (nodePos.equals(gridPositionNow)) {
                            character = "~";
                            break;
                        }
                    }
                }

                if (grid.getGridCells()[gridPositionNow.getX()][gridPositionNow.getY()].getCurrentObstacle() != null) {
                    character = "/";
                }

                System.out.print("|" + " " + character + " ");
            }
            System.out.print("|");
        }
        System.out.println();
        System.out.println(vertical);
    }
}
