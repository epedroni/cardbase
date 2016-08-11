package eu.equalparts.cardbase.comparator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.function.BiFunction;

import eu.equalparts.cardbase.card.Card;
import eu.equalparts.cardbase.cardfield.CardField;
import eu.equalparts.cardbase.comparator.SpecialFields.DirtyNumber;
import eu.equalparts.cardbase.comparator.SpecialFields.Rarity;

/**
 * I'm new to this reflection business, so bear with me.
 * <br><br>
 * The idea here is to avoid having to write one class
 * for each comparable field in {@code Card}. The program
 * can dynamically instantiate them as cards are compared 
 * by different fields.
 * <br><br>
 * This class uses reflection to determine if the specified
 * field is comparable with itself upon construction, and throws
 * an {@code IllegalArgumentException} if that is not the case.
 * 
 * @author Eduardo Pedroni
 *
 */
//@SuppressWarnings({"rawtypes", "unchecked"})
public class CardComparator implements Comparator<Card> {
	
	/**
	 * The field being compared.
	 */
	private Field fieldToCompare;
	/**
	 * The comparison delegate to use for the specified field.
	 */
	private BiFunction<CardField, CardField, Integer> comparisonDelegate = (field1, field2) -> field1.compareTo(field2);
	
	/**
	 * Creates a new comparator for the specified field only. This class
	 * will only be constructed successfully if the field comes from
	 * {@code Card} and can be compared to itself (i.e. implements
	 * {@code Comparable<T>} where T is its own type.
	 * <br>
	 * For reference, {@code String} and {@code Integer} are both self comparable.
	 * 
	 * TODO comment
	 * 
	 * @param fieldToCompare the field this comparator will use to compare cards, as declared in {@code Card}.
	 */
	public CardComparator(Field fieldToCompare) {
		if (fieldToCompare.getDeclaringClass().equals(Card.class) &&
				CardField.class.isAssignableFrom(fieldToCompare.getType())) {

			this.fieldToCompare = fieldToCompare;
			
			// if annotated with a special comparator, set the comparison delegate here
			for (Annotation annotation : fieldToCompare.getAnnotations()) {
				if (annotation.annotationType() == DirtyNumber.class) {
					this.comparisonDelegate = ComparatorDelegates::compareDirtyNumber;
				} else if (annotation.annotationType() == Rarity.class) {
					this.comparisonDelegate = ComparatorDelegates::compareRarity;
				}
			}
		} else {
			throw new IllegalArgumentException("The field provided is not valid.");
		}
	}

	@Override
	public int compare(Card o1, Card o2) {
		/*
		 * we've already checked that the field is self comparable,
		 * so we are now free to cast to comparable
		 */
		try {
			CardField field1 = (CardField) fieldToCompare.get(o1);
			CardField field2 = (CardField) fieldToCompare.get(o2);
			
			// if either or both fields' values are null, skip delegation altogether since delegates are not required to deal with null values
			if (field1.get() == null) {
				if (field2.get() == null) {
					return 0;
				} else {
					return -1;
				}
			} else if (field2.get() == null) {
				return 1;
			} else {
				return comparisonDelegate.apply(field1, field2);
			}
			
		} catch (IllegalArgumentException e) {
			System.out.println("Error: class Card does not define field \"" + fieldToCompare.getName() + "\".");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println("Error: field " + fieldToCompare.getName() + " in Card is not visible.");
			e.printStackTrace();
		}
		
		// fallback, this shouldn't happen
		return 0;
	}
}
