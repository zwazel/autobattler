package dev.zwazel.autobattler.classes.Utils.map;

import java.util.Arrays;
import java.util.List;

public class GridGraph {
    private final Node[][] nodes;

    public GridGraph(Grid grid) {
        int cols = grid.getWidth();
        int rows = grid.getHeight();

        nodes = new Node[cols][rows];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                nodes[i][j] = new Node(grid.getGridCells()[i][j]);
            }
        }

        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                Node n = nodes[c][r];
                List<Node> neighbors = n.getMyNeighbors();
                if (c > 0) {     // has north
                    neighbors.add(nodes[c - 1][r]);
                }
                if (c < cols - 1) { // has south
                    neighbors.add(nodes[c + 1][r]);
                }
                if (r > 0) {     // has west
                    neighbors.add(nodes[c][r - 1]);
                }
                if (r < rows - 1) { // has east
                    neighbors.add(nodes[c][r + 1]);
                }
            }
        }
    }

    public Node[][] getNodes() {
        return nodes;
    }

    @Override
    public String toString() {
        return "GridGraph{" +
                "nodes=" + Arrays.toString(nodes) +
                '}';
    }
}
