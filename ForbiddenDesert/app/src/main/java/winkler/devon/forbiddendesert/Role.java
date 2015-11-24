package winkler.devon.forbiddendesert;

/**
 * Created by devonwinkler on 11/23/15.
 */
public class Role {
    public static enum Type {
        Archeologist, Climber, Explorer, Meteorologist, Navigator, WaterCarrier
    }
    Type _type;

    public Role(Type type){
        _type = type;
    }

    public int getMaxWater(){
        int maxWater = 0;
        switch (_type){
            case Climber:
            case Archeologist:
                maxWater = 3;
                break;
            case Explorer:
            case Meteorologist:
            case Navigator:
                maxWater = 4;
                break;
            case WaterCarrier:
                maxWater = 5;
                break;
        }
        return maxWater;
    }

    public int getSandTileRemoveCount(){
        int removeCount = 1;
        if(_type== Type.Archeologist)
            removeCount = 2;
        return removeCount;
    }

    public int getWaterReplenishCount(){
        int waterCount = 1;
        if(_type == Type.WaterCarrier)
            waterCount = 2;
        return waterCount;
    }

    public int getColor(){
        int color = 0x00000000;
        switch (_type){
            case Climber:
                color = 0xFF000000;
                break;
            case Archeologist:
                color = 0xFFFF0000;
                break;
            case Explorer:
                color = 0xFF00FF00;
                break;
            case Meteorologist:
                color = 0xFF888888;
                break;
            case Navigator:
                color = 0xFFFFFF00;
                break;
            case WaterCarrier:
                color = 0xFF0000FF;
                break;

        }
        return color;
    }
}
