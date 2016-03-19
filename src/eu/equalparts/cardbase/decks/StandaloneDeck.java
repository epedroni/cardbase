package eu.equalparts.cardbase.decks;

import java.util.HashMap;
import java.util.Map;

import eu.equalparts.cardbase.cards.Card;

public class StandaloneDeck extends Deck {
	
	private Map<Integer, Card> cards = new HashMap<Integer, Card>();
	private Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
	
}
