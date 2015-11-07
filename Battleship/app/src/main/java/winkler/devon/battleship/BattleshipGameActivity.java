package winkler.devon.battleship;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Timer;
import java.util.TimerTask;

public class BattleshipGameActivity extends Activity implements GameDetailFragment.GameBoardListener, BattleshipDataModel.BattleshipGameController {

    BattleshipDataModel _model = BattleshipDataModel.getInstance();
    BattleshipSession _session = BattleshipSession.getInstance();
    FrameLayout _gameLayout;
    Timer _turnTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        _model.setGameController(this);
//        _model.setCurrentGame(id);
        _gameLayout = new FrameLayout(this);
        _gameLayout.setId(11);

        setContentView(_gameLayout);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        GameDetailFragment gameDetailFragment = (GameDetailFragment) getFragmentManager().findFragmentByTag(BattleshipMainActivity.GAME_DETAIL_TAG);
        WaitingForOpponentFragment waitingForOpponentFragment = (WaitingForOpponentFragment) getFragmentManager().findFragmentByTag(BattleshipMainActivity.WAIT_OPPONENT_TAG);
        if (gameDetailFragment == null) {
            gameDetailFragment = GameDetailFragment.newInstance(id);
            transaction.add(_gameLayout.getId(), gameDetailFragment, BattleshipMainActivity.GAME_DETAIL_TAG);
        }
        if(waitingForOpponentFragment == null){
            waitingForOpponentFragment = WaitingForOpponentFragment.newInstance();
            transaction.add(_gameLayout.getId(), waitingForOpponentFragment, BattleshipMainActivity.WAIT_OPPONENT_TAG);
        }
        transaction.hide(gameDetailFragment);
        transaction.hide(waitingForOpponentFragment);
        transaction.commit();
        _model.loadGameIntoSession(id);
        startTurnCheckTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        _turnTimer.cancel();
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
    public void launchMissile(int x, int y) {
        _model.launchMissile(x, y);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == WinScreen.WIN_REQUEST_CODE) {
            finish();
        }
    }

//    @Override
    public void startTurnCheckTimer() {
        _turnTimer = new Timer();
        _turnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                _model.checkTurn();
            }
        }, 0, 3000);
    }

    @Override
    public void onBoardsReceived(Game.BoardCell[] playerBoard, Game.BoardCell[] opponentBoard) {
        GameDetailFragment gameDetailFragment = (GameDetailFragment) getFragmentManager().findFragmentByTag(BattleshipMainActivity.GAME_DETAIL_TAG);
        gameDetailFragment.setOpponentBoard(opponentBoard);
        gameDetailFragment.setPlayerBoard(playerBoard);

    }

    @Override
    public void onGuessReceived(boolean hit, int shipSunk){
        String hitText = hit?"Hit!":"Miss!";
        hitText += shipSunk > 0?"\nShip of size " + shipSunk +" Sunk": "";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, hitText, duration);
        toast.show();
        startTurnCheckTimer();
    }

    @Override
    public void onTurnCheckReceived(boolean isMyturn, String winner){
        if("IN PROGRESS".equals(winner)){
            WaitingForOpponentFragment waitingForOpponentFragment = (WaitingForOpponentFragment) getFragmentManager().findFragmentByTag(BattleshipMainActivity.WAIT_OPPONENT_TAG);
            GameDetailFragment gameDetailFragment = (GameDetailFragment) getFragmentManager().findFragmentByTag(BattleshipMainActivity.GAME_DETAIL_TAG);
            _turnTimer.cancel();
            if(isMyturn){
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                if(waitingForOpponentFragment != null){
                    transaction.hide(waitingForOpponentFragment);
                }
                if(gameDetailFragment != null){
                    transaction.show(gameDetailFragment);
                }
                transaction.commit();
                _model.loadBoards();
            }else{
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                if(gameDetailFragment != null){
                    transaction.hide(gameDetailFragment);
                }
                if(waitingForOpponentFragment != null){
                    transaction.show(waitingForOpponentFragment);
                }
                transaction.commit();
                startTurnCheckTimer();
            }
        }else{
            Intent intent = new Intent(this, WinScreen.class);
            intent.putExtra("winner", winner);
            startActivityForResult(intent, WinScreen.WIN_REQUEST_CODE);
        }
    }
}
