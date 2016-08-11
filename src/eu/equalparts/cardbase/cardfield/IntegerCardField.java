package eu.equalparts.cardbase.cardfield;
import eu.equalparts.cardbase.filtering.CardFiltering.Filter;

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
		default:
			return false;
		}
	}
}