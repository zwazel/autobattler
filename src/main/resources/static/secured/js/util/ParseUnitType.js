function parseUnitTypeSimple(unit) {
    console.log(unit)
    return parseUnitType(unit, undefined, false);
}

function parseUnitType(unit, side, withPosAndPriority = true) {
    const type = (unit.type) ? unit.type : unit.unitType;

    let position;
    let priority;

    if (withPosAndPriority) {
        position = unit.position;
        priority = unit.priority;
    } else {
        position = undefined;
        priority = undefined;
    }

    switch (type) {
        case "MY_FIRST_UNIT":
            return new MyFirstUnit(side, unit.id, unit.name, unit.level, position, priority);
    }
}