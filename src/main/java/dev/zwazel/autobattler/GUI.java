package dev.zwazel.autobattler;

import dev.zwazel.autobattler.classes.Obstacle;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.units.MyFirstUnit;
import dev.zwazel.autobattler.classes.units.Unit;
import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.utils.map.*;

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
    private Unit target = null;

    private JFrame frame = new JFrame();

    private Label currentUnitLabel = new Label("Current Unit = ");
    private Label lastUnitLabel = new Label("Last Unit = ");
    private Label targetLabel = new Label("Target = ");

    private Color colorStart = Color.cyan;
    private Color colorCurrentUnit = Color.blue;
    private Color colorFriendly = Color.green;
    private Color colorEnemy = Color.pink;
    private Color colorTarget = Color.red;
    private Color colorLastUnit = Color.yellow;

    public GUI(BattlerGen2 battlerGen2, int scalar) {
        this.battlerGen2 = battlerGen2;
        Grid grid = battlerGen2.getGrid();
        this.scalar = scalar;
        this.unitIterator = battlerGen2.getUnits().listIterator();

        frame.setSize(grid.getWidth() * scalar, grid.getHeight() * scalar);

        frame.add(this, BorderLayout.CENTER);

        Button nextButton = new Button("Next");
        nextButton.addActionListener(e -> {
            System.out.println("------------------");
            if (unitIterator.hasNext()) {
                lastUnit = currentUnit;
                if (lastUnit != null) {
                    this.lastUnitLabel.setText("Last Unit = " + lastUnit.getName() + " (" + lastUnit.getID() + "), at " + lastUnit.getGridPosition());
                }
                currentUnit = unitIterator.next();
                this.currentUnitLabel.setText("Current Unit = " + currentUnit.getName() + " (" + currentUnit.getID() + ")");
                start = currentUnit.getGridPosition();
                Unit target = battlerGen2.doTurn(unitIterator, currentUnit, false);
                if (target != null) {
                    end = target.getGridPosition();
                    this.target = target;
                    FindPath findPath = new FindPath();
                    nodes = findPath.findPath(currentUnit.getGridPosition(), findPath.findClosestNearbyNode(grid, currentUnit.getGridPosition(), end), new GridGraph(grid));
                    this.targetLabel.setText("Target = " + target.getName() + " (" + target.getID() + "), at " + end);
                } else {
                    end = null;
                    nodes = new Node[0];
                    this.targetLabel.setText("Target = ");
                }

                if (!start.equals(currentUnit.getGridPosition())) {
                    currentUnitLabel.setText(currentUnitLabel.getText() + ", moved from " + start + " to " + currentUnit.getGridPosition());
                } else {
                    currentUnitLabel.setText(currentUnitLabel.getText() + ", stayed at " + currentUnit.getGridPosition());
                }
            }

            if (!battlerGen2.isFightFinished() && !unitIterator.hasNext()) {
                unitIterator = battlerGen2.getUnits().listIterator();
            }
            repaint();
        });
        frame.add(nextButton, BorderLayout.SOUTH);

        // add the labels to a new panel underneath each other and add it to the frame as well on the north side
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.add(currentUnitLabel);
        labelPanel.add(lastUnitLabel);
        labelPanel.add(targetLabel);
        frame.add(labelPanel, BorderLayout.NORTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        Grid grid = battlerGen2.getGrid();
        g.drawRect(0, 0, grid.getWidth() * scalar, grid.getHeight() * scalar);

        Vector gridPositionNow = new Vector(0, 0);
        for (int row = 0; row < grid.getHeight(); row++) {
            gridPositionNow.setY(row * scalar);
            for (int column = 0; column < grid.getWidth(); column++) {
                gridPositionNow.setX(column * scalar);

                GridCell gridCell = grid.getGridCells()[column][row];
                if (gridCell.getCurrentObstacle() != null) {
                    Obstacle obstacle = gridCell.getCurrentObstacle();
                    if (!obstacle.equals(currentUnit) && !obstacle.equals(lastUnit) && !obstacle.equals(target)) {
                        if (obstacle.getClass() == MyFirstUnit.class) {
                            MyFirstUnit unit = (MyFirstUnit) obstacle;
                            if (unit.getSide() == Side.FRIENDLY) {
                                // friendlies
                                g.setColor(colorFriendly);
                            } else {
                                // enemies
                                g.setColor(colorEnemy);
                            }
                        }
                    } else {
                        if (obstacle.equals(currentUnit)) {
                            // current unit
                            g.setColor(colorCurrentUnit);
                        } else if (obstacle.equals(lastUnit)) {
                            // last unit
                            g.setColor(colorLastUnit);
                        } else {
                            // target unit
                            g.setColor(colorTarget);
                        }
                    }
                    g.fillRect(gridPositionNow.getX(), gridPositionNow.getY(), scalar, scalar);
                    g.setColor(Color.BLACK);
                    if (obstacle.getClass() == MyFirstUnit.class) {
                        MyFirstUnit unit = (MyFirstUnit) obstacle;
                        g.drawString(unit.getName() + "(" + unit.getID() + ")", gridPositionNow.getX(), gridPositionNow.getY());
                    }
                }
            }
        }

        if (start != null) {
            g.setColor(colorStart);
            g.fillRect(start.getX() * scalar, start.getY() * scalar, scalar, scalar);
        }

//        if (end != null) {
//            g.setColor(Color.BLUE);
//            g.fillRect(end.getX() * scalar, end.getY() * scalar, scalar, scalar);
//        }

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
