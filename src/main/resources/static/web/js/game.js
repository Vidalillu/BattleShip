// Create the head of the tables

function createThead() {
    var theadArray = ["", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"];
    var col = "";
    for (var i = 0; i < theadArray.length; i++) {
        col += "<th><h3>" + theadArray[i] + "</h3></th>";
    }
    $("#tHead").append("<tr>" + col + "</tr>");
    $("#tHead2").append("<tr>" + col + "</tr>");
}

createThead();

// Create the two tables

function createTable(x) {
    var tbodyArray = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
    for (var r = 0; r < tbodyArray.length; r++) {
        var col = "";
        var col2 = "";
        var col3 = "";
        for (var c = 0; c < x; c++) {
            if (c == 0) {
                col += "<td id='" + c + "'><h3>" + tbodyArray[r] + "</h3></td>";
                col2 += "<td id='A" + c + "'><h3>" + tbodyArray[r] + "</h3></td>";
                //                col3 += "<td id='B" + c + "'></td>";

            } else {
                col += "<td id='" + tbodyArray[r] + c + "'></td>";
                col2 += "<td onclick='shoting(this);' id='A" + tbodyArray[r] + c + "'></td>";
                //                col3 += "<td id='B" + c + "'></td>";
            }
        }
        $("#board").append("<tr>" + col + "</tr>");
        $("#board2").append("<tr>" + col2 + "</tr>");
        //        $("#board3").append("<tr>" + col3 + "</tr>");
    }
}

createTable(11);

// Get the parameter of the HTML

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}


$(document).ready(function () {
    var gp = getParameterByName('gp');
    var previous = null;
    var current = null;
    setInterval(function () {
        $.getJSON("../api/game_view/" + gp, function (json) {
            current = JSON.stringify(json);
            if (previous && current && previous !== current) {
                if (document.getElementById('gameStatus').innerHTML == "Waiting for opponent shots" && json.state == "shot") {
                    location.reload();
                } else if (document.getElementById('gameStatus').innerHTML == "Waiting for opponent" && json.state == "wait_ships") {
                    location.reload();
                } else if (document.getElementById('gameStatus').innerHTML == "Waiting for opponent ships" && json.state == "wait_shot") {
                    location.reload();
                } else if (document.getElementById('gameStatus').innerHTML == "Waiting for opponent shots" && json.state == "loser") {
                    location.reload();
                }

            }
            previous = current;
        });
    }, 2000);
    $.getJSON("../api/game_view/" + gp, function (data) {
        if (data.gamePlayers[1] != null) {
            if (data.gamePlayers[0].id == gp) {
                document.getElementById('game').innerHTML = "GAME " + data.id;
                var newElement = document.createElement('h3');
                newElement.innerHTML = data.gamePlayers[0].player.userName;
                document.getElementById('player2').appendChild(newElement);
                var newElement2 = document.createElement('h3');
                newElement2.innerHTML = data.gamePlayers[1].player.userName;
                document.getElementById('player1').appendChild(newElement2);
                playerId = data.gamePlayers[0].player.id;
                opponentId = data.gamePlayers[1].player.id;
            } else {
                document.getElementById('game').innerHTML = "GAME " + data.id;
                var newElement = document.createElement('h3');
                newElement.innerHTML = data.gamePlayers[1].player.userName;
                document.getElementById('player2').appendChild(newElement);
                var newElement2 = document.createElement('h3');
                newElement2.innerHTML = data.gamePlayers[0].player.userName;
                document.getElementById('player1').appendChild(newElement2);
                playerId = data.gamePlayers[1].player.id;
                opponentId = data.gamePlayers[0].player.id;
            }
        } else {
            document.getElementById('game').innerHTML = "GAME " + data.id;
            var newElement = document.createElement('h3');
            newElement.innerHTML = data.gamePlayers[0].player.userName;
            document.getElementById('player2').appendChild(newElement);
            var newElement2 = document.createElement('h3');
            newElement2.innerHTML = "Waiting for opponent";
            document.getElementById('player1').appendChild(newElement2);
            playerId = data.gamePlayers[0].player.id;
        }
        var ships = data.ships
        for (var i in ships) {
            var locations = ships[i].locations;
            for (var j in locations) {
                if (locations[j] != "sunk") {
                    if (locations[j] == document.getElementById(locations[j]).id) {
                        var newElement = document.createElement('div');
                        newElement.setAttribute("typeShip", ships[i].type);
                        newElement.className = "ship";
                        newElement.id = "S" + locations[j];
                        document.getElementById(locations[j]).appendChild(newElement);
                    }
                }
            }
        }
        var salvoes = data.salvoes
        for (var i in salvoes) {
            if (data.gamePlayers[1] != null) {
                var locations = salvoes[i];
                var playerSalvoes = locations[playerId];
                var opponentSalvoes = locations[opponentId];
                for (var j in playerSalvoes) {
                    var opponentShips = data.hits;
                    for (var n in opponentShips) {
                        playerSalvoesTurn = playerSalvoes[j]
                        for (var k in playerSalvoesTurn) {
                            if (("A" + playerSalvoesTurn[k] == document.getElementById("A" + playerSalvoesTurn[k]).id)) {
                                if ((opponentShips[n].includes(playerSalvoesTurn[k])) && (opponentShips[n].includes("sunk")) && (document.getElementById("O" + playerSalvoesTurn[k]) == null)) {
                                    $("#A" + playerSalvoesTurn[k]).empty();
                                    var newElement = document.createElement('div');
                                    newElement.className = "opponentsunkShip";
                                    newElement.id = "O" + playerSalvoesTurn[k];
                                    document.getElementById("A" + playerSalvoesTurn[k]).appendChild(newElement);
                                    var newElement1 = document.createElement('div');
                                    newElement1.className = "salvo";
                                    newElement.appendChild(newElement1);
                                    var newElement2 = document.createElement('img');
                                    newElement2.className = "explosion";
                                    newElement2.src = "img/explosion.png";
                                    newElement1.appendChild(newElement2);
                                    var newElement3 = document.createElement('div');
                                    newElement3.className = "turn";
                                    document.getElementById("A" + playerSalvoesTurn[k]).appendChild(newElement3);
                                    var newElement4 = document.createElement('h4');
                                    newElement4.innerHTML = j;
                                    newElement3.appendChild(newElement4);
                                    var playerlastTurn = j;
                                } else if ((opponentShips[n].includes(playerSalvoesTurn[k])) && (document.getElementById("O" + playerSalvoesTurn[k]) == null)) {
                                    $("#A" + playerSalvoesTurn[k]).empty();
                                    var newElement = document.createElement('div');
                                    newElement.className = "opponentShip";
                                    newElement.id = "O" + playerSalvoesTurn[k];
                                    document.getElementById("A" + playerSalvoesTurn[k]).appendChild(newElement);
                                    var newElement1 = document.createElement('div');
                                    newElement1.className = "salvo";
                                    newElement.appendChild(newElement1);
                                    var newElement2 = document.createElement('img');
                                    newElement2.className = "explosion";
                                    newElement2.src = "img/explosion.png";
                                    newElement1.appendChild(newElement2);
                                    var newElement3 = document.createElement('div');
                                    newElement3.className = "turn";
                                    document.getElementById("A" + playerSalvoesTurn[k]).appendChild(newElement3);
                                    var newElement4 = document.createElement('h4');
                                    newElement4.innerHTML = j;
                                    newElement3.appendChild(newElement4);
                                    var playerlastTurn = j;
                                } else if (document.getElementById("O" + playerSalvoesTurn[k]) == null) {
                                    $("#A" + playerSalvoesTurn[k]).empty();
                                    var newElement = document.createElement('div');
                                    newElement.className = "salvo";
                                    document.getElementById("A" + playerSalvoesTurn[k]).appendChild(newElement);
                                    var newElement1 = document.createElement('img');
                                    newElement1.className = "explosion";
                                    newElement1.src = "img/agua.png";
                                    newElement.appendChild(newElement1);
                                    var newElement2 = document.createElement('div');
                                    newElement2.className = "turn";
                                    document.getElementById("A" + playerSalvoesTurn[k]).appendChild(newElement2);
                                    var newElement3 = document.createElement('h4');
                                    newElement3.innerHTML = j;
                                    newElement2.appendChild(newElement3);
                                    var playerlastTurn = j;
                                }
                            }
                        }
                    }
                }
                for (var l in opponentSalvoes) {
                    opponentSalvoesTurn = opponentSalvoes[l]
                    for (var m in opponentSalvoesTurn) {
                        if (opponentSalvoesTurn[m] == document.getElementById(opponentSalvoesTurn[m]).id) {
                            if ($.contains(document.getElementById(opponentSalvoesTurn[m]), document.getElementById("S" + opponentSalvoesTurn[m]))) {
                                var newElement = document.createElement('div');
                                newElement.className = "salvo";
                                document.getElementById("S" + opponentSalvoesTurn[m]).appendChild(newElement);
                                var newElement1 = document.createElement('img');
                                newElement1.className = "explosion";
                                newElement1.src = "img/explosion.png";
                                newElement.appendChild(newElement1);
                            } else {
                                var newElement = document.createElement('div');
                                newElement.className = "salvo";
                                document.getElementById(opponentSalvoesTurn[m]).appendChild(newElement);
                                var newElement1 = document.createElement('img');
                                newElement1.className = "explosion";
                                newElement1.src = "img/agua.png";
                                newElement.appendChild(newElement1);
                            }
                            var newElement2 = document.createElement('div');
                            newElement2.className = "turn";
                            document.getElementById(opponentSalvoesTurn[m]).appendChild(newElement2);
                            var newElement3 = document.createElement('h4');
                            newElement3.innerHTML = l;
                            newElement2.appendChild(newElement3);
                        }
                    }
                }
            } else {
                var locations = salvoes[i];
                var playerSalvoes = locations[playerId];
                for (var j in playerSalvoes) {
                    playerSalvoesTurn = playerSalvoes[j]
                    for (var k in playerSalvoesTurn) {
                        if ("A" + playerSalvoesTurn[k] == document.getElementById("A" + playerSalvoesTurn[k]).id) {
                            var newElement = document.createElement('div');
                            newElement.className = "salvo";
                            document.getElementById("A" + playerSalvoesTurn[k]).appendChild(newElement);
                            var newElement1 = document.createElement('img');
                            newElement1.className = "explosion";
                            newElement1.src = "img/explosion.png";
                            newElement.appendChild(newElement1);
                            var newElement2 = document.createElement('div');
                            newElement2.className = "turn";
                            document.getElementById("A" + playerSalvoesTurn[k]).appendChild(newElement2);
                            var newElement3 = document.createElement('h4');
                            newElement3.innerHTML = j;
                            newElement2.appendChild(newElement3);
                            var playerlastTurn = j;
                        }
                    }
                }
            }
        }
        $("#shotButton").click(function () {
            if ($(".shotButton").find("button").css("background-color") == "rgb(128, 128, 128)") {
                window.alert("You need to put three shots!!");
            } else {
                postShots(playerlastTurn, locationShots());
                window.alert("BOOM!!")
                location.reload();
            }
        });
        console.log(playerlastTurn);
        console.log(l);
    });
});

// PlaceShips

$(document).ready(function () {
    var gp = getParameterByName('gp');
    $.getJSON("../api/game_view/" + gp, function (data) {
        var gameId = data.id;
        if (data.state == "placeShips") {
            document.getElementById('gameStatus').innerHTML = "You can place your Ships";
            $('#table1').show();
            $('#table2').hide();
            $('#table3').show();
        } else if (data.state == "wait_opponent") {
            document.getElementById('gameStatus').innerHTML = "Waiting for opponent";
            $('#table1').show();
            $('#table2').show();
            $('#table3').hide();
        } else if (data.state == "wait_ships") {
            document.getElementById('gameStatus').innerHTML = "Waiting for opponent ships";
            $('#table1').show();
            $('#table2').show();
            $('#table3').hide();
        } else if (data.state == "shot") {
            document.getElementById('gameStatus').innerHTML = "You can Shot";
            $('#table1').show();
            $('#table2').show();
            $('#table3').hide();
        } else if (data.state == "wait_shot") {
            document.getElementById('gameStatus').innerHTML = "Waiting for opponent shots";
            $('#table1').show();
            $('#table2').show();
            $('#table3').hide();
        } else if (data.state == "loser") {
            $('#table1').fadeOut(1000);
            $('#table2').fadeOut(1000);
            $('#table3').fadeOut(1000);
            $('#game').fadeOut(1000);
            setTimeout(function () {
                $("#loser").fadeIn(1500);
                $("#youlose").addClass("youlose");
            }, 400);
            setTimeout(function () {
                $("#puny").fadeIn(1500);
                $(".imagepuny").addClass("losepuny");
            }, 1200);
            setTimeout(function () {
                $("#youlose").addClass("animated pulse infinite");
            }, 3000);

        } else if (data.state == "winner") {
            $('#table1').fadeOut(1000);
            $('#table2').fadeOut(1000);
            $('#table3').fadeOut(1000);
            $('#game').fadeOut(1000);
            setTimeout(function () {
                $("#winner").fadeIn(1500);
                $("#youwin").addClass("youlose");
            }, 400);
            setTimeout(function () {
                $("#victory").fadeIn(1500);
                $(".imagevictory").addClass("winvictory");
            }, 1200);
            setTimeout(function () {
                $("#youwin").addClass("animated pulse infinite");
            }, 3000);
        } else {
            $('#table1').show();
            $('#table2').show();
            $('#table3').hide();
        }
    });
});


$(document).ready(function () {
    $.getJSON("../api/games", function (data) {
        if (data.player != null) {
            $('#logoutBtn').show();
            document.getElementById('userOn').innerHTML = data.player.userName;
        } else {
            $('#logoutBtn').hide();
        }
    });
    $("#logoutBtn").click(function () {
        $.post("/api/logout")
            .done(function () {
                window.alert("Logged out!!")
                window.close();
            });
    });
    $("#closeButton").click(function () {
        window.close();
    });
});

//Drag&Drop Ships

var carrier = document.getElementById('drag1');
var battleship = document.getElementById('drag2');
var submarine = document.getElementById('drag3');
var destroyer = document.getElementById('drag4');
var patrolBoat = document.getElementById('drag5');

function shipsintheGrid() {
    var carrierCell = document.getElementById('drag1').parentNode;
    var battleshipCell = document.getElementById('drag2').parentNode;
    var submarineCell = document.getElementById('drag3').parentNode;
    var destroyerCell = document.getElementById('drag4').parentNode;
    var patrolboatCell = document.getElementById('drag5').parentNode;
    var carrierArray = verifyShips(carrierCell, carrier);
    var battleshipArray = verifyShips(battleshipCell, battleship);
    var submarineArray = verifyShips(submarineCell, submarine);
    var destroyerArray = verifyShips(destroyerCell, destroyer);
    var patrolboatArray = verifyShips(patrolboatCell, patrolBoat);
    compareShips(carrierArray, carrier, battleshipArray, battleship);
    compareShips(carrierArray, carrier, submarineArray, submarine);
    compareShips(carrierArray, carrier, destroyerArray, destroyer);
    compareShips(carrierArray, carrier, patrolboatArray, patrolBoat);
    compareShips(battleshipArray, battleship, submarineArray, submarine);
    compareShips(battleshipArray, battleship, destroyerArray, destroyer);
    compareShips(battleshipArray, battleship, patrolboatArray, patrolBoat);
    compareShips(submarineArray, submarine, destroyerArray, destroyer);
    compareShips(submarineArray, submarine, patrolboatArray, patrolBoat);
    compareShips(destroyerArray, destroyer, patrolboatArray, patrolBoat);
}

function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
    setTimeout(function () {
        ev.target.classList.add('hide');
    });
}

function endDrag(ev) {
    ev.target.classList.remove('hide');
}

function drop(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    if (ev.target.tagName == 'DIV' || ev.target.id == 0) {
        return false;
    } else {
        ev.target.appendChild(document.getElementById(data));
    }
    shipsintheGrid();
}

function compareShips(shipArray1, ship1, shipArray2, ship2) {
    for (var i = 0; i < shipArray1.length; i++) {
        for (var j = 0; j < shipArray2.length; j++) {
            if (shipArray1[i] == shipArray2[j]) {
                $("#" + ship1.id).find(".roundShip").css("background-color", "darkred");
                $("#" + ship2.id).find(".roundShip").css("background-color", "darkred");
            }
        }
    }
}

function verifyShips(cell, ship) {
    if (window.getComputedStyle(document.getElementById(ship.id), null).getPropertyValue('flex-direction') == "row") {
        var size = Number(ship.getAttribute('data-size'));
        var lett = cell.id.substring(0, 1);
        var num = Number(cell.id.substring(1, 3));
        var text = [];

        for (var i = 0; i < size; i++) {
            if (($("#" + ship.id).parents("#table1").length == 1)) {
                var customId = (lett + (num + i));
                text.push(customId);
            }
            if ((num + i) >= 11) {
                $("#" + ship.id).find(".roundShip").css("background-color", "darkred");
                //                ship.style.border = "1px solid red";
            } else {
                $("#" + ship.id).find(".roundShip").css("background-color", "#d9d3d3");
            }
        }
    } else if (window.getComputedStyle(document.getElementById(ship.id), null).getPropertyValue('flex-direction') == "column") {
        var size = Number(ship.getAttribute('data-size'));
        var lett = cell.id.substring(0, 1);
        var num = Number(cell.id.substring(1, 3));

        var text = [];

        for (var i = 0; i < size; i++) {
            if (($("#" + ship.id).parents("#table1").length == 1)) {
                var customId = (String.fromCharCode(lett.charCodeAt(0) + i) + num);
                text.push(customId);
            }
            if (String.fromCharCode(lett.charCodeAt(0) + i) >= "K") {
                $("#" + ship.id).find(".roundShip").css("background-color", "darkred");
            } else {
                $("#" + ship.id).find(".roundShip").css("background-color", "#d9d3d3");
            }
        }
    }
    return text;
}

$('.carrier').click(function () {
    $(this).toggleClass('rotated');
    shipsintheGrid();
});

$('.battleship').click(function () {
    $(this).toggleClass('rotated');
    shipsintheGrid();
});

$('.submarine').click(function () {
    $(this).toggleClass('rotated');
    shipsintheGrid();
});

$('.destroyer').click(function () {
    $(this).toggleClass('rotated');
    shipsintheGrid();
});

$('.patrolBoat').click(function () {
    $(this).toggleClass('rotated');
    shipsintheGrid();
});


$("#saveButton").click(function () {
    if ($("#drag1" && "#drag2" && "#drag3" && "#drag4" && "#drag5").parents("#table1").length == 1) {
        if ($("#drag1" || "#drag2" || "#drag3" || "#drag4" || "#drag5").find(".roundShip").css("background-color") == "rgb(139, 0, 0)") {
            window.alert("You need to put the Ships in the grid!!");
        } else {
            var carrierCell = document.getElementById('drag1').parentNode;
            var battleshipCell = document.getElementById('drag2').parentNode;
            var submarineCell = document.getElementById('drag3').parentNode;
            var destroyerCell = document.getElementById('drag4').parentNode;
            var patrolboatCell = document.getElementById('drag5').parentNode;
            calcCells(carrierCell, carrier);
            calcCells(battleshipCell, battleship);
            calcCells(submarineCell, submarine);
            calcCells(destroyerCell, destroyer);
            calcCells(patrolboatCell, patrolBoat);
            window.alert("Ships placed!!")
            location.reload();
        }
    } else {
        window.alert("You need to put all the Ships in the grid!!");
    }
});

function calcCells(cell, ship) {
    if (window.getComputedStyle(document.getElementById(ship.id), null).getPropertyValue('flex-direction') == "row") {
        var size = Number(ship.getAttribute('data-size'));
        var lett = cell.id.substring(0, 1);
        var num = Number(cell.id.substring(1, 3));

        var text = "";

        for (var i = 0; i < size; i++) {
            var customId = (lett + (num + i));
            if (i != (size - 1)) {
                text += (customId + ',');
            } else {
                text += (customId);
            }
        }
        var shipType = ship.className;
        postShip(shipType, text)
    } else if (window.getComputedStyle(document.getElementById(ship.id), null).getPropertyValue('flex-direction') == "column") {
        var size = Number(ship.getAttribute('data-size'));
        var lett = cell.id.substring(0, 1);
        var num = Number(cell.id.substring(1, 3));

        var text = "";

        for (var i = 0; i < size; i++) {
            var customId = (String.fromCharCode(lett.charCodeAt(0) + i) + num);
            if (i != (size - 1)) {
                text += (customId + ',');
            } else {
                text += (customId);
            }
        }
        var shipType = ship.className;
        postShip(shipType, text)
    }

}

function postShip(shipType, cells) {
    var gp = getParameterByName('gp');
    $.post("/api/games/players/" + gp + "/ships", {
            type: shipType,
            cells: cells
        }).done(function () {

        })
        .fail(function () {
            window.alert("Ship error!!");
        });
}

//Funcion disparos

function shoting(td) {
    var gp = getParameterByName('gp');
    $.getJSON("../api/game_view/" + gp, function (data) {
        if (data.state == "shot") {
            if (td.firstChild == null && howmanyShots() <= "2") {
                var newElement = document.createElement('div');
                newElement.className = "shoting";
                td.appendChild(newElement);
                var newElement1 = document.createElement('img');
                newElement1.className = "theShot";
                newElement1.src = "img/torpedo.png";
                newElement.appendChild(newElement1);
                if (howmanyShots() == "3") {
                    $(".shotButton").find("button").css("background-color", "darkred");
                    $('#table1').fadeOut(300).promise().done(function () {
                        $('#table4').fadeIn(300);
                    });
                } else if (howmanyShots() < "3") {
                    $(".shotButton").find("button").css("background-color", "grey");
                }
            } else if (td.firstChild != null && td.firstChild.className == "shoting") {
                $(td.firstChild).remove();
                $(".shotButton").find("button").css("background-color", "grey");
            }
        } else if (data.state == "wait_shot") {
            return false;
        }
    });
}

// Funcion ultimo turno

function lastTurn(playerSalvoesTurn) {
    var biggest = playerSalvoesTurn[0];
    for (var i = 0; i < playerSalvoesTurn.length; i++) {
        if (playerSalvoesTurn[i] > biggest) {
            biggest = playerSalvoesTurn[i];
        }
    }
    biggest = biggest + 1;
    console.log(biggest);
    return biggest;
}

// Contar shots
function howmanyShots() {
    var divs = document.getElementsByTagName("div");
    var numDivs = divs.length;
    var contador = 0;
    var text = "";
    for (var i = 0; i < numDivs; i++) {
        if (divs[i].className == "shoting") {
            contador++;
        }
    }
    return contador;
}

function locationShots() {
    var divs = document.getElementsByTagName("div");
    var numDivs = divs.length;
    var contador = 0;
    var text = "";
    for (var i = 0; i < numDivs; i++) {
        if (divs[i].className == "shoting") {
            var customId = divs[i].parentNode.id;
            customId = customId.substring(1, 4);
            contador++;
            if (contador != 3) {
                text += (customId + ',');
            } else {
                text += (customId);
            }
        }
    }
    return (text);
}

// Funcion Post disparos
function postShots(turn, shotCells) {
    var gp = getParameterByName('gp');
    if (turn != null) {
        turn = parseInt(turn) + 1;
        $.post("/api/games/players/" + gp + "/salvos", {
                turnNumber: turn,
                location: shotCells
            }).done(function () {

            })
            .fail(function () {
                window.alert("Boom error!!");
            });
    } else {
        turn = 1;
        $.post("/api/games/players/" + gp + "/salvos", {
                turnNumber: turn,
                location: shotCells
            }).done(function () {

            })
            .fail(function () {
                window.alert("Boom error!!");
            });
    }
}
