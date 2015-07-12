package eu.equalparts.cardbase.data;

import java.util.HashMap;
import java.util.Map;

import eu.equalparts.cardbase.Cardbase;

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
			cardReferences.put(Cardbase.makeHash(card), card.count);
		}
	}
	
}
