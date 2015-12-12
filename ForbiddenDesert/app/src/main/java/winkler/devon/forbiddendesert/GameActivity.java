package winkler.devon.forbiddendesert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class GameActivity extends Activity implements DesertBoardView.TileClickListener, PlayerView.CardPlayedListener, ForbiddenDataModel.SetModeListener, ShareWaterView.ShareWaterListener {
    public static final String GAME_ID = "GAME_ID";
    DesertBoardView _boardView;
    PartsCollectedView _partsCollected;
    LinearLayout _playerLayout;
    LinearLayout _actionBarLayout;
    TextView _cardsToDraw;
    TextView _gameModeView;
    ToggleButton _moveAction;
    ToggleButton _getWaterFromTile;
    ChoosePlayerView _playersChoiceView;
    ShareWaterView _waterShareView;

    @Override
    public void onPlayed(ItemCard card) {
        ForbiddenDataModel model = ForbiddenDataModel.getInstance();
        model.playItemCard(card);
        _boardView.setBoard(model.getBoard());
        refreshPlayerViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ForbiddenDataModel _model = ForbiddenDataModel.getInstance();
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
    public void setMode(Mode mode) {
        ForbiddenDataModel model = ForbiddenDataModel.getInstance();
        _currentMode = mode;
        if(mode == Mode.Jet) {
            _actionBarLayout.setVisibility(View.GONE);
            _gameModeView.setText("Jet Pack In Use");
            _gameModeView.setVisibility(View.VISIBLE);
            Player [] players = model.getPlayersOnJetTile();
            if(players.length > 0) {
                _playersChoiceView.setVisibility(View.VISIBLE);
                _playersChoiceView.setPlayers(players, "Choose Player to Jet With");
            }
        }else if(mode == Mode.Terrascope){
            _actionBarLayout.setVisibility(View.GONE);
            _gameModeView.setText("Terrascope In Use");
            _gameModeView.setVisibility(View.VISIBLE);
            _playersChoiceView.clearPlayers();
            _playersChoiceView.setVisibility(View.GONE);
        }else if (mode == Mode.Blaster){
            _actionBarLayout.setVisibility(View.GONE);
            _gameModeView.setText("Blaster in Use");
            _gameModeView.setVisibility(View.VISIBLE);
            _playersChoiceView.clearPlayers();
            _playersChoiceView.setVisibility(View.GONE);
        }else if (mode == Mode.Storm){
            _actionBarLayout.setVisibility(View.GONE);
            _playersChoiceView.clearPlayers();
            _playersChoiceView.setVisibility(View.GONE);
            _gameModeView.setText("Storm Turn in Progress");
            _cardsToDraw.setText("Cards to Draw: " + model.getStormCardsLeftString() + "\n(Tap to Draw)");
            _cardsToDraw.setVisibility(View.VISIBLE);
            _gameModeView.setVisibility(View.VISIBLE);
        }
        else{
            _actionBarLayout.setVisibility(View.VISIBLE);
            _gameModeView.setVisibility(View.GONE);
            _playersChoiceView.clearPlayers();
            _playersChoiceView.setVisibility(View.GONE);
            _getWaterFromTile.setVisibility(View.GONE);
            _cardsToDraw.setVisibility(View.GONE);
            _moveAction.callOnClick();
            Player currentPlayer = model.getCurrentPlayer();
            if(currentPlayer._role._type == Role.Type.Climber){
                Player [] players = model.getPlayersOnCurrentPlayersTile();
                _playersChoiceView.setPlayers(players, "Choose Player to Climb With");
                _playersChoiceView.setVisibility(View.VISIBLE);
            }
            if(currentPlayer._role._type == Role.Type.WaterCarrier){
                _getWaterFromTile.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void shareWater(Player player) {
        ForbiddenDataModel model = ForbiddenDataModel.getInstance();
        Player currentPlayer = model.getCurrentPlayer();
        if(currentPlayer.getWaterLeft() < currentPlayer._role.getMaxWater()) {
            currentPlayer.addWater(-1);
            player.addWater(1);
        }
        refreshPlayerViews();
    }

    public static enum Mode {
        Move, Remove, Excavate, Pick, Jet, Terrascope, Blaster, Storm, Extract, Share
    }
    Mode _currentMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _currentMode = Mode.Move;

        ForbiddenDataModel model = ForbiddenDataModel.getInstance();
        model.setSetModeListener(this);
        LinearLayout boardLayout = new LinearLayout(this);
        boardLayout.setOrientation(LinearLayout.VERTICAL);
        boardLayout.setGravity(Gravity.CENTER);

        Intent startGameIntent = getIntent();
        String gameId = startGameIntent.getStringExtra(GAME_ID);
        model.setCurrentGame(gameId);

        //region Parts Collected
        LinearLayout partsCollectedLayout = new LinearLayout(this);
        partsCollectedLayout.setOrientation(LinearLayout.HORIZONTAL);
        partsCollectedLayout.setGravity(Gravity.CENTER);

        _partsCollected = new PartsCollectedView(this);
        _partsCollected.setParts(model.getParts(gameId));
        partsCollectedLayout.addView(_partsCollected, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 0));

        boardLayout.addView(partsCollectedLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        //endregion

        //region Action Bar
        _actionBarLayout = new LinearLayout(this);
        _actionBarLayout.setOrientation(LinearLayout.HORIZONTAL);
        _actionBarLayout.setGravity(Gravity.CENTER);

        //MOVE
        _moveAction = new ToggleButton(this);
        _moveAction.setTextOn(getString(R.string.move));
        _moveAction.setTextOff(getString(R.string.move));
        _moveAction.setTextSize(10);
        _moveAction.setChecked(true);
        _actionBarLayout.addView(_moveAction, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0));

        //REMOVE SAND
        final ToggleButton removeSandAction = new ToggleButton(this);
        removeSandAction.setTextOn(getString(R.string.remove_sand));
        removeSandAction.setTextOff(getString(R.string.remove_sand));
        removeSandAction.setChecked(false);
        removeSandAction.setTextSize(10);
        _actionBarLayout.addView(removeSandAction, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0));

        //EXCAVATE
        final ToggleButton excavateAction = new ToggleButton(this);
        excavateAction.setTextOn(getString(R.string.excavate));
        excavateAction.setTextOff(getString(R.string.excavate));
        excavateAction.setChecked(false);
        excavateAction.setTextSize(10);
        _actionBarLayout.addView(excavateAction, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0));

        //PICK UP A PART
        final ToggleButton partAction = new ToggleButton(this);
        partAction.setTextOn(getString(R.string.pick_up_a_part));
        partAction.setTextOff(getString(R.string.pick_up_a_part));
        partAction.setChecked(false);
        partAction.setTextSize(10);
        _actionBarLayout.addView(partAction, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0));

        //SHARE WATER
        final ToggleButton shareWaterAction = new ToggleButton(this);
        shareWaterAction.setTextOn("Share Water");
        shareWaterAction.setTextOff("Share Water");
        shareWaterAction.setChecked(false);
        shareWaterAction.setTextSize(10);
        _actionBarLayout.addView(shareWaterAction, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0));


        //GET WATER FROM WELL
        _getWaterFromTile = new ToggleButton(this);
        _getWaterFromTile.setTextOn("Get Water From Well");
        _getWaterFromTile.setTextOff("Get Water From Well");
        _getWaterFromTile.setChecked(false);
        _getWaterFromTile.setTextSize(10);
        _getWaterFromTile.setVisibility(View.GONE);

        _actionBarLayout.addView(_getWaterFromTile, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0));

        boardLayout.addView(_actionBarLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        _gameModeView = new TextView(this);
        _gameModeView.setVisibility(View.GONE);
        boardLayout.addView(_gameModeView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        //endregion


        //region Play Area
        LinearLayout mainBoardLayout = new LinearLayout(this);
        mainBoardLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainBoardLayout.setGravity(Gravity.CENTER);

        LinearLayout leftPlayerArea = new LinearLayout(this);
        leftPlayerArea.setOrientation(LinearLayout.VERTICAL);

        _playersChoiceView = new ChoosePlayerView(this);
        _playersChoiceView.setVisibility(View.GONE);
        leftPlayerArea.addView(_playersChoiceView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        _waterShareView = new ShareWaterView(this);
        _waterShareView.setVisibility(View.GONE);
        _waterShareView.setShareWaterListener(this);
        leftPlayerArea.addView(_waterShareView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        mainBoardLayout.addView(leftPlayerArea, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        //region Board
        _boardView = new DesertBoardView(this);
        _boardView.setPadding(5, 5, 5, 5);
        _boardView.setBoard(model.getBoard(gameId));
        _boardView.setTileClickListener(this);
        mainBoardLayout.addView(_boardView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        //endregion

        LinearLayout rightPlayerArea = new LinearLayout(this);
        rightPlayerArea.setOrientation(LinearLayout.VERTICAL);
        ImageView stormDeck = new ImageView(this);
        stormDeck.setImageResource(R.drawable.storm_tile);
        stormDeck.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ForbiddenDataModel model = ForbiddenDataModel.getInstance();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        StormCard card = model.drawStormCard();
                        if(model.checkForLoss()){
                            model.setGameOver(false);
                            Toast.makeText(v.getContext(), "Game Over", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        if(card!=null) {
                            switch (card.type) {
                                case Sun:
                                    Toast.makeText(v.getContext(), "Sun Beats Down", Toast.LENGTH_SHORT).show();
                                    break;
                                case Storm:
                                    Toast.makeText(v.getContext(), "Storm Picks Up", Toast.LENGTH_SHORT).show();
                                    break;
                                case Move:
                                    String direction = "";
                                    String places = "";
                                    switch(card.places){
                                        case One:
                                            places = "1";
                                            break;
                                        case Two:
                                            places = "2";
                                            break;
                                        case Three:
                                            places = "3";
                                            break;
                                    }
                                    switch(card.direction){
                                        case North:
                                            direction = "North";
                                            break;
                                        case East:
                                            direction = "East";
                                            break;
                                        case South:
                                            direction = "South";
                                            break;
                                        case West:
                                            direction = "West";
                                            break;
                                    }
                                    Toast.makeText(v.getContext(), direction + " " + places, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                        _cardsToDraw.setText("Cards to Draw: " + model.getStormCardsLeftString()+"\n(Tap to Draw)");
                        _boardView.setBoard(model.getBoard());
                        refreshPlayerViews();
                        break;
                }
                return true;
            }
        });
        rightPlayerArea.addView(stormDeck, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        _cardsToDraw = new TextView(this);
        _cardsToDraw.setText("Cards to Draw: 0");
        _cardsToDraw.setTextSize(10);
        _cardsToDraw.setVisibility(View.GONE);
        rightPlayerArea.addView(_cardsToDraw, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        mainBoardLayout.addView(rightPlayerArea, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        boardLayout.addView(mainBoardLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        //endregion

        //region Players
        _playerLayout = new LinearLayout(this);
        RoleRandom rand = new RoleRandom();
        Player [] players = model.getPlayersForGame(gameId);
        for(int i = 0; i < players.length; i++){
            PlayerView playerView = new PlayerView(this);
            Player player = players[i];
            if(i == 0){
                player._actionsLeft = 4;
                player.setActive(true);
            }
            playerView.setPlayer(player);
            playerView.setCardPlayedListener(this);
            _playerLayout.addView(playerView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        }
        _boardView.setPlayers(model.getPlayersForGame(gameId));
        boardLayout.addView(_playerLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        //endregion

        _moveAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForbiddenDataModel model = ForbiddenDataModel.getInstance();
                Player currentPlayer = model.getCurrentPlayer();
                _moveAction.setChecked(true);
                removeSandAction.setChecked(false);
                excavateAction.setChecked(false);
                partAction.setChecked(false);
                shareWaterAction.setChecked(false);
                _getWaterFromTile.setChecked(false);
                _currentMode = Mode.Move;
                if(currentPlayer._role._type == Role.Type.Climber){
                    Player [] playersOnTile = model.getPlayersOnCurrentPlayersTile();
                    _playersChoiceView.setPlayers(playersOnTile, "Choose Player to Climb With");
                    _playersChoiceView.setVisibility(View.VISIBLE);
                }
                _waterShareView.setVisibility(View.GONE);
            }
        });
        removeSandAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _moveAction.setChecked(false);
                removeSandAction.setChecked(true);
                excavateAction.setChecked(false);
                partAction.setChecked(false);
                shareWaterAction.setChecked(false);
                _getWaterFromTile.setChecked(false);
                _currentMode = Mode.Remove;
                _playersChoiceView.setVisibility(View.GONE);
                _waterShareView.setVisibility(View.GONE);
            }
        });
        excavateAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _moveAction.setChecked(false);
                removeSandAction.setChecked(false);
                excavateAction.setChecked(true);
                partAction.setChecked(false);
                shareWaterAction.setChecked(false);
                _getWaterFromTile.setChecked(false);
                _currentMode = Mode.Excavate;
                _playersChoiceView.setVisibility(View.GONE);
                _waterShareView.setVisibility(View.GONE);
            }
        });
        partAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _moveAction.setChecked(false);
                removeSandAction.setChecked(false);
                excavateAction.setChecked(false);
                partAction.setChecked(true);
                shareWaterAction.setChecked(false);
                _getWaterFromTile.setChecked(false);
                _currentMode = Mode.Pick;
                _playersChoiceView.setVisibility(View.GONE);
                _waterShareView.setVisibility(View.GONE);
            }
        });
        shareWaterAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForbiddenDataModel model = ForbiddenDataModel.getInstance();
                _moveAction.setChecked(false);
                removeSandAction.setChecked(false);
                excavateAction.setChecked(false);
                partAction.setChecked(false);
                shareWaterAction.setChecked(true);
                _getWaterFromTile.setChecked(false);
                _currentMode = Mode.Share;
                _waterShareView.setPlayers(model.getPlayersForSharingWater());
                _playersChoiceView.setVisibility(View.GONE);
                _waterShareView.setVisibility(View.VISIBLE);
            }
        });
        _getWaterFromTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _moveAction.setChecked(false);
                removeSandAction.setChecked(false);
                excavateAction.setChecked(false);
                partAction.setChecked(false);
                shareWaterAction.setChecked(false);
                _getWaterFromTile.setChecked(true);
                _currentMode = Mode.Extract;
                _playersChoiceView.setVisibility(View.GONE);
                _waterShareView.setVisibility(View.GONE);

            }
        });

        Player currentPlayer = model.getCurrentPlayer();
        if(currentPlayer._role._type == Role.Type.Climber){
            Player [] playersOnTile = model.getPlayersOnCurrentPlayersTile();
            _playersChoiceView.setPlayers(playersOnTile, "Choose Player to Climb With");
            _playersChoiceView.setVisibility(View.VISIBLE);
        }
        if(currentPlayer._role._type == Role.Type.WaterCarrier){
            _getWaterFromTile.setVisibility(View.VISIBLE);
        }
        setContentView(boardLayout);
    }

    @Override
    public void onTileClick(int xPos, int yPos) {
        ForbiddenDataModel model = ForbiddenDataModel.getInstance();
        if(_currentMode == Mode.Move) {
            model.movePlayer(xPos, yPos, _playersChoiceView.getPlayerChosen());
            Player currentPlayer = model.getCurrentPlayer();
            if(currentPlayer._role._type == Role.Type.Climber){
                _playersChoiceView.setPlayers(model.getPlayersOnCurrentPlayersTile(), "Choose Player to Climb With");
            }
            _boardView.setPlayers(model.getPlayersForGame());
        }
        if(_currentMode == Mode.Remove) {
            model.removeSand(xPos, yPos);
            _boardView.setBoard(model.getBoard());
        }
        if(_currentMode == Mode.Excavate){
            model.excavate(xPos, yPos);
            _boardView.setBoard(model.getBoard());
        }
        if(_currentMode == Mode.Pick){
            Part part = model.pickUpPart(xPos, yPos);
            _boardView.setBoard(model.getBoard());
            _partsCollected.setParts(model.getParts());
        }
        if(_currentMode == Mode.Jet){
            model.jetPlayer(xPos, yPos, _playersChoiceView.getPlayerChosen());
            _boardView.setPlayers(model.getPlayersForGame());
        }
        if(_currentMode == Mode.Terrascope){
            DesertTile tile = model.scope(xPos, yPos);
            if(tile != null){
                Toast.makeText(this, peekAtTile(tile), Toast.LENGTH_LONG).show();
            }
            _boardView.setPlayers(model.getPlayersForGame());
        }
        if(_currentMode == Mode.Blaster){
            model.duneBlast(xPos, yPos);
            _boardView.setPlayers(model.getPlayersForGame());
        }
        if(_currentMode == Mode.Extract){
            DesertTile tile = model.getTileFromBoard(xPos, yPos);
            if(tile.type == DesertTile.Type.Oasis && tile.status == DesertTile.Status.Flipped) {
                model.getCurrentPlayer().addWater(2);
                model._currentGame.useAction();
            }
            _boardView.setPlayers(model.getPlayersForGame());
        }
        refreshPlayerViews();
        if(model.checkForWin()){
            model.setGameOver(true);
            Toast.makeText(this, "You Win!", Toast.LENGTH_LONG);
            finish();
        }
    }

    private String peekAtTile(DesertTile tile) {
        String tileString = "None";
        switch(tile.type){
            case Tunnel:
                tileString = "Tunnel";
                break;
            case PieceColumn:
                switch(tile.partHint){
                    case Crystal:
                        tileString = "Crystal Column";
                        break;
                    case Propeller:
                        tileString = "Propeller Column";
                        break;
                    case Engine:
                        tileString = "Engine Column";
                        break;
                    case Navigation:
                        tileString = "Navigation Column";
                        break;
                }
                break;
            case PieceRow:
                switch(tile.partHint){
                    case Crystal:
                        tileString = "Crystal Row";
                        break;
                    case Propeller:
                        tileString = "Propeller Row";
                        break;
                    case Engine:
                        tileString = "Engine Row";
                        break;
                    case Navigation:
                        tileString = "Navigation Row";
                        break;
                }
                break;
            case Mirage:
                tileString = "Mirage";
                break;
            case Oasis:
                tileString = "Oasis";
                break;
            case Landing:
                tileString = "Landing";
                break;
            case Crash:
            case Item:
                tileString = "Gear";
                break;

        }
        return tileString;
    }

    public void refreshPlayerViews(){
        ForbiddenDataModel model = ForbiddenDataModel.getInstance();
        Player [] players = model.getPlayersForGame();
        for(int i = 0; i < players.length; i++){
            PlayerView view = (PlayerView)_playerLayout.getChildAt(i);
            view.setPlayer(players[i]);
        }
    }
}
