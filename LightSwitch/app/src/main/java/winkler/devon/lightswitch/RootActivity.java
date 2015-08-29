package winkler.devon.lightswitch;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

public class RootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.DKGRAY);

        final ImageView light = new ImageView(this);
        light.setImageResource(R.drawable.off);
//        light.setBackgroundColor(Color.BLACK);
        layout.addView(light, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));


        final Switch lightSwitch = new Switch(this);
//        lightSwitch.setBackgroundColor(Color.BLACK);
        LinearLayout.LayoutParams switchLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        switchLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        switchLayoutParams.topMargin = 150;
        switchLayoutParams.bottomMargin = 150;
//        lightSwitch.setPadding(100, 100, 100, 100);
        layout.addView(lightSwitch, switchLayoutParams);

        lightSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lightSwitch.isChecked()) {
                    light.setImageResource(R.drawable.on);
//                    light.setBackgroundColor(Color.WHITE);
//                    lightSwitch.setBackgroundColor(Color.WHITE);
                    layout.setBackgroundColor(Color.WHITE);
                } else {
                    light.setImageResource(R.drawable.off);
//                    light.setBackgroundColor(Color.BLACK);
//                    lightSwitch.setBackgroundColor(Color.BLACK);
                    layout.setBackgroundColor(Color.DKGRAY);
                }
            }
        });
        setContentView(layout);

    }

}
