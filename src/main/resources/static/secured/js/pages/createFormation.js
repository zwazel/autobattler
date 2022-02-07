let copyCounter = 0;

let formation = createArray(rows, columns);

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
                let index = originalId.indexOf("pos-");
                let indexOfNumber = index + 4;
                let number = originalId.substring(indexOfNumber);
                let lastPos = extractPosFromString(number);
                formation[lastPos.x][lastPos.y] = null;

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