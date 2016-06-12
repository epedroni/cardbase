package eu.equalparts.cardbase.sorting;

import java.util.List;

import eu.equalparts.cardbase.cards.Card;
import eu.equalparts.cardbase.comparator.CardComparator;

public abstract class CardSorting {

	/**
	 * @param cards
	 * @param fieldName the name of the field by which to sort.
	 * @return an unmodifiable collection representing the cardbase sorted in the required order.
	 * @throws NoSuchFieldException if the field provided is invalid.
	 */
	public static List<Card> sortByField(List<Card> cards, String fieldName) throws NoSuchFieldException {
		cards.sort(new CardComparator(Card.class.getDeclaredField(fieldName)));
		return cards;
	}
}
