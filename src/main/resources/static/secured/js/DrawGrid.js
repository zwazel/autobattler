let rows;
let columns;

function extractPosFromCell(elementId) {
    elementId = elementId.replace("cell-", "");
    const pos = elementId.split("-");
    return new Position(parseInt(pos[0])-1, parseInt(pos[1])-1);
}

function drawField(_rows, _columns) {
    rows = _rows;
    columns = _columns;
    const drawField = document.getElementById("gameboard");
    for (let i = 1; i <= rows; i++) {
        let row = document.createElement("tr");
        row.classList.add("boardRow");
        for (let j = 1; j <= columns; j++) {
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