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
    int _actionsLeft;
    boolean _active;
    private ArrayList<ItemCard> _hand;
    DesertTile _placedASolarShield;

    public Player(Role role){
        _role = role;
        _waterLeft = role.getMaxWater();
        _actionsLeft = 0;
        _hand = new ArrayList<>();
        _placedASolarShield = null;
    }

    public String getRoleStringForView(){
        String role = getRoleString();
        if(_actionsLeft > 0){
            role += ": " + _actionsLeft + " actions";
        }
        return role;
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
        return _waterLeft <= 0;
    }

    public boolean checkLegalMove(int xPos, int yPos){
        return _role.checkLegalMove(this.xPos, this.yPos, xPos, yPos);
    }

    public boolean checkLegalSandMove(int xPos, int yPos){
        boolean result = _role.checkLegalMove(this.xPos, this.yPos, xPos, yPos);
        result = result || (this.xPos == xPos && this.yPos == yPos);
        return result;
    }

    public boolean checkLegalWaterShare(int xPos, int yPos){
        boolean result = this.xPos == xPos && this.yPos == yPos;
        if(_role._type == Role.Type.WaterCarrier){
            result = result || _role.checkLegalMove(this.xPos, this.yPos, xPos, yPos);
        }
        return result;
    }

    public void addCardToHand(ItemCard card){
        _hand.add(card);
        card.owner = this;
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

    public boolean isActive(){
        return _active;
    }

    public void setActive(boolean active){
        _active = active;
    }

    public int getNumberOfTilesAbleToRemove() {
        return _role.getSandTileRemoveCount();
    }

    public ArrayList<ItemCard> getHand(){
        return _hand;
    }

    public void setTurnTimer(DesertTile set){
        _placedASolarShield = set;
    }

    public void removeTurnTimer(){
        _placedASolarShield.destroySolarShield();
        _placedASolarShield = null;
    }

    public void removeCardFromHand(ItemCard card){
        int i;
        for(i = 0; i < _hand.size(); i++){
            ItemCard tempCard = _hand.get(i);
            if(tempCard.type == card.type) {
                break;
            }
        }
        _hand.remove(i);
    }
}
