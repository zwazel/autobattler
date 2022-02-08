const gameBoard = document.getElementById("gameboard")
let index = 0;
const defaultPlaybackSpeed = 750;
const movementDelayAmount = 2;
let historyPlaybackSpeed = defaultPlaybackSpeed; // speed per turn
let movementDelay = historyPlaybackSpeed / movementDelayAmount; // speed per movement

let unitsLeft = []
let unitsRight = []

let formations = []
let selectedFormation;

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

function parseUnitType(unit, side) {
    const type = unit.type;
    switch (type) {
        case "MY_FIRST_UNIT":
            return new MyFirstUnit(side, unit.id, unit.name, unit.level, unit.position, unit.priority)
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

async function removeAllUnits() {
    for (let i = 0; i < unitsLeft.length; i++) {
        let unit = unitsLeft[i];
        await removeUnit(unit)
    }

    for (let i = 0; i < unitsRight.length; i++) {
        let unit = unitsRight[i];
        await removeUnit(unit)
    }

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

async function loadFormations() {
    const formationListElement = document.getElementById("formationList");

    let response = await fetch(`/api/user/getAllFormations`);

    if (response.ok) { // if HTTP-status is 200-299
        let json = await response.json();

        for (let i = 0; i < json.length; i++) {
            const formationID = json[i].id;
            let formation = JSON.parse(json[i].formationJson);

            // create list item containing formation id
            let listItem = document.createElement("li");
            listItem.id = "formation-" + formationID;
            listItem.classList.add("formationListItem");

            let button = document.createElement("button");
            button.classList.add("formationListButton");
            button.innerHTML = "" + i;
            button.addEventListener("click", function () {
                loadFormation(i, button);
            });
            listItem.appendChild(button);
            formationListElement.appendChild(listItem);

            formations.push({formation: formation, id: formationID});
        }
    } else {
        alert("HTTP-Error: " + response.status);
    }
}

async function loadFormation(formationId, button) {
    toggleDisableButtons();

    await removeAllUnits()

    const allSelectedButtons = $(".selectedFormation");
    for (let i = 0; i < allSelectedButtons.length; i++) {
        allSelectedButtons[i].classList.remove("selectedFormation");
    }

    button.classList.add("selectedFormation");

    const formation = formations[formationId]
    for (let i = 0; i < formation.formation.length; i++) {
        const unit = parseUnitType(formation.formation[i], "FRIENDLY");
        unitsLeft.push(unit);
        await placeUnit(unit, unit.position);
    }

    selectedFormation = formation;

    toggleDisableButtons();
}

function toggleDisableButtons() {
    const allButtons = $(".formationListButton");
    for (let i = 0; i < allButtons.length; i++) {
        allButtons[i].disabled = !allButtons[i].disabled;
    }
}

loadGridSizeAndDrawFieldAccordingly("battle").then(() => {
    loadFormations();
})

async function startBattle() {
    const buttonStart = document.getElementById("battleStart");
    const unitList = document.getElementById("formationsContainerList");

    if(selectedFormation) {
        $("#temporaryContainer").empty();

        let response = await fetch(`/api/battle/getFightHistory/${selectedFormation.id}`);
        if (response.ok) { // if HTTP-status is 200-299
            let json = await response.json();
            console.log(json);
        } else {
            alert("HTTP-Error: " + response.status);
        }
    }
}