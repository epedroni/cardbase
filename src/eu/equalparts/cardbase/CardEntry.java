package eu.equalparts.cardbase;

import eu.equalparts.cardbase.cards.Card;

/**
 * The purpose of this class is to hold a single {@code Card} object but
 * also all associated metadata, such as count.
 * 
 * @author Eduardo Pedroni
 *
 */
public class CardEntry {

	private final Card card;
	public int count;
	
	public CardEntry(Card card, int count) {
		this.card = card;
		this.count = count;
	}
	
	public Card card() {
		return card;
	}
}
