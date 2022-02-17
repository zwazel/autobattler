package dev.zwazel.autobattler.classes.utils.map;

import dev.zwazel.autobattler.classes.utils.Vector;

import java.util.HashSet;
import java.util.PriorityQueue;

public class FindPath {
    PriorityQueue<Node> openList = new PriorityQueue<>(1, new NodeComparator());
    HashSet<Node> closedList = new HashSet<>();

    /**
     * implements the A* algorithm to find a path from the start to the end
     *
     * @param start the start of the path
     * @param end   the end of the path
     * @param grid  the grid to get the path from
     * @return the path from the start to the end, or an array with the length of 0 if no path was found
     */
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

    /**
     * utility method to expand a node.
     * expanding means adding all its neighbours to the open list, if certain conditions are met.
     *
     * @param currentNode the node to expand
     * @param end         the end of the path, the target
     */
    private void expandNode(Node currentNode, Vector end) {
        for (Node successor : currentNode.getMyNeighbors()) {
            // if successor is in closed list or has an obstacle skip it. if it is the end, don't skip.
            if (closedList.contains(successor) || (successor.getMyGridCell().getCurrentObstacle() != null && !successor.getMyGridCell().getPosition().equals(end))) {
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

    /**
     * this method is used to get only part of an already existing path. the path is assumed to be in the correct order.
     *
     * @param path      the path to get the next move steps from
     * @param moveCount the number of steps to get
     * @return the next move steps
     */
    public Node[] getNextMoveSteps(Node[] path, int moveCount) {
        if (path.length > 0) {
            int length = Math.min(path.length, moveCount);
            Node[] nodes = new Node[length];
            System.arraycopy(path, 0, nodes, 0, length);

            return nodes;
        }

        return path;
    }

    /**
     * this method is used to get part of a new path.
     *
     * @param start      the start of the path
     * @param vectorToGo the end of the path
     * @param grid       the grid to get the path from
     * @param moveCount  the number of steps to get
     * @return the next move steps
     */
    public Node[] getNextMoveSteps(Vector start, Vector vectorToGo, Grid grid, int moveCount, boolean canMoveDiagonally) {
        Node[] path = new Node[0];

        if (moveCount <= 0) {
            return path;
        }

        path = this.findPath(start, vectorToGo, new GridGraph(grid, canMoveDiagonally));

        return getNextMoveSteps(path, moveCount);
    }

    /**
     * checks if the given vector is occupied by an obstacle in the given grid
     *
     * @param vector the vector to check
     * @param grid   the grid to check
     * @return true if the vector is occupied by an obstacle
     */
    public boolean isOccupied(Vector vector, Grid grid) {
        return grid.getGridCells()[vector.getX()][vector.getY()].getCurrentObstacle() != null;
    }

    /**
     * checks if the given vector is reachable from the start vector in the given grid
     *
     * @param start the start vector
     * @param end   the end vector
     * @param grid  the grid to check
     * @return true if the vector is reachable from the start vector and not occupied by an obstacle
     */
    public boolean isReachable(Vector start, Vector end, Grid grid) {
        return (this.findPath(start, end, new GridGraph(grid)).length > 0);
    }

    /**
     * prints the nodes to the console
     *
     * @param node the node to get the hierarchy from and print to the console
     */
    private void printNodeHierarchy(Node node) {
        int amountPredecessor = node.countHowManyPredecessors();
        int counter = 0;
        for (int i = amountPredecessor; i >= 0; i--) {
            System.out.print("\n" + "\t".repeat(Math.max(0, counter++)) + node.getMyGridCell().getPosition());
            node = node.getPredecessor();
        }

    }

    /**
     * as the path is reversed in the beginning, this method reverses it again to get the correct order of the path as an array of nodes
     *
     * @param node         the starter node (which is the end in the path) to get the path from with its successors
     * @param excludeStart if true, the start node is excluded from the path and will not be included in the array.
     * @return the path as an array of nodes
     */
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
