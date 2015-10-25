package winkler.devon.battleship;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by devonwinkler on 10/24/15.
 */
public class GameDetailFragment extends Fragment implements BattleshipGridView.CellClickListener {

    public interface  GameBoardListener{
        public void launchMissile(int x, int y, BattleshipGridView view);
        public void loadGame(GameDetailFragment gameDetailFragment, int gameId);
    }

    public static final String GAME_RESOURE_KEY = "GAME_RESOURE_KEY";
    BattleshipGridView _myView;
    BattleshipGridView _opponentView;
    GameBoardListener _gameBoardListener;
    int _gameId = 0;

    public static GameDetailFragment newInstance(int gameResourceId){
        GameDetailFragment gameDetailFragment = new GameDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(GAME_RESOURE_KEY, gameResourceId);
        gameDetailFragment.setArguments(arguments);
        return gameDetailFragment;
    }

    public void setOpponentBoard(int [][] board){
        _opponentView.setBoard(board);
    }

    public void setPlayerBoard(int [][] board){
        _myView.setBoard(board);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            _gameBoardListener = (GameBoardListener) context;
        }catch (ClassCastException ex){
            throw new ClassCastException(context.toString() + " must implement GameBoardListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout boardLayout = new LinearLayout(getActivity());
        boardLayout.setOrientation(LinearLayout.VERTICAL);
        boardLayout.setGravity(Gravity.CENTER);
        _gameId = getArguments().getInt(GAME_RESOURE_KEY);
        _myView = new BattleshipGridView(getActivity(), true);
        _myView.setPadding(5, 5, 5, 5);
        _opponentView = new BattleshipGridView(getActivity(), false);
        _opponentView.setPadding(5,5,5,5);
        boardLayout.addView(_opponentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        boardLayout.addView(_myView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        _opponentView.setCellClickListener(this);
        _gameBoardListener.loadGame(this, _gameId);
        return boardLayout;
    }

    @Override
    public void onCellClick(int x, int y, BattleshipGridView view) {
        _gameBoardListener.launchMissile(x, y, view);
    }

    public void setGameId(int gameId){
        _gameId = gameId;
    }
}
