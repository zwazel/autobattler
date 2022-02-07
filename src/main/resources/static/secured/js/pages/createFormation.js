let copyCounter = 0;

let formation = createArray(rows, columns);

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
    let indexOfNumber = index + 4;
    let number = string.substring(indexOfNumber);
    return extractPosFromString(number);
}

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
            if (original.id.includes("copy")) {
                const originalId = original.id;
                const lastPos = getPosOutOfUnit(originalId);
                formation[lastPos.x][lastPos.y] = null;

                let index = originalId.indexOf("pos-");
                let indexOfNumber = index + 4;
                let newId = originalId.substring(0, indexOfNumber);
                newId += +pos.x + "-" + pos.y;
                original.id = newId;
                ev.target.appendChild(original);
                formation[pos.x][pos.y] = original.id;
            } else {
                const nodeCopy = original.cloneNode(true);
                nodeCopy.id = original.id + "-copy-" + copyCounter++ + "-pos-" + pos.x + "-" + pos.y; /* We cannot use the same ID */
                nodeCopy.addEventListener("dragstart", drag);
                nodeCopy.removeEventListener("dragover", allowDrop);
                ev.target.appendChild(nodeCopy);
                formation[pos.x][pos.y] = nodeCopy.id;
            }
        }
    } else {
        console.log("target undefined, lol")
    }
}