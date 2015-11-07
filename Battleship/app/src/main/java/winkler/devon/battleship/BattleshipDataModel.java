package winkler.devon.battleship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by devonwinkler on 10/20/15.
 */
public class BattleshipDataModel implements BattleshipNetworkLayer.BattleshipListener{

    public interface BattleshipListController{
        public void onListReceived(GameInfoObject[] games);
//        public void onGameCreated(String gameId, String playerId);
        public void onGameJoined(String gameId, String playerId);
        public void onGameDetailsReceived(GameInfoObject gameInfoObject);
    }

    public interface BattleshipGameController{
        public void onBoardsReceived(Game.BoardCell[] playerBoard, Game.BoardCell[] opponentBoard);
        public void onGuessReceived(boolean hit, int shipSunk);
        public void onTurnCheckReceived(boolean isMyturn, String winner);
    }

    private static BattleshipDataModel _Instance = null;
    private String _currentGame;
    private BattleshipListController _listController;
    private BattleshipGameController _gameController;

    public static BattleshipDataModel getInstance(){
        if(_Instance == null){
            _Instance = new BattleshipDataModel();
        }
        return _Instance;
    }

    private HashMap<String, String> _myGames;

    private BattleshipDataModel(){
        _myGames = new HashMap<>();
    }

    public void setListController(BattleshipListController controller){
        _listController = controller;
    }

    public void setGameController(BattleshipGameController controller){
        _gameController = controller;
    }

    public void createNewGame(String gameName, String playerName){
        BattleshipNetworkLayer.createNewGame(playerName, gameName, this);
    }

    public void prepareToJoinGame(String gameId){
        _myGames.put("joining", gameId);
    }

    public void joinGame(String playerName, String gameId) {
        BattleshipNetworkLayer.joinGame(playerName, gameId, this);
    }

    public void checkTurn(){
        BattleshipNetworkLayer.checkTurn(this);
    }

    public boolean isMyGame(String id) {
        return _myGames.containsKey(id);
    }

    public void loadBoards(){
        BattleshipNetworkLayer.getBoards(this);
    }

    public void loadGameIntoSession(String gameId){
        BattleshipSession session =  BattleshipSession.getInstance();
        String playerId = _myGames.get(gameId);
        session.setSessionVariable("playerId", playerId);
        session.setSessionVariable("gameId", gameId);
    }

    public void loadGameList(){
        BattleshipNetworkLayer.getGamesList(this);
    }

    public void loadGameDetails(String gameId){
        BattleshipNetworkLayer.getGame(gameId, this);
    }

    public String getCurrentGameId(){
        if(_currentGame == null){
            return "";
        }
        return _currentGame;
    }

    public void setCurrentGame(String id){
        _currentGame = id;
    }

    public ArrayList<Integer> getGameIds() {
        Integer[] ids = _myGames.keySet().toArray(new Integer[_myGames.size()]);
//        Arrays.sort(ids, Collections.reverseOrder());
        ArrayList<Integer> idArray = new ArrayList<>();
        for(int i = 0; i < ids.length; i++){
            idArray.add(ids[i]);
        }
        Collections.sort(idArray, Collections.reverseOrder());

        return idArray;
    }

//    public int getCurrentTurn(){
//        return _currentGame.get_currentTurn();
//    }

//    public void changeTurn(){
//        _currentGame.changeTurn();
//    }

    public void launchMissile(int x, int y){
        BattleshipNetworkLayer.fireMissile(x, y, this);
    }

//    public int[][] getCurrentOpponentBoard(){
//        return _currentGame.getOpponentBoard();
//    }
//
//    public int[][] getCurrentPlayerBoard(){
//        return _currentGame.getPlayerBoard();
//    }

//    public Game getGameById(int id){
//        return _myGames.get(id);
//    }
//
    public void setGames(HashMap<String, String> games){
        _myGames = games;
    }
//
    public HashMap<String, String> getGames() {
        return _myGames;
    }
//
//    public Game.GameInfoObject getGameDisplayInfo(Integer id){
//        return _myGames.get(id).getDisplayInfo();
//    }
//
//    public boolean checkForWin(){
//        return _currentGame.checkForWin();
//    }
//
//    public void finishGame(){
//        _currentGame.finishGame();
//    }

    @Override
    public void onGameCreated(boolean success, BattleshipNetworkLayer.CreateGameResponse response) {
        if(success) {
            _myGames.put(response.gameId, response.playerId);
//            _listController.onGameCreated(response.gameId, response.playerId);
        }
    }

    @Override
    public void onGameList(boolean success, GameInfoObject[] games) {
        if(success && _listController != null){
            _listController.onListReceived(games);
        }
    }

    @Override
    public void onGameJoin(boolean success, BattleshipNetworkLayer.JoinResponse response) {
        String gameId = _myGames.get("joining");
        if(success) {
            _myGames.put(gameId, response.playerId);
            _listController.onGameJoined(gameId, response.playerId);
        }
        _myGames.remove("joining");
    }

    @Override
    public void onGameInfo(boolean success, GameInfoObject game) {
        if(success){
            _listController.onGameDetailsReceived(game);
        }
    }

    @Override
    public void onGameGuess(boolean success, BattleshipNetworkLayer.GameGuessResponse response) {
        if(success){
            _gameController.onGuessReceived(response.hit, response.shipSunk);
        }
    }

    @Override
    public void onGameTurn(boolean success, BattleshipNetworkLayer.CheckTurnResponse response) {
        if(success){
            BattleshipSession session = BattleshipSession.getInstance();
            session.setSessionVariable("myTurn", response.isYourTurn);
            if(response.isYourTurn){
                session.setSessionVariable("winner", response.winner);
            }
            _gameController.onTurnCheckReceived(response.isYourTurn, response.winner);
        }
    }

    @Override
    public void onGameBoard(boolean success, BattleshipNetworkLayer.BoardResponse response) {
        if(success){
            _gameController.onBoardsReceived(response.playerBoard, response.opponentBoard);
        }
    }
}
