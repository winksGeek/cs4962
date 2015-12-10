package winkler.devon.forbiddendesert;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by devonwinkler on 11/23/15.
 */
public class PlayerView extends LinearLayout implements View.OnTouchListener {
    private Player _player;
    private CardPlayedListener _cardPlayedListener;

    public static interface CardPlayedListener{
        public void onPlayed(ItemCard card);
    }

    public void setCardPlayedListener(CardPlayedListener listener){
        _cardPlayedListener = listener;
    }

    public PlayerView(Context context){
        super(context);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setGravity(Gravity.CENTER_HORIZONTAL);
    }
    public void setPlayer(Player player){
        _player = player;
        removeAllViews();

        TextView role = new TextView(getContext());
        role.setText(_player.getRoleStringForView());
        role.setTextSize(10);
        if(_player.isActive()){
            role.setTextColor(Color.RED);
        }

        TextView waterLeft = new TextView(getContext());
        waterLeft.setText("Water Left: " + _player.getWaterLeft());
        waterLeft.setTextSize(10);

        PlayerHand hand = new PlayerHand(getContext());
        hand.setHand(_player.getHand());
        hand.setOnTouchListener(this);

        addView(role, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        addView(waterLeft, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        addView(hand, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        PlayerHand hand = (PlayerHand) v;
        if(hand._cards.size()>0) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    int x = (int) event.getX();
                    int squareWidth = hand.getWidth() / hand._cards.size();
                    int index = x / squareWidth;
                    if (_cardPlayedListener != null) {
                        _cardPlayedListener.onPlayed(hand._cards.get(index));
                    }
                    break;
            }
        }
        return true;
    }
}
