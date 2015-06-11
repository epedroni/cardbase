package eu.equalparts.cardbase.data;

import java.util.Map;

public class FullCardSet {
	
	public String name;
	public String code;
	public String magicCardsInfoCode;
	public String releaseDate;
	public String border;
	public String type;
	public String block;
	public String gathererCode;
	public Map<String, Card> cards;

	/**
	 * Searches for a card by number (the one shown on the card itself).
	 * 
	 * @param number the number of the card to search.
	 * @return the requested {@code Card}, or null if no card is found with that number.
	 */
	public Card getCardByNumber(String number) {
		return cards.get(number);
	}
}