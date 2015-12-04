package winkler.devon.forbiddendesert;

import java.util.ArrayList;

/**
 * Created by devonwinkler on 11/16/15.
 */
public class Player {
     String _name;
     Role _role;
     int _id;
    int xPos;
    int yPos;
    private int _waterLeft;
    private ArrayList<ItemCard> _hand;

    public Player(Role role){
        _role = role;
        _waterLeft = role.getMaxWater();
    }

    public String getRoleString(){
        String role = "";
        switch (_role._type){
            case Archeologist:
                role = "Archeologist";
                break;
            case Climber:
                role = "Climber";
                break;
            case Explorer:
                role = "Explorer";
                break;
            case Meteorologist:
                role = "Meteorologist";
                break;
            case Navigator:
                role = "Navigator";
                break;
            case WaterCarrier:
                role = "Water Carrier";
                break;
        }
        return role;
    }

    public int getWaterLeft(){
        return _waterLeft;
    }

    public boolean drinkWater(){
        _waterLeft--;
        return _waterLeft >= 0;
    }

    public void addCardToHand(ItemCard card){
        _hand.add(card);
    }

    public void addWaterFromOasis(){
        addWater(_role.getWaterReplenishCount());
    }

    public void addWater(int amount){
        _waterLeft += amount;
        if(_waterLeft > _role.getMaxWater()){
            _waterLeft = _role.getMaxWater();
        }
    }

    public int getNumberOfTilesAbleToRemove() {
        return _role.getSandTileRemoveCount();
    }
}
