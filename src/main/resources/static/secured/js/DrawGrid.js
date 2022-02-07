let rows;
let columns;

function drawField(_rows, _columns, dragable) {
    rows = _rows;
    columns = _columns;
    const drawField = document.getElementById("gameboard");
    const htmlForRow = "<tr class='boardRow'>";
    const htmlforCell = "<td><div class='unitCellWrapper'></div></td>"
    let drawFieldBuildString = "";

    for (let i = 1; i <= columns; i++) {
        drawFieldBuildString += htmlForRow;
        for (let j = 1; j <= rows; j++) {
            drawFieldBuildString += htmlforCell;
        }
        drawFieldBuildString += "</tr>";
    }

    drawField.innerHTML = drawFieldBuildString
}