package winkler.devon.battleship;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by devonwinkler on 11/5/15.
 */
public class BattleshipSession {
    private static BattleshipSession _Instance = null;
    private Map<String, Object> _vars;
    public static BattleshipSession getInstance(){
        if(_Instance == null){
            _Instance = new BattleshipSession();
        }
        return _Instance;
    }

    private BattleshipSession(){
        _vars = new HashMap<>();
        _vars.put("mainApiUrl", "http://battleship.pixio.com/api/");
    }

    public void setSessionVariable(String name, Object value){
        _vars.put(name, value);
    }

    public Object getSessionVariable(String name){
        if(_vars.containsKey(name)) {
            return _vars.get(name);
        }
        return null;
    }
}
