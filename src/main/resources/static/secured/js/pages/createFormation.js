let copyCounter = 0;

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
    let data = ev.dataTransfer.getData("text");
    console.log("data=" + data);
    let original = document.getElementById(data);
    if (original.id.includes("copy")) {
        ev.target.appendChild(original);
    } else {
        let nodeCopy = original.cloneNode(true);
        nodeCopy.id = original.id + "-copy-" + copyCounter++; /* We cannot use the same ID */
        nodeCopy.addEventListener("dragstart", drag);
        nodeCopy.removeEventListener("dragover", allowDrop);
        ev.target.appendChild(nodeCopy);
    }
}