package eu.equalparts.cardbase.comparator;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.function.BiFunction;

import eu.equalparts.cardbase.Cardbase;
import eu.equalparts.cardbase.data.Card;

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
@SuppressWarnings({"rawtypes", "unchecked"})
public class CardComparator implements Comparator<Card> {

	public enum Order {
		NATURAL, REVERSE;
	}
	
	/**
	 * The field being compared.
	 */
	private Field fieldToCompare;
	
	private BiFunction<Comparable, Comparable, Integer> comparisonDelegate = this::defaultComparison;

	/**
	 * Creates a new comparator for the specified field only. This class
	 * will only be constructed successfully if the field comes from
	 * {@code Card} and can be compared to itself (i.e. implements
	 * {@code Comparable<T>} where T is its own type.
	 * <br>
	 * For reference, {@code String} and {@code Integer} are both self comparable.
	 * 
	 * @param fieldToCompare the field this comparator will use to compare cards, as declared in {@code Card}.
	 */
	public CardComparator(Field fieldToCompare) {
		if (fieldToCompare.getDeclaringClass().equals(Card.class) &&
				isSelfComparable(fieldToCompare.getType())) {

			this.fieldToCompare = fieldToCompare;
			
			// if annotated with a special comparator, set the comparison delegate here
			this.comparisonDelegate = ComparatorDelegates::compareManaCost;
			
		} else {
			System.out.println(fieldToCompare.isAccessible());
			System.out.println(fieldToCompare.getDeclaringClass().equals(Card.class));
			System.out.println(isSelfComparable(fieldToCompare.getType()));
			throw new IllegalArgumentException("The field provided is not valid.");
		}
	}

	@Override
	public int compare(Card o1, Card o2) {
		/*
		 * we've already checked that the field is self comparable,
		 * so we are now free to cast to whatever type it is and compare.
		 */
		try {
			Comparable field1 = (Comparable) fieldToCompare.get(o1);
			Comparable field2 = (Comparable) fieldToCompare.get(o2);
			
			return comparisonDelegate.apply(field1, field2);
		} catch (IllegalArgumentException e) {
			System.out.println("Error: class Card does not define field" + fieldToCompare.getName() + ".");
			if (Cardbase.DEBUG) e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println("Error: field " + fieldToCompare.getName() + " in Card is not visible.");
			if (Cardbase.DEBUG) e.printStackTrace();
		}
		
		// fallback, this shouldn't happen
		return 0;
	}
	
	/**
	 * The standard comparison operation, which uses the field's own {@code compareTo()}
	 * method.
	 * 
	 * @param field1 the first object to be compared.
     * @param field2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
	 */
	private int defaultComparison(Comparable field1, Comparable field2) {
		return field1.compareTo(field2);
	}

	/**
	 * Use reflection to determine if the specified class can be compared with itself.
	 * 
	 * @param type the type to analyse.
	 * @return true if the type can be compared to itself using {@code compareTo()}, false otherwise.
	 */
	private boolean isSelfComparable(Class<?> type) {

		// go through all interfaces implemented by this class
		for (Type implementedInterface : type.getGenericInterfaces()) {
			// check if any parameterised interface found is "Comparable"
			if (implementedInterface instanceof ParameterizedType) {
				ParameterizedType genericInterface = (ParameterizedType) implementedInterface;
				if (genericInterface.getRawType().equals(Comparable.class)) {
					// check that the type argument of comparable is the same as the field type itself
					return genericInterface.getActualTypeArguments()[0].equals(type);
				}
			}
		}
		return false;
	}
}
