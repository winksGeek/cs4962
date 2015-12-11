package winkler.devon.forbiddendesert;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by devonwinkler on 12/10/15.
 */
public class ShareWaterView extends ChoosePlayerView implements View.OnClickListener {
    public static interface ShareWaterListener{
        public void shareWater(Player player);
    }

    ShareWaterListener _shareWaterListener;

    public ShareWaterView(Context context) {
        super(context);
    }

    public void setShareWaterListener(ShareWaterListener listener){
        _shareWaterListener = listener;
    }

    public void setPlayers(Player[] players) {
        super.setPlayers(players, "Choose Player to Share Water");
        Button shareButton = new Button(getContext());
        shareButton.setText("Share");
        shareButton.setOnClickListener(this);
        addView(shareButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
    }

    @Override
    public void onClick(View v) {
        if(_shareWaterListener != null){
            _shareWaterListener.shareWater(_playerChosen);
        }
    }
}
