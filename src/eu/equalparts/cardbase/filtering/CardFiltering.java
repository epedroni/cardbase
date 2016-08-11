package eu.equalparts.cardbase.filtering;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import eu.equalparts.cardbase.card.Card;
import eu.equalparts.cardbase.cardfield.CardField;

/**
 * Contains and equals are not case sensitive. Regex is.
 * 
 * @author Eduardo Pedroni
 *
 */
public class CardFiltering {

	public enum Filter {
		EQUALS, CONTAINS, REGEX;
	}

	public static void filterByField(List<Card> cards, String fieldName, Filter filterType, String filterValue) throws NoSuchFieldException {	
		Field fieldToFilter = Card.class.getDeclaredField(fieldName);
		try {
			for (Iterator<Card> iterator = cards.iterator(); iterator.hasNext();) {
				if (!((CardField<?>) fieldToFilter.get(iterator.next())).filter(filterType, filterValue)) {
					iterator.remove();
				}
			}
		} catch (IllegalArgumentException e) {
			System.out.println("Error: class Card does not define field \"" + fieldToFilter.getName() + "\".");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println("Error: field " + fieldToFilter.getName() + " in Card is not visible.");
			e.printStackTrace();
		}
	}

}
