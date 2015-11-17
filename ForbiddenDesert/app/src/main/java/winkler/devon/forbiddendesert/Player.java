package winkler.devon.forbiddendesert;

/**
 * Created by devonwinkler on 11/16/15.
 */
public class Player {
    public static enum Role{
        Archeologist, Climber, Explorer, Meteorologist, Navigator, WaterCarrier
    }
    private String name;
    private Role role;
    private int id;
}
