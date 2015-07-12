package eu.equalparts.cardbase.data;

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
	public String border;
	public String watermark;

	// Not part of upstream JSON
	public String setCode;
	public String imageCode;
	public Integer count;
	
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
		clone.border = this.border;
		clone.watermark = this.watermark;
		clone.setCode = this.setCode;
		clone.imageCode = this.imageCode;
		clone.count = this.count;
		
		return clone;
	}
	
}