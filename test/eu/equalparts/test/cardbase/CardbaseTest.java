package eu.equalparts.test.cardbase;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.Cardbase;
import eu.equalparts.cardbase.cards.Card;
import eu.equalparts.cardbase.utils.JSON;

/**
 * TODO deck functionality needs to be built into these.
 * 
 * @author Eduardo Pedroni
 *
 */
public class CardbaseTest {

	private Cardbase cardbase;
	private static Card shivanDragon;
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		shivanDragon = JSON.mapper.readValue(CardbaseTest.class.getResourceAsStream("shivandragon.json"), Card.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		cardbase = new Cardbase();
	}
	
	/***********************************************************************************
	 * Constructor tests, happy path
	 ***********************************************************************************/
	@Test
	public void cleanCardbaseIsInitialised() throws Exception {
		assertEquals("Card collection is not empty.", 0, cardbase.getCards().size());
		assertEquals("Deck collection is not empty.", 0, cardbase.getDecks().size());
	}
	
	@Test
	public void fileCardbaseIsInitialised() throws Exception {
		cardbase = new Cardbase(new File(getClass().getResource("testbase.cb").toURI()));
		
		assertEquals("Card collection contains the wrong number of card entries.", 6, cardbase.getCards().size());
		
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
			Card card = cardbase.getCard(ci.setCode, ci.number);
			assertNotNull("Missing card, set " + ci.setCode + ", " + ci.number, card);
			assertEquals("Wrong card count, set " + ci.setCode + ", " + ci.number, ci.count, card.count);
		}
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void loadFileDoesNotExist() throws Exception {
		exception.expect(IOException.class);
		cardbase = new Cardbase(new File("not a file"));
	}
	
	@Test
	public void loadFileHasWrongStructure() throws Exception {
		exception.expect(JsonMappingException.class);
		cardbase = new Cardbase(new File(getClass().getResource("testcards.json").toURI()));
	}
	
	@Test
	public void loadFileIsNotJson() throws Exception {
		exception.expect(JsonParseException.class);
		cardbase = new Cardbase(new File(getClass().getResource("notjson.txt").toURI()));
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
			cardbase.writeCollection(testFile);
			cardbase = new Cardbase(testFile);
			assertEquals("Cardbase should contain no cards.", 0, cardbase.getCards().size());

			cardbase.addCard(shivanDragon);

			cardbase.writeCollection(testFile);
			cardbase = new Cardbase(testFile);
			assertEquals("Cardbase should contain one card.", 1, cardbase.getCards().size());
			Card card = cardbase.getCard("M15", "281");
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
			cardbase.writeCollection(testFile);
		} finally {
			testFile.delete();
		}
	}
	
	/***********************************************************************************
	 * Adding card tests, happy path
	 ***********************************************************************************/
	@Test
	public void newCardIsAdded() throws Exception {
		cardbase.addCard(shivanDragon);
		Card addedCard = cardbase.getCard("M15", "281");
		
		assertNotNull("Card was not found in cardbase.", addedCard);
		assertEquals(shivanDragon, addedCard);
	}
	
	@Test
	public void existingCardIsIncremented() throws Exception {
		Card shivanClone = shivanDragon.clone();
		cardbase.addCard(shivanClone);
		Card shivanClone2 = shivanDragon.clone();
		shivanClone2.count = 2;
		cardbase.addCard(shivanClone2);
		
		Card addedCard = cardbase.getCard("M15", "281");
		assertNotNull("Card was not found in cardbase.", addedCard);
		assertEquals("Card count was not updated correctly.", new Integer(3), addedCard.count);
	}
	
	/***********************************************************************************
	 * Adding card tests, happy path
	 ***********************************************************************************/
	@Test
	public void cardRemoveCountIsLessThanCardCount() throws Exception {
		Card shivanClone = shivanDragon.clone();
		shivanClone.count = 5;
		cardbase.addCard(shivanClone);
		
		int removed = cardbase.removeCard(shivanDragon);
		
		Card removedCard = cardbase.getCard("M15", "281");
		assertNotNull("Card was not found in cardbase.", removedCard);
		assertEquals("Card count was not updated correctly.", new Integer(4), removedCard.count);
		assertEquals("Cardbase reports wrong removed count.", 1, removed);
	}
	
	@Test
	public void cardRemoveCountIsEqualToCardCount() throws Exception {
		Card shivanClone = shivanDragon.clone();
		cardbase.addCard(shivanClone);
		
		int removed = cardbase.removeCard(shivanDragon);
		
		Card removedCard = cardbase.getCard("M15", "281");
		assertNull("Card was not removed from cardbase.", removedCard);
		assertEquals("Cardbase reports wrong removed count.", 1, removed);
	}
	
	@Test
	public void cardRemoveCountIsGreaterThanCardCount() throws Exception {
		Card shivanClone = shivanDragon.clone();
		shivanClone.count = 3;
		cardbase.addCard(shivanClone);
		
		Card shivanClone2 = shivanDragon.clone();
		shivanClone2.count = 5;
		int removed = cardbase.removeCard(shivanClone2);
		
		Card removedCard = cardbase.getCard("M15", "281");
		assertNull("Card was not removed from cardbase.", removedCard);
		assertEquals("Cardbase reports wrong removed count.", 3, removed);
	}
}
