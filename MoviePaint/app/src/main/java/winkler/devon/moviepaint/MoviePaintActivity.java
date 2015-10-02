package winkler.devon.moviepaint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MoviePaintActivity extends Activity {

    PaintAreaView canvas;
    Button paintButton, clearButton, watchButton, increaseButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LinearLayout activityLayout = new LinearLayout(this);
        activityLayout.setOrientation(LinearLayout.HORIZONTAL);
        activityLayout.setBackgroundColor(0xFFDDDDDD);

        final LinearLayout menuLayout = new LinearLayout(this);
        menuLayout.setOrientation(LinearLayout.VERTICAL);
        menuLayout.setBackgroundColor(0xFFDDDDDD);

        paintButton = new Button(this);
        paintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newColorIntent = new Intent(MoviePaintActivity.this, PalleteActivity.class);
                startActivityForResult(newColorIntent, 1);
            }
        });
        menuLayout.addView(paintButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        clearButton = new Button(this);
        clearButton.setText("Clear");
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.clearLines();
            }
        });
        menuLayout.addView(clearButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        watchButton = new Button(this);
        watchButton.setText("Watch");
        watchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.setWatchMode(true);
            }
        });
        menuLayout.addView(watchButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        increaseButton = new Button(this);
        increaseButton.setText("Up");
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.increasePercentage();
            }
        });
        menuLayout.addView(increaseButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        canvas = new PaintAreaView(this);
        canvas.setBackgroundColor(Color.WHITE);

        activityLayout.addView(menuLayout);
        activityLayout.addView(canvas, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        setContentView(activityLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            File file = new File(getFilesDir(), "drawing.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            String jsonString = reader.readLine();

            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<PaintAreaView.PolyLine>>(){}.getType();
            ArrayList<PaintAreaView.PolyLine> lines = gson.fromJson(jsonString, collectionType);
            canvas.setLines(lines);

        }catch (Exception e){
            Log.e("Persistence", "Error saving file: " + e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        List<PaintAreaView.PolyLine> polyLines = canvas.getLines();
        Gson gson = new Gson();
        String jsonString = gson.toJson(polyLines);
        try{
            File file = new File(getFilesDir(), "drawing.txt");
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(jsonString);
            writer.close();

        }catch (Exception e){
            Log.e("Persistence", "Error saving file: " + e.getMessage());
        }
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            int paintColor = data.getIntExtra("paintColor", 0xFF000000);
            canvas.onPaintChange(paintColor);
            paintButton.setBackgroundColor(paintColor);
        }
    }

}
