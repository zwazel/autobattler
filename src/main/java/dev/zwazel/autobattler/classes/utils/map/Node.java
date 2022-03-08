package dev.zwazel.autobattler.classes.utils.map;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

@Getter
@Setter
public class Node {
    private final GridCell myGridCell;
    private final LinkedList<Node> myNeighbors;
    private Node predecessor;
    private double cost;

    public Node(GridCell myGridCell) {
        this.myGridCell = myGridCell;
        this.myNeighbors = new LinkedList<>();
    }

    public int countHowManyPredecessors() {
        Node node = this;
        int counter = 0;
        while(node.getPredecessor() != null) {
            counter++;
            node = node.getPredecessor();
        }
        return counter;
    }

    @Override
    public String toString() {
        return "Node{" +
                "myGridCell=" + myGridCell +
                ", cost=" + cost +
                '}';
    }
}
