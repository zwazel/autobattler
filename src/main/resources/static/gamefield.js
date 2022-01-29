let rows;
let columns;
const characterImageHtml = "characterIcon.html";

async function loadGridSize() {
    let response = await fetch("http://localhost:8080/getFightHistory");

    if (response.ok) { // if HTTP-status is 200-299
        // get the response body (the method explained below)
        let json = await response.json();
        let entity = JSON.parse(json.entity);
        console.log(entity)
        console.log(entity.gridSize)
        drawField(entity.gridSize.x, entity.gridSize.y)
    } else {
        alert("HTTP-Error: " + response.status);
    }
}

function drawField(_rows, _columns) {
    rows = _rows;
    columns = _columns;
    const gameBoad = document.getElementById("gameboard")
    const htmlForRow = "<tr class=\"boardRow\">";
    const htmlforCell = "<td></td>"
    let gameBoardBuildingSting = "";

    for (let i = 1; i <= columns; i++) {
        gameBoardBuildingSting += htmlForRow;
        for (let j = 1; j <= rows; j++) {
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

loadGridSize()