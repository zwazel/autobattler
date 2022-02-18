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

            const unit = parseUnitTypeSimple(unitJson)

            console.log(unit);

            // <div id="MY_FIRST_UNIT" class="draggableUnit" draggable="true">
            const unitDiv = document.createElement('div');
            unitDiv.id = unitId + "-" + unitName;
            unitDiv.className = "draggableUnit";
            unitDiv.draggable = true;
            unitDiv.innerHTML = `<p>${unitName}</p>`;

            const unitImage = document.createElement('img');
            unitImage.src = unit.image;
            unitImage.className = "unitImage";
            unitImage.draggable = false;

            unitDiv.appendChild(unitImage);
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
                const type = getUnitTypefromUnit(unit);

                const data = {
                    "name": type,
                    "position": {
                        "x": pos.x,
                        "y": pos.y
                    },
                    "unitType": type,
                    "priority": priorityAndID,
                    "id": priorityAndID
                };
                priorityAndID++;

                formationToSave.push(data);
            }
        }
    }

    const data = {
        "units": formationToSave
    };

    console.log(data);

    await fetch('/api/user/addFormation', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
}

function getUnitTypefromUnit(string) {
    let index = string.indexOf("-");
    return string.substring(0, index);
}

function getPosOutOfUnit(string) {
    let index = string.indexOf("pos-");

    if(index === -1) {
        return undefined;
    }

    let indexOfNumber = index + 4;
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
        console.log("pos")
        console.log(pos)
        if (formation[pos.x][pos.y] != null) {
            console.log("NO >:[")
        } else {
            const data = ev.dataTransfer.getData("text");
            const original = document.getElementById(data);

            const originalId = original.id;

            console.log("originalId")
            console.log(originalId);

            const lastPos = getPosOutOfUnit(originalId);
            if(lastPos) {
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

            // if (original.id.includes("copy")) {
            //     const originalId = original.id;
            //     const lastPos = getPosOutOfUnit(originalId);
            //     formation[lastPos.x][lastPos.y] = null;
            //
            //     let index = originalId.indexOf("pos-");
            //     let indexOfNumber = index + 4;
            //     let newId = originalId.substring(0, indexOfNumber);
            //     newId += +pos.x + "-" + pos.y;
            //     original.id = newId;
            //     ev.target.appendChild(original);
            //     formation[pos.x][pos.y] = original.id;
            // } else {
            //     const nodeCopy = original.cloneNode(true);
            //     nodeCopy.id = original.id + "-copy-" + copyCounter++ + "-pos-" + pos.x + "-" + pos.y; /* We cannot use the same ID */
            //     nodeCopy.addEventListener("dragstart", drag);
            //     nodeCopy.removeEventListener("dragover", allowDrop);
            //     ev.target.appendChild(original);
            //     formation[pos.x][pos.y] = original.id;
            // }
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