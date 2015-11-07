package winkler.devon.battleship;

import android.content.res.Resources;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by devonwinkler on 11/5/15.
 */
public class BattleshipNetworkLayer {
    public static class CreateGameResponse{
        String gameId;
        String playerId;
    }

    public static class GameGuessResponse{
        boolean hit;
        int shipSunk;
    }

    public static class CheckTurnResponse{
        boolean isYourTurn;
        String winner;
    }

    public static class BoardResponse{
        Game.BoardCell[] playerBoard;
        Game.BoardCell[] opponentBoard;
    }

    public static class JoinResponse{
        String playerId;
    }

    public interface BattleshipListener{
        public void onGameCreated(boolean success, CreateGameResponse response);
        public void onGameList(boolean success, GameInfoObject[] games);
        public void onGameJoin(boolean success, JoinResponse playerId);
        public void onGameInfo(boolean success, GameInfoObject game);
        public void onGameGuess(boolean success, GameGuessResponse response);
        public void onGameTurn(boolean success, CheckTurnResponse response);
        public void onGameBoard(boolean success, BoardResponse response);
    }

    public static void getGame(String gameId, final BattleshipListener listener){
        BattleshipRequestTask.BattleshipRequest request = new BattleshipRequestTask.BattleshipRequest();
        BattleshipSession session = BattleshipSession.getInstance();
        BattleshipRequestTask requestTask = new BattleshipRequestTask();
        request.url = session.getSessionVariable("mainApiUrl") + "games/"+gameId;
        request.function = BattleshipRequestTask.GameFunction.INFO;
        request.method = "GET";
        request.listener = listener;
        requestTask.execute(request);
    }

    public static void getGamesList(final BattleshipListener listener){
        BattleshipRequestTask.BattleshipRequest request = new BattleshipRequestTask.BattleshipRequest();
        BattleshipSession session = BattleshipSession.getInstance();
        BattleshipRequestTask requestTask = new BattleshipRequestTask();
        request.url = session.getSessionVariable("mainApiUrl") + "games";
        request.function = BattleshipRequestTask.GameFunction.LIST;
        request.method = "GET";
        request.listener = listener;
        requestTask.execute(request);
    }

    public static void joinGame(String playerName, String gameId, final BattleshipListener listener){
        BattleshipRequestTask.BattleshipRequest request = new BattleshipRequestTask.BattleshipRequest();
        BattleshipSession session = BattleshipSession.getInstance();
        Map<String, String> params = new HashMap<>();
        BattleshipRequestTask requestTask = new BattleshipRequestTask();
        params.put("playerName", playerName);
        request.url = session.getSessionVariable("mainApiUrl") + "games/"+ gameId + "/join";
        request.function = BattleshipRequestTask.GameFunction.JOIN;
        request.method = "POST";
        request.params = new JSONObject(params);
        request.listener = listener;
        requestTask.execute(request);
    }

    public static void createNewGame(String playerName, String gameName, final BattleshipListener listener){
        BattleshipRequestTask.BattleshipRequest request = new BattleshipRequestTask.BattleshipRequest();
        BattleshipSession session = BattleshipSession.getInstance();
        Map<String, String> params = new HashMap<>();
        BattleshipRequestTask requestTask = new BattleshipRequestTask();
        params.put("playerName", playerName);
        params.put("gameName",gameName);
        request.url = session.getSessionVariable("mainApiUrl") + "games";
        request.function = BattleshipRequestTask.GameFunction.CREATE;
        request.method = "POST";
        request.params = new JSONObject(params);
        request.listener = listener;
        requestTask.execute(request);
    }

    public static void fireMissile(int x, int y, final BattleshipListener listener){
        BattleshipRequestTask.BattleshipRequest request = new BattleshipRequestTask.BattleshipRequest();
        BattleshipSession session = BattleshipSession.getInstance();
        Map<String, String> params = new HashMap<>();
        BattleshipRequestTask requestTask = new BattleshipRequestTask();
        String playerId = (String)session.getSessionVariable("playerId");
        String gameId = (String)session.getSessionVariable("gameId");
        params.put("playerId", playerId);
        params.put("xPos",Integer.toString(x));
        params.put("yPos",Integer.toString(y));
        request.url = session.getSessionVariable("mainApiUrl") + "games/" + gameId + "/guess";
        request.function = BattleshipRequestTask.GameFunction.GUESS;
        request.method = "POST";
        request.params = new JSONObject(params);
        request.listener = listener;
        requestTask.execute(request);
    }

    public static void checkTurn(final BattleshipListener listener){
        BattleshipRequestTask.BattleshipRequest request = new BattleshipRequestTask.BattleshipRequest();
        BattleshipSession session = BattleshipSession.getInstance();
        Map<String, String> params = new HashMap<>();
        BattleshipRequestTask requestTask = new BattleshipRequestTask();
        String playerId = (String)session.getSessionVariable("playerId");
        String gameId = (String)session.getSessionVariable("gameId");
        params.put("playerId", playerId);
        request.url = session.getSessionVariable("mainApiUrl") + "games/" + gameId + "/status";
        request.function = BattleshipRequestTask.GameFunction.TURN;
        request.method = "POST";
        request.params = new JSONObject(params);
        request.listener = listener;
        requestTask.execute(request);
    }

    public static void getBoards(final BattleshipListener listener){
        BattleshipRequestTask.BattleshipRequest request = new BattleshipRequestTask.BattleshipRequest();
        BattleshipSession session = BattleshipSession.getInstance();
        Map<String, String> params = new HashMap<>();
        BattleshipRequestTask requestTask = new BattleshipRequestTask();
        String playerId = (String)session.getSessionVariable("playerId");
        String gameId = (String)session.getSessionVariable("gameId");
        params.put("playerId", playerId);
        request.url = session.getSessionVariable("mainApiUrl") + "games/" + gameId + "/board";
        request.function = BattleshipRequestTask.GameFunction.BOARD ;
        request.method = "POST";
        request.params = new JSONObject(params);
        request.listener = listener;
        requestTask.execute(request);
    }




}
