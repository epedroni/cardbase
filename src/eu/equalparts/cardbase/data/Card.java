package eu.equalparts.cardbase.data;

import java.util.List;

public class Card {
	
	public String layout = "";
	public String name = "";
	public List<String> names;
	public String manaCost = "";
	public Integer cmc = 0;
	public List<String> colors;
	public String type;
	public List<String> supertypes;
	public List<String> types;
	public List<String> subtypes;
	public String rarity;
	public String text;
	public String flavor;
	public String artist;
	public String number;
	public String power;
	public String toughness;
	public Integer loyalty;
	public Integer multiverseid;
	public List<String> variations;
	public String imageName;
	public String border;
	public String watermark;

	// Not part of upstream JSON
	public String setCode;
	public Integer count;

}