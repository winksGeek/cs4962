package winkler.devon.forbiddendesert;

import java.util.HashMap;
import java.util.List;

/**
 * Created by devonwinkler on 11/16/15.
 */
public class ForbiddenDataModel {
    HashMap<String, Game> _games;
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
        HashMap<Integer, Player.Role> roles = new HashMap<>();
        RoleRandom rand = new RoleRandom();
        for(int i = 0; i < numberOfPlayers; i++){
            roles.put(i, rand.next());
        }
        return addGame(numberOfPlayers, roles);
    }
    public String addGame(int numberOfPlayers, HashMap<Integer, Player.Role> roles){
        String gameId = generateGameId();
        Game newGame = new Game(gameId, numberOfPlayers);
        _games.put(gameId, newGame);
        return gameId;
    }
    public DesertTile[] getBoard(String gameId){
        Game game = _games.get(gameId);
        return game.get_board();
    }

    private String generateGameId(){
        return "test";
    }
}
