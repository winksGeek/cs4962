package winkler.devon.battleship;

import android.app.Fragment;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by devonwinkler on 10/24/15.
 */
public class GameListFragment extends Fragment implements ListAdapter{

    public static final String GAME_LIST_KEY = "GAME_LIST_KEY";

    public interface GameListItemListener {
//        public GameInfoObject getGameInformation(String id);
        public void openGame(GameInfoObject gameInfoObject);
    }

    GameListItemListener _gameListItemListener;
    View.OnClickListener _newGameListener;
    ArrayList<GameInfoObject> _gameList;
    String _currentGameId;

    public static GameListFragment newInstance(ArrayList<GameInfoObject> games){
        GameListFragment gameListFragment = new GameListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(GAME_LIST_KEY, games);
//        args.putStringArrayList(GAME_LIST_KEY, games);
        gameListFragment.setArguments(args);
        return gameListFragment;
    }

    public void addGameToGameList(GameInfoObject gameId) {
        _gameList.add(gameId);
        refreshList();
    }

    public void setGameList(ArrayList<GameInfoObject> gameList){
        _gameList = gameList;
        refreshList();
    }

    public void setCurrentGameId(String currentGameId){
        _currentGameId = currentGameId;
        refreshList();
    }

    public void refreshList(){
        Collections.sort(_gameList);
        ListView gameListView = (ListView)getView();
        gameListView.invalidateViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _gameList = getArguments().getParcelableArrayList(GAME_LIST_KEY);
        _currentGameId = "";
        Button createGame = new Button(getActivity());
        createGame.setText("New Game");
        createGame.setOnClickListener(_newGameListener);

        ListView gameListView = new ListView(getActivity());
        gameListView.addHeaderView(createGame);
        gameListView.setAdapter(this);

        gameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (_gameListItemListener != null) {
                    _gameListItemListener.openGame((GameInfoObject)getItem(position - 1));
                }
            }
        });

        return gameListView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            _gameListItemListener = (GameListItemListener)context;
        }catch (ClassCastException ex){
            throw new ClassCastException(context.toString() + " must implement GameListItemListener");
        }
        try{
            _newGameListener = (View.OnClickListener)context;
        }catch (ClassCastException ex){
            throw new ClassCastException(context.toString() + " must implement View.OnClickListener");
        }
    }

    @Override
    public void onDetach() {
        _gameListItemListener = null;
        super.onDetach();
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
        TextView gameView = new TextView(getActivity());
        int padding = (int)(8.0f * getResources().getDisplayMetrics().density);
        GameInfoObject info =  (GameInfoObject)getItem(position);
        String text = info.name + "\n" + info.status;
        if(_currentGameId == info.id){
            gameView.setBackgroundColor(0xFFFFFFCC);
        }
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
