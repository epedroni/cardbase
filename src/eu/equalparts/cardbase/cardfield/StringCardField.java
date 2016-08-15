package eu.equalparts.cardbase.cardfield;
import eu.equalparts.cardbase.card.Card;
import eu.equalparts.cardbase.comparator.SpecialFields.DirtyNumber;
import eu.equalparts.cardbase.filtering.Filter;
import eu.equalparts.cardbase.utils.Utils;

public class StringCardField extends CardField<String> {

	public StringCardField(String name, String value) {
		super(name, value);
	}

	@Override
	public boolean filter(Filter filter) {
		if (get() != null) {
			switch (filter.type) {
			case CONTAINS:
				return get().toLowerCase().contains(filter.value.toLowerCase());
			case EQUALS:
				return get().equalsIgnoreCase(filter.value);
			case REGEX:
				return get().matches(filter.value);
			case GREATER_THAN:
				try {
					if (Utils.hasAnnotation(Card.class.getField(filter.fieldName), DirtyNumber.class)) {
						return Integer.parseInt(get().replaceAll("[^0-9]+", "")) > Integer.parseInt(filter.value);
					} else {
						throw new IllegalArgumentException();
					}
				} catch (NumberFormatException | NoSuchFieldException | SecurityException e) {
					return false;
				}
			case SMALLER_THAN:
				try {
					if (Utils.hasAnnotation(Card.class.getField(filter.fieldName), DirtyNumber.class)) {
						return Integer.parseInt(get().replaceAll("[^0-9]+", "")) < Integer.parseInt(filter.value);
					} else {
						throw new IllegalArgumentException();
					}
				} catch (NumberFormatException | NoSuchFieldException | SecurityException e) {
					return false;
				}
			default:
				throw new IllegalArgumentException();
			}
		} else {
			return false;
		}
	}
}
