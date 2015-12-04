package winkler.devon.forbiddendesert;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by devonwinkler on 11/16/15.
 */
public class Game {
    private DesertTile [] _board;
    private DesertTile _crashTile;
    private String _id;
    private Calendar _gameStarted;
    private Player[] _players;
    private int _currentTurn;
    private int _stormIndex;

    public Game(String id, int numberOfPlayers){
        _currentTurn = 0;
        _id = id;
        _gameStarted = Calendar.getInstance();
        _board = generateNewBoard();
        _players = new Player[numberOfPlayers];
        _stormIndex = 12;
        placeSandTiles();
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
            if(desertTile.type == DesertTile.Type.Crash){
                _crashTile = desertTile;
            }
        }
        return board;
    }

    private void placeSandTiles(){
        _board[2].addSandTile();
        _board[6].addSandTile();
        _board[8].addSandTile();
        _board[10].addSandTile();
        _board[14].addSandTile();
        _board[16].addSandTile();
        _board[18].addSandTile();
        _board[22].addSandTile();

    }

    public void addPlayer(Player player){
        _players[player._id] = player;
    }

    public Player getCurrentPlayer(){
        return _players[_currentTurn];
    }

    public boolean sunBeatsDown(){
        boolean gameLost = false;
        for(int i = 0; i < _players.length; i++){
            Player player = _players[i];
            gameLost |= player.drinkWater();
        }
        return gameLost;
    }

    public void changeTurn(){
        _currentTurn++;
        if(_currentTurn >= _players.length){
            _currentTurn = 0;
        }
    }

    public Player[] getPlayers() {
        return _players.clone();
    }

    public void movePlayer(int xPos, int yPos){
        movePlayer(_currentTurn, xPos, yPos);
    }

    public void movePlayer(Player player, int xPos, int yPos){
        player.xPos = xPos;
        player.yPos = yPos;
    }

    public void movePlayer(int id, int xPos, int yPos){
        Player player = _players[id];
        player.xPos = xPos;
        player.yPos = yPos;
    }

    public DesertTile getCrashTile(){
        if(_crashTile != null){
            return _crashTile;
        }
        int i = 0;
        while(i < _board.length && _board[i].type != DesertTile.Type.Crash){
            i++;
        }
        DesertTile tile = _board[i];
        return tile;
    }

    public void moveStorm(StormCard.Direction direction, StormCard.Places places){
        DesertTile stormTile = _board[_stormIndex];
        int placesInt = 0;
        switch (places){
            case One:
                placesInt = 1;
                break;
            case Two:
                placesInt = 2;
                break;
            case Three:
                placesInt = 3;
                break;
        }
        switch (direction){
            case North:
                moveStormNorth(placesInt);
                break;
            case East:
                moveStormEast(placesInt);
                break;
            case South:
                moveStormSouth(placesInt);
                break;
            case West:
                moveStormWest(placesInt);
                break;
        }
    }

    private ArrayList<Player> getPlayersAt(int xPos, int yPos) {
        ArrayList<Player> players = new ArrayList<>();
        for(int i = 0; i < _players.length; i++){
            Player player = _players[i];
            if(player.xPos == xPos && player.yPos == yPos){
                players.add(player);
            }
        }
        return players;
    }

    private void movePlayersOnTile(ArrayList<Player> players, int xPos, int yPos){
        for(Player player:players){
            movePlayer(player, xPos, yPos);
        }
    }

    private void moveStormWest(int placesInt) {
        DesertTile stormTile = _board[_stormIndex];
        if(placesInt == 0 || (stormTile.xPos - 1) < 0)
            return;
        int newIndex = stormTile.xPos-1 + 5*stormTile.yPos;
        DesertTile swapTile = _board[newIndex];
        ArrayList<Player>playersOnTile = getPlayersAt(swapTile.xPos, swapTile.yPos);

        int tempX = swapTile.xPos;
        swapTile.xPos = stormTile.xPos;
        stormTile.xPos = tempX;

        swapTile.addSandTile();
        movePlayersOnTile(playersOnTile, swapTile.xPos, swapTile.yPos);

        _board[newIndex] = _board[_stormIndex];
        _board[_stormIndex] = swapTile;
        _stormIndex = newIndex;
        moveStormWest(placesInt - 1);
    }

    private void moveStormSouth(int placesInt) {
        DesertTile stormTile = _board[_stormIndex];
        if(placesInt == 0 || (stormTile.yPos + 1) > 4)
            return;
        int newIndex = stormTile.xPos + 5*(stormTile.yPos+1);
        DesertTile swapTile = _board[newIndex];
        ArrayList<Player>playersOnTile = getPlayersAt(swapTile.xPos, swapTile.yPos);

        int tempY = swapTile.yPos;
        swapTile.yPos = stormTile.yPos;
        stormTile.yPos = tempY;

        swapTile.addSandTile();
        movePlayersOnTile(playersOnTile, swapTile.xPos, swapTile.yPos);

        _board[newIndex] = _board[_stormIndex];
        _board[_stormIndex] = swapTile;
        _stormIndex = newIndex;

        moveStormSouth(placesInt - 1);
    }

    private void moveStormEast(int placesInt) {
        DesertTile stormTile = _board[_stormIndex];
        if(placesInt == 0 || (stormTile.xPos + 1) > 4)
            return;
        int newIndex = stormTile.xPos+ 1 + 5*stormTile.yPos;
        DesertTile swapTile = _board[newIndex];
        ArrayList<Player>playersOnTile = getPlayersAt(swapTile.xPos, swapTile.yPos);

        int tempX = swapTile.xPos;
        swapTile.xPos = stormTile.xPos;
        stormTile.xPos = tempX;

        swapTile.addSandTile();
        movePlayersOnTile(playersOnTile, swapTile.xPos, swapTile.yPos);

        _board[newIndex] = _board[_stormIndex];
        _board[_stormIndex] = swapTile;
        _stormIndex = newIndex;

        moveStormEast(placesInt - 1);
    }

    private void moveStormNorth(int placesInt) {
        DesertTile stormTile = _board[_stormIndex];
        if(placesInt == 0 || (stormTile.yPos - 1) < 0)
            return;
        int newIndex = stormTile.xPos + 5*(stormTile.yPos-1);
        DesertTile swapTile = _board[newIndex];
        ArrayList<Player>playersOnTile = getPlayersAt(swapTile.xPos, swapTile.yPos);

        int tempY = swapTile.yPos;
        swapTile.yPos = stormTile.yPos;
        stormTile.yPos = tempY;

        swapTile.addSandTile();
        movePlayersOnTile(playersOnTile, swapTile.xPos, swapTile.yPos);

        _board[newIndex] = _board[_stormIndex];
        _board[_stormIndex] = swapTile;
        _stormIndex = newIndex;

        moveStormNorth(placesInt - 1);
    }
}
