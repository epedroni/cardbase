package eu.equalparts.cardbase.cardfield;
import eu.equalparts.cardbase.filtering.Filter;

public class IntegerCardField extends CardField<Integer> {

	public IntegerCardField(String name, Integer value) {
		super(name, value);
	}

	@Override
	public boolean filter(Filter filter) throws NumberFormatException {
		switch (filter.type) {
		case CONTAINS:
			return get().toString().contains(filter.value);
		case EQUALS:
			return get().toString().equalsIgnoreCase(filter.value);
		case REGEX:
			return get().toString().matches(filter.value);
		case GREATER_THAN:
			return get() > Integer.parseInt(filter.value);
		case SMALLER_THAN:
			return get() < Integer.parseInt(filter.value);
		default:
			return false;
		}
	}
}