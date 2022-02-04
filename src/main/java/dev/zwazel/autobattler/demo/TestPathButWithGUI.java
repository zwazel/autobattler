package dev.zwazel.autobattler.demo;

import dev.zwazel.autobattler.classes.units.SimpleWall;
import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.utils.map.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;


public class TestPathButWithGUI extends Canvas implements MouseListener {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private final Grid grid;
    private final int scalar;
    private Node[] nodes;
    private Vector start;
    private Vector end;

    public TestPathButWithGUI(Vector start, Vector end, Grid grid, int scalar) {
        this(new FindPath().findPath(start, end, new GridGraph(grid)), start, end, grid, scalar);
    }

    public TestPathButWithGUI(Node[] nodes, Vector start, Vector end, Grid grid, int scalar) {
        this.nodes = nodes;
        this.start = start;
        this.end = end;
        this.grid = grid;
        this.scalar = scalar;
        this.addMouseListener(this);
    }

    public static void main(String[] args) {
        Grid grid = new Grid(10, 10);
        int scalar = 50;


        JFrame frame = new JFrame();
        frame.setSize(grid.getWidth() * scalar, grid.getHeight() * scalar);
        TestPathButWithGUI testPathButWithGUI = new TestPathButWithGUI(new Vector(0, 0), grid.getGridSize().subtract(1), grid, scalar);
        frame.add(testPathButWithGUI, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.drawRect(0, 0, grid.getWidth() * scalar, grid.getHeight() * scalar);

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

        g.setColor(Color.GREEN);
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
        x = Math.min(x, (grid.getWidth() - 1) * scalar);
        y = Math.min(y, (grid.getHeight() - 1) * scalar);

        if (e.getButton() == MouseEvent.BUTTON3) {
            this.end = new Vector(x / scalar, y / scalar);

            FindPath findPath = new FindPath();
            this.nodes = findPath.findPath(start, end, new GridGraph(grid));

            repaint();
        } else if (e.getButton() == MouseEvent.BUTTON1) {
            this.start = new Vector(x / scalar, y / scalar);

            FindPath findPath = new FindPath();
            this.nodes = findPath.findPath(start, end, new GridGraph(grid));

            repaint();
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            GridCell gridCell = this.grid.getGridCells()[x / scalar][y / scalar];
            if (gridCell.getCurrentObstacle() != null) {
                gridCell.setCurrentObstacle(null);
            } else {
                gridCell.setCurrentObstacle(new SimpleWall());
            }

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
}