package winkler.devon.forbiddendesert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class GameActivity extends Activity implements DesertBoardView.TileClickListener {
    public static final String GAME_ID = "GAME_ID";
    DesertBoardView _boardView;
    public static enum Mode {
        Move, Remove, Excavate, Pick
    }
    Mode _currentMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _currentMode = Mode.Move;

        ForbiddenDataModel model = ForbiddenDataModel.getInstance();
        LinearLayout boardLayout = new LinearLayout(this);
        boardLayout.setOrientation(LinearLayout.VERTICAL);
        boardLayout.setGravity(Gravity.CENTER);

        Intent startGameIntent = getIntent();
        String gameId = startGameIntent.getStringExtra(GAME_ID);
        model.setCurrentGame(gameId);
        int numOfPlayers = 5;

        LinearLayout actionBarLayout = new LinearLayout(this);
        actionBarLayout.setOrientation(LinearLayout.HORIZONTAL);
        actionBarLayout.setGravity(Gravity.CENTER);

        //MOVE
        final ToggleButton moveAction = new ToggleButton(this);
        moveAction.setTextOn(getString(R.string.move));
        moveAction.setTextOff(getString(R.string.move));
        moveAction.setChecked(true);
        actionBarLayout.addView(moveAction, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0));

        //REMOVE SAND
        final ToggleButton removeSandAction = new ToggleButton(this);
        removeSandAction.setTextOn(getString(R.string.remove_sand));
        removeSandAction.setTextOff(getString(R.string.remove_sand));
        removeSandAction.setChecked(false);
        actionBarLayout.addView(removeSandAction, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0));

        //EXCAVATE
        final ToggleButton excavateAction = new ToggleButton(this);
        excavateAction.setTextOn(getString(R.string.excavate));
        excavateAction.setTextOff(getString(R.string.excavate));
        excavateAction.setChecked(false);
        actionBarLayout.addView(excavateAction, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0));

        //PICK UP A PART
        final ToggleButton partAction = new ToggleButton(this);
        partAction.setTextOn(getString(R.string.pick_up_a_part));
        partAction.setTextOff(getString(R.string.pick_up_a_part));
        partAction.setChecked(false);
        actionBarLayout.addView(partAction, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0));

        boardLayout.addView(actionBarLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

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

        moveAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveAction.setChecked(true);
                removeSandAction.setChecked(false);
                excavateAction.setChecked(false);
                partAction.setChecked(false);
                _currentMode = Mode.Move;
            }
        });
        removeSandAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveAction.setChecked(false);
                removeSandAction.setChecked(true);
                excavateAction.setChecked(false);
                partAction.setChecked(false);
                _currentMode = Mode.Remove;
            }
        });
        excavateAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveAction.setChecked(false);
                removeSandAction.setChecked(false);
                excavateAction.setChecked(true);
                partAction.setChecked(false);
                _currentMode = Mode.Excavate;
            }
        });
        partAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveAction.setChecked(false);
                removeSandAction.setChecked(false);
                excavateAction.setChecked(false);
                partAction.setChecked(true);
                _currentMode = Mode.Pick;
            }
        });

        ForbiddenDataModel _model = ForbiddenDataModel.getInstance();
        _model.moveStorm(StormCard.Direction.North, StormCard.Places.Three);
        setContentView(boardLayout);
    }

    @Override
    public void onTileClick(int xPos, int yPos) {
        ForbiddenDataModel model = ForbiddenDataModel.getInstance();
        if(_currentMode == Mode.Move) {
            model.movePlayer(xPos, yPos);
            _boardView.setPlayers(model.getPlayersForGame());
        }
        if(_currentMode == Mode.Remove) {
            model.removeSand(xPos, yPos);
            _boardView.setBoard(model.getBoard());
        }
    }
}
