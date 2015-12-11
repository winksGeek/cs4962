package winkler.devon.forbiddendesert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class NewGameForm extends Activity {

    public static final String NUMBER_OF_PLAYERS = "NUMBER_OF_PLAYERS";
    private LinkedList<CheckBox> boxesChecked = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_form);
    }

    public void startNewGame(View button){
        ForbiddenDataModel model = ForbiddenDataModel.getInstance();
        Intent newGameIntent = new Intent(this, GameActivity.class);
        int numPlayers = getNumberOfPlayers();
        boolean random = isRoleSelectionRandom();
        if (random) {
            newGameIntent.putExtra(GameActivity.GAME_ID, model.addGame(numPlayers));
            startActivity(newGameIntent);
        } else {
            if(numPlayers > boxesChecked.size()){
                Toast toast = Toast.makeText(this, "Please select " + numPlayers + " roles.", Toast.LENGTH_LONG);
                toast.show();
            }else {
                HashMap<Integer, Role.Type> roles = new HashMap<>();
                for (int i = 0; i < numPlayers; i++) {
                    CheckBox checkbox = boxesChecked.get(i);
                    roles.put(i, getCheckBoxRole(checkbox.getId()));
                }
                newGameIntent.putExtra(GameActivity.GAME_ID, model.addGame(numPlayers, roles));
                startActivity(newGameIntent);
            }
        }
    }

    public Role.Type getCheckBoxRole(int id){
        Role.Type type = Role.Type.Archeologist;
        switch (id){
            case R.id.roles_archeologist:
                type = Role.Type.Archeologist;
                break;
            case R.id.roles_climber:
                type = Role.Type.Climber;
                break;
            case R.id.roles_explorer:
                type = Role.Type.Explorer;
                break;
            case R.id.roles_water_carrier:
                type = Role.Type.WaterCarrier;
                break;
        }
        return type;
    }

    public void addCheckBoxToQueue(View checkBox){
        CheckBox box = (CheckBox) checkBox;
        boxesChecked.add(box);
        int numPlayers = getNumberOfPlayers();
        if(boxesChecked.size() > numPlayers){
            CheckBox oldestBox = boxesChecked.remove();
            oldestBox.setChecked(false);
        }
    }

    public void toggleRoles(View radio){
        RadioButton radioButton = (RadioButton) radio;
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.roles_checkbox_group);
        if(radioButton.getId() == R.id.radio_choose){
            linearLayout.setVisibility(View.VISIBLE);
        }else{
            linearLayout.setVisibility(View.GONE);
        }
    }

    private boolean isRoleSelectionRandom(){
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.role_selection_group);
        return radioGroup.getCheckedRadioButtonId() == R.id.radio_random;
    }

    private int getNumberOfPlayers(){
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.number_of_players_group);
        int id = radioGroup.getCheckedRadioButtonId();
        int numPlayers = 2;
        switch (id){
            case R.id.radio_three:
                numPlayers = 3;
                break;
            case R.id.radio_four:
                numPlayers = 4;
                break;
        }
        return numPlayers;
    }
}
