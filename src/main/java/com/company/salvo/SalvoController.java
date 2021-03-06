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
    public ResponseEntity<String> createUser(@RequestParam String userName, @RequestParam String email, @RequestParam String password) {
        if (userName.isEmpty()) {
            return new ResponseEntity<>("No name given", HttpStatus.FORBIDDEN);
        }

        Player player = playerrepo.findByUserName(userName);
        if (player != null) {
            return new ResponseEntity<>("Name already used", HttpStatus.CONFLICT);
        }

        playerrepo.save(new Player(userName, email, password));
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
        salvoRepo.save(new Salvo(turnNumber, location, gameplayer));
        return new ResponseEntity("BOOM!!", HttpStatus.CREATED);
    }

    @Autowired
    private ScoreRepository scoreRepo;
//
//    @RequestMapping(path = "/game/{gameId}/scores", method = RequestMethod.POST)
//    public ResponseEntity<Map<String, Object>> playerScores(@PathVariable Long gameId, Authentication authentication, @RequestParam Integer score) {
//        Game game = gamerepo.findOne(gameId);
//        Player loggedPlayer = playerrepo.findByUserName(authentication.getName());
//
//        if (loggedPlayer == null) {
//            return new ResponseEntity("You have to be logged first!!", HttpStatus.UNAUTHORIZED);
//        }
//
//        scoreRepo.save(new Score(loggedPlayer, game, score, new Date()));
//        return new ResponseEntity("Score placed", HttpStatus.CREATED);
//    }

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
                    }else if (enemy == gameplayer) {
                        opponentShips.get(x);
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
                dto.put("state", getgameState(gameplayer, enemy, opponentShips));

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
            if(!gP.equals(gameplayer)) {
                return gP;
            }
        }
        System.out.println("Fuck You!!");
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
        return list;
    }

    private List<List<String>> getopponentshipLocations(GamePlayer gameplayer){



        Set<Ship>opponentShips = gameplayer.getShips();

        List<List<String>> list = new ArrayList<>();

        for(Ship ship: opponentShips) {
                list.add(ship.getLocation());
        }
        return list;

    }

    private GamePlayer getgameOwner (GamePlayer gameplayer, GamePlayer enemy){

        GamePlayer ownergamePlayer = null;

        if (gameplayer.getId() < enemy.getId()) {
            ownergamePlayer = gameplayer;
        } else {
            ownergamePlayer = enemy;
        }
        return ownergamePlayer;
    }

    private GamePlayer getgameJoiner (GamePlayer gameplayer, GamePlayer enemy){

        GamePlayer joinergamePlayer = null;

        if (gameplayer.getId() > enemy.getId()) {
            joinergamePlayer = gameplayer;
        } else {
            joinergamePlayer = enemy;
        }
        return joinergamePlayer;
    }

    private String getgameState (GamePlayer gameplayer, GamePlayer enemy, List<List<String>> opponentShips) {

        String state = new String();

        GamePlayer ownergamePlayer = getgameOwner(gameplayer, enemy);

        GamePlayer joinergamePlayer = getgameJoiner(gameplayer, enemy);

        List<String> gameplayerShips = getgameplayerShips(gameplayer);

        List<String> opponentSalvos = getopponentSalvos(enemy);

        if (ownergamePlayer == gameplayer) {
            if (ownergamePlayer.getShips().isEmpty()) {
                state = "placeShips";
            }
            if (!ownergamePlayer.getShips().isEmpty() && gameplayer == enemy){
                state = "wait_opponent";
            }
            if (!ownergamePlayer.getShips().isEmpty() && joinergamePlayer.getShips().isEmpty()) {
                state = "wait_ships";
            }
            if (gameplayer != enemy && !ownergamePlayer.getShips().isEmpty() && !joinergamePlayer.getShips().isEmpty() && (joinergamePlayer.getSalvos().size() > ownergamePlayer.getSalvos().size())) {
                state = "shot";
            }
            if (gameplayer != enemy && !ownergamePlayer.getShips().isEmpty() && !joinergamePlayer.getShips().isEmpty() && joinergamePlayer.getSalvos().size() == ownergamePlayer.getSalvos().size()) {
                state = "wait_shot";
            }
            if (opponnentshipSunks(opponentShips) == 5){
                Score winner = new Score(ownergamePlayer.getPlayer(), ownergamePlayer.getGame(), 3,new Date());
                Score losser = new Score(joinergamePlayer.getPlayer(), joinergamePlayer.getGame(), 0,new Date());
                scoreRepo.save(winner);
                scoreRepo.save(losser);
                state = "winner";
            }
            if(ownergamePlayer.getScore() != null && ownergamePlayer.getScore().getScore() == 3){
                state = "winner";
            }
            if (!ownergamePlayer.getShips().isEmpty() && !joinergamePlayer.getShips().isEmpty() && opponentSalvos.containsAll(gameplayerShips)){
                state = "loser";
            }
            if(ownergamePlayer.getScore() != null && ownergamePlayer.getScore().getScore() == 0) {
                state = "loser";
            }
        }
        if (gameplayer != enemy && joinergamePlayer == gameplayer) {
            if (joinergamePlayer.getShips().isEmpty()) {
                state = "placeShips";
            }
            if (!joinergamePlayer.getShips().isEmpty() && ownergamePlayer.getShips().isEmpty()) {
                state = "wait_ships";
            }
            if (!ownergamePlayer.getShips().isEmpty() && !joinergamePlayer.getShips().isEmpty() && joinergamePlayer.getSalvos().size() > ownergamePlayer.getSalvos().size()) {
                state = "wait_shot";
            }
            if (!ownergamePlayer.getShips().isEmpty() && !joinergamePlayer.getShips().isEmpty() && joinergamePlayer.getSalvos().size() == ownergamePlayer.getSalvos().size()) {
                state = "shot";
            }
            if (opponnentshipSunks(opponentShips) == 5){
                Score winner = new Score(joinergamePlayer.getPlayer(), joinergamePlayer.getGame(), 3,new Date());
                Score losser = new Score(ownergamePlayer.getPlayer(), ownergamePlayer.getGame(), 0,new Date());
                scoreRepo.save(winner);
                scoreRepo.save(losser);
                state = "winner";
            }
            if(joinergamePlayer.getScore() != null && joinergamePlayer.getScore().getScore() == 3) {
                state = "winner";
            }
            if (!ownergamePlayer.getShips().isEmpty() && !joinergamePlayer.getShips().isEmpty() && opponentSalvos.containsAll(gameplayerShips)){
                state = "loser";
            }
            if(joinergamePlayer.getScore() != null && joinergamePlayer.getScore().getScore() == 0) {
                state = "loser";
            }
        }
        return state;
    }

    private int opponnentshipSunks(List<List<String>> opponentShips) {

        int num = 0;

        for(int x=0;x<opponentShips.size();x++) {
            if (opponentShips.get(x).contains("sunk")) {
                num += 1;
            } else {
                num += 0;
            }
        }
        return num;
    }

    private List<String> getgameplayerShips(GamePlayer gameplayer){

        Set<Ship>gameplayerShips = gameplayer.getShips();

        List<String> list = new ArrayList<>();

        for(Ship ship: gameplayerShips) {
            for(String location: ship.getLocation()){
                list.add(location);
            }
        }
        return list;
    }

    private List<String> getopponentSalvos(GamePlayer gameplayer){

        Set<Salvo>opponentSalvos = gameplayer.getSalvos();

        List<String> list = new ArrayList<>();

        for(Salvo salvo: opponentSalvos) {
            for(String location: salvo.getLocation()) {
                list.add(location);
            }
        }
        return list;
    }

//    private List<String> getshipLocation(Ship ship){
//
//        ArrayList<String> shipLocation = new ArrayList<String>(ship.getLocation());
//
//        for(int i=0; i < shipLocation.size(); i++){
//            if (shipLocation.get(i) == "sunk") {
//                System.out.println(i);
//                shipLocation.remove(i);
//            }
//        }
//        System.out.println(shipLocation);
//        return shipLocation;
//    }
}