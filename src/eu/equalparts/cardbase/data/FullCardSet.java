package eu.equalparts.cardbase.data;

import java.util.ArrayList;

public class FullCardSet extends CardSet {
	
	private String border;
	private String type;
	private String block;
	private String gathererCode;
	private ArrayList<Card> cards;

	/**
	 * @return the border
	 */
	public String getBorder() {
		return border;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the block
	 */
	public String getBlock() {
		return block;
	}

	/**
	 * @return the gathererCode
	 */
	public String getGathererCode() {
		return gathererCode;
	}

	/**
	 * @return the cards
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}

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