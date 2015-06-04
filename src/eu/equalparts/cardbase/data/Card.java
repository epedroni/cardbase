package eu.equalparts.cardbase.data;

import java.util.ArrayList;

public class Card {
	
	public String layout;
	public String name;
	public ArrayList<String> names;
	public String manaCost;
	public Integer cmc;
	public ArrayList<String> colors;
	public String type;
	public ArrayList<String> supertypes;
	public ArrayList<String> types;
	public ArrayList<String> subtypes;
	public String rarity;
	public String text;
	public String flavor;
	public String artist;
	public String number;
	public String power;
	public String toughness;
	public Integer loyalty;
	public Integer multiverseid;
	public ArrayList<String> variations;
	public String imageName;
	public String border;
	public String watermark;

	// Not part of JSON, will be set later
	public String setCode;
	public String setName;

}