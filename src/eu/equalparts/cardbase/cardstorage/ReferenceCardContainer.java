package eu.equalparts.cardbase.cardstorage;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.equalparts.cardbase.cards.Card;

public abstract class ReferenceCardContainer {
	
	@JsonProperty private Map<Integer, Integer> cardReferences;
	
	public ReferenceCardContainer() {
		cardReferences = new HashMap<>();
	}

	public int getCount(Card cardToCount) {
		int hashCode = cardToCount.hashCode();
		return cardReferences.containsKey(hashCode) ? cardReferences.get(hashCode) : 0;
	}
	
	public void addCard(Card cardToAdd, int count) {
		int hashCode = cardToAdd.hashCode();
		if (cardReferences.containsKey(hashCode)) {
			cardReferences.replace(hashCode, cardReferences.get(hashCode) + count);
		} else {
			cardReferences.put(hashCode, count);
		}
	}

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
