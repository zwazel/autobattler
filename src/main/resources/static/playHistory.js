const container = document.getElementById("container");
let gridSize;

async function init() {
    await fetch(window.location.protocol + "//" + window.location.host + "/hello")
        .then((response) => response.json())
        .then((data) => {
            console.log(data.entity);
        })
}

function makeRows(rows, cols) {
    container.style.setProperty('--grid-rows', rows);
    container.style.setProperty('--grid-cols', cols);
    for (let c = 0; c < (rows * cols); c++) {
        let cell = document.createElement("div");
        cell.innerText = "" + (c + 1);
        container.appendChild(cell).className = "grid-item";
    }
}

makeRows(16, 16);
init().then(r => {
});