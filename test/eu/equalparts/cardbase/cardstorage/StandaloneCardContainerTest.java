package eu.equalparts.cardbase.cardstorage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.equalparts.cardbase.cards.Card;

public class StandaloneCardContainerTest {
	private StandaloneCardContainer uut;
	private static Card testCard;

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		testCard = mapper.readValue(StandaloneCardContainerTest.class.getResourceAsStream("/shivandragon.json"), Card.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		uut = new StandaloneCardContainer() {
		};
	}
	
	/***********************************************************************************
	 * StandaloneCardContainer should extend ReferenceCardContainer
	 ***********************************************************************************/
	@Test
	public void classInherits() throws Exception {
		assertTrue("StandaloneCardContainer should be subclass of ReferenceCardContainer.", uut instanceof ReferenceCardContainer);
	}
	
	/***********************************************************************************
	 * Adding card tests, happy path
	 ***********************************************************************************/
	@Test
	public void newCardIsAdded() throws Exception {
		assertNull("Container should not contain the test card to begin with.", uut.getCard(testCard.setCode, testCard.number));
		
		uut.addCard(testCard, 1);
		
		assertEquals("Container should contain the test card once it is added.", testCard, uut.getCard(testCard.setCode, testCard.number));
	}
	
	@Test
	public void existingCardIsIncremented() throws Exception {
		uut.addCard(testCard, 2);
		uut.addCard(testCard, 4);
		
		Card addedCard = uut.getCard(testCard.setCode, testCard.number);
		assertNotNull("Card was not found in cardbase.", addedCard);
		assertEquals("Card count was not updated correctly.", 6, uut.getCount(addedCard));
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
		
		uut.removeCard(testCard, 3);

		assertEquals("Card is missing from container.", testCard, uut.getCard(testCard.setCode, testCard.number));
	}
	
	@Test
	public void cardIsRemovedIfRemoveCountIsEqualToCardCount() throws Exception {
		uut.addCard(testCard, 5);
		
		uut.removeCard(testCard, 5);
		
		assertNull("Card is not missing from container.", uut.getCard(testCard.setCode, testCard.number));
	}
	
	@Test
	public void cardIsRemovedIfRemoveCountIsGreaterThanCardCount() throws Exception {
		uut.addCard(testCard, 3);
		
		uut.removeCard(testCard, 5);

		assertNull("Card is not missing from container.", uut.getCard(testCard.setCode, testCard.number));
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
		assertNull("Card is not initially missing from container.", uut.getCard(testCard.setCode, testCard.number));
		
		uut.removeCard(testCard, 1);
		
		assertNull("Card is not missing from container.", uut.getCard(testCard.setCode, testCard.number));
	}
	
	@Test
	public void removedCountIsLessThanZero() throws Exception {
		uut.addCard(testCard, 3);
		
		uut.removeCard(testCard, -4);

		assertEquals("Card should not be missing from container.", testCard, uut.getCard(testCard.setCode, testCard.number));
	}
	
	/***********************************************************************************
	 * Card getter tests, happy path
	 ***********************************************************************************/
	@Test
	public void correctCardIsReturnedByGetter() throws Exception {
		uut.addCard(testCard, 1);
		
		Card card = uut.getCard(testCard.setCode, testCard.number);
		
		for (Field field : Card.class.getFields()) {
			assertEquals("Field " + field.getName(), field.get(testCard), field.get(card));
		}
	}
	
	@Test
	public void correctCardCollectionIsReturnedByGetter() throws Exception {
		uut.addCard(testCard, 1);

		assertTrue("Not all cards were returned by the getter.", uut.getCards().contains(testCard));
	}
	
	@Test
	public void cardCollectionWhenContainerIsEmpty() throws Exception {
		assertEquals("Returned collection size should have been 0.", 0, uut.getCards().size());
	}

	@Test
	public void getCardIsNotInCardbase() throws Exception {
		assertNull("Method should have returned null", uut.getCard(testCard.setCode, testCard.number));
	}
}
