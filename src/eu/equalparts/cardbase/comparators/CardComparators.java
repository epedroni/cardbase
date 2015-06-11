package eu.equalparts.cardbase.comparators;

import java.util.Comparator;

import eu.equalparts.cardbase.data.Card;

public final class CardComparators {

	private CardComparators() {}

	public enum Order {
		NATURAL, REVERSE;
	}

	public static class NameComparator implements Comparator<Card> {

		private Order order = Order.NATURAL;

		public NameComparator() {}

		public NameComparator(Order order) {
			this.order = order;
		}

		@Override
		public int compare(Card o1, Card o2) {
			if (order == Order.NATURAL) {
				return o1.name.compareTo(o2.name);
			} else {
				return o2.name.compareTo(o1.name);
			}

		}
	}

	public static class LayoutComparator implements Comparator<Card> {

		private Order order = Order.NATURAL;

		public LayoutComparator() {}

		public LayoutComparator(Order order) {
			this.order = order;
		}

		@Override
		public int compare(Card o1, Card o2) {
			if (order == Order.NATURAL) {
				return o1.layout.compareTo(o2.layout);
			} else {
				return o2.layout.compareTo(o1.layout);
			}
		}
	}

	public static class ManaCostComparator implements Comparator<Card> {

		private Order order = Order.NATURAL;

		public ManaCostComparator() {}

		public ManaCostComparator(Order order) {
			this.order = order;
		}

		@Override
		public int compare(Card o1, Card o2) {
			if (order == Order.NATURAL) {
				return o1.manaCost.compareTo(o2.manaCost);
			} else {
				return o2.manaCost.compareTo(o1.manaCost);
			}
		}
	}

}
