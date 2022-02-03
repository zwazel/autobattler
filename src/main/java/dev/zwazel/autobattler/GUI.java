package dev.zwazel.autobattler;

import dev.zwazel.autobattler.classes.units.Unit;
import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.utils.map.Grid;
import dev.zwazel.autobattler.classes.utils.map.GridCell;
import dev.zwazel.autobattler.classes.utils.map.Node;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Iterator;

public class GUI extends Canvas {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private final int scalar;
    private final BattlerGen2 battlerGen2;
    private Iterator<Unit> unitIterator;
    private Node[] nodes = new Node[0];
    private Vector start = null;
    private Vector end = null;
    private Unit currentUnit = null;
    private Unit lastUnit = null;

    public GUI(BattlerGen2 battlerGen2, int scalar) {
        this.battlerGen2 = battlerGen2;
        Grid grid = battlerGen2.getGrid();
        this.scalar = scalar;
        this.unitIterator = battlerGen2.getUnits().listIterator();

        JFrame frame = new JFrame();
        frame.setSize(grid.getWidth() * scalar, grid.getHeight() * scalar);
        frame.add(this, BorderLayout.CENTER);
        Button nextButton = new Button("Next");
        nextButton.addActionListener(e -> {
            if (unitIterator.hasNext()) {
                lastUnit = currentUnit;
                currentUnit = unitIterator.next();
                battlerGen2.doTurn(unitIterator, currentUnit, false);
            } else {
                if (!battlerGen2.isFightFinished()) {
                    unitIterator = battlerGen2.getUnits().listIterator();
                }
            }
            repaint();
        });
        frame.add(nextButton, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        Grid grid = battlerGen2.getGrid();
        g.drawRect(0, 0, grid.getWidth() * scalar, grid.getHeight() * scalar);

        Vector gridPositionNow = new Vector(0, 0);
        System.out.println("------------------");
        System.out.println("currentUnit = " + currentUnit);
        System.out.println("lastUnit = " + lastUnit);
        for (int row = 0; row < grid.getHeight(); row++) {
            gridPositionNow.setY(row * scalar);
            for (int column = 0; column < grid.getWidth(); column++) {
                gridPositionNow.setX(column * scalar);

                GridCell gridCell = grid.getGridCells()[column][row];
                if (gridCell.getCurrentObstacle() != null) {
                    System.out.println("obstacle = " + gridCell.getCurrentObstacle());
                    if (!gridCell.getCurrentObstacle().equals(currentUnit) || !gridCell.getCurrentObstacle().equals(lastUnit)) {
                        g.setColor(Color.black);
                    } else {
                        if (gridCell.getCurrentObstacle().equals(currentUnit)) {
                            g.setColor(Color.PINK);
                        } else {
                            g.setColor(Color.YELLOW);
                        }
                    }
                    g.fillRect(gridPositionNow.getX(), gridPositionNow.getY(), scalar, scalar);
                }
            }
        }
        System.out.println("---------------------------------");

        if (start != null) {
            g.setColor(Color.GREEN);
            g.fillRect(start.getX() * scalar, start.getY() * scalar, scalar, scalar);
        }

        if (end != null) {
            g.setColor(Color.BLUE);
            g.fillRect(end.getX() * scalar, end.getY() * scalar, scalar, scalar);
        }

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
}
