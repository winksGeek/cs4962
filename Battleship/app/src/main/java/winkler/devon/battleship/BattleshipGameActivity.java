package winkler.devon.battleship;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class BattleshipGameActivity extends Activity implements GameDetailFragment.GameBoardListener  {

    BattleshipDataModel _model = BattleshipDataModel.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        _model.setCurrentGame(id);
        FrameLayout gameLayout = new FrameLayout(this);
        gameLayout.setId(11);

        setContentView(gameLayout);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        GameDetailFragment gameDetailFragment = (GameDetailFragment) getFragmentManager().findFragmentByTag(BattleshipMainActivity.GAME_DETAIL_TAG);
        if (gameDetailFragment == null) {
            gameDetailFragment = GameDetailFragment.newInstance(id);
            transaction.add(gameLayout.getId(), gameDetailFragment, BattleshipMainActivity.GAME_DETAIL_TAG);
        }
        transaction.commit();
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
    public void launchMissile(int x, int y, BattleshipGridView opponentView) {
        int result = _model.launchMissile(x, y);
        opponentView.setBoard(_model.getCurrentOpponentBoard());
        boolean winningMove  = _model.checkForWin();
        if(winningMove){
            _model.finishGame();
            Intent intent = new Intent(this, WinScreen.class);
            intent.putExtra("winner", _model.getCurrentTurn());
            startActivityForResult(intent, WinScreen.WIN_REQUEST_CODE);
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
        if(requestCode == WinScreen.WIN_REQUEST_CODE) {
            finish();
        }
    }

    @Override
    public void loadGame(GameDetailFragment gameDetailFragment, int gameId) {
        gameDetailFragment.setPlayerBoard(_model.getCurrentPlayerBoard());
        gameDetailFragment.setOpponentBoard(_model.getCurrentOpponentBoard());
    }
}
