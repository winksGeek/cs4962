package winkler.devon.battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by devonwinkler on 10/20/15.
 */
public class BattleshipDataModel {

    private static BattleshipDataModel _Instance = null;
    private Game _currentGame;
    public static BattleshipDataModel getInstance(){
        if(_Instance == null){
            _Instance = new BattleshipDataModel();
        }
        return _Instance;
    }

    private HashMap<Integer, Game> _games;

    private BattleshipDataModel(){
        _games = new HashMap<>();
    }

    public int createNewGame(){
        int id = _games.size();
        _games.put(id, new Game(id));
        return id;
    }

    public int getCurrentGameId(){
        if(_currentGame == null){
            return -1;
        }
        return _currentGame.getGameId();
    }

    public void setCurrentGame(int id){
        _currentGame = _games.get(id);
    }

    public ArrayList<Integer> getGameIds() {
        Integer[] ids = _games.keySet().toArray(new Integer[_games.size()]);
//        Arrays.sort(ids, Collections.reverseOrder());
        ArrayList<Integer> idArray = new ArrayList<>();
        for(int i = 0; i < ids.length; i++){
            idArray.add(ids[i]);
        }
        Collections.sort(idArray, Collections.reverseOrder());

        return idArray;
    }

    public int getCurrentTurn(){
        return _currentGame.get_currentTurn();
    }

    public void changeTurn(){
        _currentGame.changeTurn();
    }

    public int launchMissile(int x, int y){
        return _currentGame.launchMissile(x, y);
    }

    public int[][] getCurrentOpponentBoard(){
        return _currentGame.getOpponentBoard();
    }

    public int[][] getCurrentPlayerBoard(){
        return _currentGame.getPlayerBoard();
    }

    public Game getGameById(int id){
        return _games.get(id);
    }

    public void setGames(HashMap<Integer, Game> games){
        _games = games;
    }

    public HashMap<Integer, Game> getGames(){
        return _games;
    }

    public Game.GameInfoObject getGameDisplayInfo(Integer id){
        return _games.get(id).getDisplayInfo();
    }

    public boolean checkForWin(){
        return _currentGame.checkForWin();
    }

    public void finishGame(){
        _currentGame.finishGame();
    }
}
