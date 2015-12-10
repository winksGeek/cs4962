package winkler.devon.forbiddendesert;

import android.widget.Toast;

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
    private Part [] _parts;
    private int _currentTurn;
    private boolean _stormActive;
    private int _stormIndex;
    private StormDeck _stormDeck;
    private ItemDeck _itemDeck;
    private int [] _stormCardsPerLevel;
    private int _stormLevel;
    private int _cardsToDraw;
    private int _sandTilesLeft;
    private String gameOver;

    public Game(String id, int numberOfPlayers){
        _currentTurn = 0;
        _id = id;
        _gameStarted = Calendar.getInstance();
        _sandTilesLeft = 48;
        _board = generateNewBoard();
        _players = new Player[numberOfPlayers];
        _stormIndex = 12;
        placeSandTiles();
        _parts = new Part[4];
        _parts[0] = new Part(Part.Type.Crystal,0,0);
        _parts[1] = new Part(Part.Type.Propeller,1,0);
        _parts[2] = new Part(Part.Type.Navigation,2,0);
        _parts[3] = new Part(Part.Type.Engine,3,0);
        _stormDeck = new StormDeck();
        _itemDeck = new ItemDeck();
        _stormActive = false;
        _stormCardsPerLevel = fillStormLevels(numberOfPlayers);
        _stormLevel = 0;
        _cardsToDraw = 0;
        gameOver = "No";
    }

    private int[] fillStormLevels(int numberOfPlayers) {
        int [] twoPlayerStormLevels = new int []{2,3,3,3,4,4,4,4,5,5,5,6,6};
        int [] threePlayerStormLevels = new int []{2,3,3,3,3,4,4,4,4,5,5,5,6,6};
        int [] fourPlayerStormLevels = new int []{2,3,3,3,3,4,4,4,4,5,5,5,6,6};
        int [] fivePlayerStormLevels = new int []{2,3,3,3,3,3,4,4,4,4,5,5,5,6,6};
        switch(numberOfPlayers){
            case 3:
                return threePlayerStormLevels;
            case 4:
                return fourPlayerStormLevels;
            case 5:
                return fivePlayerStormLevels;
        }
        return twoPlayerStormLevels;
    }

    public DesertTile[] get_board(){
        return _board;
    }

    public Part[] get_parts(){
        return _parts;
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
        _sandTilesLeft -= 8;

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
            if(checkPlayerInOpen(player)) {
                gameLost |= player.drinkWater();
            }
        }
        return gameLost;
    }

    private boolean checkPlayerInOpen(Player player) {
        boolean playerInOpen = true;
        int index = player.yPos * 5 + player.xPos;
        DesertTile tile = _board[index];
        if(tile.type == DesertTile.Type.Tunnel && tile.status == DesertTile.Status.Flipped) {
            playerInOpen = false;
        }
        if(tile.coveredBySolarShield()){
            playerInOpen = false;
        }
        return playerInOpen;
    }

    public void startStormTurn(){
        _players[_currentTurn].setActive(false);
        _cardsToDraw = _stormCardsPerLevel[_stormLevel];
        _stormActive = true;
    }

    public void startNextPlayerTurn(){
        _stormActive = false;
        _currentTurn++;
        if(_currentTurn >= _players.length){
            _currentTurn = 0;
        }
        _players[_currentTurn]._actionsLeft = 4;
        _players[_currentTurn].setActive(true);
        if(_players[_currentTurn]._placedASolarShield != null){
            _players[_currentTurn].removeTurnTimer();
        }
    }

    public Player[] getPlayers() {
        return _players.clone();
    }

    public boolean movePlayer(int xPos, int yPos){
        return movePlayer(_currentTurn, xPos, yPos);
    }

    public void movePlayer(Player player, int xPos, int yPos){
        player.xPos = xPos;
        player.yPos = yPos;
    }

    public boolean movePlayer(int id, int xPos, int yPos){
        Player player = _players[id];
        DesertTile playerTile = getTileFromBoard(player.xPos, player.yPos);
        DesertTile moveTile = getTileFromBoard(xPos, yPos);
        if(playerTile.numberOfSandTiles < 2 || player._role._type == Role.Type.Climber) {
            if (player.checkLegalMove(xPos, yPos) || ((playerTile.type == DesertTile.Type.Tunnel && playerTile.status == DesertTile.Status.Flipped) && (moveTile.type == DesertTile.Type.Tunnel && moveTile.status == DesertTile.Status.Flipped))) {
                player.xPos = xPos;
                player.yPos = yPos;
                return true;
            }
        }
        return false;
    }

    public void useAction(){
        Player currentPlayer = _players[_currentTurn];
        if(!_stormActive) {
            currentPlayer._actionsLeft--;
            if (currentPlayer._actionsLeft <= 0) {
                startStormTurn();
            }
        }
    }

    public boolean hasActionsLeft(){
        Player currentPlayer = _players[_currentTurn];
        return currentPlayer._actionsLeft > 0;
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
        _sandTilesLeft--;
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
        _sandTilesLeft--;
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
        _sandTilesLeft--;
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
        _sandTilesLeft--;
        movePlayersOnTile(playersOnTile, swapTile.xPos, swapTile.yPos);

        _board[newIndex] = _board[_stormIndex];
        _board[_stormIndex] = swapTile;
        _stormIndex = newIndex;

        moveStormNorth(placesInt - 1);
    }

    public void revealPart(Part.Type type){
        DesertTile columnTile = null;
        DesertTile rowTile = null;
        for(int i = 0; i < _board.length; i++){
            DesertTile tile = _board[i];
            if(tile.partHint == type && tile.status == DesertTile.Status.Flipped){
                if(tile.type == DesertTile.Type.PieceColumn) {
                    columnTile = tile;
                }
                else {
                    rowTile = tile;
                }
            }
        }
        if(columnTile != null && rowTile != null){
            int xPos = columnTile.xPos;
            int yPos = rowTile.yPos;
            int index = yPos * 5 + xPos;
            DesertTile partTile = _board[index];
            partTile.discoverPart(new Part(type, xPos, yPos));
        }
    }

    public void collectPart(Part.Type type){
        for(int i = 0; i < _parts.length; i++){
            Part part = _parts[i];
            if(part._type == type){
                part.collected = true;
            }
        }
        checkForWin();
    }

    public boolean checkForWin() {
        boolean win = true;
        for(int i = 0; i < _parts.length; i++){
            Part part = _parts[i];
            win = win && part.collected;
        }
        return win;
    }

    public StormCard dealStormCard(){
        if(_stormActive){
           StormCard card =  _stormDeck.getNext();
            switch (card.type){
                case Sun:
                    sunBeatsDown();
                    break;
                case Storm:
                    increaseStormLevel();
                    break;
                case Move:
                    moveStorm(card.direction, card.places);
                    break;
            }
            _cardsToDraw--;
            if(_cardsToDraw <=0){
                startNextPlayerTurn();
            }
            return card;
        }
        return null;
    }

    public boolean increaseStormLevel(){
        _stormLevel++;
        return _stormLevel >= _stormCardsPerLevel.length;
    }

    public void addItemCardToPlayersHand() {
        Player player = _players[_currentTurn];
        ItemCard card = _itemDeck.getNext();
        if(card != null) {
            player.addCardToHand(card);
        }
    }

    public DesertTile getTileFromBoard(int xPos, int yPos){
        int index = yPos * 5 + xPos;
        DesertTile tile =_board[index];
        return tile;
    }

    public boolean checkForLoss(){
        boolean loss = false;
        loss = loss || _sandTilesLeft <= 0;
        loss = loss || playerDiedOfThirst();
        loss = loss || _stormLevel >= _stormCardsPerLevel.length;
        return loss;
    }

    private boolean playerDiedOfThirst(){
        boolean playerDied = false;
        for(int i = 0; i < _players.length; i++){
            Player player = _players[i];
            playerDied = playerDied || player.getWaterLeft() < 0;
        }
        return playerDied;
    }

    public void setGameOver(boolean win) {
        if(win){
            gameOver = "Won";
        }else{
            gameOver = "Lost";
        }

    }

    public boolean isStormActive() {
        return _stormActive;
    }

    public void addWaterToPlayersOnTile(int xPos, int yPos, int amount) {
        for(int i = 0; i < _players.length; i++){
            Player player = _players[i];
            if(player.xPos == xPos && player.yPos == yPos){
                player.addWater(amount);
            }
        }
    }
}
