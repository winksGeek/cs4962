package winkler.devon.forbiddendesert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class GameActivity extends Activity {
    public static final String GAME_ID = "GAME_ID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ForbiddenDataModel model = ForbiddenDataModel.getInstance();
        LinearLayout boardLayout = new LinearLayout(this);
        boardLayout.setOrientation(LinearLayout.VERTICAL);
        boardLayout.setGravity(Gravity.CENTER);

        Intent startGameIntent = getIntent();
        String gameId = startGameIntent.getStringExtra(GAME_ID);

        DesertBoardView boardView = new DesertBoardView(this);
        boardView.setPadding(5, 5, 5, 5);
        boardView.setBoard(model.getBoard(gameId));
        boardLayout.addView(boardView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        setContentView(boardLayout);
    }
}
