package eu.equalparts.cardbase.comparator;


final class ComparatorDelegates {

	private ComparatorDelegates() {}

	/*
	 * Delegates
	 */
	public static Integer compareDirtyNumber(Comparable<String> field1, Comparable<String> field2) {
		// this assumes that the format is uninterrupted numbers and letters
		String number1 = ((String) field1).replaceAll("[^0-9]+", "");
		String number2 = ((String) field2).replaceAll("[^0-9]+", "");
		
		Integer int1 = number1.matches("[0-9]+") ? Integer.parseInt(number1) : null;
		Integer int2 = number2.matches("[0-9]+") ? Integer.parseInt(number2) : null;
		
		if (int1 == null) {
			if (int2 != null) {
				// field1 has no numbers but field2 does, field1 is less by default
				// if neither have numbers, letters will be looked at below
				return -1;
			}
		} else if (int2 == null) {
			// field2 has no numbers but field1 does, field1 is more by default
			return 1;
		} else {
			// both have numbers, perform comparison if not identical
			// if identical, look at letters below
			if (int1 != int2)
				return int1.compareTo(int2);
		}
		// compare by letters
		String letter1 = number1.replaceAll("[0-9]+", "");
		String letter2 = number2.replaceAll("[0-9]+", "");
		return letter1.compareTo(letter2);
	}

	public static Integer compareRarity(Comparable<String> field1, Comparable<String> field2) {
		// assign numerical values based on the different possibilities and compare those instead
		Integer value1 = getRarityValue((String) field1);
		Integer value2 = getRarityValue((String) field2);
		
		return value1.compareTo(value2);
	}
	
	/*
	 * Internally used utility functions
	 */
	private static int getRarityValue(String rarityString) {
		return rarityString.equalsIgnoreCase("Common") ? 0 :
			   rarityString.equalsIgnoreCase("Uncommon") ? 1 :
			   rarityString.equalsIgnoreCase("Rare") ? 2 :
			   rarityString.equalsIgnoreCase("Mythic Rare") ? 3 : 
			   rarityString.equalsIgnoreCase("Basic Land") ? 4 : 
			   rarityString.equalsIgnoreCase("Special") ? 5 : 6;
	}
}
