package winkler.devon.battleship;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

public class Game{

    public static final int HITS_TO_WIN = 17;
    public static enum BoardCellStatus{
        HIT, MISS, SHIP, NONE
    }

    public static class BoardCell{
        int xPos;
        int yPos;
        BoardCellStatus status;
    }

    private int _currentTurn;
    private int _gameId;
    private int[] _hits;
    int [][] _player1board;
    int [][] _player2board;
    int [][][] _boards;
    int [] _missilesLaunched;
    int _gridSize;
    boolean _active;
    String _gameStatus;


    public Game(int gameId){
        _currentTurn = 0;
        _gridSize = 10;
        _player1board = new int[_gridSize][_gridSize];
        _player2board = new int[_gridSize][_gridSize];
        _boards = new int[][][]{_player1board,_player2board};
        _missilesLaunched = new int[]{0,0};
        _hits = new int[]{0,0};
        _gameId = gameId;
        _gameStatus = "In Progress";
        _active = true;
        initializeBoards();
    }

    private void initializeBoards(){
        int [] boatSizes = new int[]{2,3,3,4,5};
        int i, j;
        for(i = 0; i < _gridSize; i++){
            for(j=0; j<_gridSize;j++){
                _player1board[i][j] = 0;
                _player2board[i][j] = 0;
            }
        }
        for(i = 0; i < boatSizes.length; i++){
            placeShip(boatSizes[i], _player1board);
            placeShip(boatSizes[i], _player2board);
        }
    }

    private void placeShip(int length, int[][]board){
        boolean placed = false;
        while(!placed) {
            int[] pos = getRandomShipPosition();
            int x = pos[0];
            int y = pos[1];
            boolean row = new Random().nextBoolean();
            if (placeShip(x, y, length, board, row)) {
                placed = true;
            } else if (placeShip(x, y, length, board, !row)) {
                placed = true;
            }
        }
    }

    private boolean placeShip(int x, int y, int shipLength, int[][]board,boolean row){
        boolean canPlace = true;
        if(row){
            canPlace = x + shipLength < board.length;
            if(canPlace) {
                for (int i = 0; i < shipLength; i++) {
                    canPlace = canPlace && board[x + i][y] != 1;
                }
            }
            if(canPlace){
                for (int i = 0; i < shipLength; i++) {
                    board[x + i][y] = 1;
                }
            }
        }else{
            canPlace = y + shipLength < board[x].length;
            if(canPlace) {
                for (int i = 0; i < shipLength; i++) {
                    canPlace = canPlace && board[x][y + i] != 1;
                }
            }
            if(canPlace) {
                for (int i = 0; i < shipLength; i++) {
                    board[x][y + i] = 1;
                }
            }
        }
        return canPlace;
    }

    private int[] getRandomShipPosition(){
        Random rand = new Random();
        int x = rand.nextInt(10);
        int y = rand.nextInt(10);
        return new int[]{x,y};
    }

    public int launchMissile(int x, int y){
        int [][] opponentBoard = getOpponentBoard();
        int contained = opponentBoard[x][y];
        int result = 0;
        if(_active) {
            if (contained < 1) {
                opponentBoard[x][y] = 2;
                result = 2;
            }
            if (contained == 1) {
                opponentBoard[x][y] = 3;
                result = 3;
                _hits[_currentTurn]++;
            }
            if(result > 1) {
                _missilesLaunched[_currentTurn]++;
            }
        }
        return result;
    }

    public boolean checkForWin(){
        if(_hits[_currentTurn] >= HITS_TO_WIN){
            return true;
        }
        return false;
    }

    public int get_currentTurn() {
        return _currentTurn;
    }

    public int[][] getPlayerBoard(){
        return _boards[_currentTurn];
    }

    public int[][] getOpponentBoard(){
        int opponentIndex = get_currentTurn() == 1?0:1;
        return _boards[opponentIndex];
    }

    public void changeTurn(){
        _currentTurn = _currentTurn == 1?0:1;
    }

    public int getGameId(){
        return _gameId;
    }

    public void finishGame(){
        String winner = _currentTurn == 0?"Player 1":"Player 2";
        _gameStatus = "Finished, Winner: " + winner;
        _active = false;
    }
}