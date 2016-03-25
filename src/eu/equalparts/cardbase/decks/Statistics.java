package eu.equalparts.cardbase.decks;

import eu.equalparts.cardbase.cards.Card;

public final class Statistics {

	private Statistics() {}
	
	public static double calculatePercentage(StandaloneDeck deck, String type) {
		double allCardsByType = count(deck, type);
		double allCards = count(deck);
		return allCardsByType / allCards;
	}
	
	public static int count(StandaloneDeck deck, String type) {
		int count = type.contains("Land") ? countBasicLands(deck) : 0;
		for (Card card : deck.cards) {
			if (card.type != null &&
					card.type.contains(type)) {
				// TODO sort this out
				count += 1;
			}
		}
		return count;
	}

	public static int count(StandaloneDeck deck) {
		int totalCards = countBasicLands(deck);
		
		for (Card card : deck.cards) {
			// TODO sort this out
			totalCards += 1;
		}
		
		return totalCards;
	}
	
	private static int countBasicLands(StandaloneDeck deck) {
		return deck.plains + 
				deck.islands + 
				deck.swamps + 
				deck.mountains + 
				deck.forests;
	}
	
	public static int[] computeDistribution(StandaloneDeck deck, String type) {
		int arraySize = 0;
		for (Card card : deck.cards) {
			if (card.type != null && card.type.contains(type))
				if (card.cmc != null && card.cmc >= arraySize)
					arraySize = card.cmc + 1;
		}

		int[] costs = new int[arraySize];
		for (Card card : deck.cards) {
			if (card.type != null && card.type.contains(type))
				if (card.cmc != null)
					// TODO sort this out
					costs[card.cmc] += 1; 
		}
		
		return costs;
	}

	public static int[] computeDistribution(StandaloneDeck deck) {
		int arraySize = 0;
		for (Card card : deck.cards) {
			if (card.cmc != null && card.cmc >= arraySize)
				arraySize = card.cmc + 1;
		}
		
		int[] costs = new int[arraySize];
		for (Card card : deck.cards) {
			if (card.cmc != null)
				// TODO sort this out
				costs[card.cmc] += 1; 
		}
		
		return costs;
	}
	
}
