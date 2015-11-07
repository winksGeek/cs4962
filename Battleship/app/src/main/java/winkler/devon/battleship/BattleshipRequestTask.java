package winkler.devon.battleship;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by devonwinkler on 11/5/15.
 */
public class BattleshipRequestTask extends AsyncTask<BattleshipRequestTask.BattleshipRequest, Void, BattleshipRequestTask.BattleshipResponse> {
    public static enum GameFunction{
        JOIN, CREATE, LIST, INFO, TURN, BOARD, GUESS
    }

    public static class BattleshipResponse{
        public GameFunction function;
        public boolean success;
        public String json;
        BattleshipNetworkLayer.BattleshipListener listener;
    }

    public static class BattleshipRequest{
        String url;
        String method;
        JSONObject params;
        GameFunction function;
        BattleshipNetworkLayer.BattleshipListener listener;
    }

    @Override
    protected BattleshipResponse doInBackground(BattleshipRequest[] params) {
        BattleshipRequest request = params[0];
        BattleshipResponse response = new BattleshipResponse();
        try {
            URL requestURL = new URL(request.url);
            HttpURLConnection connection = (HttpURLConnection)requestURL.openConnection();
            connection.setRequestMethod(request.method);
            connection.addRequestProperty("Content-Type", "application/json");
            if("POST".equals(request.method) || "PUT".equals(request.method)){
                connection.setDoOutput(true);
                OutputStreamWriter body = new OutputStreamWriter(connection.getOutputStream());
                body.write(request.params.toString());
                body.flush();
                body.close();
            }
            int code = connection.getResponseCode();
            Scanner responseScanner = new Scanner(connection.getInputStream());
            StringBuilder responseSB = new StringBuilder();
            while(responseScanner.hasNext()){
                responseSB.append(responseScanner.nextLine());
            }
            response.function = request.function;
            response.listener = request.listener;
            response.json = responseSB.toString();
            response.success = code == 200;
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(BattleshipResponse response) {
        BattleshipNetworkLayer.BattleshipListener listener = response.listener;
        Gson gson = new Gson();
        try {
            switch (response.function) {
                case INFO:
                    listener.onGameInfo(response.success, gson.fromJson(response.json, GameInfoObject.class));
                    break;
                case LIST:
                    listener.onGameList(response.success, gson.fromJson(response.json, GameInfoObject[].class));
                    break;
                case JOIN:
                    listener.onGameJoin(response.success, gson.fromJson(response.json, BattleshipNetworkLayer.JoinResponse.class));
                    break;
                case CREATE:
                    listener.onGameCreated(response.success, gson.fromJson(response.json, BattleshipNetworkLayer.CreateGameResponse.class));
                    break;
                case GUESS:
                    listener.onGameGuess(response.success, gson.fromJson(response.json, BattleshipNetworkLayer.GameGuessResponse.class));
                    break;
                case TURN:
                    listener.onGameTurn(response.success, gson.fromJson(response.json, BattleshipNetworkLayer.CheckTurnResponse.class));
                    break;
                case BOARD:
                    listener.onGameBoard(response.success, gson.fromJson(response.json, BattleshipNetworkLayer.BoardResponse.class));
                    break;
            }
        }catch(IllegalStateException ex){
            ex.printStackTrace();
        }
    }
}
