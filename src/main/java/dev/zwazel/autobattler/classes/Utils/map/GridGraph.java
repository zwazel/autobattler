package dev.zwazel.autobattler.classes.Utils.map;

import java.util.Arrays;
import java.util.List;

public class GridGraph {
    private final Node[][] nodes;

    public GridGraph(Grid grid) {
        int rows = grid.getHeight();
        int cols = grid.getWidth();

        nodes = new Node[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                nodes[i][j] = new Node(grid.getGridCells()[i][j]);
            }
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Node n = nodes[r][c];
                List<Node> neighbors = n.getMyNeighbors();
                if (r > 0) {     // has north
                    neighbors.add(nodes[r - 1][c]);
                }
                if (r < rows - 1) { // has south
                    neighbors.add(nodes[r + 1][c]);
                }
                if (c > 0) {     // has west
                    neighbors.add(nodes[r][c - 1]);
                }
                if (c < cols - 1) { // has east
                    neighbors.add(nodes[r][c + 1]);
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
