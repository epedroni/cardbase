package eu.equalparts.cardbase.containers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import eu.equalparts.cardbase.card.Card;
import eu.equalparts.cardbase.json.JSON;

public class ReferenceCardContainerTest {

	private ReferenceCardContainer uut;
	private static Card testCard;
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	@Rule
	public final TemporaryFolder tempFolder = new TemporaryFolder();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testCard = JSON.mapper.readValue(ReferenceCardContainerTest.class.getResourceAsStream("/shivandragon.json"), Card.class);
	}
	
	@Before
	public void setUp() throws Exception {
		uut = new ReferenceCardContainer();
	}

	/***********************************************************************************
	 * Adding card tests, happy path
	 ***********************************************************************************/
	@Test
	public void newCardIsAdded() throws Exception {
		assertEquals("Container should not contain the test card to begin with.", 0, uut.getCount(testCard));
		
		uut.addCard(testCard, 1);
		
		assertEquals("Container should contain 1 test card.", 1, uut.getCount(testCard));
	}
	
	@Test
	public void existingCardIsIncremented() throws Exception {
		uut.addCard(testCard, 2);
		uut.addCard(testCard, 4);
		
		assertEquals("Card count was not updated correctly.", 6, uut.getCount(testCard));
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
	public void cardIsStillPresentIfRemoveCountIsLessThanCardCount() throws Exception {
		uut.addCard(testCard, 5);
		
		int removed = uut.removeCard(testCard, 3);

		assertEquals("Card count was not updated correctly.", 2, uut.getCount(testCard));
		assertEquals("Container reports wrong removed count.", 3, removed);
	}
	
	@Test
	public void cardIsRemovedIfRemoveCountIsEqualToCardCount() throws Exception {
		uut.addCard(testCard, 5);
		
		int removed = uut.removeCard(testCard, 5);
		
		assertEquals("Card was not removed from container.", 0, uut.getCount(testCard));
		assertEquals("Container reports wrong removed count.", 5, removed);
	}
	
	@Test
	public void cardIsRemovedIfRemoveCountIsGreaterThanCardCount() throws Exception {
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
	public void removedCardIsNotInContainer() throws Exception {
		assertEquals("Card is not initially missing from container.", 0, uut.getCount(testCard));
		
		int removed = uut.removeCard(testCard, 1);
		
		assertEquals("Removed count should be 0.", 0, removed);
		assertEquals("Card should still be missing from container.", 0, uut.getCount(testCard));
	}
	
	@Test
	public void removedCountIsLessThanZero() throws Exception {
		uut.addCard(testCard, 3);
		
		int removed = uut.removeCard(testCard, -4);

		assertEquals("Card count in container should be unchanged.", 3, uut.getCount(testCard));
		assertEquals("Container reports wrong removed count.", 0, removed);
	}
	
	/***********************************************************************************
	 * Land tests
	 ***********************************************************************************/
	@Test
	public void containsIslands() throws Exception {
		assertEquals(0, uut.getIslands());
		uut.setIslands(5);
		assertEquals(5, uut.getIslands());
	}

	@Test
	public void containsPlains() throws Exception {
		assertEquals(0, uut.getPlains());
		uut.setPlains(5);
		assertEquals(5, uut.getPlains());
	}
	
	@Test
	public void containsSwamps() throws Exception {
		assertEquals(0, uut.getSwamps());
		uut.setSwamps(5);
		assertEquals(5, uut.getSwamps());
	}
	
	@Test
	public void containsMountains() throws Exception {
		assertEquals(0, uut.getMountains());
		uut.setMountains(5);
		assertEquals(5, uut.getMountains());
	}
	
	@Test
	public void containsForests() throws Exception {
		assertEquals(0, uut.getForests());
		uut.setForests(5);
		assertEquals(5, uut.getForests());
	}

}
