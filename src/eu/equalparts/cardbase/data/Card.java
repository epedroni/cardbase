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
	
}