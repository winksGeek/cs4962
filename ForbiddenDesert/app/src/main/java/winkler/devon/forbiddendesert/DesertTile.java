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
    public static enum Part{
        None, Propeller, Engine, Crystal, Navigation
    }

    public Type type;
    public int xPos;
    public int yPos;
    public int numberOfSandTiles = 0;
    public Status status = Status.Unflipped;
    public Part partHint = Part.None;
    public Part partContained = Part.None;
    public boolean highlighted = false;

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
        numberOfSandTiles++;
        return numberOfSandTiles > 1;
    }

    public void removeSand(int number){
        numberOfSandTiles-=number;
        if(numberOfSandTiles < 0) {
            numberOfSandTiles = 0;
        }
    }

    public void discoverPart(Part part){
        partContained = part;
    }
}
