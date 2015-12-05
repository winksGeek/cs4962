package winkler.devon.forbiddendesert;

/**
 * Created by devonwinkler on 12/5/15.
 */
public class Part {
    public static enum Type{
        None, Propeller, Engine, Crystal, Navigation
    }

    Type _type;
    int xPos;
    int yPos;
    boolean collected;
    public Part(Type type, int xPos, int yPos){
        _type = type;
        this.xPos = xPos;
        this.yPos = yPos;
        collected = false;
    }
}
