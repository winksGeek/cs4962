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
    public Part part = Part.None;
    public boolean isPassable = true;

}
