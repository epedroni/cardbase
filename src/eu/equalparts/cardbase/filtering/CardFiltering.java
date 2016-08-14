package eu.equalparts.cardbase.filtering;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import eu.equalparts.cardbase.card.Card;
import eu.equalparts.cardbase.cardfield.CardField;
import eu.equalparts.cardbase.cardfield.IntegerCardField;
import eu.equalparts.cardbase.comparator.SpecialFields.DirtyNumber;
import eu.equalparts.cardbase.filtering.Filter.FilterType;
import eu.equalparts.cardbase.utils.Utils;

/**
 * Contains and equals are not case sensitive. Regex is.
 * 
 * @author Eduardo Pedroni
 *
 */
public class CardFiltering {
	
	public static void filterByField(List<Card> cards, Filter filter) throws NoSuchFieldException {	
		Field fieldToFilter = Card.class.getDeclaredField(filter.fieldName);
		try {
			for (Iterator<Card> iterator = cards.iterator(); iterator.hasNext();) {
				if (!((CardField<?>) fieldToFilter.get(iterator.next())).filter(filter)) {
					iterator.remove();
				}
			}
		} catch (IllegalAccessException e) {
			System.out.println("Error: field " + fieldToFilter.getName() + " in Card is not visible.");
			e.printStackTrace();
		}
	}
	
	public static boolean isFilterValid(Filter filter) {
		// Check the integrity of the filter object
		if (filter == null || filter.fieldName == null || filter.value == null) {
			return false;
		}
		
		// Check that the specified field exists
		Field fieldToFilter;
		try {
			fieldToFilter = Card.class.getDeclaredField(filter.fieldName);
		} catch (NoSuchFieldException | SecurityException e) {
			return false;
		}
		
		// If filter is numeric, check that the field and value are valid
		if (filter.type == FilterType.GREATER_THAN || filter.type == FilterType.SMALLER_THAN) {
			// Check that the field is of type integer
			if (!fieldToFilter.getType().equals(IntegerCardField.class)
					&& !Utils.hasAnnotation(fieldToFilter, DirtyNumber.class)) {
				return false;
			}
			
			// Check that the value is an integer
			try {
				Integer.parseInt(filter.value);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		
		return true;
	}
}
