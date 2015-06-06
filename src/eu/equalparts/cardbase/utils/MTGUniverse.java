package eu.equalparts.cardbase.utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.data.Card;
import eu.equalparts.cardbase.data.CardSet;
import eu.equalparts.cardbase.data.FullCardSet;

/**
 * Access point to the complete set of cards that exist in the
 * MTG universe. This class has a series of utility functions that
 * query remote databases to acquire card information.
 * <br>
 * Conversely, {@code CardbaseManager}'s methods are used solely to
 * acquire information regarding the loaded cardbase, which will
 * most likely contain only a subset of the MTG universe.
 * 
 * @author Eduardo Pedroni
 */
public final class MTGUniverse {

	/**
	 * The base URL from where the information is fetched.
	 */
	private static final String BASE_URL = "http://mtgjson.com/json/";
	/**
	 * If the upstream set code list can't be loaded, this is loaded instead.
	 */
	private static final String FALLBACK_LIST_PATH = "/setlist.json";
	/**
	 * A cache of CardSets to avoid querying the server many times for the same information.
	 */
	private static ArrayList<CardSet> cardSets;

	/**
	 * A cache of {@code FullCardSets} to avoid querying the server many times for the same information.
	 */
	private static HashMap<String, FullCardSet> cardSetCache = new HashMap<String, FullCardSet>();

	/**
	 * Private constructor, this class is not to be instantiated.
	 */
	private MTGUniverse() {}
	
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
	public static Card getCard(String setCode, String number) throws JsonParseException, JsonMappingException, IOException {
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
	 * 
	 * @param setCode the code of the set to be returned.
	 * @return the requested {@code FullCardSet} or null if no set matches the given code.
	 * 
	 * @throws JsonParseException if the upstream JSON is not formatted correctly.
	 * @throws JsonMappingException if the upstream JSON does not map to {@code FullCardSet}.
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 */
	public static FullCardSet getFullCardSet(String setCode) throws JsonParseException, JsonMappingException, IOException {
		FullCardSet requestedSet = null;
		String validCode = validateSetCode(setCode);
		if (validCode != null) {
			// if the set is cached, no need to fetch
			if (cardSetCache.containsKey(validCode)) {
				requestedSet = cardSetCache.get(validCode);
			} 
			// not cached; fetch, cache and return it
			else {
				requestedSet = IO.jsonMapper.readValue(new URL(BASE_URL + validCode + ".json"), FullCardSet.class);
				// MTG JSON does not include set code in the card information, but it is useful for sorting
				for (Card card : requestedSet.getCards()) {
					card.setCode = validCode;
				}
				cardSetCache.put(validCode, requestedSet);
			}
		}
		return requestedSet;
	}
	
	/**
	 * @return a list of all card sets in the form of {@code CardSet} objects.
	 */
	public static ArrayList<CardSet> getCardSetList() {
		// if the list isn't cached, fetch and cache it
		if (cardSets == null) {
			try {
				cardSets = IO.jsonMapper.readValue(new URL(BASE_URL + "SetList.json"), new TypeReference<ArrayList<CardSet>>() {});
			} catch (Exception e) {
				System.out.println("Error: could not fetch/parse set code list from upstream, loading fallback json...");
				e.printStackTrace();
				
				try {
					cardSets = IO.jsonMapper.readValue(MTGUniverse.class.getResourceAsStream(FALLBACK_LIST_PATH), new TypeReference<ArrayList<CardSet>>() {});
				} catch (Exception f) {
					System.out.println("Error: could not parse fallback set code list, aborting...");
					f.printStackTrace();
					System.exit(1);
				}
			}
		}
		return cardSets;
	}
	
	/**
	 * This method effectively converts different set code spellings
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
	public static String validateSetCode(String setCode) {
		for (CardSet cardSet : getCardSetList()) {
			if (cardSet.getCode().equalsIgnoreCase(setCode)) {
				return cardSet.getCode();
			}
		}
		return null;
	}
}
