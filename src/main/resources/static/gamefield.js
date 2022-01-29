let rows;
let columns;
const gameBoard = document.getElementById("gameboard")

async function loadGridSize() {
    let response = await fetch("http://localhost:8080/getFightHistory");

    if (response.ok) { // if HTTP-status is 200-299
        // get the response body (the method explained below)
        let json = await response.json();
        let entity = JSON.parse(json.entity);
        drawField(entity.gridSize.x, entity.gridSize.y)
        initUnits(entity.unitsLeft)
        initUnits(entity.unitsRight)
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

function getUnitIcon(unitId, unitName) {
    let div = document.createElement("div");

    let pUnitId = document.createElement("p")
    pUnitId.id = "unitId-" + unitId
    pUnitId.innerHTML = unitId

    let pUnitName = document.createElement("p")
    pUnitName.id = "unitName-" + unitName
    pUnitName.innerHTML = unitName

    let imgUnit = document.createElement("img")
    imgUnit.classList.add("characterIconImage")
    imgUnit.src = "img/circle_01.png"
    imgUnit.alt = ""

    div.append(pUnitId, pUnitName, imgUnit)

    return div
}

function initUnits(units) {
    console.log(units)
    for (let i = 0; i < units.length; i++) {
        let unit = units[i];
        console.log(unit)
        let unitPos = unit.position;
        console.log(unitPos)
        $(gameBoard.rows[unitPos.y].cells[unitPos.x]).append(getUnitIcon(unit.id, unit.name));
    }
}

function setRandomCharacter() {
    let rowPos = Math.floor(Math.random() * rows + 1);
    let columnPos = Math.floor(Math.random() * columns + 1);
    $(gameBoard.rows[rowPos].cells[columnPos]).load(characterImageHtml);
}

loadGridSize()