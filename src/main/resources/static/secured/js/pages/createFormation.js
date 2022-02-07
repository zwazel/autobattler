console.log("guten tag 1")

// get all the elements with the param draggable=true and add the event listener
let elms = document.querySelectorAll("[draggable=true]");
for (let i = 0; i < elms.length; i++) {
    console.log("guten tag 3")
    let draggable = elms[i];
    draggable.addEventListener("dragstart", drag);
}

const allGridCells = document.querySelectorAll('.unitCellWrapper');
for (let i = 0; i < allGridCells.length; i++) {
    allGridCells[i].addEventListener("dragover", allowDrop);
    allGridCells[i].addEventListener("drop", drop);
}

function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
    console.log("guten tag 2")
}

function drop(ev) {
    ev.preventDefault();
    let data = ev.dataTransfer.getData("text");
    ev.dataTransfer.dropEffect = "copy";
    ev.target.appendChild(document.getElementById(data));
}