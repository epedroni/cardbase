package eu.equalparts.cardbase.cardfield;
import eu.equalparts.cardbase.filtering.CardFiltering.Filter;

public class StringCardField extends CardField<String> {
	
	public StringCardField(String name, String value) {
		super(name, value);
	}

	@Override
	public boolean filter(Filter filter, String s) {
		switch (filter) {
		case CONTAINS:
			return get().toLowerCase().contains(s.toLowerCase());
		case EQUALS:
			return get().equalsIgnoreCase(s);
		case REGEX:
			return get().matches(s);
		default:
			return false;
		}
	}
}
