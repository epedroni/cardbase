package eu.equalparts.cardbase.data;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.query.IO;

public class CardbaseManager {
	
	private ArrayList<MetaCardSet> metaSets;
	public Cardbase cardBase;

	/**
	 * Parse a cardbase file and create an associated CardBase object.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public CardbaseManager(File cardBaseFile) throws JsonParseException, JsonMappingException, IOException {
		metaSets = IO.getAllMetaSets();
		cardBase = IO.readCardBase(cardBaseFile);
	}

	/**
	 * Create an empty CardBase.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public CardbaseManager() throws JsonParseException, JsonMappingException, IOException {
		metaSets = IO.getAllMetaSets();
		cardBase = new Cardbase();
	}
	
	public ArrayList<MetaCardSet> getAllMetaSets() {
		return metaSets;
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
		Card card = cardBase.getCardByNumber(newCard.setCode, newCard.number);
		if (card != null) {
			card.count += count;
		} else {
			newCard.count = count;
			cardBase.cards.add(newCard);
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
		Card card = cardBase.getCardByNumber(remove.setCode, remove.number);
		if (card != null) {
			if (card.count <= count) {
				cardBase.cards.remove(card);
			} else {
				card.count -= count;
			}
		}
	}
	
	/**
	 * @return an iterator to the cards in the cardbase.
	 */
	public Iterator<Card> cardIterator() {
		return cardBase.cards.iterator();
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
		return cardBase.getCardByNumber(code, number);
	}
}
