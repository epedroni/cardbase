package eu.equalparts.cardbase.utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import eu.equalparts.cardbase.cards.Card;
import eu.equalparts.cardbase.cards.CardSetInformation;
import eu.equalparts.cardbase.cards.FullCardSet;

/**
 * Access point to the complete set of cards that exist in the
 * MTG universe. This class has a series of methods that
 * query remote databases to acquire card information.
 * <br>
 * Conversely, {@code Cardbase}'s methods are used solely to
 * acquire information regarding the loaded cardbase, which will
 * most likely contain only a subset of the MTG universe of cards.
 * 
 * @author Eduardo Pedroni
 */
public final class MTGUniverse {

	/**
	 * The base URL from where the information is fetched.
	 */
	private String BASE_DATA_URL = "http://mtgjson.com/json/";
	/**
	 * If the upstream set code list can't be loaded, this is loaded instead.
	 */
	private final String FALLBACK_LIST_PATH = "/fallbackSetList.json";
	/**
	 * A cache of CardSets to avoid querying the server many times for the same information.
	 */
	private List<CardSetInformation> cardSets;
	/**
	 * A cache of {@code FullCardSets} to avoid querying the server many times for the same information.
	 */
	private HashMap<String, FullCardSet> cardSetCache = new HashMap<String, FullCardSet>();
	
	public MTGUniverse() {}
	
	public MTGUniverse(String dataLocation) {
		this.BASE_DATA_URL = dataLocation;
	}
	
	/**
	 * Returns the specified card in the form of a {@code Card} object. If the specified number does
	 * not correspond to a card in the set or the specified set code does not correspond to a set,
	 * this returns null.
	 * 
	 * @param setCode the set to which the requested card belongs.
	 * @param number the requested card's set number.
	 * @return the requested {@code Card} or null if no card is found.
	 * 
	 * @throws JsonParseException if the upstream JSON is not formatted correctly.
	 * @throws JsonMappingException if the upstream JSON does not map to a local class.
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 */
	public Card getCard(String setCode, String number) throws JsonParseException, JsonMappingException, IOException {
		Card card = null;
		FullCardSet fullCardSet = getFullCardSet(setCode);
		
		if (fullCardSet != null) {
			card = fullCardSet.getCardByNumber(number);
		}
		
		return card;
	}
	
	/**
	 * Returns the specified set in the form of a {@code FullCardSet} object. If the specified
	 * set code does not correspond to a set, this returns null.
	 * <br>
	 * This method takes care of case differences in set code names.
	 * 
	 * 
	 * @param setCode the code of the set to be returned.
	 * @return the requested {@code FullCardSet} or null if no set matches the given code.
	 * 
	 * @throws JsonParseException if the upstream JSON is not formatted correctly.
	 * @throws JsonMappingException if the upstream JSON does not map to {@code FullCardSet}.
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 */
	public FullCardSet getFullCardSet(String setCode) throws JsonParseException, JsonMappingException, IOException {
		FullCardSet requestedSet = null;
		String validCode = validateSetCode(setCode);
		if (validCode != null) {
			// if the set is cached, no need to fetch
			if (cardSetCache.containsKey(validCode)) {
				requestedSet = cardSetCache.get(validCode);
			} 
			// not cached; fetch and cache
			else {
				requestedSet = parseFullSet(JSON.mapper.readValue(new URL(BASE_DATA_URL + validCode + ".json"), JsonNode.class));
				cardSetCache.put(validCode, requestedSet);
			}
		}
		return requestedSet;
	}
	
	/**
	 * @return a list of all card sets in the form of {@code CardSet} objects.
	 */
	public List<CardSetInformation> getCardSetList() {
		// if the list isn't cached, fetch and cache it
		if (cardSets == null) {
			try {
				cardSets = JSON.mapper.readValue(new URL(BASE_DATA_URL + "SetList.json"), new TypeReference<ArrayList<CardSetInformation>>() {});
			} catch (Exception e) {
				System.out.println("Error: could not fetch or parse set code list from upstream, using fallback json...");
//				e.printStackTrace();
				
				try {
					cardSets = JSON.mapper.readValue(MTGUniverse.class.getResourceAsStream(FALLBACK_LIST_PATH), new TypeReference<ArrayList<CardSetInformation>>() {});
				} catch (Exception f) {
					System.out.println("Error: could not parse fallback set code list, aborting...");
//					f.printStackTrace();
					System.exit(1);
				}
			}
		}
		return cardSets;
	}
	
	/**
	 * This method converts different set code spellings
	 * into the format parsed from the set code list.
	 * <br>
	 * For instance, if "m15" is passed as the argument, this returns
	 * "M15", which is the set code as it is stated in the formal list.
	 * If the specified set code does not approximate any entry from
	 * the list, this returns null.
	 * 
	 * @param setCode the set code to be validated.
	 * @return the valid form of the set code if any, null otherwise.
	 */
	public String validateSetCode(String setCode) {
		for (CardSetInformation cardSet : getCardSetList()) {
			if (cardSet.getCode().equalsIgnoreCase(setCode)) {
				return cardSet.getCode();
			}
		}
		return null;
	}
	
	/**
	 * This method is necessary to adapt the list of cards in the json to
	 * the map format used in cardbase.
	 * 
	 * @param jsonTree the tree-representation of the json to be parsed.
	 * @return the parsed full card set.
	 * 
	 * @throws JsonMappingException if the upstream JSON does not map to {@code FullCardSet}.
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 */
	private FullCardSet parseFullSet(JsonNode jsonTree) throws JsonMappingException, IOException {
		
		FullCardSet fcs = new FullCardSet();
		
		/*
		 * These fields are critical, if any of them is not present an exception is thrown.
		 */
		if (jsonTree.hasNonNull("name")) {
			fcs.name = jsonTree.get("name").asText();
		} else {
			throw new JsonMappingException("Field \"name\" not found.");
		}
		
		String setCode;
		if (jsonTree.hasNonNull("code")) {
			setCode = jsonTree.get("code").asText();
			fcs.code = setCode;
		} else {
			throw new JsonMappingException("Field \"code\" not found.");
		}
		
		String imageCode;
		if (jsonTree.hasNonNull("magicCardsInfoCode")) {
			imageCode = jsonTree.get("magicCardsInfoCode").asText();
			fcs.magicCardsInfoCode = imageCode;
		} else {
			throw new JsonMappingException("Field \"magicCardsInfoCode\" not found.");
		}
		
		if (jsonTree.hasNonNull("releaseDate")) {
			fcs.releaseDate = jsonTree.get("releaseDate").asText();
		} else {
			throw new JsonMappingException("Field \"releaseDate\" not found.");
		}
		
		if (jsonTree.hasNonNull("cards")) {
			// attempt to read card list as a POJO using the standard mapper
			List<Card> rawList = jsonTree.get("cards").traverse(JSON.mapper).readValueAs(new TypeReference<List<Card>>() {});
			// generate the map
			Map<String, Card> cardMap = new HashMap<String, Card>();
			for (Card card : rawList) {
				// add set code for convenience
				card.setCode = setCode;
				card.imageCode = imageCode;
				cardMap.put(card.number, card);
			}
			fcs.cards = cardMap;
		} else {
			throw new JsonMappingException("Field \"cards\" not found.");
		}
		
		/*
		 * These fields are optional and are set to null if not present.
		 */
		fcs.gathererCode = jsonTree.hasNonNull("gathererCode") ? jsonTree.get("gathererCode").asText() : null;
		fcs.border = jsonTree.hasNonNull("border") ? jsonTree.get("border").asText() : null;
		fcs.type = jsonTree.hasNonNull("type") ? jsonTree.get("type").asText() : null;
		fcs.block = jsonTree.hasNonNull("block") ? jsonTree.get("block").asText() : null;
		
		return fcs;
	}
}