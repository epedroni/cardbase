package eu.equalparts.cardbase.cards;

import eu.equalparts.cardbase.comparator.SpecialFields.DirtyNumber;
import eu.equalparts.cardbase.comparator.SpecialFields.Rarity;

public class Card {
	
	public String name;
	public String layout;
	public String manaCost;
	public Integer cmc;
	public String type;
	@Rarity
	public String rarity;
	public String text;
	public String flavor;
	public String artist;
	@DirtyNumber
	public String number;
	@DirtyNumber
	public String power;
	@DirtyNumber
	public String toughness;
	public Integer loyalty;
	public Integer multiverseid;
	public String imageName;
	public String watermark;

	// Not part of upstream JSON
	public String setCode;
	public String imageCode;
	//public Integer count;
	
	@Override
	public Card clone() {
		Card clone = new Card();
		
		clone.name = this.name;
		clone.layout = this.layout;
		clone.manaCost = this.manaCost;
		clone.cmc = this.cmc;
		clone.type = this.type;
		clone.rarity = this.rarity;
		clone.text = this.text;
		clone.flavor = this.flavor;
		clone.artist = this.artist;
		clone.number = this.number;
		clone.power = this.power;
		clone.toughness = this.toughness;
		clone.loyalty = this.loyalty;
		clone.multiverseid = this.multiverseid;
		clone.imageName = this.imageName;
		clone.watermark = this.watermark;
		clone.setCode = this.setCode;
		clone.imageCode = this.imageCode;
		
		return clone;
	}

	public static int makeHash(String setCode, String number) {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((setCode == null) ? 0 : setCode.hashCode());
		return result;
	}
	
	@Override
	public int hashCode() {
		return makeHash(setCode, number);
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