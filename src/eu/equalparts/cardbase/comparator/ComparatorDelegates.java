package eu.equalparts.cardbase.comparator;


abstract class ComparatorDelegates {

	private ComparatorDelegates() {}
	
	public static Integer compareManaCost(Comparable<String> field1, Comparable<String> field2) {
		// avoid casting syntax nightmare
		String mc1 = (String) field1, mc2 = (String) field2;
		
		// first by number of colours
		int mc1count = 0, mc2count = 0;
		if (mc1.contains("W")) mc1count++;
		if (mc1.contains("U")) mc1count++;
		if (mc1.contains("B")) mc1count++;
		if (mc1.contains("R")) mc1count++;
		if (mc1.contains("G")) mc1count++;
		
		if (mc2.contains("W")) mc2count++;
		if (mc2.contains("U")) mc2count++;
		if (mc2.contains("B")) mc2count++;
		if (mc2.contains("R")) mc2count++;
		if (mc2.contains("G")) mc2count++;
		
		if (mc1count != mc2count)
			return (mc1count < mc2count) ? -1 : ((mc1count == mc2count) ? 0 : 1);
		
		// next by colour wheel
		
		return 0;
	}
	
}
/*
 * first by number of colours
 * next by colour wheel: white > blue > black > red > green
 * next by cmc
 */