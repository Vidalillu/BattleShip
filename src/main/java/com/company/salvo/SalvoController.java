package com.company.salvo;

import java.util.*;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gamerepo;

    @Autowired
    private PlayerRepository playerrepo;

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestParam String userName, @RequestParam String password) {
        if (userName.isEmpty()) {
            return new ResponseEntity<>("No name given", HttpStatus.FORBIDDEN);
        }

        Player player = playerrepo.findByUserName(userName);
        if (player != null) {
            return new ResponseEntity<>("Name already used", HttpStatus.CONFLICT);
        }

        playerrepo.save(new Player(userName, password));
        return new ResponseEntity<>("Named added", HttpStatus.CREATED);
    }


    @RequestMapping(path = "/games", method = RequestMethod.GET)
    public Map<String, Object> getAllGames(Authentication authentication) {

        Map<String, Object> dto = new LinkedHashMap<String, Object>();

        if (authentication != null) {
            Player currentPlayer = playerrepo.findByUserName(authentication.getName());
            dto.put("player", currentPlayer);
        }
        dto.put("games",gamerepo
                .findAll()
                .stream()
                .map(game -> makeGameDTO(game))
                .collect(Collectors.toList()));

        return dto;

    }

    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", game.getGamePlayers()
                .stream()
                .map(gamePlayer -> makeGamePlayerDTO(gamePlayer))
                .collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", gamePlayer.getPlayer());
        if (gamePlayer.getScore() != null) {
            dto.put("score", gamePlayer.getScore().getScore());
        }
        return dto;
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity createGame(Authentication authentication) {
        Player loggedPlayer = playerrepo.findByUserName(authentication.getName());
        if (loggedPlayer != null) {
            Game game = gamerepo.save(new Game(new Date()));
            gameplayerRepo.save(new GamePlayer(loggedPlayer, game, new Date()));
            return new ResponseEntity<>("Game added", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("You have to be logged first!!", HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(path = "/game/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long gameId, Authentication authentication) {
        Game game = gamerepo.findOne(gameId);
        Player loggedPlayer = playerrepo.findByUserName(authentication.getName());
        if (loggedPlayer == null) {
            return new ResponseEntity("You have to be logged first!!", HttpStatus.UNAUTHORIZED);
        }
        if (gamerepo.findOne(gameId) == null) {
            return new ResponseEntity("No such game", HttpStatus.FORBIDDEN);
        }
        if (game.getGamePlayers().size() >= 2) {
            return new ResponseEntity("Game is full", HttpStatus.FORBIDDEN);
        }
        gameplayerRepo.save(new GamePlayer(loggedPlayer, game, new Date()));
        return new ResponseEntity("Game joined", HttpStatus.CREATED);
    }

    @Autowired
    private ShipRepository shipRepo;

    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> placeShips(@PathVariable Long gamePlayerId, Authentication authentication, @RequestParam String type, @RequestParam List<String> cells) {
        GamePlayer gameplayer = gameplayerRepo.findOne(gamePlayerId);
        Player loggedPlayer = playerrepo.findByUserName(authentication.getName());
        Player currentPlayer = gameplayer.getPlayer();

        if (loggedPlayer == null) {
            return new ResponseEntity("You have to be logged first!!", HttpStatus.UNAUTHORIZED);
        }
        if (gameplayerRepo.findOne(gamePlayerId) == null) {
            return new ResponseEntity("No such game", HttpStatus.UNAUTHORIZED);
        }
        if (loggedPlayer != currentPlayer) {
            return new ResponseEntity("It's not your game!!", HttpStatus.UNAUTHORIZED);
        }
        if (gameplayer.getShips().size() >= 5) {
            return new ResponseEntity("You have alredy placed the ships", HttpStatus.FORBIDDEN );
        }
        shipRepo.save(new Ship(type, cells, gameplayer));
        return new ResponseEntity("Game joined", HttpStatus.CREATED);
    }

    @Autowired
    private SalvoRepository salvoRepo;

    @RequestMapping(path = "/games/players/{gamePlayerId}/salvos", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> placeSalvo(@PathVariable Long gamePlayerId, Authentication authentication, @RequestParam int turnNumber, @RequestParam List<String> location) {
        GamePlayer gameplayer = gameplayerRepo.findOne(gamePlayerId);
        Player loggedPlayer = playerrepo.findByUserName(authentication.getName());
        Player currentPlayer = gameplayer.getPlayer();

        if (loggedPlayer == null) {
            return new ResponseEntity("You have to be logged first!!", HttpStatus.UNAUTHORIZED);
        }
        if (gameplayerRepo.findOne(gamePlayerId) == null) {
            return new ResponseEntity("No such game", HttpStatus.UNAUTHORIZED);
        }
        if (loggedPlayer != currentPlayer) {
            return new ResponseEntity("It's not your game!!", HttpStatus.UNAUTHORIZED);
        }
//        if (!gameplayer.getSalvos().isEmpty()) {
//            return new ResponseEntity("You have alredy shot this turn", HttpStatus.FORBIDDEN );
//        }
        salvoRepo.save(new Salvo(turnNumber, location, gameplayer));
        return new ResponseEntity("BOOM!!", HttpStatus.CREATED);
    }

    @Autowired
    private GamePlayerRepository gameplayerRepo;

    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> findGamePlayer(@PathVariable Long gamePlayerId, Authentication authentication) {

        Player loggedPlayer = playerrepo.findByUserName(authentication.getName());
        GamePlayer gameplayer = gameplayerRepo.findOne(gamePlayerId);
        Player currentPlayer = gameplayer.getPlayer();

            if (loggedPlayer == currentPlayer) {

                Game game = gameplayer.getGame();

                GamePlayer enemy = getEnemy(gameplayer);

                List<String> gameplayerSalvo = getgameplayerLocations(gameplayer);

                List<List<String>> opponentShips = getopponentshipLocations(enemy);

                for(int x=0;x<opponentShips.size();x++) {
                    if(gameplayerSalvo.containsAll(opponentShips.get(x))){
                        opponentShips.get(x).retainAll(gameplayerSalvo);
                        opponentShips.get(x).add("sunk");
                    }else{
                        opponentShips.get(x).retainAll(gameplayerSalvo);
                    }
                }



                Map<String, Object> dto = new LinkedHashMap<String, Object>();
                dto.put("id", game.getId());
                dto.put("created", game.getCreationDate());
                dto.put("gamePlayers", game.getGamePlayers()
                        .stream()
                        .map(gamePlayer -> makeGamePlayerDTO(gamePlayer))
                        .collect(Collectors.toList()));
                dto.put("ships", gameplayer.getShips()
                        .stream()
                        .map(ship -> makeShipDTO(ship))
                        .collect(Collectors.toList()));
                dto.put("salvoes", gameplayer.getGame().getGamePlayers()
                        .stream()
                        .map(salvo -> makeSalvoDTO(salvo))
                        .collect(Collectors.toList()));
                dto.put("hits", opponentShips);

                return new ResponseEntity(dto, HttpStatus.OK);

            }
            return new ResponseEntity("It's not your game!!", HttpStatus.UNAUTHORIZED);
        }

        private Map<String, Object> makeShipDTO (Ship ship){
            Map<String, Object> dto = new LinkedHashMap<String, Object>();
            dto.put("type", ship.getShipType());
            dto.put("locations", ship.getLocation());
            return dto;
        }

        private Map<String, Object> makeSalvoDTO (GamePlayer gP1){
            Map<String, Object> dto = new LinkedHashMap<String, Object>();

            String playerIdString = Long.toString(gP1.getPlayer().getId());

            Map<String, Object> turns = new LinkedHashMap<String, Object>();

            for (Salvo salvo : gP1.getSalvos()) {
                String turnNumber = Long.toString(salvo.getTurnNumber());
                turns.put(turnNumber, salvo.getLocation());
            }

            dto.put(playerIdString, turns);
            return dto;
        }


    @Autowired
    private PlayerRepository playerScorerepo;

    @RequestMapping("/leaderboard")
    public List<Map<String, Object>> getAllScores() {
        return playerScorerepo
                .findAll()
                .stream()
                .map(player -> makeScoresDTO(player))
                .collect(Collectors.toList());
    }

    private Map<String, Object> makeScoresDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("player", player.getUserName());
        dto.put("totalScore", player.totalScores(player));
        dto.put("won", player.wonScores(player));
        dto.put("lost", player.lostScores(player));
        dto.put("tied", player.tiedScores(player));
        return dto;
    }

    private GamePlayer getEnemy(GamePlayer gameplayer){
        Game game = gameplayer.getGame();

        Set<GamePlayer>gamePlayers = game.getGamePlayers();

        for(GamePlayer gP: gamePlayers) {
            System.out.println(gP);
            if(!gP.equals(gameplayer)) {
                return gP;
            }
        }
        return gameplayer;
    }

    private List<String> getgameplayerLocations(GamePlayer gameplayer){

        Set<Salvo>gameplayerSalvos = gameplayer.getSalvos();

        List<String> list = new ArrayList<>();

        for(Salvo salvo: gameplayerSalvos) {
            for(String location: salvo.getLocation()){
                list.add(location);
            }
        }
        System.out.println(list);
        return list;
    }

    private List<List<String>> getopponentshipLocations(GamePlayer gameplayer){

        Set<Ship>opponentShips = gameplayer.getShips();

        List<List<String>> list = new ArrayList<>();

        for(Ship ship: opponentShips) {
                list.add(ship.getLocation());
        }
        System.out.println(list);
        return list;
    }

}