package winkler.devon.battleship;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

/**
 * Created by devonwinkler on 11/6/15.
 */

public class GameInfoObject implements Parcelable, Comparable {
    String currentTurn;
    String missilesLaunched;
    String player1;
    String player2;
    String winner;
    String name;
    String status;
    String id;

    protected GameInfoObject(Parcel in) {
        currentTurn = in.readString();
        missilesLaunched = in.readString();
        player1 = in.readString();
        player2 = in.readString();
        winner = in.readString();
        name = in.readString();
        status = in.readString();
        id = in.readString();
    }

    public static final Creator<GameInfoObject> CREATOR = new Creator<GameInfoObject>() {
        @Override
        public GameInfoObject createFromParcel(Parcel in) {
            return new GameInfoObject(in);
        }

        @Override
        public GameInfoObject[] newArray(int size) {
            return new GameInfoObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currentTurn);
        dest.writeString(missilesLaunched);
        dest.writeString(player1);
        dest.writeString(player2);
        dest.writeString(winner);
        dest.writeString(name);
        dest.writeString(status);
        dest.writeString(id);
    }

    @Override
    public int compareTo(Object another) {
        GameInfoObject info = (GameInfoObject) another;
        String anotherName = info.name;
        return this.name.compareTo(anotherName);
    }
}