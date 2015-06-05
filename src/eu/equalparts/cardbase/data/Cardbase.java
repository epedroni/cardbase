package eu.equalparts.cardbase.data;

import java.util.ArrayList;

public class Cardbase {

	public ArrayList<Card> cards = new ArrayList<>();
	public ArrayList<Deck> decks = new ArrayList<>();
	
	/**
	 * @param setCode
	 * @param number 
	 * @return the card if found, else null.
	 */
	public Card getCardByNumber(String setCode, String number) {
		for (Card card : cards) {
			if (card.setCode.equals(setCode) && card.number.equals(number))
				return card;
		}
		
		return null;
	}
	
}
