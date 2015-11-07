package winkler.devon.battleship;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class BattleshipMainActivity extends Activity implements View.OnClickListener,  GameListFragment.GameListItemListener, GameDetailFragment.GameBoardListener, BattleshipDataModel.BattleshipListController {

    BattleshipDataModel _model = BattleshipDataModel.getInstance();
    public static final String MENU_LIST_TAG = "MENU_LIST_TAG";
    public static final String GAME_DETAIL_TAG = "GAME_DETAIL_TAG";
    public static final String GAME_INFO_TAG = "GAME_INFO_TAG";
    public static final String WAIT_OPPONENT_TAG = "WAIT_OPPONENT_TAG";
    boolean _isTablet;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        _isTablet = isTablet();
        _model.setListController(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        FrameLayout menuLayout = new FrameLayout(this);
        menuLayout.setId(10);
        layout.addView(menuLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        GameListFragment gameListFragment = (GameListFragment)getFragmentManager().findFragmentByTag(MENU_LIST_TAG);
        if(gameListFragment == null){
            gameListFragment = GameListFragment.newInstance(new ArrayList<GameInfoObject>());
            transaction.add(menuLayout.getId(), gameListFragment, MENU_LIST_TAG);
        }
        transaction.commit();
        _model.loadGameList();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                _model.loadGameList();
            }
        },0, 3000);
        setContentView(layout);
    }

    private boolean isTablet(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float scaleFactor = getResources().getDisplayMetrics().density;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float widthDpi = metrics.xdpi;
        float heightDpi = metrics.ydpi;

        float widthInches = (float)widthPixels / widthDpi;
        float heightInches = (float)heightPixels / heightDpi;

        double screenSize = Math.sqrt(widthInches * widthInches + heightInches*heightInches);
        return true;
    }
    public int getOpponentTurn(int currentTurn){
        return currentTurn == 1?0:1;
    }

//    @Override
//    public Game.GameInfoObject getGameInformation(int id) {
//        return _model.getGameDisplayInfo(id);
//    }

    @Override
    public void openGame(GameInfoObject gameInfoObject) {
        String status = gameInfoObject.status;
        if("WAITING".equals(status)){
            if(!_model.isMyGame(gameInfoObject.id)) {
                _model.prepareToJoinGame(gameInfoObject.id);
                Intent joinGameIntent = new Intent(this, InfoFormActivity.class);
                joinGameIntent.putExtra("create", false);
                joinGameIntent.putExtra("gameId", gameInfoObject.id);
                startActivityForResult(joinGameIntent, InfoFormActivity.JOIN_GAME_INTENT);
            }else{
                _model.loadGameDetails(gameInfoObject.id);
            }
        }else if("PLAYING".equals(status)){
            boolean isMyGame = _model.isMyGame(gameInfoObject.id);
            if(isMyGame){
                startGame(gameInfoObject.id);
            }else{
                _model.loadGameDetails(gameInfoObject.id);
            }
        }else if("DONE".equals(status)){
            _model.loadGameDetails(gameInfoObject.id);
        }
    }

//    @Override
    public void loadGame(GameDetailFragment gameDetailFragment, String id) {
//        if(_model.getCurrentGameId() >= 0) {
//            gameDetailFragment.setPlayerBoard(_model.getCurrentPlayerBoard());
//            gameDetailFragment.setOpponentBoard(_model.getCurrentOpponentBoard());
//        }
    }

    public void startGame(String id){
        Intent gameActivityIntent = new Intent(this, BattleshipGameActivity.class);
        gameActivityIntent.putExtra("id", id);
        startActivity(gameActivityIntent);
    }

    public void viewGameDetails(GameInfoObject gameInfoObject){
        Intent gameInfoIntent = new Intent(this, BattleshipGameDisplayActivity.class);
        gameInfoIntent.putExtra("gameInfoObject", gameInfoObject);
        startActivity(gameInfoIntent);

    }

    //For New Game Button
    @Override
    public void onClick(View v) {
        Intent createGameIntent = new Intent(this, InfoFormActivity.class);
        createGameIntent.putExtra("create", true);
        startActivityForResult(createGameIntent, InfoFormActivity.CREATE_GAME_INTENT);
    }

    @Override
    public void launchMissile(int x, int y) {
        _model.launchMissile(x, y);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == InfoFormActivity.CREATE_GAME_INTENT){
            String gameName = data.getStringExtra("gameName");
            String playerName = data.getStringExtra("playerName");
            _model.createNewGame(gameName, playerName);
        }
        if(resultCode == InfoFormActivity.JOIN_GAME_INTENT){
            String playerName = data.getStringExtra("playerName");
            String gameId = data.getStringExtra("gameId");
            _model.joinGame(playerName, gameId);
        }
//        if(requestCode == BlindActivity.BLIND_REQUEST_CODE) {
//            GameDetailFragment gameDetailFragment = (GameDetailFragment) getFragmentManager().findFragmentByTag(BattleshipMainActivity.GAME_DETAIL_TAG);
//            gameDetailFragment.setOpponentBoard(_model.getCurrentOpponentBoard());
//            gameDetailFragment.setPlayerBoard(_model.getCurrentPlayerBoard());
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Gson gson = new Gson();
        String jsonString = gson.toJson(_model.getGames());
        try{
            File file = new File(getFilesDir(), "gameModel.txt");
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(jsonString);
            writer.close();

        }catch (Exception e){
            Log.e("Persistence", "Error saving file: " + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            File file = new File(getFilesDir(), "gameModel.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            String jsonString = reader.readLine();

            Gson gson = new Gson();
            Type collectionType = new TypeToken<HashMap<String, String>>(){}.getType();
            HashMap<String, String> games = gson.fromJson(jsonString, collectionType);
            _model.setGames(games);
            GameListFragment gameListFragment = (GameListFragment)getFragmentManager().findFragmentByTag(MENU_LIST_TAG);
//            gameListFragment.setGameList(_model.getGameIds());
            gameListFragment.setCurrentGameId(_model.getCurrentGameId());
        }catch (Exception e){
            Log.e("Persistence", "Error loading file: " + e.getMessage());
        }
    }

    @Override
    public void onListReceived(GameInfoObject[] games) {
        GameListFragment gameListFragment = (GameListFragment)getFragmentManager().findFragmentByTag(MENU_LIST_TAG);
        ArrayList<GameInfoObject> gamesArray = new ArrayList<>();
        for(int i = 0; i < games.length; i++) {
            GameInfoObject gameInfoObject = games[i];
            gamesArray.add(gameInfoObject);
        }
        gameListFragment.setGameList(gamesArray);
    }

//    @Override
//    public void onGameCreated(String gameId, String playerId) {
//        startGame(gameId);
//    }

    @Override
    public void onGameJoined(String gameId, String playerId) {
        startGame(gameId);
    }

    @Override
    public void onGameDetailsReceived(GameInfoObject gameInfoObject){
        viewGameDetails(gameInfoObject);
    }

    public void onMissileFired(boolean hit, int shipSunk){
        if(hit) {
//                _model.changeTurn();
//                Intent intent = new Intent(this, BlindActivity.class);
//                intent.putExtra("result", result);
//                intent.putExtra("nextPlayer", _model.getCurrentTurn());
//                startActivityForResult(intent, BlindActivity.BLIND_REQUEST_CODE);
        }
    }
}
