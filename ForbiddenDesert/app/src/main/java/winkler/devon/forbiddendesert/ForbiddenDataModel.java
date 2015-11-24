package winkler.devon.forbiddendesert;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by devonwinkler on 11/16/15.
 */
public class ForbiddenDataModel {
    HashMap<String, Game> _games;
    Game _currentGame;
    private static ForbiddenDataModel _Instance = null;
    public static ForbiddenDataModel getInstance(){
        if(_Instance==null){
            _Instance = new ForbiddenDataModel();
        }
        return _Instance;
    }
    private ForbiddenDataModel(){
        _games = new HashMap<>();
    }
    public void setGames(HashMap <String, Game> games){
        _games = games;
    }
    public String addGame(int numberOfPlayers){
        HashMap<Integer, Role.Type> roles = new HashMap<>();
        RoleRandom rand = new RoleRandom();
        for(int i = 0; i < numberOfPlayers; i++){
            roles.put(i, rand.next());
        }
        return addGame(numberOfPlayers, roles);
    }
    public String addGame(int numberOfPlayers, HashMap<Integer, Role.Type> roles){
        String gameId = generateGameId();
        Game newGame = new Game(gameId, numberOfPlayers);
        for(int i = 0; i < numberOfPlayers; i++){
            Player player = new Player(new Role(roles.get(i)));
            player._id = i;
            player._name = "Player " + i;
            newGame.addPlayer(player);
        }
        _games.put(gameId, newGame);
        return gameId;
    }
    public DesertTile[] getBoard(String gameId){
        Game game = _games.get(gameId);
        return game.get_board();
    }

    private String generateGameId(){
        UUID id = UUID.randomUUID();
        return id.toString();
    }

    public void addPlayerToGame(String gameId, Player player) {
        Game game = _games.get(gameId);
        game.addPlayer(player);
    }

    public Player[] getPlayersForGame(String gameId){
        Game game = _games.get(gameId);
        return game.getPlayers();
    }

    public Player[] getPlayersForGame(){
        return _currentGame.getPlayers();
    }

    public void setCurrentGame(String gameId){
        _currentGame = _games.get(gameId);
    }

    public void movePlayer(int xPos, int yPos) {
        _currentGame.movePlayer(xPos, yPos);
    }

    public void moveStorm(StormCard.Direction direction, StormCard.Places places){
        _currentGame.moveStorm(direction, places);
    }
}
