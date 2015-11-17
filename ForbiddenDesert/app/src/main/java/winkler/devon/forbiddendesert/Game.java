package winkler.devon.forbiddendesert;

import java.util.Calendar;

/**
 * Created by devonwinkler on 11/16/15.
 */
public class Game {
    private DesertTile [] _board;
    private String _id;
    private Calendar _gameStarted;
    private Player[] _players;
    private int _currentTurn;

    public Game(String id, int numberOfPlayers){
        _currentTurn = 0;
        _id = id;
        _gameStarted = Calendar.getInstance();
        _board = generateNewBoard();
        _players = new Player[numberOfPlayers];
    }

    public DesertTile[] get_board(){
        return _board;
    }

    private DesertTile[] generateNewBoard(){
        DesertTile[] board = new DesertTile[25];
        TileDeck tiles = new TileDeck();
        int j = 0;
        for(int i = 0; i < board.length; i++){
            DesertTile desertTile;
            if(i == 12){
               desertTile = tiles.getStormTile();
            }else{
                desertTile = tiles.getNext();
            }
            desertTile.xPos = i%5;
            desertTile.yPos = j;
            if(i%5 == 4){
                j++;
            }
            board[i] = desertTile;
        }
        return board;
    }

}
