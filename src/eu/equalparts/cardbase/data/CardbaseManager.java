package eu.equalparts.cardbase.data;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.query.IO;

public class CardbaseManager {
	
	private ArrayList<CardSet> cardSets;
	public Cardbase cardbase;

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
	 * @param remove
	 * @param count
	 */
	public void removeCard(Card remove, Integer count) {
		Card card = cardbase.getCardByNumber(remove.setCode, remove.number);
		if (card != null) {
			if (card.count <= count) {
				cardbase.cards.remove(card);
			} else {
				card.count -= count;
			}
		}
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
