package eu.equalparts.cardbase.data;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.utils.IO;

/**
 * Provides a variety of utility methods to interact with the loaded cardbase.
 * 
 * @author Eduardo Pedroni
 */
public class CardbaseManager {

	/**
	 * The cardbase being managed.
	 */
	private Cardbase cardbase;
	
	/**
	 * Creates an empty cardbase.
	 * 
	 */
	public CardbaseManager() {
		cardbase = new Cardbase();
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
	public CardbaseManager(File cardbaseFile) throws JsonParseException, JsonMappingException, IOException {
		cardbase = IO.jsonMapper.readValue(cardbaseFile, Cardbase.class);
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
		IO.jsonMapper.writeValue(outputFile, cardbase);
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
		Card card = cardbase.getCardByNumber(cardToAdd.setCode, cardToAdd.number);
		if (card != null) {
			card.count += count;
		} else {
			cardToAdd.count = count;
			cardbase.cards.add(cardToAdd);
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
		Card card = cardbase.getCardByNumber(cardToRemove.setCode, cardToRemove.number);
		Integer removed = 0;
		if (card != null) {
			if (card.count <= count) {
				cardbase.cards.remove(card);
				removed = card.count;
			} else {
				card.count -= count;
				removed = count;
			}
		} 
		return removed;
	}

	/**
	 * @return an iterator to the {@code Card}s in the cardbase.
	 */
	public Iterator<Card> cardIterator() {
		return cardbase.cards.iterator();
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
		return cardbase.getCardByNumber(setCode, number);
	}
}
