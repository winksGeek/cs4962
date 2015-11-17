package winkler.devon.forbiddendesert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by devonwinkler on 11/16/15.
 */
public class RoleRandom {
    private ArrayList<Player.Role> _roles;
    public RoleRandom(){
        _roles = new ArrayList<>();
        _roles.add(Player.Role.Archeologist);
        _roles.add(Player.Role.Climber);
        _roles.add(Player.Role.Explorer);
        _roles.add(Player.Role.Meteorologist);
        _roles.add(Player.Role.Navigator);
        _roles.add(Player.Role.WaterCarrier);
    }

    public Player.Role next(){
        if(_roles.size() > 0) {
            Random rand = new Random();
            int index = rand.nextInt(_roles.size());
            return _roles.remove(index);
        }
        return null;
    }
}
