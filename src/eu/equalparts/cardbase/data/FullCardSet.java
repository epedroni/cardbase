package eu.equalparts.cardbase.data;

import java.util.Collections;
import java.util.Map;

public class FullCardSet {
	
	private String name;
	private String code;
	private String magicCardsInfoCode;
	private String releaseDate;
	private String border;
	private String type;
	private String block;
	private String gathererCode;
	private Map<String, Card> cards;

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
	 * @return a full unmodifiable map of the set's cards.
	 */
	public Map<String, Card> getCards() {
		return Collections.unmodifiableMap(cards);
	}
	
	/**
	 * Searches for a card by number (the one shown on the card itself).
	 * 
	 * @param number the number of the card to search.
	 * @return the requested {@code Card}, or null if no card is found with that number.
	 */
	public Card getCardByNumber(String number) {
		return cards.get(number);
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @param releaseDate the releaseDate to set
	 */
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	/**
	 * @param border the border to set
	 */
	public void setBorder(String border) {
		this.border = border;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param block the block to set
	 */
	public void setBlock(String block) {
		this.block = block;
	}

	/**
	 * @param gathererCode the gathererCode to set
	 */
	public void setGathererCode(String gathererCode) {
		this.gathererCode = gathererCode;
	}

	/**
	 * @param cards the cards to set
	 */
	public void setCards(Map<String, Card> cards) {
		this.cards = cards;
	}

	/**
	 * @return the magicCardsInfoCode
	 */
	public String getMagicCardsInfoCode() {
		return magicCardsInfoCode;
	}

	/**
	 * @param magicCardsInfoCode the magicCardsInfoCode to set
	 */
	public void setMagicCardsInfoCode(String magicCardsInfoCode) {
		this.magicCardsInfoCode = magicCardsInfoCode;
	}
}