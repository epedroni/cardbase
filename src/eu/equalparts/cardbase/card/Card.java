package eu.equalparts.cardbase.card;

import eu.equalparts.cardbase.cardfield.IntegerCardField;
import eu.equalparts.cardbase.cardfield.StringCardField;
import eu.equalparts.cardbase.comparator.SpecialFields.DirtyNumber;
import eu.equalparts.cardbase.comparator.SpecialFields.Rarity;

public class Card {
	public StringCardField name;
	public StringCardField layout;
	public StringCardField manaCost;
	public IntegerCardField cmc;
	public StringCardField type;
	@Rarity
	public StringCardField rarity;
	public StringCardField text;
	public StringCardField flavor;
	public StringCardField artist;
	@DirtyNumber
	public StringCardField number;
	@DirtyNumber
	public StringCardField power;
	@DirtyNumber
	public StringCardField toughness;
	public IntegerCardField loyalty;
	public IntegerCardField multiverseid;
	public StringCardField imageName;

	// Not part of upstream JSON
	public StringCardField setCode;
	
	public static int makeHash(String setCode, String number) {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((setCode == null) ? 0 : setCode.hashCode());
		return result;
	}
	
	@Override
	public int hashCode() {
		return makeHash(setCode.get(), number.get());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (setCode == null) {
			if (other.setCode != null)
				return false;
		} else if (!setCode.equals(other.setCode))
			return false;
		return true;
	}
}
