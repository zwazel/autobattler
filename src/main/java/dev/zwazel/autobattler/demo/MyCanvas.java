package dev.zwazel.autobattler.demo;

import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.utils.map.FindPath;
import dev.zwazel.autobattler.classes.utils.map.Grid;
import dev.zwazel.autobattler.classes.utils.map.GridGraph;
import dev.zwazel.autobattler.classes.utils.map.Node;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;


public class MyCanvas extends Canvas implements MouseListener {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private Node[] nodes;
    private Vector start;
    private Vector end;
    private Grid grid;
    private int scalar;

    public MyCanvas(Node[] nodes, Vector start, Vector end, Grid grid, int scalar) {
        this.nodes = nodes;
        this.start = start;
        this.end = end;
        this.grid = grid;
        this.scalar = scalar;
        this.addMouseListener(this);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.black);
        Vector gridPositionNow = new Vector(0, 0);
        for (int row = 0; row < grid.getHeight(); row++) {
            gridPositionNow.setY(row * scalar);
            for (int column = 0; column < grid.getWidth(); column++) {
                gridPositionNow.setX(column * scalar);

                if (grid.getGridCells()[column][row].getCurrentObstacle() != null) {
                    g.fillRect(gridPositionNow.getX(), gridPositionNow.getY(), scalar, scalar);
                }
            }
        }

        g.setColor(Color.PINK);
        g.fillRect(start.getX() * scalar, start.getY() * scalar, scalar, scalar);

        g.setColor(Color.BLUE);
        g.fillRect(end.getX() * scalar, end.getY() * scalar, scalar, scalar);

        Vector nodeBefore = null;
        for (Node n : nodes) {
            if (nodeBefore == null) {
                nodeBefore = start;
            }
            g.setColor(Color.BLACK);
            g.drawRect(n.getMyGridCell().getPosition().getX() * scalar, n.getMyGridCell().getPosition().getY() * scalar, scalar, scalar);

            g.setColor(Color.RED);
            g.drawLine(nodeBefore.getX() * scalar + (scalar / 2), nodeBefore.getY() * scalar + (scalar / 2), n.getMyGridCell().getPosition().getX() * scalar + (scalar / 2), n.getMyGridCell().getPosition().getY() * scalar + (scalar / 2));
            g.drawString("" + df.format(n.getCost()), n.getMyGridCell().getPosition().getX() * scalar, n.getMyGridCell().getPosition().getY() * scalar);

            nodeBefore = n.getMyGridCell().getPosition();
        }
        if (nodeBefore != null) {
            g.setColor(Color.RED);
            g.drawLine(nodeBefore.getX() * scalar + (scalar / 2), nodeBefore.getY() * scalar + (scalar / 2), end.getX() * scalar + (scalar / 2), end.getY() * scalar + (scalar / 2));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        // clamp x and y to the width and height of the grid
        x = Math.min(x, (grid.getWidth()-1) * scalar);
        y = Math.min(y, (grid.getHeight()-1) * scalar);

        if(e.getButton() == MouseEvent.BUTTON1) {
            this.end = new Vector(x / scalar, y / scalar);

            FindPath findPath = new FindPath();
            this.nodes = findPath.findPath(start, end, new GridGraph(grid));

            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public Node[] getNodes() {
        return nodes;
    }

    public void setNodes(Node[] nodes) {
        this.nodes = nodes;
    }

    public Vector getStart() {
        return start;
    }

    public void setStart(Vector start) {
        this.start = start;
    }

    public Vector getEnd() {
        return end;
    }

    public void setEnd(Vector end) {
        this.end = end;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public int getScalar() {
        return scalar;
    }

    public void setScalar(int scalar) {
        this.scalar = scalar;
    }
}