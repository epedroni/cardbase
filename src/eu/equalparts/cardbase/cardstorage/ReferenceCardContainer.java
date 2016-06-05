package eu.equalparts.cardbase.cardstorage;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.equalparts.cardbase.cards.Card;

/**
 * A class which contains card quantities with absolutely no other
 * information about the cards themselves.
 * 
 * @author Eduardo Pedroni
 *
 */
public class ReferenceCardContainer {

	/**
	 * Land field initialised to 0, accessed with getter and updated with setter.
	 */
	private int plains = 0, islands = 0, swamps = 0, forests = 0, mountains = 0;
	/**
	 * A map with card hashes as entry keys (calculated used {@code Card.hashCode()})
	 * and card amounts as entry values.
	 */
	@JsonProperty private Map<Integer, Integer> cardReferences = new HashMap<>();

	/**
	 * Returns the amount of the specified card. If the card is not present at all, return 0.
	 * 
	 * @param cardToCount a card whose count is to be returned.
	 * @return the count of the returned card in the container.
	 */
	public int getCount(Card cardToCount){
		int cardHash = cardToCount.hashCode();
		return cardReferences.containsKey(cardHash) ? cardReferences.get(cardHash) : 0;
	}
	
	/**
	 * @param cardToAdd the card to add the container.
	 * @param count the amount to add.
	 */
	public void addCard(Card cardToAdd, int count) {
		cardReferences.put(cardToAdd.hashCode(), getCount(cardToAdd) + count);
	}
	
	/**
	 * @param cardToRemove the card to remove from the container.
	 * @param count the amount to remove.
	 * @return the amount that was effectively removed. Could be less than {@code count}
	 * depending on how many of the card were present.
	 */
	public int removeCard(Card cardToRemove, int count) {
		int cardHash = cardToRemove.hashCode();
		if (count <= 0 || !cardReferences.containsKey(cardHash)) {
			return 0;
		}
		
		if (count >= cardReferences.get(cardHash)) {
			return cardReferences.remove(cardHash);
		} else {
			cardReferences.put(cardHash, cardReferences.get(cardHash) - count);
			return count;
		}
	}

	/**
	 * @return the plains
	 */
	public int getPlains() {
		return plains;
	}

	/**
	 * @return the islands
	 */
	public int getIslands() {
		return islands;
	}

	/**
	 * @return the swamps
	 */
	public int getSwamps() {
		return swamps;
	}

	/**
	 * @return the forests
	 */
	public int getForests() {
		return forests;
	}

	/**
	 * @return the mountains
	 */
	public int getMountains() {
		return mountains;
	}

	/**
	 * @param plains the plains to set
	 */
	public void setPlains(int plains) {
		this.plains = plains;
	}

	/**
	 * @param islands the islands to set
	 */
	public void setIslands(int islands) {
		this.islands = islands;
	}

	/**
	 * @param swamps the swamps to set
	 */
	public void setSwamps(int swamps) {
		this.swamps = swamps;
	}

	/**
	 * @param forests the forests to set
	 */
	public void setForests(int forests) {
		this.forests = forests;
	}

	/**
	 * @param mountains the mountains to set
	 */
	public void setMountains(int mountains) {
		this.mountains = mountains;
	}
}
