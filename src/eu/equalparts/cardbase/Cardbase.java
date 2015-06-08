package eu.equalparts.cardbase;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.comparators.CardComparator;
import eu.equalparts.cardbase.data.Card;
import eu.equalparts.cardbase.utils.JSON;

/**
 * Provides a variety of utility methods to interact with the loaded cardbase.
 * 
 * @author Eduardo Pedroni
 */
public class Cardbase {
	
	/**
	 * The cards in the cardbase.
	 */
	private List<Card> cards;
	/**
	 * Debug flag is raised when the DEBUG environment variable is set. This causes additional
	 * information to be printed to the console.
	 */
	public static final boolean DEBUG = System.getenv("CB_DEBUG") != null; 
	
	/**
	 * Creates an empty cardbase.
	 */
	public Cardbase() {
		cards = new ArrayList<Card>();
	}
	
	/**
	 * Initialises the cardbase with the contents of a file.
	 *
	 * @param cardbaseFile the cardbase JSON to load.
	 * 
	 * @throws JsonParseException if the specified file does not contain valid JSON.
	 * @throws JsonMappingException if the specified file structure does not match that of {@code Cardbase}.
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 */
	public Cardbase(File cardbaseFile) throws JsonParseException, JsonMappingException, IOException {
		cards = JSON.mapper.readValue(cardbaseFile, new TypeReference<ArrayList<Card>>() {});
	}

	/**
	 * Writes the provided {@code Cardbase} to the provided file in JSON format.
	 * 
	 * @param file the file to which to write the {@code Cardbase}.
	 * @param cardbase the {@code Cardbase} to write out.
	 * 
	 * @throws JsonGenerationException if the data structure given does not generate valid JSON.
	 * @throws JsonMappingException if the data structure given does not generate valid JSON as well?
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 */
	public void writeCardbase(File outputFile) throws JsonGenerationException, JsonMappingException, IOException {
		JSON.mapper.writeValue(outputFile, cards);
	}

	/**
	 * Adds a specific amount of a card to the cardbase.
	 * If the card is not already in the cardbase, it is added.
	 * If it is already present, the count is simply updated.
	 * 
	 * @param cardToAdd the card to be added.
	 * @param count the amount of the card to be added.
	 */
	public void addCard(Card cardToAdd, Integer count) {
		Card card = getCard(cardToAdd.setCode, cardToAdd.number);
		if (card != null) {
			card.count += count;
		} else {
			cardToAdd.count = count;
			cards.add(cardToAdd);
		}
	}

	/**
	 * Removes a specific amount of a card from the cardbase.
	 * If the card is not present in the cardbase, nothing happens.
	 * If the card is present in the cardbase, the specified amount is removed.
	 * If that amount is equal to or exceeds the count already in the cardbase,
	 * the card entry is removed altogether.
	 * <br><br>
	 * In any case, the value returned is the actual number of cards removed.
	 * For example, if 5 Shivan Dragons are in the cardbase and the method is
	 * called to remove 10 Shivan Dragons, the {@code Card} representing the
	 * Shivan Dragon is removed from the cardbase, and the value returned is 5.
	 * 
	 * @param cardToRemove the card to be removed.
	 * @param count the amount of the card to be removed.
	 * @return the number of cards actually removed.
	 */
	public Integer removeCard(Card cardToRemove, Integer count) {
		Card card = getCard(cardToRemove.setCode, cardToRemove.number);
		Integer removed = 0;
		if (card != null) {
			if (card.count <= count) {
				cards.remove(card);
				removed = card.count;
			} else {
				card.count -= count;
				removed = count;
			}
		} 
		return removed;
	}

	/**
	 * This method is intended to allow iteration directly on the list of cards,
	 * while at the same time retaining control over the insert and remove procedures.
	 * The returned {@code List} is a read-only; trying to modify its structure will
	 * result in a {@code UnsupportedOperationException}.
	 * 
	 * @return an unmodifiable list of all the cards in the cardbase.
	 */
	public List<Card> getCards() {
		return Collections.unmodifiableList(cards);
	}
	
	/**
	 * Sorts the cardbase by the specified field. The field must be specified exactly
	 * as it is defined in {@code Card}, case-sensitive. It must also be comparable to
	 * itself, as {@code String} and {@code Integer} are.
	 * 
	 * @param fieldName the declared name of the field to be used for sorting.
	 * @return true if the sort was successful, false if no such field was found.
	 */
	public boolean sortBy(String fieldName) {
		for (Field field : Card.class.getDeclaredFields()) {
			if (field.getName().equals(fieldName)) {
				cards.sort(new CardComparator(field));
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a card from the cardbase by set code and number.
	 * If no such card is in the cardbase, returns null.
	 * 
	 * @param setCode the set to which the requested card belongs.
	 * @param number the requested card's set number.
	 * @return the requested {@code Card} or null if no card is found.
	 */
	public Card getCard(String setCode, String number) {
		for (Card card : cards) {
			if (card.setCode.equals(setCode) && card.number.equals(number))
				return card;
		}
		
		return null;
	}
}
