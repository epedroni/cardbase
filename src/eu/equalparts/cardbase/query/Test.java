package eu.equalparts.cardbase.query;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.equalparts.cardbase.data.Card;
import eu.equalparts.cardbase.data.CardSet;

public class Test {
	
//	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//		@SuppressWarnings("unchecked")
//		List<Card> allCards = getAllCards((Map<String, CardSet>) mapper
//				.readValue(new File("AllSets.json"), new TypeReference<Map<String, CardSet>>() {}));
//
//		System.out.println("Number of cards: " + allCards.size());
//	}
//
//	public static List<Card> getAllCards(Map<String, CardSet> sets) {
//		List<Card> allCards = new ArrayList<Card>();
//
//		for (CardSet set : sets.values()) {
//			for (Card card : set.getCards()) {
//				card.setSetCode(set.getCode());
//				card.setSetName(set.getName());
//				// System.out.println(set.getName() + ": " + card.getName());
//
//				allCards.add(card);
//			}
//		}
//
//		return allCards;
//	}
	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

//		@SuppressWarnings("unchecked")
		CardSet set = mapper.readValue(new File("M15.json"), CardSet.class);
		System.out.println(set.getCardByNumber("281").getName());
		
	}

	public static List<Card> getAllCards(Map<String, CardSet> sets) {
		List<Card> allCards = new ArrayList<Card>();

		for (CardSet set : sets.values()) {
			for (Card card : set.getCards()) {
				card.setSetCode(set.getCode());
				card.setSetName(set.getName());
				// System.out.println(set.getName() + ": " + card.getName());

				allCards.add(card);
			}
		}

		return allCards;
	}
}
