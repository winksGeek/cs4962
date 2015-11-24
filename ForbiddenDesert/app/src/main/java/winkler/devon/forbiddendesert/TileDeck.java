package winkler.devon.forbiddendesert;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by devonwinkler on 11/16/15.
 */
public class TileDeck {
    private final int NUM_TUNNELS = 3;
    private final int NUM_OASIS = 2;
    private final int NUM_ITEMS = 8;
    ArrayList<DesertTile> _tiles;
    public TileDeck(){
        _tiles = new ArrayList<DesertTile>();
        _tiles.addAll(getAllWaterTiles());
        _tiles.addAll(getAllPartTiles());
        _tiles.addAll(getAllOtherTiles());
    }

    private ArrayList<DesertTile> getAllOtherTiles() {
        ArrayList<DesertTile> tiles = new ArrayList<>();

        //Crash Site
        tiles.add(getCrashTile());

        //Landing Site
        tiles.add(getLandingTile());

        //Tunnels
        for(int i = 0; i < NUM_TUNNELS; i++){
            tiles.add(getTunnelTile());
        }

        //Items
        for(int j = 0; j < NUM_ITEMS; j++){
            tiles.add(getItemTile());
        }

        return tiles;
    }

    private ArrayList<DesertTile> getAllPartTiles() {
        ArrayList<DesertTile> tiles = new ArrayList<>();

        //Engine
        tiles.add(getPartTile(DesertTile.Type.PieceRow, DesertTile.Part.Engine));
        tiles.add(getPartTile(DesertTile.Type.PieceColumn, DesertTile.Part.Engine));

        //Crystal
        tiles.add(getPartTile(DesertTile.Type.PieceRow, DesertTile.Part.Crystal));
        tiles.add(getPartTile(DesertTile.Type.PieceColumn, DesertTile.Part.Crystal));

        //Propeller
        tiles.add(getPartTile(DesertTile.Type.PieceRow, DesertTile.Part.Propeller));
        tiles.add(getPartTile(DesertTile.Type.PieceColumn, DesertTile.Part.Propeller));

        //Navigation
        tiles.add(getPartTile(DesertTile.Type.PieceRow, DesertTile.Part.Navigation));
        tiles.add(getPartTile(DesertTile.Type.PieceColumn, DesertTile.Part.Navigation));

        return tiles;
    }

    private ArrayList<DesertTile> getAllWaterTiles() {
        ArrayList<DesertTile> tiles = new ArrayList<>();
        for(int i = 0; i < NUM_OASIS; i++){
            tiles.add(getWaterTile());
        }
        tiles.add(getMirageTile());
        return tiles;
    }

    private DesertTile getWaterTile(){
        DesertTile waterTile = new DesertTile();
        waterTile.type = DesertTile.Type.Oasis;
        return waterTile;
    }

    private DesertTile getMirageTile(){
        DesertTile mirageTile = new DesertTile();
        mirageTile.type = DesertTile.Type.Mirage;
        return mirageTile;
    }

    private DesertTile getTunnelTile(){
        DesertTile tunnelTile = new DesertTile();
        tunnelTile.type = DesertTile.Type.Tunnel;
        return tunnelTile;
    }

    private DesertTile getItemTile(){
        DesertTile itemTile = new DesertTile();
        itemTile.type = DesertTile.Type.Item;
        return itemTile;
    }

    private DesertTile getLandingTile(){
        DesertTile landingTile = new DesertTile();
        landingTile.type = DesertTile.Type.Landing;
        return landingTile;
    }

    private DesertTile getCrashTile(){
        DesertTile crashTile = new DesertTile();
        crashTile.type = DesertTile.Type.Crash;
        return crashTile;
    }

    private DesertTile getPartTile(DesertTile.Type type, DesertTile.Part part){
        DesertTile partTile = new DesertTile();
        partTile.type = type;
        partTile.partHint = part;
        return partTile;
    }

    public DesertTile getStormTile(){
        DesertTile stormTile = new DesertTile();
        stormTile.type = DesertTile.Type.Storm;
        return stormTile;
    }

    public DesertTile getNext(){
        Random rand = new Random();
        int index = rand.nextInt(_tiles.size());
        return _tiles.remove(index);
    }
}
