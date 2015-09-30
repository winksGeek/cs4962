package winkler.devon.moviepaint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class PalleteActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(0xFFFFFFFF);

        final PaletteView paletteView = new PaletteView(this);
        paletteView.setPadding(30, 30, 30, 30);
        paletteView.setBackgroundColor(0xFFFFFFFF);
        LinearLayout.LayoutParams paletteLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        paletteLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        DeletePaintView deletePaintView = new DeletePaintView(this);
        deletePaintView.setMyDeletelistener(paletteView);
        paletteView.addView(deletePaintView);
        paletteView.initializePalette();
        layout.addView(paletteView, paletteLayoutParams);

        Button chooseColorButton = new Button(this);
        chooseColorButton.setText("Choose");
        chooseColorButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.putExtra("paintColor", paletteView.activePaint.getColor());
                setResult(RESULT_OK, intent);
                finish();
            }

        });
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        buttonLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layout.addView(chooseColorButton, buttonLayoutParams);

        setContentView(layout);
    }
}
