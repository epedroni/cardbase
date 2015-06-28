import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.data.Card;
import eu.equalparts.cardbase.utils.JSON;


public class Testest {

	
	public static class FullCardSet {
		
		public String name;
		public String code;
		public String magicCardsInfoCode;
		public String releaseDate;
		public String border;
		public String type;
		public String block;
		public String gathererCode;
		public List<Card> cards;
		
		public FullCardSet() {}
	}
	
	
	public static void main(String... args) throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
		Map<String, FullCardSet> setMap = JSON.mapper.readValue(new File("./AllSets.json"), new TypeReference<Map<String, FullCardSet>> () {});
		
		List<String> rarities = new ArrayList<String>();
		
		int count = 0;
		
		for (FullCardSet cardSet : setMap.values()) {
			for (Card card : cardSet.cards) {
				count++;
				if (!rarities.contains(card.rarity)) {
					rarities.add(card.rarity);
					System.out.println(card.rarity);
				}
			}
		}
		
		System.out.println("Done, " + count + " cards looked at.");
	}
}
