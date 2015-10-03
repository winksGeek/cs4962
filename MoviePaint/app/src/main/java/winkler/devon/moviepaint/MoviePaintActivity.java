package winkler.devon.moviepaint;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

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

    PaintAreaView _canvas;
    Button _paintButton, _clearButton, _watchButton, _drawButton;
    ImageView _playButton;
    boolean _playing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LinearLayout activityLayout = new LinearLayout(this);
        activityLayout.setOrientation(LinearLayout.VERTICAL);
        activityLayout.setBackgroundColor(0xFFAAAAAA);

        final LinearLayout watchMenuLayout = new LinearLayout(this);
        watchMenuLayout.setOrientation(LinearLayout.HORIZONTAL);
        watchMenuLayout.setBackgroundColor(0xFFAAAAAA);
        watchMenuLayout.setGravity(Gravity.CENTER);

        final LinearLayout drawMenuLayout = new LinearLayout(this);
        drawMenuLayout.setOrientation(LinearLayout.HORIZONTAL);
        drawMenuLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        drawMenuLayout.setBackgroundColor(0xFFAAAAAA);

        _paintButton = new Button(this);
        _paintButton.setTextColor(0xFF000000);
        _paintButton.setText("Color");
        _paintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newColorIntent = new Intent(MoviePaintActivity.this, PalleteActivity.class);
                startActivityForResult(newColorIntent, 1);
            }
        });
        drawMenuLayout.addView(_paintButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        _clearButton = new Button(this);
        _clearButton.setText("Clear");
        _clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _canvas.clearLines();
            }
        });
        drawMenuLayout.addView(_clearButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        _watchButton = new Button(this);
        _watchButton.setText("Watch");
        _watchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _canvas.setWatchMode(true);
                activityLayout.removeView(drawMenuLayout);
                activityLayout.addView(watchMenuLayout);
            }
        });
        drawMenuLayout.addView(_watchButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));



        _drawButton = new Button(this);
        _drawButton.setText("Draw");
        _drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _canvas.setWatchMode(false);
                activityLayout.removeView(watchMenuLayout);
                activityLayout.addView(drawMenuLayout);
            }
        });
        watchMenuLayout.addView(_drawButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        _playButton = new ImageView(this);
        _playButton.setImageResource(R.drawable.player6);
        watchMenuLayout.addView(_playButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        final SeekBar sb = new SeekBar(this);
        watchMenuLayout.addView(sb, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                _canvas.setPercentage(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //not doing anything here
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //not doing anything here
            }
        });

        final ValueAnimator animator = new ValueAnimator();
        _playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!_playing){
                    if(sb.getProgress() >= 100){
                        sb.setProgress(0);
                    }
                    animator.setIntValues(sb.getProgress(), 100);
                    animator.setDuration((int)(5000.0 * (100.0 - (double)sb.getProgress())/100.0));
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            sb.setProgress((Integer) animation.getAnimatedValue());
                        }
                    });
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            _playing = true;
                            _playButton.setImageResource(R.drawable.rounded57);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            _playing = false;
                            _playButton.setImageResource(R.drawable.player6);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            _playing = false;
                            _playButton.setImageResource(R.drawable.player6);
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                            //not using this
                        }
                    });
                    animator.start();
                }else{
                    animator.cancel();
                }
            }
        });


        _canvas = new PaintAreaView(this);
        _canvas.setBackgroundColor(Color.WHITE);

        activityLayout.addView(_canvas, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        activityLayout.addView(drawMenuLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

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
            _canvas.setLines(lines);

        }catch (Exception e){
            Log.e("Persistence", "Error saving file: " + e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        List<PaintAreaView.PolyLine> polyLines = _canvas.getLines();
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
            _canvas.onPaintChange(paintColor);
            _paintButton.setTextColor(paintColor);
        }
    }

}
