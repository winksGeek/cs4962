package winkler.devon.forbiddendesert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void startNewGame(View button){
        Intent newGameIntent = new Intent(this, NewGameForm.class);
        startActivity(newGameIntent);
    }
}
