let rows;
let columns;
const gameBoard = document.getElementById("gameboard")

async function loadGridSize() {
    let response = await fetch("http://localhost:8080/getFightHistory");

    if (response.ok) { // if HTTP-status is 200-299
        // get the response body (the method explained below)
        let json = await response.json();
        let entity = JSON.parse(json.entity);
        console.log(entity)
        drawField(entity.gridSize.x, entity.gridSize.y)
        initUnits(entity.unitsLeft)
        initUnits(entity.unitsRight)
        // playHistory()
    } else {
        alert("HTTP-Error: " + response.status);
    }
}

function playHistory(history) {

}

function drawField(_rows, _columns) {
    rows = _rows;
    columns = _columns;
    const gameBoad = document.getElementById("gameboard")
    const htmlForRow = "<tr class=\"boardRow\">";
    const htmlforCell = "<td><div class='unitCellWrapper'></div></td>"
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
    let pUnitId = document.createElement("p")
    pUnitId.id = "unitId-" + unitId
    pUnitId.innerHTML = unitId

    let pUnitName = document.createElement("p")
    pUnitName.id = "unitName-" + unitName
    pUnitName.innerHTML = unitName
    let imgUnit = document.createElement("img")

    let divP = document.createElement("div")
    divP.classList.add("unitTextInfo")
    divP.append(unitId, unitName)

    imgUnit.classList.add("characterIconImage")
    imgUnit.src = "img/circle_01.png"
    imgUnit.alt = ""

    let imgDiv = document.createElement("div")
    imgDiv.classList.add("unitImageWrapper")
    imgDiv.append(imgUnit)

    let wrapper = document.createElement("div")
    wrapper.append(divP, imgDiv)

    return wrapper
}

function initUnits(units) {
    for (let i = 0; i < units.length; i++) {
        let unit = units[i];
        let unitPos = unit.position;
        $(gameBoard.rows[unitPos.y].cells[unitPos.x]).children(".unitCellWrapper").append(getUnitIcon(unit.id, unit.name));
    }
}

loadGridSize()