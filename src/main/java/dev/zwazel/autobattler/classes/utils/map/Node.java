package dev.zwazel.autobattler.classes.utils.map;

import java.util.LinkedList;

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

    public GridCell getMyGridCell() {
        return myGridCell;
    }

    public LinkedList<Node> getMyNeighbors() {
        return myNeighbors;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public String toString() {
        return "Node{" +
                "myGridCell=" + myGridCell +
                ", cost=" + cost +
                '}';
    }
}
