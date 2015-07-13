package eu.equalparts.cardbase.decks;

import java.util.HashMap;
import java.util.Map;

import eu.equalparts.cardbase.cards.Card;
import eu.equalparts.cardbase.utils.UID;

public class ReferenceDeck extends Deck {

	public Map<String, Integer> cardReferences = new HashMap<String, Integer>();
	
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
			cardReferences.put(UID.makeHash(card), card.count);
		}
	}
	
}
