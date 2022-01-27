package dev.zwazel.autobattler.classes.Utils.map;

import dev.zwazel.autobattler.classes.Utils.Vector;

import java.util.HashSet;
import java.util.PriorityQueue;

public class FindPath {
    PriorityQueue<Node> openList = new PriorityQueue<>(1, new NodeComparator());
    HashSet<Node> closedList = new HashSet<>();

    public Node findPath(Vector start, Vector end, GridGraph grid) {
        Node node = grid.getNodes()[start.getX()][start.getY()];
        node.setCost(0);
        openList.add(node);

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            System.out.println(getNodeInfos(currentNode));
            if (currentNode.getMyGridCell().getPosition().equals(end)) {
                return currentNode;
            }
            closedList.add(currentNode);
            expandNode(currentNode, end);
        }

        return null;
    }

    private String getNodeInfos(Node node) {
        return node.getMyGridCell().getPosition()+",cost{"+node.getCost()+"}";
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
}
