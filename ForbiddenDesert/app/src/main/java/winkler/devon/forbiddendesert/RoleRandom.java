package winkler.devon.forbiddendesert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by devonwinkler on 11/16/15.
 */
public class RoleRandom {
    private ArrayList<Role.Type> _roles;
    public RoleRandom(){
        _roles = new ArrayList<>();
        _roles.add(Role.Type.Archeologist);
        _roles.add(Role.Type.Climber);
        _roles.add(Role.Type.Explorer);
        _roles.add(Role.Type.WaterCarrier);
    }

    public Role.Type next(){
        if(_roles.size() > 0) {
            Random rand = new Random();
            int index = rand.nextInt(_roles.size());
            return _roles.remove(index);
        }
        return null;
    }
}
