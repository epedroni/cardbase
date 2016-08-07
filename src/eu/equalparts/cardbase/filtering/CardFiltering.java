package eu.equalparts.cardbase.filtering;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import eu.equalparts.cardbase.cards.Card;

/**
 * Contains and equals are not case sensitive. Regex is.
 * 
 * @author Eduardo Pedroni
 *
 */
public class CardFiltering {

	enum FilterType {
		EQUALS {
			@Override
			public boolean doFilter(String value, String filter) {
				return value.equalsIgnoreCase(filter);
			}
		},	CONTAINS {
			@Override
			public boolean doFilter(String value, String filter) {
				return value.toLowerCase().contains(filter.toLowerCase());
			}
		}, REGEX {
			@Override
			public boolean doFilter(String value, String filter) {
				return value.matches(filter);
			}
		};

		public abstract boolean doFilter(String value, String filter);
	}

	public static Collection<Card> filterByField(List<Card> cards, String fieldName, FilterType filterType, String filterValue) throws NoSuchFieldException {
		Field fieldToFilter = Card.class.getDeclaredField(fieldName);
		try {
			for (Iterator<Card> iterator = cards.iterator(); iterator.hasNext();) {
				if (!filterType.doFilter((String) fieldToFilter.get(iterator.next()), filterValue)) {
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
		return cards;
	}

}
