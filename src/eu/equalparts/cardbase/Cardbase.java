package eu.equalparts.cardbase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.cards.Card;
import eu.equalparts.cardbase.cardstorage.StandaloneCardContainer;
import eu.equalparts.cardbase.comparator.CardComparator;
import eu.equalparts.cardbase.utils.JSON;

/**
 * Provides a variety of utility methods to interact with an optionally loaded cardbase.
 * 
 * @author Eduardo Pedroni
 */
public class Cardbase implements StandaloneCardContainer {
	
	/**
	 * A map with card hashes as entry keys (calculated used {@code Card.hashCode()}) and card amounts as entry values.
	 */
	@JsonProperty private Map<Integer, Integer> cardReferences = new HashMap<>();
	/**
	 * A map with card hashes as entry keys (calculated used {@code Card.hashCode()})
	 * and card objects as entry values.
	 */
	@JsonProperty private Map<Integer, Card> cardData = new HashMap<>();
	/**
	 * The decks which have been saved along with this collection of cards.
	 */
	@JsonProperty private Map<Integer, Object> decks;
	
	/**
	 * Creates a clean cardbase.
	 */
	public Cardbase() {
		cardReferences = new HashMap<>();
		cardData = new HashMap<>();
		decks = new HashMap<>();
	}
	
	/**
	 * Creates and returns a cardbase with the contents of a file.
	 *
	 * @param cardbaseFile the cardbase JSON to load.
	 * 
	 * @throws JsonParseException if the specified file does not contain valid JSON.
	 * @throws JsonMappingException if the specified file structure does not match that of {@code Cardbase}.
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 * 
	 * @return the initialised {@code Cardbase} object.
	 */
	public static Cardbase load(File cardbaseFile) throws JsonParseException, JsonMappingException, IOException {
		return JSON.mapper.readValue(cardbaseFile, Cardbase.class);
	}
	
	/**
	 * Writes the {@code Cardbase} instance to the provided file in JSON format.
	 * 
	 * @param file the file to which to write the {@code Cardbase}.
	 * @param cardbase the {@code Cardbase} to write out.
	 * 
	 * @throws JsonGenerationException if the data structure given does not generate valid JSON.
	 * @throws JsonMappingException if the data structure given does not generate valid JSON as well?
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 */
	public void write(File outputFile) throws JsonGenerationException, JsonMappingException, IOException {
		JSON.mapper.writeValue(outputFile, this);
	}

	@Override
	public int getCount(Card cardToCount) {
		int hashCode = cardToCount.hashCode();
		return cardReferences.containsKey(hashCode) ? cardReferences.get(hashCode) : 0;
	}
	
	@Override
	public void addCard(Card cardToAdd, int count) {
		int hashCode = cardToAdd.hashCode();
		if (cardReferences.containsKey(hashCode)) {
			cardReferences.replace(hashCode, cardReferences.get(hashCode) + count);
		} else {
			cardReferences.put(hashCode, count);
		}
		cardData.putIfAbsent(hashCode, cardToAdd);
	}

	@Override
	public int removeCard(Card cardToRemove, int count) {
		int hashCode = cardToRemove.hashCode();
		int removed = 0;
		
		if (cardReferences.containsKey(hashCode) && count > 0) {
			int oldCount = cardReferences.get(hashCode);
			
			if (oldCount > count) {
				cardReferences.replace(hashCode, oldCount - count);
				removed = count;
			} else {
				cardReferences.remove(hashCode);
				cardData.remove(cardToRemove.hashCode());
				removed = oldCount;
			}
		}

		return removed;
	}
	
	@Override
	public Card getCard(String setCode, String number) {
		return cardData.get(Card.makeHash(setCode, number));
	}
	
	@Override
	public Collection<Card> getCards() {
		return Collections.unmodifiableCollection(cardData.values());
	}
	
	@Override
	public Collection<Card> sortByField(String fieldName) throws NoSuchFieldException {
		List<Card> sortedCards = new ArrayList<Card>(getCards());
		sortedCards.sort(new CardComparator(Card.class.getDeclaredField(fieldName)));
		return Collections.unmodifiableCollection(sortedCards);
	}
}
