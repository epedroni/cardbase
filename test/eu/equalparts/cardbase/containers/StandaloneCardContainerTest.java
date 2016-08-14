package eu.equalparts.cardbase.containers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import eu.equalparts.cardbase.card.Card;
import eu.equalparts.cardbase.json.JSON;

public class StandaloneCardContainerTest {
	private StandaloneCardContainer uut;
	private static Card testCard;

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Rule
	public final TemporaryFolder tempFolder = new TemporaryFolder();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testCard = JSON.mapper.readValue(StandaloneCardContainerTest.class.getResourceAsStream("/shivandragon.json"), Card.class);
	}

	@Before
	public void setUp() throws Exception {
		uut = new StandaloneCardContainer();
	}

	/***********************************************************************************
	 * Adding card tests, happy path
	 ***********************************************************************************/
	@Test
	public void newCardIsAdded() throws Exception {
		assertNull("Container should not contain the test card to begin with.", uut.getCard(testCard.setCode.get(), testCard.number.get()));

		uut.addCard(testCard, 1);

		assertEquals("Container should contain the test card once it is added.", testCard, uut.getCard(testCard.setCode.get(), testCard.number.get()));
		assertEquals("Container should have contained 1 test card.", 1, uut.getCount(testCard));
	}

	@Test
	public void existingCardIsIncremented() throws Exception {
		uut.addCard(testCard, 2);
		uut.addCard(testCard, 4);

		Card addedCard = uut.getCard(testCard.setCode.get(), testCard.number.get());
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

		int removed = uut.removeCard(testCard, 3);

		assertEquals("Card count was not updated correctly.", 2, uut.getCount(testCard));
		assertEquals("Container reports wrong removed count.", 3, removed);
		assertEquals("Card is missing from container.", testCard, uut.getCard(testCard.setCode.get(), testCard.number.get()));
	}

	@Test
	public void cardIsRemovedIfRemoveCountIsEqualToCardCount() throws Exception {
		uut.addCard(testCard, 5);

		int removed = uut.removeCard(testCard, 5);

		assertEquals("Card was not removed from container.", 0, uut.getCount(testCard));
		assertEquals("Container reports wrong removed count.", 5, removed);
		assertNull("Card is not missing from container.", uut.getCard(testCard.setCode.get(), testCard.number.get()));
	}

	@Test
	public void cardIsRemovedIfRemoveCountIsGreaterThanCardCount() throws Exception {
		uut.addCard(testCard, 3);

		int removed = uut.removeCard(testCard, 5);

		assertEquals("Card was not removed from container.", 0, uut.getCount(testCard));
		assertEquals("Container reports wrong removed count.", 3, removed);
		assertNull("Card is not missing from container.", uut.getCard(testCard.setCode.get(), testCard.number.get()));
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
		assertNull("Card is not initially missing from container.", uut.getCard(testCard.setCode.get(), testCard.number.get()));

		int removed = uut.removeCard(testCard, 1);

		assertEquals("Removed count should be 0.", 0, removed);
		assertNull("Card is not missing from container.", uut.getCard(testCard.setCode.get(), testCard.number.get()));
	}

	@Test
	public void removedCountIsLessThanZero() throws Exception {
		uut.addCard(testCard, 3);

		int removed = uut.removeCard(testCard, -4);

		assertEquals("Card count in container should be unchanged.", 3, uut.getCount(testCard));
		assertEquals("Container reports wrong removed count.", 0, removed);
		assertEquals("Card should not be missing from container.", testCard, uut.getCard(testCard.setCode.get(), testCard.number.get()));
	}

	/***********************************************************************************
	 * Card getter tests, happy path
	 ***********************************************************************************/
	@Test
	public void correctCardIsReturnedByGetter() throws Exception {
		uut.addCard(testCard, 1);

		Card card = uut.getCard(testCard.setCode.get(), testCard.number.get());

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
		assertNull("Method should have returned null", uut.getCard(testCard.setCode.get(), testCard.number.get()));
	}
}
