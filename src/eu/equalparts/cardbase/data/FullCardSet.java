package eu.equalparts.cardbase.data;

import java.util.List;

public class FullCardSet {
	
	private String name;
	private String code;
	private String releaseDate;
	private String border;
	private String type;
	private String block;
	private String gathererCode;
	private List<Card> cards;

	/**
	 * @return the set's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the set code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the set's release date.
	 */
	public String getReleaseDate() {
		return releaseDate;
	}
	
	/**
	 * @return the set's border type.
	 */
	public String getBorder() {
		return border;
	}

	/**
	 * @return the type of the set.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the set's block.
	 */
	public String getBlock() {
		return block;
	}

	/**
	 * @return the set's Gatherer code.
	 */
	public String getGathererCode() {
		return gathererCode;
	}

	/**
	 * @return a full list of the set's cards.
	 */
	public List<Card> getCards() {
		return cards;
	}

	/**
	 * Searches for a card by number (the one shown on the card itself).
	 * 
	 * @param number the number of the card to search.
	 * @return the requested {@code Card}, or null if no card is found with that number.
	 */
	public Card getCardByNumber(String number) {
		for (Card card : cards) {
			if (card.number.equals(number)) 
				return card;
		}
		return null;
	}
}