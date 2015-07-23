package eu.equalparts.cardbase;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
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
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		testCard = mapper.readValue(CardbaseTest.class.getResourceAsStream("shivandragon.json"), Card.class);
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
		uut = new Cardbase(new File(getClass().getResource("testbase.cb").toURI()));
		
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
			assertEquals("Wrong card count, set " + ci.setCode + ", " + ci.number, ci.count, card.count);
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
		exception.expect(IOException.class);
		uut = new Cardbase(new File("not a file"));
	}
	
	@Test
	public void loadFileHasWrongStructure() throws Exception {
		exception.expect(JsonMappingException.class);
		uut = new Cardbase(new File(getClass().getResource("testcards.json").toURI()));
	}
	
	@Test
	public void loadFileIsNotJson() throws Exception {
		exception.expect(JsonParseException.class);
		uut = new Cardbase(new File(getClass().getResource("notjson.txt").toURI()));
	}
	
	/***********************************************************************************
	 * Saving cardbase tests, happy path
	 ***********************************************************************************/
	private boolean validateSaveFile(File testFile) throws IOException {
		if (!testFile.exists()) {
			if (testFile.createNewFile()) {
				if (testFile.canWrite()) {
					return true;
				} else {
					fail("Cannot write to testsave.cb, aborting...");
				}
			} else {
				fail("testsave.cb could not be created, aborting...");
			}
		} else {
			fail("testsave.cb already exists, aborting...");
		}
		return false;
	}
	
	@Test
	public void cardbaseIsSaved() throws Exception {
		File testFile = new File("savetest.cb");
		validateSaveFile(testFile);
		try {
			uut.writeCollection(testFile);
			uut = new Cardbase(testFile);
			assertEquals("Cardbase should contain no cards.", 0, uut.getCards().size());

			uut.addCard(testCard);

			uut.writeCollection(testFile);
			uut = new Cardbase(testFile);
			assertEquals("Cardbase should contain one card.", 1, uut.getCards().size());
			Card card = uut.getCard("M15", "281");
			assertNotNull("Cardbase should contain a Shivan Dragon.", card);
			assertEquals("Cardbase should contain only one Shivan Dragon", new Integer(1), card.count);
		} finally {
			testFile.delete();
		}
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void saveFileCannotBeWrittenTo() throws Exception {
		File testFile = new File("savetest.cb");
		validateSaveFile(testFile);
		testFile.setWritable(false);
		try {
			exception.expect(IOException.class);
			uut.writeCollection(testFile);
		} finally {
			testFile.delete();
		}
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
		uut.addCard(testCard);
		Card addedCard = uut.getCard("M15", "281");
		
		assertNotNull("Card was not found in cardbase.", addedCard);
		assertEquals(testCard, addedCard);
	}
	
	@Test
	public void existingCardIsIncremented() throws Exception {
		Card shivanClone = testCard.clone();
		uut.addCard(shivanClone);
		Card shivanClone2 = testCard.clone();
		shivanClone2.count = 2;
		uut.addCard(shivanClone2);
		
		Card addedCard = uut.getCard("M15", "281");
		assertNotNull("Card was not found in cardbase.", addedCard);
		assertEquals("Card count was not updated correctly.", new Integer(3), addedCard.count);
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void cardAddedIsNull() throws Exception {
		exception.expect(NullPointerException.class);
		uut.addCard(null);
	}
	
	/***********************************************************************************
	 * Removing card tests, happy path
	 ***********************************************************************************/
	@Test
	public void cardRemoveCountIsLessThanCardCount() throws Exception {
		Card shivanClone = testCard.clone();
		shivanClone.count = 5;
		uut.addCard(shivanClone);
		
		int removed = uut.removeCard(testCard);
		
		Card removedCard = uut.getCard("M15", "281");
		assertNotNull("Card was not found in cardbase.", removedCard);
		assertEquals("Card count was not updated correctly.", new Integer(4), removedCard.count);
		assertEquals("Cardbase reports wrong removed count.", 1, removed);
	}
	
	@Test
	public void cardRemoveCountIsEqualToCardCount() throws Exception {
		Card shivanClone = testCard.clone();
		uut.addCard(shivanClone);
		
		int removed = uut.removeCard(testCard);
		
		Card removedCard = uut.getCard("M15", "281");
		assertNull("Card was not removed from cardbase.", removedCard);
		assertEquals("Cardbase reports wrong removed count.", 1, removed);
	}
	
	@Test
	public void cardRemoveCountIsGreaterThanCardCount() throws Exception {
		Card shivanClone = testCard.clone();
		shivanClone.count = 3;
		uut.addCard(shivanClone);
		
		Card shivanClone2 = testCard.clone();
		shivanClone2.count = 5;
		int removed = uut.removeCard(shivanClone2);
		
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
		uut.removeCard(null);
	}
	
	@Test
	public void removedCardIsNotInCardbase() throws Exception {
		int removed = uut.removeCard(testCard);
		
		assertEquals("Removed count should be 0.", 0, removed);
	}

	/***********************************************************************************
	 * Card getter tests, happy path
	 ***********************************************************************************/
	@Test
	public void correctCardIsReturnedByGetter() throws Exception {
		uut.addCard(testCard.clone());
		
		Card card = uut.getCard("M15", "281");
		
		for (Field field : Card.class.getFields()) {
			assertEquals("Field " + field.getName(), field.get(testCard), field.get(card));
		}
	}
	
	@Test
	public void correctCardCollectionIsReturnedByGetter() throws Exception {
		uut = new Cardbase(new File(getClass().getResource("testbase.cb").toURI()));
		Map<Integer, Card> cards = new ObjectMapper().readValue(getClass().getResourceAsStream("testbase.cb"), new TypeReference<Map<Integer, Card>>() {});
		
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
