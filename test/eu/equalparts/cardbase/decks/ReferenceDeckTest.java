package eu.equalparts.cardbase.decks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import eu.equalparts.cardbase.cardstorage.ReferenceCardContainer;

public class ReferenceDeckTest {
	private ReferenceDeck uut;

	@Before
	public void setUp() throws Exception {
		uut = new ReferenceDeck();
	}
	
	/***********************************************************************************
	 * Typing
	 ***********************************************************************************/
	@Test
	public void deckIsReferenceCardContainer() throws Exception {
		assertTrue("Deck should be instance of ReferenceCardContainer.", uut instanceof ReferenceCardContainer);
	}
	
	/***********************************************************************************
	 * Name tests
	 ***********************************************************************************/
	@Test
	public void deckHasName() throws Exception {
		assertEquals("Deck should not have a name to begin with.", "", uut.getName());
		
		uut.setName("Test Name");
		
		assertEquals("Wrong name.", "Test Name", uut.getName());
	}
	
	@Test
	public void deckNameInConstructor() throws Exception {
		uut = new ReferenceDeck("Another Test");
		
		assertEquals("Deck should have a name to begin with.", "Another Test", uut.getName());
	}
}
