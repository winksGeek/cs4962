package winkler.devon.battleship;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by devonwinkler on 10/24/15.
 */
public class GameInfoFragment extends Fragment {


    public static final String GAME_INFO_KEY = "GAME_INFO_KEY";
    GameInfoObject _gameInfoObject = null;

    public static GameInfoFragment newInstance(GameInfoObject gameInfoObject){
        GameInfoFragment gameDetailFragment = new GameInfoFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(GAME_INFO_KEY, gameInfoObject);
        gameDetailFragment.setArguments(arguments);
        return gameDetailFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        _gameInfoObject = getArguments().getParcelable(GAME_INFO_KEY);

        if(_gameInfoObject != null) {

            TextView player1 = new TextView(getActivity());
            player1.setText("Player 1: " + _gameInfoObject.player1);
            layout.addView(player1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

            TextView player2 = new TextView(getActivity());
            player2.setText("Player 2: " + _gameInfoObject.player2);
            layout.addView(player2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

            TextView missilesLaunched = new TextView(getActivity());
            missilesLaunched.setText("Missiles Launched: " + _gameInfoObject.missilesLaunched);
            layout.addView(missilesLaunched, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

            TextView winner = new TextView(getActivity());
            String status = "IN PROGRESS".equals(_gameInfoObject.winner) ? _gameInfoObject.winner : "Winner: " + _gameInfoObject.winner;
            winner.setText(status);
            layout.addView(winner, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        }

        return layout;
    }
}
