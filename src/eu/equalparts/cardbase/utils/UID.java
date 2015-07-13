package eu.equalparts.cardbase.utils;

import eu.equalparts.cardbase.cards.Card;

public class UID {

	/**
	 * Used in the hash generation.
	 */
	private static final String HASH_DIVIDER = "~";
	
	/**
	 * Generate the hash used as a key in the storage map.
	 * 
	 * @param setCode the card's set code.
	 * @param number the card's set number.
	 * @return the generated hash.
	 */
	public static String makeHash(String setCode, String number) {
		return setCode + HASH_DIVIDER + number;
	}

	/**
	 * Generate the hash used as a key in the storage map.
	 * 
	 * @param the {@code Card} whose hash is desired.
	 * @return the generated hash.
	 */
	public static String makeHash(Card card) {
		return card.setCode + HASH_DIVIDER + card.number;
	}

}
