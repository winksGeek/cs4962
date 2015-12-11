package winkler.devon.forbiddendesert;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by devonwinkler on 12/10/15.
 */
public class LoadGameActivity extends Activity implements ListAdapter {

    ArrayList<Game> _gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ForbiddenDataModel model = ForbiddenDataModel.getInstance();
        _gameList = model.getGamesArrayList();
        ListView listView = new ListView(this);
        listView.setAdapter(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent gameIntent = new Intent(view.getContext(), GameActivity.class);
                gameIntent.putExtra(GameActivity.GAME_ID, _gameList.get(position)._id);
                startActivity(gameIntent);
            }
        });
        setContentView(listView);
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public int getCount() {
        return _gameList.size();
    }

    @Override
    public Object getItem(int position) {
        return _gameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView gameView = new TextView(this);
        int padding = (int)(8.0f * getResources().getDisplayMetrics().density);
        Game game =  (Game)getItem(position);
        String text = "Game Started: " + game._gameStarted.getTime() + "\nStatus: " + game.gameOver;
        gameView.setText(text);
        gameView.setPadding(padding, padding, padding, padding);
        return gameView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }
}
