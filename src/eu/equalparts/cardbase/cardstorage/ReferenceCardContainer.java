package eu.equalparts.cardbase.cardstorage;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.equalparts.cardbase.cards.Card;

/**
 * A class which holds card counts by reference (hash).
 * 
 * @author Eduardo Pedroni
 *
 */
public abstract class ReferenceCardContainer {
	
	/**
	 * A map with card hashes as entry keys (calculated used {@code Card.hashCode()}) and card amounts as entry values.
	 */
	@JsonProperty private Map<Integer, Integer> cardReferences = new HashMap<>();

	/**
	 * Returns the amount of the specified card. If the card is not present at all, return 0.
	 * 
	 * @param cardToCount a card whose count is to be returned.
	 * @return the count of the returned card in the container.
	 */
	public int getCount(Card cardToCount) {
		int hashCode = cardToCount.hashCode();
		return cardReferences.containsKey(hashCode) ? cardReferences.get(hashCode) : 0;
	}
	
	/**
	 * @param cardToAdd the card to add the container.
	 * @param count the amount to add.
	 */
	public void addCard(Card cardToAdd, int count) {
		int hashCode = cardToAdd.hashCode();
		if (cardReferences.containsKey(hashCode)) {
			cardReferences.replace(hashCode, cardReferences.get(hashCode) + count);
		} else {
			cardReferences.put(hashCode, count);
		}
	}

	/**
	 * @param cardToRemove the card to remove from the container.
	 * @param count the amount to remove.
	 * @return the amount that was effectively removed. Could be less than {@code count} depending on how many of the card were present.
	 */
	public int removeCard(Card cardToRemove, int count) {
		int hashCode = cardToRemove.hashCode();
		int removed = 0;
		
		if (cardReferences.containsKey(hashCode) && count > 0) {
			int oldCount = cardReferences.get(hashCode);
			
			if (oldCount > count) {
				cardReferences.replace(hashCode, oldCount - count);
				removed = count;
			} else {
				cardReferences.remove(hashCode);
				removed = oldCount;
			}
		}
		
		return removed;
	}
}
