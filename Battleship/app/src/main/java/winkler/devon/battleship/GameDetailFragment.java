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
        public void launchMissile(int x, int y);
//        public void loadGame(GameDetailFragment gameDetailFragment, String gameId);
    }

    public static final String GAME_RESOURE_KEY = "GAME_RESOURE_KEY";
    BattleshipGridView _myView;
    BattleshipGridView _opponentView;
    GameBoardListener _gameBoardListener;
    String _gameId = "";

    public static GameDetailFragment newInstance(String gameResourceId){
        GameDetailFragment gameDetailFragment = new GameDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putString(GAME_RESOURE_KEY, gameResourceId);
        gameDetailFragment.setArguments(arguments);
        return gameDetailFragment;
    }

    public void setOpponentBoard(Game.BoardCell[] board){
        _opponentView.setBoard(board);
        _opponentView.invalidate();
    }

    public void setPlayerBoard(Game.BoardCell[] board){
        _myView.setBoard(board);
        _myView.invalidate();
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
        _gameId = getArguments().getString(GAME_RESOURE_KEY);
        _myView = new BattleshipGridView(getActivity(), true);
        _myView.setPadding(5, 5, 5, 5);
        _opponentView = new BattleshipGridView(getActivity(), false);
        _opponentView.setPadding(5,5,5,5);
        boardLayout.addView(_opponentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        boardLayout.addView(_myView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        _opponentView.setCellClickListener(this);
//        _gameBoardListener.loadGame(this, _gameId);
        return boardLayout;
    }

    @Override
    public void onCellClick(int x, int y) {
        _gameBoardListener.launchMissile(x, y);
    }

    public void setGameId(String gameId){
        _gameId = gameId;
    }
}
