package eu.equalparts.cardbase;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.equalparts.cardbase.Cardbase;
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
		uut = new Cardbase(new File(getClass().getResource("/testbase.cb").getFile()));
		
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
		uut = new Cardbase(null);
	}
	
	@Test
	public void loadFileDoesNotExist() throws Exception {
		File notAFile = tempFolder.newFile();
		tempFolder.delete();
		
		exception.expect(IOException.class);
		uut = new Cardbase(notAFile);
	}
	
	@Test
	public void loadFileHasWrongStructure() throws Exception {
		File wrongStructure = tempFolder.newFile("wrongStructure.json");
		try (FileWriter writer = new FileWriter(wrongStructure)) {
			writer.write("{\"cards\":\"content\",\"collection\":50,\"decks\":{\"subfield\":10}}");
		}
		
		exception.expect(JsonMappingException.class);
		uut = new Cardbase(wrongStructure);
	}
	
	@Test
	public void loadFileHasUnkownStructure() throws Exception {
		File unkownStructure = tempFolder.newFile("wrongStructure.json");
		try (FileWriter writer = new FileWriter(unkownStructure)) {
			writer.write("{\"field1\":\"content\",\"field2\":50,\"field3\":{\"subfield\":10},\"list\":[10,20,30]}");
		}
		
		uut = new Cardbase(unkownStructure);
		
		assertEquals("Cardbase should contain 0 cards.", 0, uut.getCards().size());
	}
	
	@Test
	public void loadFileIsNotJson() throws Exception {
		File notJson = tempFolder.newFile("wrongStructure.json");
		try (FileWriter writer = new FileWriter(notJson)) {
			writer.write("This is a file which does not contain valid JSON.");
		}
		
		exception.expect(JsonParseException.class);
		uut = new Cardbase(notJson);
	}
	
	/***********************************************************************************
	 * Saving cardbase tests, happy path
	 ***********************************************************************************/
	@Test
	public void cardbaseIsSaved() throws Exception {
		final int testCount = 5;
		
		File testFile = tempFolder.newFile("saveTest.cb");
		uut.writeCollection(testFile);
		uut = new Cardbase(testFile);
		assertEquals("Cardbase should contain no cards.", 0, uut.getCards().size());

		uut.addCard(testCard, testCount);

		uut.writeCollection(testFile);
		uut = new Cardbase(testFile);
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
		uut.writeCollection(testFile);
	}
	
	@Test
	public void saveFileIsNull() throws Exception {
		exception.expect(NullPointerException.class);
		uut = new Cardbase(null);
	}
	
	/***********************************************************************************
	 * Adding card tests, happy path
	 ***********************************************************************************/
	@Test
	public void newCardIsAdded() throws Exception {
		uut.addCard(testCard, 1);
		Card addedCard = uut.getCard("M15", "281");
		
		assertNotNull("Card was not found in cardbase.", addedCard);
		assertEquals(testCard, addedCard);
	}
	
	@Test
	public void existingCardIsIncremented() throws Exception {
		uut.addCard(testCard, 2);
		uut.addCard(testCard, 4);
		
		Card addedCard = uut.getCard("M15", "281");
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
	public void cardRemoveCountIsLessThanCardCount() throws Exception {
		uut.addCard(testCard, 5);
		
		int removed = uut.removeCard(testCard, 3);
		
		Card removedCard = uut.getCard("M15", "281");
		assertNotNull("Card was not found in cardbase.", removedCard);
		assertEquals("Card count was not updated correctly.", 2, uut.getCount(removedCard));
		assertEquals("Cardbase reports wrong removed count.", 3, removed);
	}
	
	@Test
	public void cardRemoveCountIsEqualToCardCount() throws Exception {
		uut.addCard(testCard, 5);
		
		int removed = uut.removeCard(testCard, 5);
		
		Card removedCard = uut.getCard("M15", "281");
		assertNull("Card was not removed from cardbase.", removedCard);
		assertEquals("Cardbase reports wrong removed count.", 5, removed);
	}
	
	@Test
	public void cardRemoveCountIsGreaterThanCardCount() throws Exception {
		uut.addCard(testCard, 3);
		int removed = uut.removeCard(testCard, 5);
		
		Card removedCard = uut.getCard("M15", "281");
		assertNull("Card was not removed from cardbase.", removedCard);
		assertEquals("Cardbase reports wrong removed count.", 3, removed);
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

	/***********************************************************************************
	 * Card getter tests, happy path
	 ***********************************************************************************/
	@Test
	public void correctCardIsReturnedByGetter() throws Exception {
		uut.addCard(testCard, 1);
		
		Card card = uut.getCard("M15", "281");
		
		for (Field field : Card.class.getFields()) {
			assertEquals("Field " + field.getName(), field.get(testCard), field.get(card));
		}
	}
	
	@Test
	public void correctCardCollectionIsReturnedByGetter() throws Exception {
		uut = new Cardbase(new File(getClass().getResource("/testbase.cb").getFile()));
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rawFile = mapper.readTree(getClass().getResourceAsStream("/testbase.cb"));
		Map<Integer, Card> cards = rawFile.get("cards").traverse(mapper).readValueAs(new TypeReference<Map<Integer, Card>>() {});

		assertTrue("Not all cards were returned by the getter.", uut.getCards().containsAll(cards.values()));
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void getCardIsNotInCardbase() throws Exception {
		assertNull("Method should have returned null", uut.getCard("M15", "281"));
	}
	
	@Test
	public void cardCollectionWhenCardbaseIsEmpty() throws Exception {
		assertEquals("Returned collection size should have been 0.", 0, uut.getCards().size());
	}
}
