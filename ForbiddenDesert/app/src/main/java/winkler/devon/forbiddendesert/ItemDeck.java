package winkler.devon.forbiddendesert;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by devonwinkler on 11/23/15.
 */
public class ItemDeck {
    private final int NUM_THROTTLE = 1;
    private final int NUM_SOLAR = 2;
    private final int NUM_JET = 3;
    private final int NUM_TERRA = 2;
    private final int NUM_WATER = 1;
    private final int NUM_BLASTER = 3;
    ArrayList<ItemCard> _cards;

    public ItemDeck(){
        _cards = new ArrayList<ItemCard>();
        _cards.addAll(getItemCards());
    }

    public ItemDeck(ArrayList<ItemCard> cards){
        _cards.addAll(cards);
    }

    private ArrayList<ItemCard> getItemCards(){
        ArrayList<ItemCard> cards = new ArrayList<>();
        for(int i = 0; i < NUM_BLASTER; i++){
            cards.add(createItemCard(ItemCard.Type.Blaster));
            if(i < NUM_THROTTLE)
                cards.add(createItemCard(ItemCard.Type.Throttle));
            if(i < NUM_WATER)
                cards.add(createItemCard(ItemCard.Type.Water));
            if(i < NUM_TERRA)
                cards.add(createItemCard(ItemCard.Type.Terrascope));
            if(i < NUM_SOLAR)
                cards.add(createItemCard(ItemCard.Type.Solar));
            if(i < NUM_JET)
                cards.add(createItemCard(ItemCard.Type.Jet));
        }
        return cards;
    }

    private ItemCard createItemCard(ItemCard.Type type){
        ItemCard card = new ItemCard();
        card.type = type;
        return card;
    }

    public ItemCard getNext(){
        Random rand = new Random();
        if(_cards.size() > 0) {
            int index = rand.nextInt(_cards.size());
            return _cards.remove(index);
        }
        return null;
    }
}
