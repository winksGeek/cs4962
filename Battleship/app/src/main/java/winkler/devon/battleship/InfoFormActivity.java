package winkler.devon.battleship;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class InfoFormActivity extends Activity {
    public static final int CREATE_GAME_INTENT = 11441;
    public static final int JOIN_GAME_INTENT = 11442;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final boolean create = intent.getBooleanExtra("create", false);
        final String gameId = intent.getStringExtra("gameId");
        getActionBar().setTitle(create ? "Create Game" : "Join Game");

        LinearLayout layout = new LinearLayout(this);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText gameNameField = new EditText(this);
        gameNameField.setHint("Game Name");
        if(create){
            layout.addView(gameNameField, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        }
        final EditText playerNameField = new EditText(this);
        playerNameField.setHint("Player Name");
        layout.addView(playerNameField, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        Button submit = new Button(this);
        submit.setText("Submit");
        layout.addView(submit, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent responseIntent = new Intent();
                if (create) {
                    String gameName = "".equals(gameNameField.getText().toString()) ? "New Game" : gameNameField.getText().toString();
                    responseIntent.putExtra("gameName", gameName);
                }
                String playerName = "".equals(playerNameField.getText().toString()) ? "New Player" : playerNameField.getText().toString();
                responseIntent.putExtra("playerName", playerName);
                if(!create){
                    responseIntent.putExtra("gameId", gameId);
                }
                setResult(create ? CREATE_GAME_INTENT : JOIN_GAME_INTENT, responseIntent);
                finish();
            }
        });

        setContentView(layout);
    }
}
