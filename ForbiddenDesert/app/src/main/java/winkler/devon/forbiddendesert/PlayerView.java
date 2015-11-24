package winkler.devon.forbiddendesert;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by devonwinkler on 11/23/15.
 */
public class PlayerView extends LinearLayout {
    private Player _player;

    public PlayerView(Context context){
        super(context);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setGravity(Gravity.CENTER_HORIZONTAL);
    }
    public void setPlayer(Player player){
        _player = player;
        removeAllViews();

        TextView role = new TextView(getContext());
        role.setText(_player.getRoleString());

        TextView waterLeft = new TextView(getContext());
        waterLeft.setText("Water Left: " + _player.getWaterLeft());

        addView(role, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        addView(waterLeft, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
    }
}
