let rows;
let columns;
const gameBoard = document.getElementById("gameboard")
let index = 0;
let historyPlaybackSpeed = 50; // speed in ms (i think)
let movementDelay = historyPlaybackSpeed / 2;

let unitsLeft = []
let unitsRight = []

async function loadGridSize() {
    let response = await fetch("/getFightHistory");

    if (response.ok) { // if HTTP-status is 200-299
        // get the response body (the method explained below)
        let json = await response.json();
        let entity = JSON.parse(json.entity);
        console.log(entity)
        drawField(entity.gridSize.x, entity.gridSize.y)

        await initUnits(entity.unitsLeft)
        await initUnits(entity.unitsRight)

        let timeStart = new Date().getTime();
        manageHistoryPlayback(entity.history)
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
    $(gameBoard.rows[position.y].cells[position.x]).children(".unitCellWrapper").append(getUnitIcon(unit.id, unit.name));

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

function drawField(_rows, _columns) {
    rows = _rows;
    columns = _columns;
    const gameBoad = document.getElementById("gameboard")
    const htmlForRow = "<tr class=\"boardRow\">";
    const htmlforCell = "<td><div class='unitCellWrapper'></div></td>"
    let gameBoardBuildingSting = "";

    for (let i = 1; i <= columns; i++) {
        gameBoardBuildingSting += htmlForRow;
        for (let j = 1; j <= rows; j++) {
            gameBoardBuildingSting += htmlforCell;
        }
        gameBoardBuildingSting += "</tr>";
    }

    gameBoad.innerHTML = gameBoardBuildingSting
}

function getUnitIcon(unitId, unitName) {
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
    imgUnit.src = "img/circle_01.png"
    imgUnit.alt = ""

    let imgDiv = document.createElement("div")
    imgDiv.classList.add("unitImageWrapper")
    imgDiv.append(imgUnit)

    let wrapper = document.createElement("div")
    wrapper.append(divP, imgDiv)

    return wrapper
}

loadGridSize()