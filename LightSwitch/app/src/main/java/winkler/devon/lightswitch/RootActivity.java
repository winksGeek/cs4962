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

        //root layout
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.DKGRAY);

        //add light bulb off image
        final ImageView light = new ImageView(this);
        light.setImageResource(R.drawable.off);
        layout.addView(light, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        //add switch object
        final Switch lightSwitch = new Switch(this);
        LinearLayout.LayoutParams switchLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        switchLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        switchLayoutParams.topMargin = 150;
        switchLayoutParams.bottomMargin = 150;
        layout.addView(lightSwitch, switchLayoutParams);

        //set switch to turn the light on and off
        lightSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lightSwitch.isChecked()) {
                    light.setImageResource(R.drawable.on);
                    layout.setBackgroundColor(Color.WHITE);
                } else {
                    light.setImageResource(R.drawable.off);
                    layout.setBackgroundColor(Color.DKGRAY);
                }
            }
        });
        setContentView(layout);

    }

}
