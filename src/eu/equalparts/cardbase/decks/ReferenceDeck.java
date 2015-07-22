package eu.equalparts.cardbase.decks;

import java.util.HashMap;
import java.util.Map;

import eu.equalparts.cardbase.cards.Card;

public class ReferenceDeck extends Deck {

	public Map<Integer, Integer> cardReferences = new HashMap<Integer, Integer>();
	
	public ReferenceDeck() {
		
	}
	
	public ReferenceDeck(StandaloneDeck deck) {
		this.name = deck.name;
		this.plains = deck.plains;
		this.islands = deck.islands;
		this.swamps = deck.swamps;
		this.mountains = deck.mountains;
		this.forests = deck.forests;
		
		for (Card card : deck.cards) {
			cardReferences.put(card.hashCode(), card.count);
		}
	}
	
}
