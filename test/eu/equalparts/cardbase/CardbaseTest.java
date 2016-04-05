package eu.equalparts.cardbase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.equalparts.cardbase.cards.Card;

/**
 * TODO deck functionality needs to be built into these.
 * 
 * @author Eduardo Pedroni
 *
 */
public class CardbaseTest {

	private Cardbase uut;
	private static Card testCard;
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	@Rule
	public final TemporaryFolder tempFolder = new TemporaryFolder();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		testCard = mapper.readValue(CardbaseTest.class.getResourceAsStream("/shivandragon.json"), Card.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		uut = new Cardbase();
	}
	
	/***********************************************************************************
	 * Constructor tests, happy path
	 ***********************************************************************************/
	@Test
	public void cleanCardbaseIsInitialised() throws Exception {
		assertEquals("Card collection is not empty.", 0, uut.getCards().size());
	}
	
	@Test
	public void fileCardbaseIsInitialised() throws Exception {
		uut = Cardbase.load(new File(getClass().getResource("/testbase.cb").getFile()));
		
		assertEquals("Card collection contains the wrong number of card entries.", 6, uut.getCards().size());
		
		class CardInfo {
			String setCode, number;
			Integer count;
			public CardInfo(String setCode, String number, Integer count) {
				this.setCode = setCode;
				this.number = number;
				this.count = count;
			}
		}
		CardInfo[] testCards = new CardInfo[] {
				new CardInfo("M12", "34", 2),
				new CardInfo("FRF", "129", 8),
				new CardInfo("M12", "26", 1),
				new CardInfo("FRF", "127", 1),
				new CardInfo("FRF", "128", 1),
				new CardInfo("M12", "152", 1)};
		
		for (CardInfo ci : testCards) {
			Card card = uut.getCard(ci.setCode, ci.number);
			assertNotNull("Missing card, set " + ci.setCode + ", " + ci.number, card);
			assertEquals("Wrong card count, set " + ci.setCode + ", " + ci.number, ci.count, (Integer) uut.getCount(card));
		}
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void loadFileIsNull() throws Exception {
		exception.expect(NullPointerException.class);
		uut = Cardbase.load(null);
	}
	
	@Test
	public void loadFileDoesNotExist() throws Exception {
		File notAFile = tempFolder.newFile();
		tempFolder.delete();
		
		exception.expect(IOException.class);
		uut = Cardbase.load(notAFile);
	}
	
	@Test
	public void loadFileHasWrongStructure() throws Exception {
		File wrongStructure = tempFolder.newFile("wrongStructure.json");
		try (FileWriter writer = new FileWriter(wrongStructure)) {
			writer.write("{\"cardData\":\"content\",\"cardReferences\":50,\"decks\":{\"subfield\":10}}");
		}
		
		exception.expect(JsonMappingException.class);
		uut = Cardbase.load(wrongStructure);
	}
	
	@Test
	public void loadFileHasUnkownStructure() throws Exception {
		File unkownStructure = tempFolder.newFile("wrongStructure.json");
		try (FileWriter writer = new FileWriter(unkownStructure)) {
			writer.write("{\"field1\":\"content\",\"field2\":50,\"field3\":{\"subfield\":10},\"list\":[10,20,30]}");
		}
		
		uut = Cardbase.load(unkownStructure);
		
		assertEquals("Cardbase should contain 0 cards.", 0, uut.getCards().size());
	}
	
	@Test
	public void loadFileIsNotJson() throws Exception {
		File notJson = tempFolder.newFile("wrongStructure.json");
		try (FileWriter writer = new FileWriter(notJson)) {
			writer.write("This is a file which does not contain valid JSON.");
		}
		
		exception.expect(JsonParseException.class);
		uut = Cardbase.load(notJson);
	}
	
	/***********************************************************************************
	 * Saving cardbase tests, happy path
	 ***********************************************************************************/
	@Test
	public void cardbaseIsSaved() throws Exception {
		final int testCount = 5;
		
		File testFile = tempFolder.newFile("saveTest.cb");
		uut.write(testFile);
		uut = Cardbase.load(testFile);
		assertEquals("Cardbase should contain no cards.", 0, uut.getCards().size());

		uut.addCard(testCard, testCount);

		uut.write(testFile);
		uut = Cardbase.load(testFile);
		assertEquals("Cardbase should contain 1 card.", 1, uut.getCards().size());
		Card card = uut.getCard("M15", "281");
		assertNotNull("Cardbase should contain a Shivan Dragon.", card);
		assertEquals("Cardbase should contain " + testCount + " Shivan Dragon.", testCount, uut.getCount(card));
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void saveFileCannotBeWrittenTo() throws Exception {
		File testFile = tempFolder.newFile("saveTest.cb");
		testFile.setWritable(false);
		exception.expect(IOException.class);
		uut.write(testFile);
	}
	
	@Test
	public void saveFileIsNull() throws Exception {
		exception.expect(NullPointerException.class);
		uut = Cardbase.load(null);
	}
	
	/***********************************************************************************
	 * Adding card tests, happy path
	 ***********************************************************************************/
	@Test
	public void newCardIsAdded() throws Exception {
		assertNull("Container should not contain the test card to begin with.", uut.getCard(testCard.setCode, testCard.number));
		
		uut.addCard(testCard, 1);
		
		assertEquals("Container should contain the test card once it is added.", testCard, uut.getCard(testCard.setCode, testCard.number));
		assertEquals("Container should have contained 1 test card.", 1, uut.getCount(testCard));
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
		
		int removed = uut.removeCard(testCard, 3);

		assertEquals("Card count was not updated correctly.", 2, uut.getCount(testCard));
		assertEquals("Container reports wrong removed count.", 3, removed);
		assertEquals("Card is missing from container.", testCard, uut.getCard(testCard.setCode, testCard.number));
	}
	
	@Test
	public void cardIsRemovedIfRemoveCountIsEqualToCardCount() throws Exception {
		uut.addCard(testCard, 5);
		
		int removed = uut.removeCard(testCard, 5);
		
		assertEquals("Card was not removed from container.", 0, uut.getCount(testCard));
		assertEquals("Container reports wrong removed count.", 5, removed);
		assertNull("Card is not missing from container.", uut.getCard(testCard.setCode, testCard.number));
	}
	
	@Test
	public void cardIsRemovedIfRemoveCountIsGreaterThanCardCount() throws Exception {
		uut.addCard(testCard, 3);
		
		int removed = uut.removeCard(testCard, 5);

		assertEquals("Card was not removed from container.", 0, uut.getCount(testCard));
		assertEquals("Container reports wrong removed count.", 3, removed);
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
		
		int removed = uut.removeCard(testCard, 1);
		
		assertEquals("Removed count should be 0.", 0, removed);
		assertNull("Card is not missing from container.", uut.getCard(testCard.setCode, testCard.number));
	}
	
	@Test
	public void removedCountIsLessThanZero() throws Exception {
		uut.addCard(testCard, 3);
		
		int removed = uut.removeCard(testCard, -4);

		assertEquals("Card count in container should be unchanged.", 3, uut.getCount(testCard));
		assertEquals("Container reports wrong removed count.", 0, removed);
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
