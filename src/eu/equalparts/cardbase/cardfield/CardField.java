package eu.equalparts.cardbase.cardfield;
import eu.equalparts.cardbase.filtering.Filter;

public abstract class CardField<T extends Comparable<T>> implements Comparable<CardField<T>> {
	private String name;
	private T value;
	
	public CardField(String name, T value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public T get() {
		return value;
	}
	
	public void set(T newValue) {
		this.value = newValue;
	}
	
	@Override
	public int compareTo(CardField<T> o) {
		return value.compareTo(o.get());
	}
	
	public abstract boolean filter(Filter filter);
}
