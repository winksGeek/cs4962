package winkler.devon.forbiddendesert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ForbiddenDataModel _model = ForbiddenDataModel.getInstance();
        try{
            File file = new File(getFilesDir(), "gameModel.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            String jsonString = reader.readLine();

            Gson gson = new Gson();
            Type collectionType = new TypeToken<HashMap<String, Game>>(){}.getType();
            HashMap<String, Game> games = gson.fromJson(jsonString, collectionType);
            _model.setGames(games);
        }catch (Exception e){
            Log.e("Persistence", "Error loading file: " + e.getMessage());
        }
    }

    public void startNewGame(View button){
        Intent newGameIntent = new Intent(this, NewGameForm.class);
        startActivity(newGameIntent);
    }

    public void startLoadGame(View button){
        Intent loadGameIntent = new Intent(this, LoadGameActivity.class);
        startActivity(loadGameIntent);
    }

    public void startInstructions(View button){
        Intent instructionsIntent = new Intent(this, InstructionsActivity.class);
        startActivity(instructionsIntent);
    }
}
