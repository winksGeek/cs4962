package winkler.devon.battleship;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class BattleshipGameDisplayActivity extends Activity {
    BattleshipDataModel _model = BattleshipDataModel.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        GameInfoObject gameInfoObject = intent.getParcelableExtra("gameInfoObject");

        getActionBar().setTitle(gameInfoObject.name);

        FrameLayout layout = new FrameLayout(this);
        layout.setId(25);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        GameInfoFragment gameInfoFragment = (GameInfoFragment)getFragmentManager().findFragmentByTag(BattleshipMainActivity.GAME_INFO_TAG);
        if(gameInfoFragment == null){
            gameInfoFragment = GameInfoFragment.newInstance(gameInfoObject);
            transaction.add(layout.getId(), gameInfoFragment, BattleshipMainActivity.GAME_INFO_TAG);
        }
        transaction.commit();
        setContentView(layout);
    }
}
