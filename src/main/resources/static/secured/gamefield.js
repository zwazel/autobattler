const gameBoard = document.getElementById("gameboard")
let index = 0;
const defaultPlaybackSpeed = 750;
const movementDelayAmount = 2;
let historyPlaybackSpeed = defaultPlaybackSpeed; // speed per turn
let movementDelay = historyPlaybackSpeed / movementDelayAmount; // speed per movement

let unitsLeft = []
let unitsRight = []

function speedUp(scalar) {
    historyPlaybackSpeed = defaultPlaybackSpeed / scalar;
    movementDelay = historyPlaybackSpeed / movementDelayAmount;
}

function speedDown(scalar) {
    historyPlaybackSpeed = defaultPlaybackSpeed * scalar;
    movementDelay = historyPlaybackSpeed / movementDelayAmount;
}

function resetSpeed() {
    historyPlaybackSpeed = defaultPlaybackSpeed;
    movementDelay = historyPlaybackSpeed / movementDelayAmount;
}

async function loadGridSize() {
    let response = await fetch("/api/battle/getFightHistory");

    if (response.ok) { // if HTTP-status is 200-299
        // get the response body (the method explained below)
        let json = await response.json();
        let entity = JSON.parse(json.entity);
        console.log(entity);
        drawField(entity.gridSize.x, entity.gridSize.y);

        await initUnits(entity.unitsLeft);
        await initUnits(entity.unitsRight);

        let timeStart = new Date().getTime();
        manageHistoryPlayback(entity.history);
    } else {
        alert("HTTP-Error: " + response.status);
    }
}

function manageHistoryPlayback(history) {
    if (index < history.length) {
        setTimeout(async function () {
            await playHistory(history)
        }, historyPlaybackSpeed);
    } else {
        console.log("WE DONE")
    }
}

async function playHistory(history) {
    let historyObject = history[index++];
    let unit = historyObject.user;

    if (unit.side === "ENEMY") {
        unit = findUnit(unitsRight, unit.id)
    } else {
        unit = findUnit(unitsLeft, unit.id)
    }

    if (unit !== undefined) {
        if (historyObject.type === "DIE") {
            console.log("UNIT DIED: " + unit.name + "(" + unit.id + ")")
            await removeUnit(unit)
        } else {
            await moveUnit(unit, historyObject.positions)
        }

        manageHistoryPlayback(history);
    } else {
        alert("UNIT UNDEFINED!")
    }
}

async function initUnits(units) {
    for (let i = 0; i < units.length; i++) {
        let unit = units[i];
        let unitPos = unit.position;
        unit = new MyFirstUnit(unit.side, unit.id, unit.name, unit.level, new Position(unitPos.x, unitPos.y), unit.priority)
        if (unit.side === "ENEMY") {
            unitsRight.push(unit)
        } else {
            unitsLeft.push(unit)
        }
        await placeUnit(unit, unitPos);
    }

    return new Promise((resolve) => {
        resolve();
    })
}

function findUnit(array, id) {
    for (let i = 0; i < array.length; i++) {
        let unit = array[i];
        if (unit.id === id) return unit;
    }
    return undefined;
}

async function moveUnit(unit, positions) {
    for (let i = 0; i < positions.length; i++) {
        let position = positions[i];
        if (i === 0) {
            await removeUnit(unit)
            await placeUnit(unit, position)
        } else {
            setTimeout(async function () {
                await removeUnit(unit)
                await placeUnit(unit, position)
            }, movementDelay);
        }
    }

    return new Promise((resolve) => {
        resolve();
    })
}

async function placeUnit(unit, position) {
    $(gameBoard.rows[position.y].cells[position.x]).children(".unitCellWrapper").append(getUnitIcon(unit));

    unit.position = new Position(position.x, position.y);

    return new Promise((resolve) => {
        resolve();
    })
}

async function removeUnit(unit) {
    let unitPos = unit.position;
    $(gameBoard.rows[unitPos.y].cells[unitPos.x]).children(".unitCellWrapper").empty();

    return new Promise((resolve) => {
        resolve();
    })
}

function getUnitIcon(unit) {
    let unitId = unit.id;
    let unitName = unit.name;
    let unitImage = unit.image;

    let pUnitId = document.createElement("p")
    pUnitId.id = "unitId-" + unitId
    pUnitId.innerHTML = unitId

    let pUnitName = document.createElement("p")
    pUnitName.id = "unitName-" + unitName
    pUnitName.innerHTML = unitName
    let imgUnit = document.createElement("img")

    let divP = document.createElement("div")
    divP.classList.add("unitTextInfo")
    divP.append(unitId, unitName)

    imgUnit.classList.add("characterIconImage")
    imgUnit.src = unitImage;
    imgUnit.alt = ""

    let imgDiv = document.createElement("div")
    imgDiv.classList.add("unitImageWrapper")
    imgDiv.append(imgUnit)

    let wrapper = document.createElement("div")
    wrapper.append(divP, imgDiv)

    return wrapper
}

loadGridSize()