package dev.zwazel.autobattler.demo;

import dev.zwazel.autobattler.classes.Utils.Vector;
import dev.zwazel.autobattler.classes.Utils.map.FindPath;
import dev.zwazel.autobattler.classes.Utils.map.Grid;
import dev.zwazel.autobattler.classes.Utils.map.GridGraph;
import dev.zwazel.autobattler.classes.Utils.map.Node;

import java.util.ArrayList;

public class TestPath {
    public static void main(String[] args) {
        Grid grid = new Grid(new Vector(5, 5));
        Vector start = new Vector(0, 1);
        Vector end = new Vector(2, 4);
        ArrayList<Node> nodes = new ArrayList<>();

        FindPath findPath = new FindPath();
        Node node = findPath.findPath(start, end, new GridGraph(grid));

        int counter = 0;
        System.out.println("PATH");
        if (node == null) {
            System.out.println("no path found");
        } else {
            nodes.add(node);
            boolean done = false;
            while (!done) {
                nodes.add(node.getPredecessor());
                if (node.getPredecessor() == null) {
                    done = true;
                } else {
                    node = node.getPredecessor();
                }
            }

            for (int i = nodes.size() - 2; i >= 0; i--) {
//                System.out.println("i = " + i);
                System.out.print(nodes.get(i).getMyGridCell().getPosition() + "->");
            }
        }
        System.out.println("PATH VISUALIZED");
        drawGrid(grid, nodes, start, end);
    }

    private static void drawGrid(Grid grid, ArrayList<Node> nodes, Vector start, Vector end) {
        StringBuilder vertical = new StringBuilder();
        vertical.append("-".repeat((grid.getWidth() + 1) * 4 + 1));

        Vector gridPositionNow = new Vector(0, 0);

        for (int row = 0; row <= grid.getHeight(); row++) {
            System.out.println();
            System.out.println(vertical);

            for (int column = 0; column <= grid.getWidth(); column++) {
                String character = " ";
                gridPositionNow.setX(column);
                gridPositionNow.setY(row);

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

                System.out.print("|" + " " + character + " ");
            }
            System.out.print("|");
        }
        System.out.println();
        System.out.println(vertical);
    }
}
