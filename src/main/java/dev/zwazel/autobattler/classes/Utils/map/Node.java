package dev.zwazel.autobattler.classes.Utils.map;

import java.util.LinkedList;

public class Node {
    private final GridCell myGridCell;
    private final LinkedList<Node> myNeighbors;

    public Node(GridCell myGridCell) {
        this.myGridCell = myGridCell;
        this.myNeighbors = new LinkedList<>();
    }

    public GridCell getMyGridCell() {
        return myGridCell;
    }

    public LinkedList<Node> getMyNeighbors() {
        return myNeighbors;
    }

    @Override
    public String toString() {
        return "Node{" +
                "myGridCell=" + myGridCell +
                ", myNeighbors=" + myNeighbors +
                '}';
    }
}
