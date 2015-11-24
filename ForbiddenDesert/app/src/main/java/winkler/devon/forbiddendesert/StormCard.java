package winkler.devon.forbiddendesert;

/**
 * Created by devonwinkler on 11/23/15.
 */
public class StormCard {
    public static enum Direction{
        North, East, South, West, None
    }
    public static enum Places{
        One, Two, Three, Zero
    }
    public static enum Type{
        Sun, Storm, Move
    }

    Type type;
    Places places;
    Direction direction;

}
