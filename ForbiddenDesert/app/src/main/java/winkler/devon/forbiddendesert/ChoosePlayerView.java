package winkler.devon.forbiddendesert;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by devonwinkler on 12/10/15.
 */
public class ChoosePlayerView extends LinearLayout {
    public ChoosePlayerView(Context context) {
        super(context);
    }
    Player _playerChosen;
    public void setPlayers(Player[]players, String message){
        removeAllViews();
        _playerChosen = null;
        setOrientation(VERTICAL);
        TextView textView = new TextView(this.getContext());
        textView.setText(message);
        textView.setTextSize(10);
        textView.setPadding(2, 2, 2, 2);
        RadioGroup playersGroup = new RadioGroup(this.getContext());
        playersGroup.setOrientation(VERTICAL);
        RadioButton noneButton = new RadioButton(this.getContext());
        noneButton.setText("None");
        noneButton.setTextSize(10);
        noneButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    _playerChosen = null;
                }
            }
        });
        playersGroup.addView(noneButton, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        for(int i = 0; i < players.length; i++){
            final Player player = players[i];
            RadioButton playerRadio = new RadioButton(this.getContext());
            playerRadio.setText(player.getRoleString());
            playerRadio.setTextSize(10);
            playerRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        _playerChosen = player;
                    }
                }
            });
            playersGroup.addView(playerRadio, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        }
        noneButton.setChecked(true);
        addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        addView(playersGroup, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

    }

    public void clearPlayers(){
        removeAllViews();
        _playerChosen = null;
    }

    public Player getPlayerChosen(){
        return _playerChosen;
    }
}
