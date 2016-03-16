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
import com.fasterxml.jackson.databind.JsonNode;

import eu.equalparts.cardbase.cards.Card;
import eu.equalparts.cardbase.comparator.CardComparator;
import eu.equalparts.cardbase.decks.ReferenceDeck;
import eu.equalparts.cardbase.decks.StandaloneDeck;
import eu.equalparts.cardbase.utils.JSON;

/**
 * Provides a variety of utility methods to interact with an optionally loaded cardbase.
 * 
 * @author Eduardo Pedroni
 */
public class Cardbase {
	
	private static class DataContainer {
		/**
		 * The cards in the cardbase, set in key-value pairs where the key is the card hash,
		 * generated using {makeHash()}.
		 */
		public Map<Integer, Card> cards;
		/**
		 * TODO comment
		 */
		public Map<Integer, Integer> collection;
		/**
		 * The decks which have been saved along with this collection of cards.
		 */
		public Map<String, ReferenceDeck> decks;
		
		public DataContainer() {
			cards = new HashMap<Integer, Card>();
			collection = new HashMap<Integer, Integer>();
			decks = new HashMap<String, ReferenceDeck>();
		}
	}
	
	private DataContainer dataContainer;
	
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
		dataContainer = JSON.mapper.readValue(cardbaseFile, DataContainer.class);
	}
	
	/**
	 * Initialises a clean cardbase.
	 */
	public Cardbase() {
		dataContainer = new DataContainer();
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
		JSON.mapper.writeValue(outputFile, dataContainer);
	}

	/**
	 * Adds a specific amount of a card to the cardbase.
	 * If the card is not already in the cardbase, it is added.
	 * If it is already present, the count is simply updated.
	 * 
	 * @param cardToAdd the card to be added. The count value
	 * of this object is added to the existing count if the card
	 * already exists.
	 *TODO fix comment
	 */
	public void addCard(Card cardToAdd, int addCount) {
		Integer hashCode = cardToAdd.hashCode();
		
		// ensure that card is in the card map
		dataContainer.cards.putIfAbsent(hashCode, cardToAdd);
		
		// ensure that card is in the collection, with the correct count
		Integer currentCount = dataContainer.collection.get(hashCode);
		if (currentCount != null) {
			dataContainer.collection.replace(hashCode, currentCount + addCount);
		} else {
			dataContainer.collection.put(hashCode, addCount);
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
	 * @param removeCount the amount of the card to be removed.
	 * @return the number of cards actually removed.
	 *TODO comment
	 */
	public Integer removeCard(Card cardToRemove, int removeCount) {
		Integer hashCode = cardToRemove.hashCode();
		int removed = 0;
		
		Integer currentCount = dataContainer.collection.get(hashCode);
		if (currentCount != null) {
			if (removeCount >= currentCount) {
				dataContainer.collection.remove(hashCode);
				dataContainer.cards.remove(hashCode);
				removed = currentCount;
			} else {
				dataContainer.collection.replace(hashCode, currentCount - removeCount);
				removed = removeCount;
			}
		}
		return removed;
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
		return dataContainer.cards.get(Card.makeHash(setCode, number));
	}
	
	/**
	 * Returns a card from the cardbase by hash. The card's hash
	 * can be generated using {@code Cardbase.makeHash()}.
	 * If no such card is in the cardbase, returns null.
	 * 
	 * @param hash the Cardbase hash of the requested card.
	 * @return the requested {@code Card} or null if no card is found.
	 */
	protected Card getCardByHash(Integer hash) {
		return dataContainer.cards.get(hash);
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
		return Collections.unmodifiableCollection(dataContainer.cards.values());
	}
	
	/**
	 * @param field the name of the field by which to sort.
	 * @return an unmodifiable collection representing the cardbase sorted in the required order.
	 * @throws NoSuchFieldException if the field provided is invalid.
	 */
	public Collection<Card> sort(String field) throws NoSuchFieldException {
		List<Card> sortedCards = new ArrayList<Card>(dataContainer.cards.values());
		sortedCards.sort(new CardComparator(Card.class.getDeclaredField(field)));
		return Collections.unmodifiableCollection(sortedCards);
	}
	
	public Map<String, ReferenceDeck> getDecks() {
		return Collections.unmodifiableMap(dataContainer.decks);
	}

	public int getCount(Card card) {
		Integer count = dataContainer.collection.get(Card.makeHash(card.setCode, card.number));
		return count != null ? count : 0;
	}
	
//	public List<Card> getMissingCards(StandaloneDeck deckToCheck) {
//		List<Card> missingCards = new ArrayList<Card>();
//		for (Card card : deckToCheck.cards) {
//			Integer hash = card.hashCode();
//			if (cards.containsKey(hash)) {
//				if (cards.get(hash).count < card.count) {
//					Card missingCard = card.clone();
//					missingCard.count = card.count - cards.get(hash).count;
//					missingCards.add(missingCard);
//				}
//			} else {
//				missingCards.add(card);
//			}
//		}
//		return missingCards;
//	}
	
//	public void addStandaloneDeck(StandaloneDeck deckToAdd) {
//		List<Card> missingCards = getMissingCards(deckToAdd);
//		if (missingCards.size() <= 0) {
//			decks.put(deckToAdd.name, new ReferenceDeck(deckToAdd));
//		} else {
//			throw new IllegalArgumentException("The cardbase is missing cards to add this deck.");
//		}
//	}
	
//	public StandaloneDeck exportDeck(String deckName) {
//		ReferenceDeck referenceDeck = decks.get(deckName);
//		
//		if (referenceDeck != null) {
//			StandaloneDeck standaloneDeck = new StandaloneDeck();
//			
//			standaloneDeck.name = referenceDeck.name;
//			standaloneDeck.plains = referenceDeck.plains;
//			standaloneDeck.islands = referenceDeck.islands;
//			standaloneDeck.swamps = referenceDeck.swamps;
//			standaloneDeck.mountains = referenceDeck.mountains;
//			standaloneDeck.forests = referenceDeck.forests;
//			
//			for (Integer cardHash : referenceDeck.cardReferences.keySet()) {
//				Card card = getCardByHash(cardHash);
//				if (card != null) {
//					// must clone otherwise the original count is affected too
//					card = card.clone();
//					card.count = referenceDeck.cardReferences.get(cardHash);
//					standaloneDeck.cards.add(card);
//				} else {
//					throw new IllegalArgumentException("Deck refers to card not in cardbase: " + cardHash);
//				}
//			}
//
//			return standaloneDeck;
//		} else {
//			throw new IllegalArgumentException("The specified deck does not exist.");
//		}
//	}
}
