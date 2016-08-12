package eu.equalparts.cardbase.cardfield;
import eu.equalparts.cardbase.filtering.CardFiltering.Filter;
import eu.equalparts.cardbase.utils.InputSanity;

public class IntegerCardField extends CardField<Integer> {

	public IntegerCardField(String name, Integer value) {
		super(name, value);
	}

	@Override
	public boolean filter(Filter filter, String s) {
		switch (filter) {
		case CONTAINS:
			return get().toString().contains(s);
		case EQUALS:
			return get().toString().equalsIgnoreCase(s);
		case REGEX:
			return get().toString().matches(s);
		case GREATER_THAN:
			if (InputSanity.isInteger(s)) {
				return get() > Integer.valueOf(s);
			}
		case SMALLER_THAN:
			if (InputSanity.isInteger(s)) {
				return get() < Integer.valueOf(s);
			}
		default:
			return false;
		}
	}
}