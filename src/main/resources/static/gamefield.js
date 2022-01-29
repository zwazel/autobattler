let rows;
let columns;
const characterImageHtml = "characterIcon.html";

function drawField(_rows, _columns) {
    rows = _rows;
    columns = _columns;
    const gameBoad = document.getElementById("gameboard")
    const htmlForRow = "<tr class=\"boardRow\">";
    const htmlforCell = "<td></td>"
    let gameBoardBuildingSting = "";

    for (let i = 1; i <= rows; i++) {
        gameBoardBuildingSting += htmlForRow;
        for (let j = 1; j <= columns; j++) {
            gameBoardBuildingSting += htmlforCell;
        }
        gameBoardBuildingSting += "</tr>";
    }

    gameBoad.innerHTML = gameBoardBuildingSting
}

function setRandomCharacter() {
    const gameBoad = document.getElementById("gameboard")
    let rowPos = Math.floor(Math.random() * rows + 1);
    let columnPos = Math.floor(Math.random() * columns + 1);
    $(gameBoad.rows[rowPos].cells[columnPos]).load(characterImageHtml);

}