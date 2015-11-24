package winkler.devon.forbiddendesert;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by devonwinkler on 11/23/15.
 */
public class StormDeck {
    private final int NUM_SUN = 4;
    private final int NUM_STORM = 3;
    private final int NUM_THREE = 1;
    private final int NUM_TWO = 2;
    private final int NUM_ONE = 3;
    ArrayList<StormCard> _cards;

    public StormDeck(){
        _cards = new ArrayList<StormCard>();
        _cards.addAll(getStormPicksUpCards());
        _cards.addAll(getSunBeatsDownCards());
        _cards.addAll(getStormMovesCards());
    }

    private ArrayList<StormCard> getStormPicksUpCards(){
        ArrayList<StormCard> cards = new ArrayList<>();
        for(int i = 0; i < NUM_STORM; i++){
            StormCard stormCard = new StormCard();
            stormCard.type = StormCard.Type.Storm;
            stormCard.direction = StormCard.Direction.None;
            stormCard.places = StormCard.Places.Zero;
            cards.add(stormCard);
        }
        return cards;
    }

    private ArrayList<StormCard> getSunBeatsDownCards(){
        ArrayList<StormCard> cards = new ArrayList<>();
        for(int i = 0; i < NUM_SUN; i++){
            StormCard stormCard = new StormCard();
            stormCard.type = StormCard.Type.Sun;
            stormCard.direction = StormCard.Direction.None;
            stormCard.places = StormCard.Places.Zero;
            cards.add(stormCard);
        }
        return cards;
    }

    private ArrayList<StormCard> getStormMovesCards(){
        ArrayList<StormCard> cards = new ArrayList<>();

        //add storm moves north
        for(int n = 0; n < NUM_ONE; n++){
            cards.add(createStormMoveCard(StormCard.Direction.North, StormCard.Places.One));
            if(n < NUM_TWO){
                cards.add(createStormMoveCard(StormCard.Direction.North, StormCard.Places.Two));
            }
            if(n < NUM_THREE){
                cards.add(createStormMoveCard(StormCard.Direction.North, StormCard.Places.Three));
            }

        }

        //add storm moves east
        for(int e = 0; e < NUM_ONE; e++){
            cards.add(createStormMoveCard(StormCard.Direction.East, StormCard.Places.One));
            if(e < NUM_TWO){
                cards.add(createStormMoveCard(StormCard.Direction.East, StormCard.Places.Two));
            }
            if(e < NUM_THREE){
                cards.add(createStormMoveCard(StormCard.Direction.East, StormCard.Places.Three));
            }
        }

        //add storm moves south
        for(int s = 0; s < NUM_ONE; s++){
            cards.add(createStormMoveCard(StormCard.Direction.South, StormCard.Places.One));
            if(s < NUM_TWO){
                cards.add(createStormMoveCard(StormCard.Direction.South, StormCard.Places.Two));
            }
            if(s < NUM_THREE){
                cards.add(createStormMoveCard(StormCard.Direction.South, StormCard.Places.Three));
            }

        }

        //add storm moves west
        for(int w = 0; w < NUM_ONE; w++){
            cards.add(createStormMoveCard(StormCard.Direction.West, StormCard.Places.One));
            if(w < NUM_TWO){
                cards.add(createStormMoveCard(StormCard.Direction.West, StormCard.Places.Two));
            }
            if(w < NUM_THREE){
                cards.add(createStormMoveCard(StormCard.Direction.West, StormCard.Places.Three));
            }

        }
        return cards;
    }

    private StormCard createStormMoveCard(StormCard.Direction direction, StormCard.Places places){
        StormCard card = new StormCard();
        card.type = StormCard.Type.Move;
        card.places = places;
        card.direction = direction;
        return card;
    }

    public StormCard getNext(){
        Random rand = new Random();
        int index = rand.nextInt(_cards.size());
        return _cards.remove(index);
    }
}
