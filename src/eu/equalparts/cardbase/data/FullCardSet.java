package eu.equalparts.cardbase.data;

import java.util.ArrayList;

public class FullCardSet extends CardSet {
	public String border;
	public String type;
	public String block;
	public String gathererCode;
	public ArrayList<Card> cards;

	/**
	 * Searches for a card by number (the one shown on the card itself).
	 * 
	 * @param number the number of the card to search.
	 * @return the card, or null if no card is found with that number.
	 */
	public Card getCardByNumber(String number) {
		for (Card card : cards) {
			if (card.number.equals(number)) 
				return card;
		}
		return null;
	}
}