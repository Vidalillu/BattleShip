$(document).ready(function () {
    $.getJSON("../api/games", function (data) {
        if (data.player != null) {
            $('#modal').hide();
            $('#logoutBtn').show();
            $('#newgameButton').show();
            document.getElementById('userOn').innerHTML = data.player.userName;
        } else {
            $('#logoutBtn').hide();
            $('#modal').show();
            $('#newgameButton').hide();
        }
        var games = data.games
        $.each(games, function (i, item) {
            if (games[i].gamePlayers[1] != null) {
                if (data.player != null && (games[i].gamePlayers[0].player.userName == data.player.userName)) {
                    if (games[i].gamePlayers[0].score != null) {
                        var newElement = document.createElement('tr');
                        var milliseconds = games[i].created;
                        var myDate = new Date(milliseconds);
                        newElement.innerHTML = "<td>" + myDate.toLocaleString() + "</td>" + "<td>" + games[i].gamePlayers[0].player.userName + "</td>" + "<td>" + games[i].gamePlayers[0].score + "</td>" + "<td>" + games[i].gamePlayers[1].player.userName + "</td>" + "<td>" + games[i].gamePlayers[1].score + "</td>" + '<td><a class="gameButton" href="game.html?gp=' + games[i].gamePlayers[0].id + '" target="_blank"><button>CONTINUE</button></a></td>';
                        document.getElementById('board').appendChild(newElement);
                    } else {
                        var newElement = document.createElement('tr');
                        var milliseconds = games[i].created;
                        var myDate = new Date(milliseconds);
                        newElement.innerHTML = "<td>" + myDate.toLocaleString() + "</td>" + "<td>" + games[i].gamePlayers[0].player.userName + "</td>" + "<td>" + "</td>" + "<td>" + games[i].gamePlayers[1].player.userName + "</td>" + "<td>" + "</td>" + '<td><a class="gameButton" href="game.html?gp=' + games[i].gamePlayers[0].id + '" target="_blank"><button>CONTINUE</button></a></td>';
                        document.getElementById('board').appendChild(newElement);
                    }
                } else if (data.player != null && (games[i].gamePlayers[1].player.userName == data.player.userName)) {
                    if (games[i].gamePlayers[0].score != null) {
                        var newElement = document.createElement('tr');
                        var milliseconds = games[i].created;
                        var myDate = new Date(milliseconds);
                        newElement.innerHTML = "<td>" + myDate.toLocaleString() + "</td>" + "<td>" + games[i].gamePlayers[0].player.userName + "</td>" + "<td>" + games[i].gamePlayers[0].score + "</td>" + "<td>" + games[i].gamePlayers[1].player.userName + "</td>" + "<td>" + games[i].gamePlayers[1].score + "</td>" + '<td><a class="gameButton" href="game.html?gp=' + games[i].gamePlayers[1].id + '" target="_blank"><button>CONTINUE</button></a></td>';
                        document.getElementById('board').appendChild(newElement);
                    } else {
                        var newElement = document.createElement('tr');
                        var milliseconds = games[i].created;
                        var myDate = new Date(milliseconds);
                        newElement.innerHTML = "<td>" + myDate.toLocaleString() + "</td>" + "<td>" + games[i].gamePlayers[0].player.userName + "</td>" + "<td>" + "</td>" + "<td>" + games[i].gamePlayers[1].player.userName + "</td>" + "<td>" + "</td>" + '<td><a class="gameButton" href="game.html?gp=' + games[i].gamePlayers[1].id + '" target="_blank"><button>CONTINUE</button></a></td>';
                        document.getElementById('board').appendChild(newElement);
                    }
                } else {
                    if (games[i].gamePlayers[0].score != null) {
                        var newElement = document.createElement('tr');
                        var milliseconds = games[i].created;
                        var myDate = new Date(milliseconds);
                        newElement.innerHTML = "<td>" + myDate.toLocaleString() + "</td>" + "<td>" + games[i].gamePlayers[0].player.userName + "</td>" + "<td>" + games[i].gamePlayers[0].score + "</td>" + "<td>" + games[i].gamePlayers[1].player.userName + "</td>" + "<td>" + games[i].gamePlayers[1].score + "</td>" + "</td>" + "<td>";
                        document.getElementById('board').appendChild(newElement);
                    } else {
                        var newElement = document.createElement('tr');
                        var milliseconds = games[i].created;
                        var myDate = new Date(milliseconds);
                        newElement.innerHTML = "<td>" + myDate.toLocaleString() + "</td>" + "<td>" + games[i].gamePlayers[0].player.userName + "</td>" + "<td>" + "</td>" + "<td>" + games[i].gamePlayers[1].player.userName + "</td>" + "<td>" + "</td>" + "</td>" + "<td>";
                        document.getElementById('board').appendChild(newElement);
                    }
                }
            } else {
                if (data.player != null && (games[i].gamePlayers[0].player.userName == data.player.userName)) {
                    var newElement = document.createElement('tr');
                    var milliseconds = games[i].created;
                    var myDate = new Date(milliseconds);
                    newElement.innerHTML = "<td>" + myDate.toLocaleString() + "</td>" + "<td>" + games[i].gamePlayers[0].player.userName + "</td>" + "<td>" + "</td>" + "<td>" + "</td>" + "<td>" + "</td>" + '<td><a class="gameButton" href="game.html?gp=' + games[i].gamePlayers[0].id + '" target="_blank"><button>CONTINUE</button></a></td>';
                    document.getElementById('board').appendChild(newElement);
                } else if (data.player != null && (games[i].gamePlayers[0].player.userName != data.player.userName)) {
                    var newElement = document.createElement('tr');
                    var milliseconds = games[i].created;
                    var myDate = new Date(milliseconds);
                    newElement.innerHTML = "<td>" + myDate.toLocaleString() + "</td>" + "<td>" + games[i].gamePlayers[0].player.userName + "</td>" + "<td>" + "</td>" + "<td>" + "</td>" + "<td>" + "</td>" + '<td><a class="joinButton" id="game' + games[i].id + '"><button>JOIN</button></a></td>';
                    document.getElementById('board').appendChild(newElement);
                    $('#game' + games[i].id).click(function () {
                        $.post("/api/game/" + games[i].id + "/players")
                            .done(function () {
                                window.alert("Join Succesfull!!");
                                window.open("game.html?gp=" + biggestId(data));
                                location.reload();
                            })
                            .fail(function () {
                                window.alert("Join error!!");
                            });
                    });
                } else {
                    var newElement = document.createElement('tr');
                    var milliseconds = games[i].created;
                    var myDate = new Date(milliseconds);
                    newElement.innerHTML = "<td>" + myDate.toLocaleString() + "</td>" + "<td>" + games[i].gamePlayers[0].player.userName + "</td>" + "<td>" + "</td>" + "<td>" + "</td>" + "<td>" + "</td>" + "<td>" + "</td>";
                    document.getElementById('board').appendChild(newElement);
                }
            }
        })
        $('#table1').dataTable({
            "scrollCollapse": true,
            "paging": false,
            "scrollX": false,
            "info": false,
            "order": [[3, "desc"]]
        });
    });
    $.getJSON("../api/leaderboard", function (data) {
        for (var i in data) {
            var newElement = document.createElement('tr');
            newElement.innerHTML = "<td>" + data[i].player + "</td>" + "<td>" + data[i].totalScore + "</td>" + "<td>" + data[i].won + "</td>" + "<td>" + data[i].lost + "<td>" + data[i].tied + "</td>";
            document.getElementById('board2').appendChild(newElement);
        }
        $('#table2').dataTable({
            "scrollCollapse": true,
            "paging": false,
            "scrollX": false,
            "info": false,
            "order": [[2, "desc"]]
        });

    });
});

$(document).ready(function () {
    $("#loginBtn").click(function () {
        var usuario = document.login.email.value;
        var password = document.login.pwd.value;
        $.post("/api/login", {
                userName: usuario,
                password: password
            })
            .done(function () {
                window.alert("Logged in!!")
                location.reload();
            })
            .fail(function () {
                window.alert("Login error!!");
            });
    });
    $("#logoutBtn").click(function () {
        $.post("/api/logout")
            .done(function () {
                window.alert("Logged out!!")
                location.reload();
            });
    });
});

$(document).ready(function () {
    $("#registerBtn").click(function () {
        var usuario = document.register.newemail.value;
        var password = document.register.newpwd.value;
        $.post("/api/players", {
                userName: usuario,
                password: password
            })
            .done(function () {
                window.alert("Registration done!!")
                location.reload();
            })
            .fail(function () {
                window.alert("Registration error!!");
            });
    });
});

$(document).ready(function () {
    $.getJSON("../api/games", function (data) {
        $("#newgameButton").click(function () {
            $.post("/api/games")
                .done(function () {
                        window.alert("New Game created!!")
                        window.open("game.html?gp=" + biggestId(data));
                        location.reload();
                })
                .fail(function () {
                    window.alert("Registration error!!");
                });
        });
    });
});

// Funcion ultima Id

function biggestId(data) {
    var biggest = data.games[0].gamePlayers[0].id;
    for (var i = 0; i < data.games.length; i++) {
        for (var j = 0; j < data.games[i].gamePlayers.length; j++) {
            if (data.games[i].gamePlayers[j].id > biggest) {
                biggest = data.games[i].gamePlayers[j].id;
            }
        }
    }
    biggest = biggest + 1;
    return biggest;
}
