package eu.equalparts.cardbase.cardstorage;

import java.util.Collection;

import eu.equalparts.cardbase.cards.Card;

/**
 * TODO fix comments
 * Holds actual card data in addition to the card counts in {@code ReferenceCardContainer}.
 * 
 * @author Eduardo Pedroni
 *
 */
public interface StandaloneCardContainer extends ReferenceCardContainer {
	/**
	 * Returns a card from the cardbase by set code and number.
	 * If no such card is in the cardbase, returns null.
	 * 
	 * @param setCode the set to which the requested card belongs.
	 * @param number the requested card's set number.
	 * @return the requested {@code Card} or null if no card is found.
	 */
	public Card getCard(String setCode, String number);
	/**
	 * This method is intended to allow iteration directly on the list of cards,
	 * while at the same time retaining control over the insert and remove procedures.
	 * The returned {@code List} is a read-only; trying to modify its structure will
	 * result in an {@code UnsupportedOperationException}.
	 * 
	 * @return an unmodifiable list of all the cards in the cardbase.
	 */
	public Collection<Card> getCards();
	/**
	 * @param fieldName the name of the field by which to sort.
	 * @return an unmodifiable collection representing the cardbase sorted in the required order.
	 * @throws NoSuchFieldException if the field provided is invalid.
	 */
	public Collection<Card> sortByField(String fieldName) throws NoSuchFieldException;
	
}
