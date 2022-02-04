package dev.zwazel.autobattler.classes.utils.map;

import dev.zwazel.autobattler.classes.utils.Vector;

import java.util.HashSet;
import java.util.PriorityQueue;

public class FindPath {
    PriorityQueue<Node> openList = new PriorityQueue<>(1, new NodeComparator());
    HashSet<Node> closedList = new HashSet<>();

    public Node[] findPath(Vector start, Vector end, GridGraph grid) {
        Node node = grid.getNodes()[start.getX()][start.getY()];
        node.setCost(0);
        openList.add(node);

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            if (currentNode.getMyGridCell().getPosition().equals(end)) {
                return getPathInCorrectOrder(currentNode, true);
            }
            closedList.add(currentNode);
            expandNode(currentNode, end);
        }

        return new Node[0];
    }

    private void expandNode(Node currentNode, Vector end) {
        for (Node successor : currentNode.getMyNeighbors()) {
            if (closedList.contains(successor) || successor.getMyGridCell().getCurrentObstacle() != null) {
                continue;
            }

            double tentativeCost = currentNode.getCost() + (currentNode.getMyGridCell().getPosition().getDistanceFrom(successor.getMyGridCell().getPosition()));

            if (openList.contains(successor) && tentativeCost >= successor.getCost()) {
                continue;
            }

            successor.setPredecessor(currentNode);
            successor.setCost(tentativeCost + end.getDistanceFrom(successor.getMyGridCell().getPosition()));

            if (!openList.contains(successor)) {
                openList.add(successor);
            }
        }
    }

    public Node[] getNextMoveSteps(Vector start, Vector vectorToGo, Grid grid, int moveCount) {
        Node[] path = new Node[0];

        if (moveCount <= 0) {
            return path;
        }

        if (isReachable(start, vectorToGo, grid)) {
            path = this.findPath(start, vectorToGo, new GridGraph(grid));
        } else {
            vectorToGo = findClosestNearbyNode(grid, start, vectorToGo);
            if (vectorToGo != null) {
                path = this.findPath(start, vectorToGo, new GridGraph(grid));
            }
        }

        if (path.length > 0) {
            int length = Math.min(path.length, moveCount);
            Node[] nodes = new Node[length];
            System.arraycopy(path, 0, nodes, 0, length);

            return nodes;
        }

        return path;
    }

    // TODO: 29.01.2022 we have a problem here, i can't think anymore so future me has to solve it 
    public Vector findClosestNearbyNode(Grid grid, Vector start, Vector end) {
        GridGraph graph = new GridGraph(grid);
        Node targetNode = graph.getNodes()[end.getX()][end.getY()];

        for (Node node : targetNode.getMyNeighbors()) {
            double cost = node.getMyGridCell().getPosition().getDistanceFrom(start);
            node.setCost(cost);
        }

        PriorityQueue<Node> neighbors = new PriorityQueue<>(targetNode.getMyNeighbors().size(), new NodeComparator());
        neighbors.addAll(targetNode.getMyNeighbors());
        for (Node node : neighbors) {
            Vector vector = node.getMyGridCell().getPosition();
            if (isReachable(start, vector, grid)) {
                
                return vector;
            }
        }
        return null;
    }

    public boolean isReachable(Vector start, Vector end, Grid grid) {
        if (grid.getGridCells()[end.getX()][end.getY()].getCurrentObstacle() != null) {
            return false;
        }

        return (this.findPath(start, end, new GridGraph(grid)).length > 0);
    }

    private void printNodeHierarchy(Node node) {
        int amountPredecessor = node.countHowManyPredecessors();
        int counter = 0;
        for (int i = amountPredecessor; i >= 0; i--) {
            System.out.print("\n" + "\t".repeat(Math.max(0, counter++)) + node.getMyGridCell().getPosition());
            node = node.getPredecessor();
        }
        
    }

    private Node[] getPathInCorrectOrder(Node node, boolean excludeStart) {
        int amountPredecessor = node.countHowManyPredecessors() + 1;
        if (excludeStart) amountPredecessor--;
        Node[] nodes = new Node[amountPredecessor];

        for (int i = amountPredecessor - 1; i >= 0; i--) {
            nodes[i] = node;
            node = node.getPredecessor();
        }

        return nodes;
    }
}