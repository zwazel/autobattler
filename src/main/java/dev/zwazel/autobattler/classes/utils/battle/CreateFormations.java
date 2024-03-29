package dev.zwazel.autobattler.classes.utils.battle;

import dev.zwazel.autobattler.classes.abstractClasses.Unit;
import dev.zwazel.autobattler.classes.enums.Side;
import dev.zwazel.autobattler.classes.enums.UnitTypes;
import dev.zwazel.autobattler.classes.exceptions.UnknownUnitType;
import dev.zwazel.autobattler.classes.model.User;
import dev.zwazel.autobattler.classes.units.SimpleUnit;
import dev.zwazel.autobattler.classes.utils.Formation;
import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.utils.map.FindPath;
import dev.zwazel.autobattler.classes.utils.map.Grid;

import java.util.ArrayList;
import java.util.Arrays;

import static dev.zwazel.autobattler.classes.enums.Side.ENEMY;

public class CreateFormations {
    private final Grid grid;
    private final boolean useFullWidth;

    public CreateFormations(Vector gridSize, boolean useFullWidth) {
        this.grid = new Grid(gridSize);
        this.useFullWidth = useFullWidth;
    }

    public Formation createTestFormation(Side side, long idCounter, boolean randomPositioning, UnitTypes[] allowedUnitTypes, int minLevel, int maxLevel, int amountUnits, int unitSlots) {
        // get a random number between amountUnits + minlevel and amountUnits + maxLevel
        int totalLevel = (amountUnits + minLevel) + (int) (Math.random() * ((amountUnits + maxLevel) - (amountUnits + minLevel)));

        return createTestFormation(side, idCounter, randomPositioning, allowedUnitTypes, minLevel, maxLevel, totalLevel, amountUnits, unitSlots);
    }

    public Formation createTestFormation(Side side, long idCounter, boolean randomPositioning, UnitTypes[] allowedUnitTypes, int minLevel, int maxLevel, int targetTotalLevel, int amountUnits, int unitSlots) {
        Formation formation;
        ArrayList<Unit> units = new ArrayList<>();

        int slotsTaken = 0;
        int priorityCounter = 0;

        int[] possibleSlotSizes = new int[allowedUnitTypes.length];
        for (int i = 0; i < allowedUnitTypes.length; i++) {
            possibleSlotSizes[i] = allowedUnitTypes[i].getSlotSize();
        }

        for (int j = 0; j < amountUnits; j++) {
            if (slotsTaken >= unitSlots) {
                break;
            }

            UnitTypes type = allowedUnitTypes[(int) (Math.random() * allowedUnitTypes.length)];
            int tempSlotAmountTaken = type.getSlotSize() + slotsTaken;
            if (tempSlotAmountTaken > unitSlots) {
                boolean isPossibleToFillSlot = false;
                for (int possibleSlotSize : possibleSlotSizes) {
                    if (possibleSlotSize + slotsTaken <= unitSlots) {
                        isPossibleToFillSlot = true;
                        break;
                    }
                }

                if (isPossibleToFillSlot) {
                    while (tempSlotAmountTaken > unitSlots) {
                        type = allowedUnitTypes[(int) (Math.random() * allowedUnitTypes.length)];
                        tempSlotAmountTaken = type.getSlotSize() + slotsTaken;
                    }
                } else {
                    System.err.println("no more possible slots to fill! could not find a unit type of a slot size of " + (unitSlots - slotsTaken));
                    break;
                }
            }

            slotsTaken += type.getSlotSize();

            Vector vector = (randomPositioning) ? findFreeRandomSpaceOnSide(side) : findFreeSpaceOnSide(side);
            if (vector == null) {
                System.err.println("No free space on side " + side);
                break;
            }
            Unit unit = createTestUnit(idCounter++, priorityCounter++, vector, type, minLevel, maxLevel);

            if (unit == null) {
                System.err.println("Unit is null");
                break;
            }
            units.add(unit);
            grid.updateOccupiedGrid(unit);
        }

        int totalLevel = getTotalLevel(units);

        int maxLevelDifference = 5;

        float difference = Math.abs(totalLevel - targetTotalLevel);
        boolean[] indexesOk = new boolean[units.size()];
        Arrays.fill(indexesOk, true);

        while (difference > maxLevelDifference) {
            for (int i = 0; i < indexesOk.length; i++) {
                if (indexesOk[i]) {
                    Unit unit = units.get(i);

                    int randomLevelDifference = (int) (Math.random() * (maxLevelDifference + 1)) + 1;

                    if (totalLevel > targetTotalLevel) {
                        if (!unit.setLevel(unit.getLevel() - randomLevelDifference)) {
                            if (!unit.setLevel(unit.getLevel() - 1)) {
                                indexesOk[i] = false;
                            }
                        }
                    } else {
                        if (!unit.setLevel(unit.getLevel() + randomLevelDifference)) {
                            if (!unit.setLevel(unit.getLevel() + 1)) {
                                indexesOk[i] = false;
                            }
                        }
                    }

                    totalLevel = getTotalLevel(units);
                    difference = Math.abs(totalLevel - targetTotalLevel);
                    if (difference <= maxLevelDifference) {
                        break;
                    }
                }
            }
        }

        formation = new Formation(new User("TestUser_" + side, "TestUser_" + side), units);

        return formation;
    }

    private int getTotalLevel(ArrayList<Unit> units) {
        int totalLevel = 0;
        for (Unit unit : units) {
            totalLevel += unit.getLevel();
        }
        return totalLevel;
    }

    public Unit createTestUnit(long id, int priority, Vector position, UnitTypes type, int minLevel, int maxLevel) {
        // get random number between min and max level
        int level = minLevel + (int) (Math.random() * (maxLevel - minLevel));

        SimpleUnit simpleUnit = new SimpleUnit(id, priority, level, position, type, getRandomUnitName());

        try {
            return simpleUnit.getUnit();
        } catch (UnknownUnitType e) {
            e.printStackTrace();
        }

        return null;
    }

    public Vector findFreeRandomSpaceOnSide(Side side) {
        return findFreeRandomSpaceOnSide(side, 0);
    }

    private int[] getStarterAndEnd(Side side) {
        int starter = 0;
        int end = (useFullWidth) ? grid.getWidth() : grid.getWidth() / 2;
        if (side == ENEMY) {
            starter = (useFullWidth) ? 0 : grid.getWidth() / 2;
            end = grid.getWidth();
        }

        return new int[]{starter, end};
    }

    /**
     * find a random free space on the correct side
     *
     * @param side    the side to find a free space on
     * @param counter keep track of how many times this method has been called, to prevent endless loops. stops after going through all spaces on the side
     * @return a vector with the coordinates of the free space on the side or null if no free space was found
     */
    public Vector findFreeRandomSpaceOnSide(Side side, int counter) {
        int[] ints = getStarterAndEnd(side);
        int starter = ints[0];
        int end = ints[1];

        // get random x between starter and end
        int x = (int) (Math.random() * (end - starter)) + starter;
        int y = (int) (Math.random() * grid.getHeight());

        Vector vector = new Vector(x, y);

        FindPath findPath = new FindPath();

        if (findPath.isOccupied(vector, grid) && counter <= x + y) {
            return findFreeRandomSpaceOnSide(side, counter + 1);
        } else if (counter > x + y) {
            return null;
        }
        return vector;
    }

    public Vector findFreeSpaceOnSide(Side side) {
        Vector vector;
        int[] ints = getStarterAndEnd(side);
        int starter = ints[0];
        int end = ints[1];

        FindPath findPath = new FindPath();

        for (int i = starter; i < end; i++) {
            for (int j = 0; j < grid.getHeight(); j++) {
                vector = new Vector(i, j);
                if (!findPath.isOccupied(vector, grid)) {
                    return vector;
                }
            }
        }

        return null;
    }

    public String getRandomUnitName() {
        String[] unitNames = new String[]{
                "Tim", "Ash", "Phillip", "Moritz", "Mütz", "Marc", "Magnus", "Nadina", "Leon", "Bjarne", "Niklas",
                "Dennis", "Loic", "Jerry", "Cherry", "BobbyLongDick", "Matilda", "George", "Richard", "Amy", "Perla",
                "Hans", "Hansjürgen", "Daniel", "David", "Alyssa", "Karen", "Jorge", "Feuerkiller162", "Zwazel",
                "Ash-Broccoli", "Uljanow", "Marconymous", "Dreamweaver", "Mühla", "LordMühla", "Shirin197", "Nahro",
                "Pixem", "Fabian", "Florin", "FliegenderHolländer", "Sweaty Baguette", "Cream Walk", "Chief Queef",
                "Easy.MR60", "boorgar", "Ugandalf", "ChickenLarry", "Noel", "Lösche", "Eärendil our most beloved star",
                "Marisa", "Finn", "FaceofLight", "Snow/Henry", "Henry"
        };

        // get a random name from the list
        int randomIndex = (int) (Math.random() * unitNames.length);
        return unitNames[randomIndex];
    }
}
