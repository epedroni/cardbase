package eu.equalparts.cardbase.cardstorage;

import eu.equalparts.cardbase.cards.Card;

/**
 * A class which holds card counts by reference (hash).
 * 
 * @author Eduardo Pedroni
 *
 */
public interface ReferenceCardContainer {
	/**
	 * Returns the amount of the specified card. If the card is not present at all, return 0.
	 * 
	 * @param cardToCount a card whose count is to be returned.
	 * @return the count of the returned card in the container.
	 */
	public int getCount(Card cardToCount);
	
	/**
	 * @param cardToAdd the card to add the container.
	 * @param count the amount to add.
	 */
	public void addCard(Card cardToAdd, int count);
	
	/**
	 * @param cardToRemove the card to remove from the container.
	 * @param count the amount to remove.
	 * @return the amount that was effectively removed. Could be less than {@code count} depending on how many of the card were present.
	 */
	public int removeCard(Card cardToRemove, int count);
}
