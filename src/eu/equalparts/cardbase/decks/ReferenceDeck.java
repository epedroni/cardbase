package eu.equalparts.cardbase.decks;

import java.util.Map.Entry;

public class ReferenceDeck extends Deck {
	
	public ReferenceDeck(String deckName) {
		this.name = deckName;
	}
	
	public ReferenceDeck(StandaloneDeck deck) {
		this.name = deck.name;
		this.plains = deck.plains;
		this.islands = deck.islands;
		this.swamps = deck.swamps;
		this.mountains = deck.mountains;
		this.forests = deck.forests;
		
		for (Entry<Integer, Integer> entry : deck.getCardReferences().entrySet()) {
			getCardReferences().put(entry.getKey(), entry.getValue());
		}
	}
	
}
