package eu.equalparts.cardbase.cardstorage;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.equalparts.cardbase.cards.Card;

/**
 * This test class tests storage-by-reference behaviour only.
 * For standalone storage behaviour tests, see {@code StandaloneCardContainerTest}.
 * 
 * @author Eduardo Pedroni
 *
 */
public class ReferenceCardContainerTest {
	private ReferenceCardContainer uut;
	private static Card testCard;

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		testCard = mapper.readValue(ReferenceCardContainerTest.class.getResourceAsStream("/shivandragon.json"), Card.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		uut = new ReferenceCardContainer() {
		};
	}
	
	/***********************************************************************************
	 * Adding card tests, happy path
	 ***********************************************************************************/
	@Test
	public void newCardIsAdded() throws Exception {
		uut.addCard(testCard, 1);
		
		assertEquals("Container should have contained 1 test card.", 1, uut.getCount(testCard));
	}
	
	@Test
	public void existingCardIsIncremented() throws Exception {
		uut.addCard(testCard, 2);
		uut.addCard(testCard, 4);
		
		assertEquals("Container should have contained 6 test cards.", 6, uut.getCount(testCard));
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void cardAddedIsNull() throws Exception {
		exception.expect(NullPointerException.class);
		uut.addCard(null, 0);
	}
	
	/***********************************************************************************
	 * Removing card tests, happy path
	 ***********************************************************************************/
	@Test
	public void cardRemoveCountIsLessThanCardCount() throws Exception {
		uut.addCard(testCard, 5);
		
		int removed = uut.removeCard(testCard, 3);

		assertEquals("Card count was not updated correctly.", 2, uut.getCount(testCard));
		assertEquals("Container reports wrong removed count.", 3, removed);
	}
	
	@Test
	public void cardRemoveCountIsEqualToCardCount() throws Exception {
		uut.addCard(testCard, 5);
		
		int removed = uut.removeCard(testCard, 5);
		
		assertEquals("Card was not removed from container.", 0, uut.getCount(testCard));
		assertEquals("Container reports wrong removed count.", 5, removed);
	}
	
	@Test
	public void cardRemoveCountIsGreaterThanCardCount() throws Exception {
		uut.addCard(testCard, 3);
		
		int removed = uut.removeCard(testCard, 5);

		assertEquals("Card was not removed from container.", 0, uut.getCount(testCard));
		assertEquals("Container reports wrong removed count.", 3, removed);
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void removedCardIsNull() throws Exception {
		exception.expect(NullPointerException.class);
		uut.removeCard(null, 0);
	}
	
	@Test
	public void removedCardIsNotInCardbase() throws Exception {
		int removed = uut.removeCard(testCard, 1);
		
		assertEquals("Removed count should be 0.", 0, removed);
	}
	
	@Test
	public void removedCountIsLessThanZero() throws Exception {
		uut.addCard(testCard, 3);
		
		int removed = uut.removeCard(testCard, -4);

		assertEquals("Card count in container should be unchanged.", 3, uut.getCount(testCard));
		assertEquals("Container reports wrong removed count.", 0, removed);
	}
}
