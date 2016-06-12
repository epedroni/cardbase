package eu.equalparts.cardbase.cardstorage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.equalparts.cardbase.cards.Card;

/**
 * TODO fix comments
 * Holds actual card data in addition to the card counts in {@code ReferenceCardContainer}.
 * 
 * @author Eduardo Pedroni
 *
 */
public class StandaloneCardContainer extends ReferenceCardContainer {
	/**
	 * A map with card hashes as entry keys (calculated used {@code Card.hashCode()})
	 * and card objects as entry values.
	 */
	@JsonProperty private Map<Integer, Card> cardData = new HashMap<>();

	/**
	 * Returns a card from the cardbase by set code and number.
	 * If no such card is in the cardbase, returns null.
	 * 
	 * @param setCode the set to which the requested card belongs.
	 * @param number the requested card's set number.
	 * @return the requested {@code Card} or null if no card is found.
	 */
	public Card getCard(String setCode, String number) {
		return cardData.get(Card.makeHash(setCode, number));
	}
	
	/**
	 * This method is intended to allow iteration directly on the list of cards,
	 * while at the same time retaining control over the insert and remove procedures.
	 * The returned {@code List} is a read-only; trying to modify its structure will
	 * result in an {@code UnsupportedOperationException}.
	 * 
	 * @return an unmodifiable list of all the cards in the cardbase.
	 */
	public List<Card> getCards() {
		return new LinkedList<Card>(cardData.values());
	}
	
	@Override
	public void addCard(Card cardToAdd, int count) {
		super.addCard(cardToAdd, count);
		cardData.putIfAbsent(cardToAdd.hashCode(), cardToAdd);
	}

	@Override
	public int removeCard(Card cardToRemove, int count) {
		int removed = super.removeCard(cardToRemove, count);
		
		if (getCount(cardToRemove) <= 0) {
			cardData.remove(cardToRemove.hashCode());
		}
		
		return removed;
	}
}
