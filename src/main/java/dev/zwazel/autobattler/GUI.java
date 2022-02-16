package dev.zwazel.autobattler;

import dev.zwazel.autobattler.classes.Obstacle;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.units.MyFirstUnit;
import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.utils.json.ActionHistory;
import dev.zwazel.autobattler.classes.utils.map.*;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Iterator;

public class GUI extends Canvas {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private final int SCALAR;
    private final BattlerGen2 BATTLER_GEN2;

    private final boolean DIFFERENTIATE_SIDE = true;
    private final boolean DIFFERENTIATE_LAST_UNIT = false;
    private final boolean DIFFERENTIATE_CURRENT_UNIT = true;
    private final boolean DIFFERENTIATE_TARGET = true;
    private final boolean SHOW_LAST_POSITION = true;
    private final boolean DRAW_PATH = true;

    private final JFrame FRAME = new JFrame();
    private final Label CURRENT_UNIT_LABEL = new Label("Current Unit = ");
    private final Label LAST_UNIT_LABEL = new Label("Last Unit = ");
    private final Label TARGET_LABEL = new Label("Target = ");
    private final Label CURRENT_ACTION = new Label("Current Action = ");
    private final Label UNIT_LEGEND = new Label("UnitTextMeaning = (id,priority,health)");

    private final Color COLOR_START = Color.cyan;
    private final Color COLOR_CURRENT_UNIT = Color.blue;
    private final Color COLOR_FRIENDLY = Color.green;
    private final Color COLOR_ENEMY = Color.pink;
    private final Color COLOR_TARGET = Color.red;
    private final Color COLOR_LAST_UNIT = Color.yellow;
    private final Color COLOR_GRID = Color.gray;
    private final Color COLOR_PATH_GRID = Color.BLACK;
    private final Color COLOR_PATH = Color.RED;

    private Iterator<Unit> unitIterator;
    private Node[] nodes = new Node[0];
    private Vector start = null;
    private Vector end = null;
    private Unit currentUnit = null;
    private Unit lastUnit = null;
    private Unit target = null;
    private boolean currentUnitMoved = false;
    private boolean canDoNext = true;

    public GUI(BattlerGen2 battlerGen2, int scalar) {
        this.BATTLER_GEN2 = battlerGen2;
        Grid grid = battlerGen2.getGrid();
        this.SCALAR = scalar;
        this.unitIterator = BATTLER_GEN2.getUnits().listIterator();

        FRAME.setSize((grid.getWidth() * SCALAR) + SCALAR * 2, (grid.getHeight() * SCALAR) + (SCALAR * 5));

        FRAME.add(this, BorderLayout.CENTER);

        Button nextButton = new Button("Next");
        nextButton.addActionListener(e -> {
            if (canDoNext) {
                canDoNext = false;

                if (unitIterator.hasNext() && !BATTLER_GEN2.isFightFinished()) {
                    lastUnit = currentUnit;
                    if (lastUnit != null) {
                        this.LAST_UNIT_LABEL.setText("Last Unit = " + lastUnit.getName() + " (" + lastUnit.getID() + "), at " + lastUnit.getGridPosition());
                    }
                    currentUnit = unitIterator.next();
                    this.CURRENT_UNIT_LABEL.setText("Current Unit = " + currentUnit.getName() + " (" + currentUnit.getID() + ")");
                    if (SHOW_LAST_POSITION) {
                        start = currentUnit.getGridPosition();
                    }
                    Vector positionBefore = currentUnit.getGridPosition();
                    ActionHistory actionHistory = BATTLER_GEN2.doTurn(unitIterator, currentUnit, false);
                    if (!SHOW_LAST_POSITION) {
                        start = currentUnit.getGridPosition();
                    }
                    if (actionHistory.targets().length > 0) {
                        Unit target = actionHistory.targets()[0];
                        this.CURRENT_ACTION.setText("Current Action = " + actionHistory.actionType());
                        if (target != null) {
                            end = target.getGridPosition();
                            this.target = target;
                            FindPath findPath = new FindPath();
                            System.out.println("GUI.GUI -> reconstructing path");
                            nodes = findPath.findPath(currentUnit.getGridPosition(), findPath.findClosestNearbyNode(grid, currentUnit.getGridPosition(), end), new GridGraph(grid));
                            this.TARGET_LABEL.setText("Target = " + target.getName() + " (" + target.getID() + "), at " + end);
                        } else {
                            end = null;
                            nodes = new Node[0];
                            this.TARGET_LABEL.setText("Target = ");
                        }
                    } else {
                        end = null;
                        nodes = new Node[0];
                        this.TARGET_LABEL.setText("Target = ");
                        this.CURRENT_ACTION.setText("Current Action = ");
                    }

                    if (!positionBefore.equals(currentUnit.getGridPosition())) {
                        CURRENT_UNIT_LABEL.setText(CURRENT_UNIT_LABEL.getText() + ", moved from " + start + " to " + currentUnit.getGridPosition());
                        currentUnitMoved = true;
                    } else {
                        CURRENT_UNIT_LABEL.setText(CURRENT_UNIT_LABEL.getText() + ", stayed at " + currentUnit.getGridPosition() + ", ");
                        currentUnitMoved = false;
                    }
                }

                if (!BATTLER_GEN2.isFightFinished() && !unitIterator.hasNext()) {
                    unitIterator = BATTLER_GEN2.getUnits().listIterator();
                }

                if (BATTLER_GEN2.isFightFinished()) {
                    this.CURRENT_ACTION.setText("Current Action = battle finished");
                }

                repaint();
                canDoNext = true;
            } else {
                System.out.println("Can't do next");
            }
        });
        FRAME.add(nextButton, BorderLayout.SOUTH);

        // add the labels to a new panel underneath each other and add it to the frame as well on the north side
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.add(CURRENT_UNIT_LABEL);
        labelPanel.add(LAST_UNIT_LABEL);
        labelPanel.add(TARGET_LABEL);
        labelPanel.add(CURRENT_ACTION);
        labelPanel.add(UNIT_LEGEND);
        FRAME.add(labelPanel, BorderLayout.NORTH);

        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        Grid grid = BATTLER_GEN2.getGrid();
        // draw border of the map/grid
        g.drawRect(0, 0, grid.getWidth() * SCALAR, grid.getHeight() * SCALAR);

        // Draw start (where the unit initially was)
        if (start != null && currentUnitMoved && SHOW_LAST_POSITION) {
            g.setColor(COLOR_START);
            g.fillRect(start.getX() * SCALAR, start.getY() * SCALAR, SCALAR, SCALAR);
        }

        // draw all the units
        Vector gridPositionNow = new Vector(0, 0);
        for (int row = 0; row < grid.getHeight(); row++) {
            gridPositionNow.setY(row * SCALAR);
            for (int column = 0; column < grid.getWidth(); column++) {
                gridPositionNow.setX(column * SCALAR);

                // draw rect around the cell
                g.setColor(COLOR_GRID);
                g.drawRect(gridPositionNow.getX(), gridPositionNow.getY(), SCALAR, SCALAR);

                GridCell gridCell = grid.getGridCells()[column][row];
                if (gridCell.getCurrentObstacle() != null) {
                    Obstacle obstacle = gridCell.getCurrentObstacle();
                    if (!obstacle.equals(currentUnit) && !obstacle.equals(lastUnit) && !obstacle.equals(target)) {
                        if (obstacle.getClass() == MyFirstUnit.class) {
                            MyFirstUnit unit = (MyFirstUnit) obstacle;
                            if (DIFFERENTIATE_SIDE) {
                                if (unit.getSide() == Side.FRIENDLY) {
                                    // friendlies
                                    g.setColor(COLOR_FRIENDLY);
                                } else {
                                    // enemies
                                    g.setColor(COLOR_ENEMY);
                                }
                            } else {
                                g.setColor(COLOR_FRIENDLY);
                            }
                        }
                    } else {
                        if (obstacle.equals(currentUnit)) {
                            // current unit
                            differentiateFriendlyAndEnemy(g, currentUnit, DIFFERENTIATE_CURRENT_UNIT, COLOR_CURRENT_UNIT);
                        } else if (obstacle.equals(lastUnit) && !lastUnit.equals(target)) {
                            // last unit
                            differentiateFriendlyAndEnemy(g, lastUnit, DIFFERENTIATE_LAST_UNIT, COLOR_LAST_UNIT);
                        } else {
                            // target unit
                            differentiateFriendlyAndEnemy(g, target, DIFFERENTIATE_TARGET, COLOR_TARGET);
                        }
                    }
                    g.fillRect(gridPositionNow.getX(), gridPositionNow.getY(), SCALAR, SCALAR);
                    g.setColor(Color.BLACK);
                    if (obstacle.getClass() == MyFirstUnit.class) {
                        MyFirstUnit unit = (MyFirstUnit) obstacle;
                        g.drawString(unit.getName() + "(" + unit.getID() + "," + unit.getPriority() + "," + unit.getHealth() + ")", gridPositionNow.getX(), gridPositionNow.getY());
                    }
                }
            }
        }

        // draw the path
        if (DRAW_PATH && currentUnitMoved) {
            Vector nodeBefore = null;
            for (Node n : nodes) {
                if (nodeBefore == null) {
                    nodeBefore = currentUnit.getGridPosition();
                }
                g.setColor(COLOR_PATH_GRID);
                g.drawRect(n.getMyGridCell().getPosition().getX() * SCALAR, n.getMyGridCell().getPosition().getY() * SCALAR, SCALAR, SCALAR);

                g.setColor(COLOR_PATH);
                g.drawLine(nodeBefore.getX() * SCALAR + (SCALAR / 2), nodeBefore.getY() * SCALAR + (SCALAR / 2), n.getMyGridCell().getPosition().getX() * SCALAR + (SCALAR / 2), n.getMyGridCell().getPosition().getY() * SCALAR + (SCALAR / 2));
                g.drawString("" + DECIMAL_FORMAT.format(n.getCost()), n.getMyGridCell().getPosition().getX() * SCALAR, n.getMyGridCell().getPosition().getY() * SCALAR);

                nodeBefore = n.getMyGridCell().getPosition();
            }
            if (nodeBefore != null) {
                g.setColor(COLOR_PATH);
                g.drawLine(nodeBefore.getX() * SCALAR + (SCALAR / 2), nodeBefore.getY() * SCALAR + (SCALAR / 2), end.getX() * SCALAR + (SCALAR / 2), end.getY() * SCALAR + (SCALAR / 2));
            }
        }
    }

    private void differentiateFriendlyAndEnemy(Graphics g, Unit unit, boolean shouldDifferentiate, Color colorIfDifferentiate) {
        if (shouldDifferentiate) {
            g.setColor(colorIfDifferentiate);
        } else {
            if (DIFFERENTIATE_SIDE) {
                if (unit.getSide() == Side.FRIENDLY) {
                    g.setColor(COLOR_FRIENDLY);
                } else {
                    g.setColor(COLOR_ENEMY);
                }
            } else {
                g.setColor(COLOR_FRIENDLY);
            }
        }
    }
}
