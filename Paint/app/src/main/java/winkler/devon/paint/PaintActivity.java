package winkler.devon.paint;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class PaintActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(0xFFDDDDDD);

        PaintAreaView canvas = new PaintAreaView(this);
        canvas.setBackgroundColor(Color.WHITE);

        layout.addView(canvas, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        PaletteView paletteView = new PaletteView(this);
        paletteView.setPadding(30, 30, 30, 30);
        paletteView.setBackgroundColor(0xFFDDDDDD);
        paletteView.setMyPaintChangeListener(canvas);
        LinearLayout.LayoutParams paletteLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        paletteLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        DeletePaintView deletePaintView = new DeletePaintView(this);
        deletePaintView.setMyDeletelistener(paletteView);
        paletteView.addView(deletePaintView);
        paletteView.initializePalette();
        layout.addView(paletteView, paletteLayoutParams);

        setContentView(layout);
    }
}