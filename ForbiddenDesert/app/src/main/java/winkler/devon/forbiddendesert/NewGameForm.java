package winkler.devon.forbiddendesert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class NewGameForm extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_form);
    }

    public void startNewGame(View button){
        ForbiddenDataModel model = ForbiddenDataModel.getInstance();
        Intent newGameIntent = new Intent(this, GameActivity.class);
        newGameIntent.putExtra(GameActivity.GAME_ID, model.addGame(5));
        startActivity(newGameIntent);
    }
}
