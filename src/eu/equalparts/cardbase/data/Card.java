package eu.equalparts.cardbase.data;

import eu.equalparts.cardbase.comparator.SpecialFields.ManaCost;

public class Card {
	
	public String name;
	public String layout;
	@ManaCost
	public String manaCost;
	public Integer cmc;
	public String type;
	public String rarity;
	public String text;
	public String flavor;
	public String artist;
	public String number;
	public String power;
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
	
}