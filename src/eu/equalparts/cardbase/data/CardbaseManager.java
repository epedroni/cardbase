package eu.equalparts.cardbase.data;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.io.IO;

public class CardbaseManager {

	private ArrayList<CardSet> cardSets;

	/**
	 * A cache of CardSets to avoid querying the server many times for the same information.
	 */
	private HashMap<String, FullCardSet> cardSetCache = new HashMap<String, FullCardSet>();
	/**
	 * 
	 */
	private Cardbase cardbase;
	
	
	/**
	 * Parse a cardbase file and create an associated Cardbase object.
	 *
	 * @param cardbaseFile
	 * 
	 * @throws JsonParseException if underlying input contains invalid content of type JsonParser supports (JSON for default case).
	 * @throws JsonMappingException if the input JSON structure does not match structure expected for result type (or has other mismatch issues).
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 */
	public CardbaseManager(File cardbaseFile) throws JsonParseException, JsonMappingException, IOException {
		cardSets = IO.getCardSetList();
		cardbase = IO.readCardbase(cardbaseFile);
	}

	/**
	 * Create an empty Cardbase.
	 * 
	 * @throws JsonParseException if underlying input contains invalid content of type JsonParser supports (JSON for default case).
	 * @throws JsonMappingException if the input JSON structure does not match structure expected for result type (or has other mismatch issues).
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 */
	public CardbaseManager() throws JsonParseException, JsonMappingException, IOException {
		cardSets = IO.getCardSetList();
		cardbase = new Cardbase();
	}

	public ArrayList<CardSet> getCardSetList() {
		return cardSets;
	}

	public void writeCardbase(File outputFile) throws JsonGenerationException, JsonMappingException, IOException {
		IO.writeCardbase(outputFile, cardbase);
	}
	
	/**
	 * Returns the specified set in the form of a {@code FullCardSet} object. 
	 * 
	 * @param code the code of the set to be returned.
	 * @return the requested {@code FullCardSet} or null if no set matches the given code.
	 * 
	 * @throws JsonParseException if the upstream JSON is not formatted correctly.
	 * @throws JsonMappingException if the upstream JSON does not map to {@code FullCardSet}.
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 */
	public FullCardSet getFullCardSet(String code) throws JsonParseException, JsonMappingException, IOException {
		FullCardSet requestedSet = null;
		for (CardSet cardSet : cardSets) {
			if (cardSet.getCode().equalsIgnoreCase(code)) {
				// if the set is cached, no need to fetch
				if (cardSetCache.containsKey(cardSet.getCode())) {
					requestedSet = cardSetCache.get(cardSet.getCode());
				} 
				// not cached; fetch, cache and return it
				else {
					requestedSet = IO.getFullCardSet(cardSet.getCode());
					cardSetCache.put(cardSet.getCode(), requestedSet);
				}
				return requestedSet;
			}
		}
		// not found
		return null;
	}

	/**
	 * Add a specific amount of a card to the cardbase.
	 * If the card is not already in the cardbase, it is added.
	 * If it is already present, the count is simply updated.
	 * 
	 * 
	 * @param newCard
	 * @param count
	 */
	public void addCard(Card newCard, Integer count) {
		Card card = cardbase.getCardByNumber(newCard.setCode, newCard.number);
		if (card != null) {
			card.count += count;
		} else {
			newCard.count = count;
			cardbase.cards.add(newCard);
		}
	}

	/**
	 * Remove a specific amount of a card from the cardbase.
	 * If the card is not present in the cardbase, nothing happens.
	 * If the card is present in the card, the specified amount is removed.
	 * If that amount is equal to or exceeds the count already in the cardbase,
	 * the card entry is removed altogether. 
	 * 
	 * @param cardToRemove
	 * @param count
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
	 * @return an iterator to the cards in the cardbase.
	 */
	public Iterator<Card> cardIterator() {
		return cardbase.cards.iterator();
	}

	/**
	 * Return a card from the cardBase by setCode and number.
	 * If no such card is in the cardbase, return null.
	 * 
	 * @param code
	 * @param string
	 * @return
	 */
	public Card getCard(String code, String number) {
		return cardbase.getCardByNumber(code, number);
	}
}
