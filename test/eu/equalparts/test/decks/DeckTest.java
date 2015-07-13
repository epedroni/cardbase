package eu.equalparts.test.decks;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.equalparts.cardbase.cards.Card;
import eu.equalparts.cardbase.decks.ReferenceDeck;
import eu.equalparts.cardbase.decks.StandaloneDeck;
import eu.equalparts.cardbase.utils.JSON;
import eu.equalparts.cardbase.utils.UID;

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
		StandaloneDeck standaloneDeck = JSON.mapper.readValue(getClass().getResourceAsStream("deck.cbd"), StandaloneDeck.class);
		
		ReferenceDeck referenceDeck = new ReferenceDeck(standaloneDeck);
		
		boolean condition = referenceDeck.name == standaloneDeck.name &&
				referenceDeck.plains == standaloneDeck.plains &&
				referenceDeck.islands == standaloneDeck.islands &&
				referenceDeck.swamps == standaloneDeck.swamps &&
				referenceDeck.mountains == standaloneDeck.mountains &&
				referenceDeck.forests == standaloneDeck.forests;
		assertTrue("Metadata was not correctly set.", condition);
		assertEquals("Wrong number of cards.", referenceDeck.cardReferences.size(), standaloneDeck.cards.size());
		for (Card card : standaloneDeck.cards) {
			Integer count = referenceDeck.cardReferences.get(UID.makeHash(card));
			assertNotNull("Reference missing in deck.", count);
			assertEquals("Card count is wrong.", card.count, count);
		}
	}
}
