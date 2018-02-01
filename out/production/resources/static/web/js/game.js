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
        for (var c = 0; c < x; c++) {
            if (c == 0) {
                col += "<td id='" + c + "'><h3>" + tbodyArray[r] + "</h3></td>";
                col2 += "<td id='A" + c + "'><h3>" + tbodyArray[r] + "</h3></td>";

            } else {
                col += "<td id='" + tbodyArray[r] + c + "'></td>";
                col2 += "<td id='A" + tbodyArray[r] + c + "'></td>";
            }
        }
        $("#board").append("<tr>" + col + "</tr>");
        $("#board2").append("<tr>" + col2 + "</tr>");
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
    $.getJSON("../api/game_view/" + gp, function (data) {
        if (data.gamePlayers[1] != null) {
            if (data.gamePlayers[0].id == gp) {
                document.getElementById('game').innerHTML = "GAME " + data.id;
                var newElement = document.createElement('h3');
                newElement.innerHTML = data.gamePlayers[0].player.userName + " (you) vs " + data.gamePlayers[1].player.userName;
                document.getElementById('players').appendChild(newElement);
                playerId = data.gamePlayers[0].player.id;
                opponentId = data.gamePlayers[1].player.id;
            } else {
                document.getElementById('game').innerHTML = "GAME " + data.id;
                var newElement = document.createElement('h3');
                newElement.innerHTML = data.gamePlayers[0].player.userName + " vs " + data.gamePlayers[1].player.userName + " (you)";
                document.getElementById('players').appendChild(newElement);
                playerId = data.gamePlayers[1].player.id;
                opponentId = data.gamePlayers[0].player.id;
            }
        } else {
            document.getElementById('game').innerHTML = "GAME " + data.id;
            var newElement = document.createElement('h3');
            newElement.innerHTML = data.gamePlayers[0].player.userName + " (you) vs nobody"
            document.getElementById('players').appendChild(newElement);
            playerId = data.gamePlayers[0].player.id;
        }
        var ships = data.ships
        for (var i in ships) {
            var locations = ships[i].locations;
            console.log(ships[i].locations);
            for (var j in locations) {
                console.log(locations[j]);
                if (locations[j] == document.getElementById(locations[j]).id) {
                    var newElement = document.createElement('div');
                    newElement.className = "ship";
                    newElement.id = "S" + locations[j];
                    document.getElementById(locations[j]).appendChild(newElement);
                    console.log(locations[j]);
                }
            }
        }
        var salvoes = data.salvoes
        for (var i in salvoes) {
            if (data.gamePlayers[1] != null) {
                var locations = salvoes[i];
                var playerSalvoes = locations[playerId];
                var opponentSalvoes = locations[opponentId];
                console.log(playerSalvoes);
                console.log(opponentSalvoes);
                for (var j in playerSalvoes) {
                    console.log(playerSalvoes[j]);
                    playerSalvoesTurn = playerSalvoes[j]
                    for (var k in playerSalvoesTurn) {
                        console.log(playerSalvoesTurn[k])
                        if ("A" + playerSalvoesTurn[k] == document.getElementById("A" + playerSalvoesTurn[k]).id) {
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
                            var newElement3 = document.createElement('h3');
                            newElement3.innerHTML = j;
                            newElement2.appendChild(newElement3);
                        }
                    }
                }
                for (var l in opponentSalvoes) {
                    console.log(opponentSalvoes[l]);
                    opponentSalvoesTurn = opponentSalvoes[l]
                    for (var m in opponentSalvoesTurn) {
                        console.log(opponentSalvoesTurn[m])
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
                            var newElement3 = document.createElement('h3');
                            newElement3.innerHTML = l;
                            newElement2.appendChild(newElement3);
                        }
                    }
                }
            } else {
                var locations = salvoes[i];
                var playerSalvoes = locations[playerId];
                console.log(playerSalvoes);
                console.log(opponentSalvoes);
                for (var j in playerSalvoes) {
                    console.log(playerSalvoes[j]);
                    playerSalvoesTurn = playerSalvoes[j]
                    for (var k in playerSalvoesTurn) {
                        console.log(playerSalvoesTurn[k])
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
                            var newElement3 = document.createElement('h3');
                            newElement3.innerHTML = j;
                            newElement2.appendChild(newElement3);
                        }
                    }
                }
            }
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
                location.reload();
            });
    });
});
