package eu.equalparts.cardbase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.cards.Card;
import eu.equalparts.cardbase.comparator.CardComparator;
import eu.equalparts.cardbase.decks.ReferenceDeck;
import eu.equalparts.cardbase.decks.StandaloneDeck;
import eu.equalparts.cardbase.utils.JSON;
import eu.equalparts.cardbase.utils.UID;

/**
 * Provides a variety of utility methods to interact with an optionally loaded cardbase.
 * 
 * @author Eduardo Pedroni
 */
public class Cardbase {
	
	/**
	 * The cards in the cardbase, set in key-value pairs where the key is the card hash,
	 * generated using {makeHash()}.
	 */
	private Map<String, Card> cards;
	/**
	 * The decks which have been saved along with this collection of cards.
	 */
	private Map<String, ReferenceDeck> decks;
	/**
	 * Debug flag is raised when the DEBUG environment variable is set. This causes additional
	 * information to be printed to the console.
	 */
	public static final boolean DEBUG = System.getenv("CB_DEBUG") != null;
	
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
		cards = JSON.mapper.readValue(cardbaseFile, new TypeReference<Map<String, Card>>() {});
	}
	
	/**
	 * Initialises a clean cardbase.
	 */
	public Cardbase() {
		cards = new HashMap<String, Card>();
		decks = new HashMap<String, ReferenceDeck>();
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
	public void writeCollection(File outputFile) throws JsonGenerationException, JsonMappingException, IOException {
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
			cards.put(UID.makeHash(cardToAdd), cardToAdd);
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
				cards.remove(UID.makeHash(card));
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
	 * result in an {@code UnsupportedOperationException}.
	 * 
	 * @return an unmodifiable list of all the cards in the cardbase.
	 */
	public Collection<Card> getCards() {
		return Collections.unmodifiableCollection(cards.values());
	}
	
	/**
	 * @param field the name of the field by which to sort.
	 * @return an unmodifiable collection representing the cardbase sorted in the required order.
	 * @throws NoSuchFieldException if the field provided is invalid.
	 */
	public Collection<Card> sort(String field) throws NoSuchFieldException {
		List<Card> sortedCards = new ArrayList<Card>(cards.values());
		sortedCards.sort(new CardComparator(Card.class.getDeclaredField(field)));
		return Collections.unmodifiableCollection(sortedCards);
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
		return cards.get(UID.makeHash(setCode, number));
	}
	
	/**
	 * Returns a card from the cardbase by hash. The card's hash
	 * can be generated using {@code Cardbase.makeHash()}.
	 * If no such card is in the cardbase, returns null.
	 * 
	 * @param hash the Cardbase hash of the requested card.
	 * @return the requested {@code Card} or null if no card is found.
	 */
	public Card getCardByHash(String hash) {
		return cards.get(hash);
	}
	
	public Map<String, ReferenceDeck> getDecks() {
		return Collections.unmodifiableMap(decks);
	}
	
	public List<Card> getMissingCards(StandaloneDeck deckToCheck) {
		List<Card> missingCards = new ArrayList<Card>();
		for (Card card : deckToCheck.cards) {
			String hash = UID.makeHash(card);
			if (cards.containsKey(hash)) {
				if (cards.get(hash).count < card.count) {
					Card missingCard = card.clone();
					missingCard.count = card.count - cards.get(hash).count;
					missingCards.add(missingCard);
				}
			} else {
				missingCards.add(card);
			}
		}
		return missingCards;
	}
	
	public void addStandaloneDeck(StandaloneDeck deckToAdd) {
		List<Card> missingCards = getMissingCards(deckToAdd);
		if (missingCards.size() <= 0) {
			decks.put(deckToAdd.name, new ReferenceDeck(deckToAdd));
		} else {
			throw new IllegalArgumentException("The cardbase is missing cards to add this deck.");
		}
	}
	
	public StandaloneDeck exportDeck(String deckName) {
		ReferenceDeck referenceDeck = decks.get(deckName);
		
		if (referenceDeck != null) {
			StandaloneDeck standaloneDeck = new StandaloneDeck();
			
			standaloneDeck.name = referenceDeck.name;
			standaloneDeck.plains = referenceDeck.plains;
			standaloneDeck.islands = referenceDeck.islands;
			standaloneDeck.swamps = referenceDeck.swamps;
			standaloneDeck.mountains = referenceDeck.mountains;
			standaloneDeck.forests = referenceDeck.forests;
			
			for (String cardHash : referenceDeck.cardReferences.keySet()) {
				Card card = getCardByHash(cardHash);
				if (card != null) {
					// must clone otherwise the original count is affected too
					card = card.clone();
					card.count = referenceDeck.cardReferences.get(cardHash);
					standaloneDeck.cards.add(card);
				} else {
					throw new IllegalArgumentException("Deck refers to card not in cardbase: " + cardHash);
				}
			}

			return standaloneDeck;
		} else {
			throw new IllegalArgumentException("The specified deck does not exist.");
		}
	}
}
