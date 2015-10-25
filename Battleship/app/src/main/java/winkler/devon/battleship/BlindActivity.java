package winkler.devon.battleship;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BlindActivity extends Activity {

    BattleshipDataModel _model = BattleshipDataModel.getInstance();
    public static final int BLIND_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        Intent intent = getIntent();
        int result = intent.getIntExtra("result", 2);
        int nextPlayer = intent.getIntExtra("nextPlayer",0);
        String resultString = "MISS!";
        if(result == 3){
            resultString = "HIT!";
        }

        TextView resultTextView = new TextView(this);
        resultTextView.setText(resultString);
        resultTextView.setTextSize(18.0f);
        layout.addView(resultTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        TextView nextPlayerTextView = new TextView(this);
        nextPlayerTextView.setText("Hand the device to Player " + (nextPlayer + 1));
        layout.addView(nextPlayerTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        Button button = new Button(this);
        button.setText("Continue");
        layout.addView(button, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setContentView(layout);
    }
}
