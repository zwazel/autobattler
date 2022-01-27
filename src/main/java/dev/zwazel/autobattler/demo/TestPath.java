package dev.zwazel.autobattler.demo;

import dev.zwazel.autobattler.classes.Utils.Vector;
import dev.zwazel.autobattler.classes.Utils.map.FindPath;
import dev.zwazel.autobattler.classes.Utils.map.Grid;
import dev.zwazel.autobattler.classes.Utils.map.GridGraph;
import dev.zwazel.autobattler.classes.Utils.map.Node;
import dev.zwazel.autobattler.classes.units.SimpleWall;

import java.util.ArrayList;

public class TestPath {
    public static void main(String[] args) {
        Grid grid = new Grid(new Vector(5, 5));

        grid.getGridCells()[1][0].setCurrentObstacle(new SimpleWall());
        grid.getGridCells()[1][1].setCurrentObstacle(new SimpleWall());
//        grid.getGridCells()[1][2].setCurrentObstacle(new SimpleWall());
        grid.getGridCells()[1][3].setCurrentObstacle(new SimpleWall());
        grid.getGridCells()[1][4].setCurrentObstacle(new SimpleWall());
        grid.getGridCells()[3][3].setCurrentObstacle(new SimpleWall());
        grid.getGridCells()[4][3].setCurrentObstacle(new SimpleWall());

        Vector start = new Vector(0, 0);
        Vector end = new Vector(4, 4);
        ArrayList<Node> nodes = new ArrayList<>();

        FindPath findPath = new FindPath();
        Node node = findPath.findPath(start, end, new GridGraph(grid));

        System.out.println("PATH");
        if (node == null) {
            System.out.println("no path found");
        } else {
            nodes.add(node);
            while (node != null) {
                node = node.getPredecessor();
                if (node != null) {
                    nodes.add(node);
                }
            }
        }
        System.out.println("PATH VISUALIZED");
        drawGrid(grid, nodes, start, end);
    }

    private static void drawGrid(Grid grid, ArrayList<Node> nodes, Vector start, Vector end) {
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
                    for (int i = nodes.size() - 2; i >= 0; i--) {
                        Node node = nodes.get(i);
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
