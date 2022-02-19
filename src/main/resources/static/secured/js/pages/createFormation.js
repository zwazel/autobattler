let copyCounter = 0;

let formation;

async function getAllUnitsOfUser() {
    let response = await fetch(`/api/user/getAllUnits`);

    if (response.ok) { // if HTTP-status is 200-299
        let json = await response.json();

        const unitListContainer = document.getElementById("unitsToDragList");
        for (let i = 0; i < json.length; i++) {
            const unitJson = json[i];
            const unitId = unitJson.id;
            const unitName = unitJson.name;
            const unitLevel = unitJson.level;
            const unitType = unitJson.unitType;

            const unit = parseUnitTypeSimple(unitJson);

            // <div id="MY_FIRST_UNIT" class="draggableUnit" draggable="true">
            const unitDiv = document.createElement('div');
            unitDiv.id = "unitId-" + unitId + "-name:" + unitName + ":";
            unitDiv.className = "draggableUnit";
            unitDiv.draggable = true;

            const unitImage = document.createElement('img');
            unitImage.src = unit.image;
            unitImage.className = "unitImage";
            unitImage.draggable = false;

            const changeUnitNameField = document.createElement('input');
            changeUnitNameField.id = "changeUnitNameField-" + unitId;
            changeUnitNameField.className = "changeUnitNameField";
            changeUnitNameField.type = "text";
            changeUnitNameField.value = unitName;
            changeUnitNameField.draggable = false;

            unitDiv.append(unitImage, changeUnitNameField);
            unitListContainer.appendChild(unitDiv);
        }
    } else {
        alert("HTTP-Error: " + response.status);
    }
}

async function saveFormation() {
    let formationToSave = [];
    let priorityAndID = 1;
    for (let i = 0; i < formation.length; i++) {
        for (let j = 0; j < formation[i].length; j++) {
            if (formation[i][j] != null) {
                let unit = formation[i][j];
                const pos = getPosOutOfUnit(unit);
                const idFromUnit = getUnitIdFromUnit(unit);
                const name = getUnitNameFromUnitInputField(unit);

                const data = {
                    "id": idFromUnit,
                    "name": name,
                    "position": {
                        "x": pos.x,
                        "y": pos.y
                    },
                    "priority": priorityAndID,
                };
                priorityAndID++;

                formationToSave.push(data);
            }
        }
    }

    const data = {
        "units": formationToSave
    };

    await fetch('/api/user/addFormation', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
}

function getUnitNameFromUnitInputField(string) {
    const unitField = document.getElementById("changeUnitNameField-" + getUnitIdFromUnit(string));
    return unitField.value;
}

function getUnitNameFromUnit(string) {
    const search = "name:";

    let index = string.indexOf(search);

    if (index === -1) {
        return undefined;
    }

    string = string.substring(index + search.length, string.length);

    index = string.indexOf(":");
    return string.substring(0, index);
}

function getUnitIdFromUnit(string) {
    const searchString = "unitId-";
    let index = string.indexOf(searchString);

    if (index === -1) {
        return undefined;
    }

    string = string.substring(index + searchString.length, string.length);

    index = string.indexOf("-");
    let number = string.substring(0, index);

    return +number;
}

function getPosOutOfUnit(string) {
    const searchString = "pos-";

    let index = string.indexOf(searchString);

    if (index === -1) {
        return undefined;
    }

    let indexOfNumber = index + searchString.length;
    let number = string.substring(indexOfNumber);
    return extractPosFromString(number);
}

function initDraggableElements() {
    formation = createArray(rows, columns);

    // get all the elements with the param draggable=true and add the event listener
    let elms = document.querySelectorAll("[draggable=true]");
    for (let i = 0; i < elms.length; i++) {
        let draggable = elms[i];
        draggable.addEventListener("dragstart", drag);
    }

    // find all elements with the class unitCellWrapper using jquery and add the event listener
    const allGridCells = $(".unitCellWrapper");
    for (let i = 0; i < allGridCells.length; i++) {
        allGridCells[i].addEventListener("dragover", allowDrop);
        allGridCells[i].addEventListener("drop", drop);
    }
}

function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
    ev.preventDefault();

    const target = ev.target;
    if (ev.target.id) {
        const pos = extractPosFromCell(target.id);
        if (formation[pos.x][pos.y] != null) {
            console.log("NO >:[")
        } else {
            const data = ev.dataTransfer.getData("text");
            const original = document.getElementById(data);

            const originalId = original.id;

            const lastPos = getPosOutOfUnit(originalId);
            if (lastPos) {
                formation[lastPos.y][lastPos.x] = null;

                let index = originalId.indexOf("pos-");
                let indexOfNumber = index + 4;
                let newId = originalId.substring(0, indexOfNumber);
                newId += +pos.x + "-" + pos.y;
                original.id = newId;
            } else {
                original.id = originalId + "-pos-" + pos.x + "-" + pos.y;
            }

            ev.target.appendChild(original);
            formation[pos.y][pos.x] = original.id;
        }
    } else {
        console.log("target undefined, lol")
    }
}

loadGridSizeAndDrawFieldAccordingly("user").then(() => {
    getAllUnitsOfUser().then(() => {
        initDraggableElements();
    })
});