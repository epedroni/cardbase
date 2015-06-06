package eu.equalparts.cardbase.data;

import java.util.ArrayList;

public class Cardbase {

	public ArrayList<Card> cards = new ArrayList<>();
	public ArrayList<Deck> decks = new ArrayList<>();
	
	/**
	 * @param setCode the set to which the requested card belongs.
	 * @param number the requested card's set number.
	 * @return the requested {@code Card} or null if no card is found.
	 */
	public Card getCardByNumber(String setCode, String number) {
		for (Card card : cards) {
			if (card.setCode.equals(setCode) && card.number.equals(number))
				return card;
		}
		
		return null;
	}
	
}
