package winkler.devon.battleship;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WaitingForOpponentFragment extends Fragment {


    public static WaitingForOpponentFragment newInstance(){
        WaitingForOpponentFragment gameDetailFragment = new WaitingForOpponentFragment();
        return gameDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        TextView waitText = new TextView(getActivity());
        waitText.setText("Waiting for Opponent");
        layout.addView(waitText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        return layout;
    }
}
