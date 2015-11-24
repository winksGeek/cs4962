package winkler.devon.forbiddendesert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class GameActivity extends Activity implements DesertBoardView.TileClickListener {
    public static final String GAME_ID = "GAME_ID";
    DesertBoardView _boardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ForbiddenDataModel model = ForbiddenDataModel.getInstance();
        LinearLayout boardLayout = new LinearLayout(this);
        boardLayout.setOrientation(LinearLayout.VERTICAL);
        boardLayout.setGravity(Gravity.CENTER);

        Intent startGameIntent = getIntent();
        String gameId = startGameIntent.getStringExtra(GAME_ID);
        model.setCurrentGame(gameId);
        int numOfPlayers = 5;

        _boardView = new DesertBoardView(this);
        _boardView.setPadding(5, 5, 5, 5);
        _boardView.setBoard(model.getBoard(gameId));
        _boardView.setTileClickListener(this);
        boardLayout.addView(_boardView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        LinearLayout playerLayout = new LinearLayout(this);
        RoleRandom rand = new RoleRandom();
        Player [] players = model.getPlayersForGame(gameId);
        for(int i = 0; i < players.length; i++){
            PlayerView playerView = new PlayerView(this);
            Player player = players[i];
            playerView.setPlayer(player);
            playerLayout.addView(playerView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        }
        _boardView.setPlayers(model.getPlayersForGame(gameId));
        boardLayout.addView(playerLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        setContentView(boardLayout);
    }

    @Override
    public void onTileClick(int xPos, int yPos) {
        ForbiddenDataModel model = ForbiddenDataModel.getInstance();
        model.movePlayer(xPos, yPos);
        _boardView.setPlayers(model.getPlayersForGame());
    }
}
