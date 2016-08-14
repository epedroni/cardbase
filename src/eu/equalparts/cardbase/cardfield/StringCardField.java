package eu.equalparts.cardbase.cardfield;
import eu.equalparts.cardbase.filtering.Filter;

public class StringCardField extends CardField<String> {
	
	public StringCardField(String name, String value) {
		super(name, value);
	}

	@Override
	public boolean filter(Filter filter) {
		switch (filter.type) {
		case CONTAINS:
			return get().toLowerCase().contains(filter.value.toLowerCase());
		case EQUALS:
			return get().equalsIgnoreCase(filter.value);
		case REGEX:
			return get().matches(filter.value);
		default:
			throw new IllegalArgumentException();
		}
	}
}
