package winkler.devon.paint;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class PaintActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        PaletteView paletteView = new PaletteView(this);
        paletteView.setPadding(30, 60, 90, 120);
        PaintAreaView canvas = new PaintAreaView(this);
        layout.addView(canvas,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 7));
        layout.addView(paletteView,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 3));
        paletteView.setBackgroundColor(0xFFDDDDDD);
        setContentView(layout);

        for(int i = 0; i < 10; i++){
            View splotchView = new View(this);
            splotchView.setBackgroundColor(0xFF000000 | (int)(Math.random() * 0x00FFFFFF));
            paletteView.addView(splotchView);
        }
    }
}