let rows;
let columns;

async function loadGridSizeAndDrawFieldAccordingly(gridSizeType) {
    let response = await fetch(`/api/battle/getGridSize/${gridSizeType}`);

    if (response.ok) { // if HTTP-status is 200-299
        let json = await response.json();
        drawField(json.width, json.height);
    } else {
        alert("HTTP-Error: " + response.status);
    }
}

function extractPosFromCell(elementId) {
    let index = elementId.indexOf("cell-");
    let indexOfNumber = index + 5;
    let number = elementId.substring(indexOfNumber);
    return extractPosFromString(number, true);
}

function extractPosFromString(string, minusOne = false) {
    const pos = string.split("-");
    if(minusOne) {
        return new Position(parseInt(pos[0]) - 1, parseInt(pos[1]) - 1);
    } else {
        return new Position(parseInt(pos[0]), parseInt(pos[1]));
    }
}

function drawField(_rows, _columns) {
    rows = _rows;
    columns = _columns;
    const drawField = document.getElementById("gameboard");
    for (let i = 1; i <= columns; i++) {
        let row = document.createElement("tr");
        row.classList.add("boardRow");
        for (let j = 1; j <= rows; j++) {
            let col = document.createElement("td");
            col.draggable = false;
            let div = document.createElement("div");
            div.classList.add("unitCellWrapper");
            div.id = `cell-${i}-${j}`;
            div.draggable = false;
            col.appendChild(div);
            row.appendChild(col);
        }
        drawField.append(row);
    }
}