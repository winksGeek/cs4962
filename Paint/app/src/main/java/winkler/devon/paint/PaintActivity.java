package winkler.devon.paint;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class PaintActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PaletteView paletteView = new PaletteView(this);
        paletteView.setPadding(30, 60,90,120);
        setContentView(paletteView);

        for(int i = 0; i < 10; i++){
            View splotchView = new View(this);
            splotchView.setBackgroundColor(0xFF000000 | (int)(Math.random() * 0x00FFFFFF));
            paletteView.addView(splotchView);
        }
    }
}