package eu.equalparts.cardbase.decks;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.equalparts.cardbase.cards.Card;
import eu.equalparts.cardbase.decks.ReferenceDeck;
import eu.equalparts.cardbase.decks.StandaloneDeck;

public class DeckTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_createReferenceDeckFromStandaloneDeck() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		StandaloneDeck standaloneDeck = mapper.readValue(getClass().getResourceAsStream("deck.cbd"), StandaloneDeck.class);
		
		ReferenceDeck uut = new ReferenceDeck(standaloneDeck);
		
		boolean condition = uut.name == standaloneDeck.name &&
				uut.plains == standaloneDeck.plains &&
				uut.islands == standaloneDeck.islands &&
				uut.swamps == standaloneDeck.swamps &&
				uut.mountains == standaloneDeck.mountains &&
				uut.forests == standaloneDeck.forests;
		assertTrue("Metadata was not correctly set.", condition);
		assertEquals("Wrong number of cards.", uut.cardReferences.size(), standaloneDeck.cards.size());
		for (Card card : standaloneDeck.cards) {
			Integer count = uut.cardReferences.get(card.hashCode());
			assertNotNull("Reference missing in deck.", count);
			assertEquals("Card count is wrong.", card.count, count);
		}
	}
}
