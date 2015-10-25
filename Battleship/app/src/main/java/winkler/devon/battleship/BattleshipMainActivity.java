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


public class BattleshipMainActivity extends Activity implements View.OnClickListener,  GameListFragment.GameListItemListener, GameDetailFragment.GameBoardListener {

    BattleshipDataModel _model = BattleshipDataModel.getInstance();
    public static final String MENU_LIST_TAG = "MENU_LIST_TAG";
    public static final String GAME_DETAIL_TAG = "GAME_DETAIL_TAG";
    boolean _isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _isTablet = isTablet();

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        FrameLayout menuLayout = new FrameLayout(this);
        menuLayout.setId(10);
        layout.addView(menuLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        GameListFragment gameListFragment = (GameListFragment)getFragmentManager().findFragmentByTag(MENU_LIST_TAG);
        if(gameListFragment == null){
            gameListFragment = GameListFragment.newInstance(_model.getGameIds());
            transaction.add(menuLayout.getId(), gameListFragment, MENU_LIST_TAG);
        }
        if(_isTablet) {
            FrameLayout gameLayout = new FrameLayout(this);
            gameLayout.setId(11);
            layout.addView(gameLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2));
            GameDetailFragment gameDetailFragment = (GameDetailFragment) getFragmentManager().findFragmentByTag(GAME_DETAIL_TAG);
            if (gameDetailFragment == null) {
                gameDetailFragment = GameDetailFragment.newInstance(0);
                transaction.add(gameLayout.getId(), gameDetailFragment, GAME_DETAIL_TAG);
            }
        }
        transaction.commit();
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

    @Override
    public Game.GameInfoObject getGameInformation(int id) {
        return _model.getGameDisplayInfo(id);
    }

    @Override
    public void openGame(int id) {
        GameDetailFragment gameDetailFragment = (GameDetailFragment)getFragmentManager().findFragmentByTag(GAME_DETAIL_TAG);
        GameListFragment gameListFragment = (GameListFragment)getFragmentManager().findFragmentByTag(MENU_LIST_TAG);
        if(gameDetailFragment != null) {
            gameDetailFragment.setGameId(id);
            gameListFragment.setCurrentGameId(id);
            _model.setCurrentGame(id);
            loadGame(gameDetailFragment,id);
        }else{
            Intent intent = new Intent(this, BattleshipGameActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }

    @Override
    public void loadGame(GameDetailFragment gameDetailFragment, int id) {
        if(_model.getCurrentGameId() >= 0) {
            gameDetailFragment.setPlayerBoard(_model.getCurrentPlayerBoard());
            gameDetailFragment.setOpponentBoard(_model.getCurrentOpponentBoard());
        }
    }

    //For New Game Button
    @Override
    public void onClick(View v) {
        GameListFragment gameListFragment = (GameListFragment)getFragmentManager().findFragmentByTag(MENU_LIST_TAG);
        int gameId = _model.createNewGame();
        gameListFragment.addGameToGameList(gameId);
        openGame(gameId);

    }

    @Override
    public void launchMissile(int x, int y, BattleshipGridView opponentView) {
        GameListFragment gameListFragment = (GameListFragment)getFragmentManager().findFragmentByTag(MENU_LIST_TAG);
        int result = _model.launchMissile(x, y);
        opponentView.setBoard(_model.getCurrentOpponentBoard());
        gameListFragment.refreshList();
        boolean winningMove  = _model.checkForWin();
        if(winningMove){
            //TODO:End Game Stuff
        }else{
            if(result > 1) {
                _model.changeTurn();
                Intent intent = new Intent(this, BlindActivity.class);
                intent.putExtra("result", result);
                intent.putExtra("nextPlayer", _model.getCurrentTurn());
                startActivityForResult(intent, BlindActivity.BLIND_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BlindActivity.BLIND_REQUEST_CODE) {
            GameDetailFragment gameDetailFragment = (GameDetailFragment) getFragmentManager().findFragmentByTag(BattleshipMainActivity.GAME_DETAIL_TAG);
            gameDetailFragment.setOpponentBoard(_model.getCurrentOpponentBoard());
            gameDetailFragment.setPlayerBoard(_model.getCurrentPlayerBoard());
        }
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
            Type collectionType = new TypeToken<HashMap<Integer, Game>>(){}.getType();
            HashMap<Integer, Game> games = gson.fromJson(jsonString, collectionType);
            _model.setGames(games);
            GameListFragment gameListFragment = (GameListFragment)getFragmentManager().findFragmentByTag(MENU_LIST_TAG);
            gameListFragment.setGameList(_model.getGameIds());
            gameListFragment.setCurrentGameId(_model.getCurrentGameId());
        }catch (Exception e){
            Log.e("Persistence", "Error loading file: " + e.getMessage());
        }
    }
}
