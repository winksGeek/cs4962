package winkler.devon.moviepaint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class MoviePaintActivity extends Activity {

    PaintAreaView canvas;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setBackgroundColor(0xFFDDDDDD);

        b = new Button(this);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newColorIntent = new Intent(MoviePaintActivity.this, PalleteActivity.class);
                startActivityForResult(newColorIntent, 1);
            }
        });
        layout.addView(b, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        canvas = new PaintAreaView(this);
        canvas.setBackgroundColor(Color.WHITE);

        layout.addView(canvas, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        setContentView(layout);
    }
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            int paintColor = data.getIntExtra("paintColor", 0xFF000000);
            canvas.onPaintChange(paintColor);
            b.setBackgroundColor(paintColor);
        }
    }

}
