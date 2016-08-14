package eu.equalparts.cardbase.filtering;

public class Filter {
	
	public enum FilterType { EQUALS, CONTAINS, REGEX, GREATER_THAN, SMALLER_THAN }
	
	public FilterType type;
	public String fieldName, value;
	
	public Filter(FilterType type, String fieldName, String value) {
		this.type = type;
		this.fieldName = fieldName;
		this.value = value;
	}
	
}
