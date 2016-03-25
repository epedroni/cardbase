package eu.equalparts.cardbase.decks;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Deck {
	public String name = "Unnamed Deck";
	public int plains = 0,
			islands = 0,
			swamps = 0,
			mountains = 0,
			forests = 0;

	private Map<Integer, Integer> cardReferences = new HashMap<Integer, Integer>();
	
	public void addCard(Integer cardHash, Integer count) {
		cardReferences.put(cardHash, count);
	}
	
	public Map<Integer, Integer> getCardReferences() {
		return Collections.unmodifiableMap(cardReferences);
	}
}
