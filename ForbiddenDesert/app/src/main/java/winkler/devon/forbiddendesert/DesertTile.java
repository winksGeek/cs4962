package winkler.devon.forbiddendesert;

import android.os.Parcelable;

/**
 * Created by devonwinkler on 11/16/15.
 */
public class DesertTile {
    public static enum Type{
        PieceColumn, Item, Tunnel, PieceRow, Storm, Mirage, Oasis, Landing, Crash
    }
    public static enum Status{
        Flipped, Unflipped
    }

    public Type type;
    public int xPos;
    public int yPos;
    public int numberOfSandTiles = 0;
    public Status status = Status.Unflipped;
    public Part.Type partHint = Part.Type.None;
    public Part partContained = null;
    public boolean highlighted = false;
    private boolean _solarSheildActive = false;

    public boolean isPassable(){
        return numberOfSandTiles <= 1 && type != Type.Storm;
    }

    public boolean flipTile(){
        if(numberOfSandTiles <= 0 && status == Status.Unflipped){
            status = Status.Flipped;
            return true;
        }
        return false;
    }

    public boolean addSandTile(){
        return addSandTile(1);
    }

    public boolean addSandTile(int number){
        numberOfSandTiles += number;
        return numberOfSandTiles > 1;
    }

    public boolean removeSand(int number){
        boolean removedSand = numberOfSandTiles > 0;
        numberOfSandTiles-=number;
        if(numberOfSandTiles < 0) {
            numberOfSandTiles = 0;
        }
        return removedSand;
    }

    public void discoverPart(Part part){
        partContained = part;
    }

    public Part claimPart(){
        Part partCopy = partContained;
        partContained = null;
        return partCopy;
    }

    public boolean coveredBySolarShield(){
        return _solarSheildActive;
    }

    public void placeSolarShield(){
        _solarSheildActive = true;
    }

    public void destroySolarShield(){
        _solarSheildActive = false;
    }
}
