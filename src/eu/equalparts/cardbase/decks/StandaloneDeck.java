package eu.equalparts.cardbase.decks;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import eu.equalparts.cardbase.cards.Card;

public class StandaloneDeck extends Deck {
	private Map<Integer, Card> cardData = new HashMap<Integer, Card>();
	
	public Map<Integer, Card> getCardData() {
		return Collections.unmodifiableMap(cardData);
	}
}
