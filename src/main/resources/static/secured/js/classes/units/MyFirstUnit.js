class MyFirstUnit extends Unit {
    constructor(side, id, name, level, position, priority) {
        super("MY_FIRST_UNIT", side, id, name, level, scaleAttribute(level, 10, function (level, health) {
            return (health + (health * ((level - 1) * 0.25)));
        }), position, priority, "img/units/my_first_unit/goodSoupMobil.png");
    }
}