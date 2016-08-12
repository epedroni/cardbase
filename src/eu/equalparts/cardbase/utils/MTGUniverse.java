package eu.equalparts.cardbase.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.card.Card;
import eu.equalparts.cardbase.card.CardSetInformation;
import eu.equalparts.cardbase.card.FullCardSet;
import eu.equalparts.cardbase.json.JSON;

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
	private String BASE_DATA_URL;
	/**
	 * If the upstream set code list can't be loaded, this local copy is loaded instead.
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
				requestedSet = JSON.mapper.readValue(getInputStream(BASE_DATA_URL + validCode + ".json"), FullCardSet.class);
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
				cardSets = JSON.mapper.readValue(getInputStream(BASE_DATA_URL + "SetList.json"), new TypeReference<ArrayList<CardSetInformation>>() {});
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
	 * Generates and returns a proper input stream with the correct user agent to ensure
	 * MTG JSON doesn't return any annoying 403s.
	 * 
	 * @param URL
	 * @return
	 * @throws IOException 
	 */
	private InputStream getInputStream(String url) throws IOException {
		URLConnection connection = new URL(url).openConnection();
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		connection.connect();
		return connection.getInputStream();
	}
}