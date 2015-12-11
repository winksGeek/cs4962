package winkler.devon.forbiddendesert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by devonwinkler on 11/16/15.
 */
public class ForbiddenDataModel {

    public static interface SetModeListener{
        public void setMode(GameActivity.Mode mode);
    }

    HashMap<String, Game> _games;
    Game _currentGame;
    Player _itemUsingPlayer;
    SetModeListener _setModeListener;
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

    public HashMap<String, Game> getGames() {
        return _games;
    }

    public ArrayList<Game> getGamesArrayList() {
        ArrayList<Game> games = new ArrayList<>(_games.values());
        Collections.sort(games, Collections.reverseOrder());
        return games;
    }

    public String getStormCardsLeftString() {
       return _currentGame.getCardstoDrawString();
    }

    public Player getCurrentPlayer() {
        return _currentGame.getCurrentPlayer();
    }

    public void setGames(HashMap <String, Game> games){
        _games = games;
    }

    public void setSetModeListener(SetModeListener listener){
        _setModeListener = listener;
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
        DesertTile crashTile = newGame.getCrashTile();
        for(int i = 0; i < numberOfPlayers; i++){
            Player player = new Player(new Role(roles.get(i)));
            player._id = i;
            player._name = "Player " + i;
            player.xPos = crashTile.xPos;
            player.yPos = crashTile.yPos;
            newGame.addPlayer(player);
        }
        _games.put(gameId, newGame);
        return gameId;
    }

    public DesertTile[] getBoard(){
        return _currentGame.get_board();
    }

    public Part[] getParts(){
        return _currentGame.get_parts();
    }

    public Player [] getPlayersForSharingWater() {
        return _currentGame.getPlayersForWaterShare();
    }

    public DesertTile[] getBoard(String gameId){
        Game game = _games.get(gameId);
        return game.get_board();
    }

    public Part[] getParts(String gameId){
        Game game = _games.get(gameId);
        return game.get_parts();
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

    public void movePlayer(int xPos, int yPos, Player player) {
        DesertTile tile = getTileFromBoard(xPos, yPos);
        Player currentPlayer = getCurrentPlayer();
        if(tile.isPassable() || currentPlayer._role._type == Role.Type.Climber) {
            if(_currentGame.hasActionsLeft()) {
                if (_currentGame.movePlayer(xPos, yPos)) {
                    _currentGame.useAction();
                    if(player != null){
                        _currentGame.movePlayer(player, xPos, yPos);
                    }
                    if(_currentGame.isStormActive()){
                        _setModeListener.setMode(GameActivity.Mode.Storm);
                    }
                }
            }
        }
    }

    public void moveStorm(StormCard.Direction direction, StormCard.Places places){
        _currentGame.moveStorm(direction, places);
    }

    public void removeSand(int xPos, int yPos) {
        Player player = _currentGame.getCurrentPlayer();
        if(player.checkLegalSandMove(xPos, yPos)) {
            DesertTile tile = getTileFromBoard(xPos, yPos);
            if(_currentGame.hasActionsLeft()) {
                if (tile.removeSand(player.getNumberOfTilesAbleToRemove())) {
                    _currentGame.addToSandTilesLeft(player.getNumberOfTilesAbleToRemove());
                    _currentGame.useAction();
                    if(_currentGame.isStormActive()){
                        _setModeListener.setMode(GameActivity.Mode.Storm);
                    }
                }
            }
        }
    }

    public void excavate(int xPos, int yPos) {
        DesertTile tile = getTileFromBoard(xPos, yPos);
        Player player = _currentGame.getCurrentPlayer();
        if(player.xPos == tile.xPos && player.yPos == tile.yPos){
            if(_currentGame.hasActionsLeft()) {
                boolean flipped = tile.flipTile();
                if (flipped) {
                    if (tile.type == DesertTile.Type.PieceColumn || tile.type == DesertTile.Type.PieceRow) {
                        _currentGame.revealPart(tile.partHint);
                    }
                    if (tile.type == DesertTile.Type.Item || tile.type == DesertTile.Type.Crash || tile.type == DesertTile.Type.Tunnel) {
                        _currentGame.addItemCardToPlayersHand();
                    }
                    if(tile.type == DesertTile.Type.Oasis){
                        _currentGame.addWaterToPlayersOnTile(xPos, yPos, 2);
                    }
                    _currentGame.useAction();
                    if(_currentGame.isStormActive()){
                        _setModeListener.setMode(GameActivity.Mode.Storm);
                    }
                }
            }
        }
    }

    public Part pickUpPart(int xPos, int yPos){
        DesertTile tile = getTileFromBoard(xPos, yPos);
        Player player = _currentGame.getCurrentPlayer();
        if(player.xPos == tile.xPos && player.yPos == tile.yPos){
            if(_currentGame.hasActionsLeft()) {
                Part partClaimed = tile.claimPart();
                if (partClaimed != null) {
                    _currentGame.collectPart(partClaimed._type);
                    _currentGame.useAction();
                    if(_currentGame.isStormActive()){
                        _setModeListener.setMode(GameActivity.Mode.Storm);
                    }
                    return partClaimed;
                }
            }
        }
        return null;
    }

    public boolean checkForWin(){
        return _currentGame.checkForWin();
    }

    public DesertTile getTileFromBoard(int xPos, int yPos){
        return _currentGame.getTileFromBoard(xPos, yPos);
    }

    public StormCard drawStormCard(){
        StormCard card = _currentGame.dealStormCard();
        if(!_currentGame.isStormActive()){
            _setModeListener.setMode(GameActivity.Mode.Move);
        }
        return card;
    }

    public void playItemCard(ItemCard card) {
        Player player = card.owner;
        Player currentPlayer = _currentGame.getCurrentPlayer();
        DesertTile tile = getTileFromBoard(player.xPos, player.yPos);
        switch(card.type){
            case Solar:
                tile.placeSolarShield();
                player.setTurnTimer(tile);
                break;
            case Jet:
                if(_setModeListener != null) {
                    _itemUsingPlayer = player;
                    _setModeListener.setMode(GameActivity.Mode.Jet);
                }
                break;
            case Throttle:
                if(player._role._type == currentPlayer._role._type && !_currentGame.isStormActive()){
                    currentPlayer._actionsLeft += 2;
                }else{
                    return;
                }
                break;
            case Water:
                _currentGame.addWaterToPlayersOnTile(player.xPos, player.yPos, 2);
                break;
            case Terrascope:
                if(_setModeListener != null) {
                    _setModeListener.setMode(GameActivity.Mode.Terrascope);
                }
                break;
            case Blaster:
                if(_setModeListener != null){
                    _itemUsingPlayer = player;
                    _setModeListener.setMode(GameActivity.Mode.Blaster);
                }
                break;

        }
        player.removeCardFromHand(card);
    }

    public boolean checkForLoss(){
        return _currentGame.checkForLoss();
    }

    public void setGameOver(boolean win) {
        _currentGame.setGameOver(win);
    }

    public void jetPlayer(int xPos, int yPos, Player player) {
        DesertTile tile = getTileFromBoard(xPos, yPos);
        if(tile.isPassable() && _itemUsingPlayer != null) {
            _currentGame.movePlayer(_itemUsingPlayer, xPos, yPos);
            if(player != null){
                _currentGame.movePlayer(player, xPos, yPos);
            }
            _setModeListener.setMode(GameActivity.Mode.Move);
            _itemUsingPlayer = null;
        }
    }

    public Player[] getPlayersOnJetTile() {
        Player[] players = _currentGame.getPlayers();
        ArrayList<Player> tempPlayers = new ArrayList<>();
        for(int i = 0; i < players.length; i++){
            Player player = players[i];
            if(player._role._type != _itemUsingPlayer._role._type){
                if(player.xPos == _itemUsingPlayer.xPos && player .yPos == _itemUsingPlayer.yPos){
                    tempPlayers.add(player);
                }
            }
        }
        return tempPlayers.toArray(new Player[]{});
    }

    public Player[] getPlayersOnCurrentPlayersTile() {
        Player[] players = _currentGame.getPlayers();
        Player currentPlayer = _currentGame.getCurrentPlayer();
        ArrayList<Player> tempPlayers = new ArrayList<>();
        for(int i = 0; i < players.length; i++){
            Player player = players[i];
            if(player._role._type != currentPlayer._role._type){
                if(player.xPos == currentPlayer.xPos && player .yPos == currentPlayer.yPos){
                    tempPlayers.add(player);
                }
            }
        }
        return tempPlayers.toArray(new Player[]{});
    }

    public DesertTile scope(int xPos, int yPos){
        DesertTile tile = getTileFromBoard(xPos, yPos);
        if(tile.status == DesertTile.Status.Unflipped){
            _setModeListener.setMode(GameActivity.Mode.Move);
            return tile;
        }
        return null;
    }

    public void duneBlast(int xPos, int yPos){
        DesertTile tile = getTileFromBoard(xPos, yPos);
        if(_itemUsingPlayer != null) {
            if (_itemUsingPlayer.checkLegalSandMove(xPos, yPos)) {
                if(tile.numberOfSandTiles > 0){
                    _currentGame.addToSandTilesLeft(tile.numberOfSandTiles);
                    tile.removeSand(tile.numberOfSandTiles);
                    _setModeListener.setMode(GameActivity.Mode.Move);
                    _itemUsingPlayer = null;
                }
            }
        }
    }
}
