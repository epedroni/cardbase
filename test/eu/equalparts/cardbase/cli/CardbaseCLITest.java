package eu.equalparts.cardbase.cli;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.equalparts.cardbase.cards.Card;
import eu.equalparts.cardbase.cards.FullCardSet;
import eu.equalparts.cardbase.utils.MTGUniverse;

public class CardbaseCLITest {

	private CardbaseCLI uut;
	
	private ByteArrayOutputStream testOutput;
	private final PrintStream console = System.out;
	private final String EOL = System.getProperty("line.separator");
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		uut = new CardbaseCLI();
		testOutput = new ByteArrayOutputStream();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/***********************************************************************************
	 * Constructor tests, happy path
	 ***********************************************************************************/
	@Test
	public void instantiationWithoutArguments() throws Exception {
		uut = new CardbaseCLI();
		
		assertEquals("Cardbase contains the wrong number of card entries.", 0, uut.cardbase.getCards().size());
	}
	
	@Test
	public void instantiationWithCardbaseFile() throws Exception {
		uut = new CardbaseCLI(getClass().getResource("/testbase.cb").getFile());
		
		assertEquals("Cardbase contains the wrong number of card entries.", 6, uut.cardbase.getCards().size());
		
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
			Card card = uut.cardbase.getCard(ci.setCode, ci.number);
			assertNotNull("Missing card, set " + ci.setCode + ", " + ci.number, card);
			assertEquals("Wrong card count, set " + ci.setCode + ", " + ci.number, ci.count, card.count);
		}
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void instantiationWithInvalidArguments() throws Exception {
		File notAFile = tempFolder.newFile();
		tempFolder.delete();

		exception.expect(IllegalArgumentException.class);
		uut = new CardbaseCLI(notAFile.getAbsolutePath());
	}
	
	@Test
	public void instantiationWithEmptyArguments() throws Exception {
		uut = new CardbaseCLI("");
		
		assertEquals("Cardbase contains the wrong number of card entries.", 0, uut.cardbase.getCards().size());
	}
	
	/***********************************************************************************
	 * help() tests, happy path
	 ***********************************************************************************/
	@Test
	public void helpInformationIsPrinted() throws Exception {
		try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/help_en"))) {
			String help = scanner.useDelimiter("\\Z").next();
			
			try {
				System.setOut(new PrintStream(testOutput));
				uut.interpretInput("help");
			} finally {
				System.setOut(console);
			}
			assertEquals(help + EOL, testOutput.toString());
		}
	}
	
	/***********************************************************************************
	 * write() tests, happy path
	 ***********************************************************************************/
	@Test
	public void writeCardbaseToSpecifiedFile() throws Exception {
		File testFile = tempFolder.newFile("saveTest.cb");
		
		try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/shivandragon.json"));
				Scanner scanner2 = new Scanner(testFile)) {
			String cardJSON = scanner.useDelimiter("\\Z").next();
			Card testCard = new ObjectMapper().readValue(cardJSON, Card.class);
			testCard.count = 1;
			uut.cardbase.addCard(testCard);

			try {
				System.setOut(new PrintStream(testOutput));
				uut.interpretInput("write " + testFile.getAbsolutePath());
			} finally {
				System.setOut(console);
			}
			
			String save = scanner2.useDelimiter("\\Z").next();
			assertTrue(save.contains(cardJSON));
			
			assertEquals("Cardbase was saved to \"" + testFile.getAbsolutePath() + "\". "
							+ "Subsequent writes will be done to this same file unless otherwise requested." + EOL, testOutput.toString());
		}
	}
	
	@Test
	public void specifiedFileIsSubsequentlyUsedByDefault() throws Exception {
		File testFile = tempFolder.newFile("saveTest.cb");
		
		try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/shivandragon.json"));
				Scanner scanner2 = new Scanner(testFile)) {
			uut.interpretInput("write " + testFile.getAbsolutePath());
			String cardJSON = scanner.useDelimiter("\\Z").next();
			Card testCard = new ObjectMapper().readValue(cardJSON, Card.class);
			testCard.count = 1;
			uut.cardbase.addCard(testCard);

			try {
				System.setOut(new PrintStream(testOutput));
				uut.interpretInput("write");
			} finally {
				System.setOut(console);
			}
			
			String save = scanner2.useDelimiter("\\Z").next();
			assertTrue(save.contains(cardJSON));
			
			assertEquals("Cardbase was saved to \"" + testFile.getAbsolutePath() + "\". "
					+ "Subsequent writes will be done to this same file unless otherwise requested." + EOL, testOutput.toString());
		}
	}

	@Test
	public void noFileIsProvidedAndNoDefaultIsAvailable() throws Exception {
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("write");
		} finally {
			System.setOut(console);
		}
		assertEquals("Please provide a file name." + EOL, testOutput.toString());
	}
	
	@Test
	public void reasonableFileNameWithoutExtension() throws Exception {
		File testFile = tempFolder.newFile("saveTest");
		
		try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/shivandragon.json"));
				Scanner scanner2 = new Scanner(new File(tempFolder.getRoot().getAbsolutePath() + "/saveTest.cb"))) {
			String cardJSON = scanner.useDelimiter("\\Z").next();
			Card testCard = new ObjectMapper().readValue(cardJSON, Card.class);
			testCard.count = 1;
			uut.cardbase.addCard(testCard);

			try {
				System.setOut(new PrintStream(testOutput));
				uut.interpretInput("write " + testFile.getAbsolutePath());
			} finally {
				System.setOut(console);
			}
			
			String save = scanner2.useDelimiter("\\Z").next();
			assertTrue(save.contains(cardJSON));
			
			assertEquals("Cardbase was saved to \"" + tempFolder.getRoot().getAbsolutePath() + "saveTest.cb\". "
							+ "Subsequent writes will be done to this same file unless otherwise requested." + EOL, testOutput.toString());
		}
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void pathProvidedIsDirectory() throws Exception {
		File directory = tempFolder.newFolder("testdirectory.cb");
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("write " + directory.getAbsolutePath());
		} finally {
			System.setOut(console);
		}
		assertEquals("Could not write to \"" + directory.getAbsolutePath() + "\"." + EOL, testOutput.toString());
	}
	
	@Test
	public void nameWithBrokenExtension() throws Exception {
		try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/shivandragon.json"))) {
			String cardJSON = scanner.useDelimiter("\\Z").next();
			Card testCard = new ObjectMapper().readValue(cardJSON, Card.class);
			testCard.count = 1;
			uut.cardbase.addCard(testCard);

			try {
				System.setOut(new PrintStream(testOutput));
				uut.interpretInput("write " + tempFolder.getRoot().getAbsolutePath() + "/saveTest.c b");
			} finally {
				System.setOut(console);
			}

			try (Scanner scanner2 = new Scanner(new File(tempFolder.getRoot().getAbsolutePath() + "/saveTest.c.cb"))) {
				String save = scanner2.useDelimiter("\\Z").next();
				assertTrue(save.contains(cardJSON));

				assertEquals("Cardbase was saved to \"" + tempFolder.getRoot().getAbsolutePath() + "/saveTest.c.cb\". "
						+ "Subsequent writes will be done to this same file unless otherwise requested." + EOL, testOutput.toString());
			}
		}
	}
	
	@Test
	public void nameWithWrongExtension() throws Exception {
		try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/shivandragon.json"))) {
			String cardJSON = scanner.useDelimiter("\\Z").next();
			Card testCard = new ObjectMapper().readValue(cardJSON, Card.class);
			testCard.count = 1;
			uut.cardbase.addCard(testCard);

			try {
				System.setOut(new PrintStream(testOutput));
				uut.interpretInput("write " + tempFolder.getRoot().getAbsolutePath() + "/saveTest.tar");
			} finally {
				System.setOut(console);
			}

			try (Scanner scanner2 = new Scanner(new File(tempFolder.getRoot().getAbsolutePath() + "/saveTest.tar.cb"))) {
				String save = scanner2.useDelimiter("\\Z").next();
				assertTrue(save.contains(cardJSON));

				assertEquals("Cardbase was saved to \"" + tempFolder.getRoot().getAbsolutePath() + "/saveTest.tar.cb\". "
						+ "Subsequent writes will be done to this same file unless otherwise requested." + EOL, testOutput.toString());
			}
		}
	}
	
	@Test
	public void nameWithIllegalCharacters() throws Exception {
		try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/shivandragon.json"))) {
			String cardJSON = scanner.useDelimiter("\\Z").next();
			Card testCard = new ObjectMapper().readValue(cardJSON, Card.class);
			testCard.count = 1;
			uut.cardbase.addCard(testCard);

			try {
				System.setOut(new PrintStream(testOutput));
				uut.interpretInput("write " + tempFolder.getRoot().getAbsolutePath() + "/f1lEnämẽ\"--._-//");
			} finally {
				System.setOut(console);
			}
			
			assertEquals("Error: lost contact with the output file." + EOL, testOutput.toString());
		}
	}
	
	/***********************************************************************************
	 * version() tests, happy path
	 ***********************************************************************************/
	@Test
	public void correctVersionIsPrinted() throws Exception {
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("version");
		} finally {
			System.setOut(console);
		}
		assertEquals("Cardbase v" + CardbaseCLI.VERSION + EOL, testOutput.toString());
	}
	
	/***********************************************************************************
	 * exit() tests, happy path
	 ***********************************************************************************/
	@Test
	public void exitFlagIsRaised() throws Exception {
		uut.interpretInput("exit");
		
		assertEquals("Incorrect state for exit flag.", true, uut.exit);
	}
	
	@Test
	public void saveReminderIsPrintedIfPromptFlagIsRaised() throws Exception {
		uut.savePrompt = true;
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("exit");
		} finally {
			System.setOut(console);
		}
		assertEquals("Don't forget to save. If you really wish to quit without saving, type \"exit\" again." + EOL, testOutput.toString());
		assertEquals("Incorrect state for exit flag.", false, uut.exit);
	}
	
	@Test
	public void exitFlagIsRaisedAfterSavePromptIsAcknowledged() throws Exception {
		uut.savePrompt = true;
		
		uut.interpretInput("exit");
		
		assertEquals("Incorrect state for exit flag.", false, uut.exit);
		assertEquals("Incorrect state for save flag.", false, uut.savePrompt);

		uut.interpretInput("exit");
		
		assertEquals("Incorrect state for exit flag.", true, uut.exit);
	}
	
	/***********************************************************************************
	 * sets() tests, happy path
	 ***********************************************************************************/
	@Test
	public void correctSetListIsPrinted() throws Exception {
		uut.mtgUniverse = new MTGUniverse(getClass().getResource("").toString());
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("sets");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("LEA          : Limited Edition Alpha\n"
				+ "LEB          : Limited Edition Beta\n"
				+ "ARN          : Arabian Nights\n"
				+ "M15          : Magic 2015 Core Set" + EOL, testOutput.toString());
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void fallbackListIsPrintedIfListCannotBeFound() throws Exception {
		File noListLocation = tempFolder.newFolder();
		tempFolder.delete();
		uut.mtgUniverse = new MTGUniverse("file:" + noListLocation.getCanonicalPath() + "/");
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("sets");
		} finally {
			System.setOut(console);
		}
		
		try (Scanner scanner = new Scanner(getClass().getResourceAsStream("expectedFallbackList"))) {
			String expectedOutput = scanner.useDelimiter("\\Z").next();
			assertEquals(expectedOutput + EOL, testOutput.toString());
		}
	}
	
	/***********************************************************************************
	 * set() tests, happy path
	 ***********************************************************************************/
	@Test
	public void correctSetIsSelected() throws Exception {
		uut.mtgUniverse = new MTGUniverse(getClass().getResource("").toString());
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("set M15");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Selected set: Magic 2015 Core Set." + EOL, testOutput.toString());
		assertEquals(uut.selectedSet.code, "M15");
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void invalidSetIsProvided() throws Exception {
		uut.mtgUniverse = new MTGUniverse(getClass().getResource("").toString());
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("set not_a_set");
		} finally {
			System.setOut(console);
		}

		assertEquals("\"not_a_set\" does not correspond to any set (use \"sets\" to see all valid set codes)." 
				+ EOL, testOutput.toString());
		assertNull(uut.selectedSet);
	}
	
	/***********************************************************************************
	 * glance() tests, happy path
	 ***********************************************************************************/
	@Test
	public void glanceIsPrintedWithOneCard() throws Exception {
		Card testCard = new ObjectMapper().readValue(getClass().getResourceAsStream("/shivandragon.json"), Card.class);
		testCard.count = 1;
		uut.cardbase.addCard(testCard);
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("glance");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("1    Shivan Dragon (M15, 281)\nTotal: 1" + EOL, testOutput.toString());
	}
	
	@Test
	public void glanceIsPrintedWithZeroCards() throws Exception {
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("glance");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Total: 0" + EOL, testOutput.toString());
	}
	
	@Test
	public void glanceIsPrintedWithManyCards() throws Exception {
		uut = new CardbaseCLI(getClass().getResource("/testbase.cb").getFile());
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("glance");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("1    Reverberate (M12, 152)\n"
				+ "1    Mighty Leap (M12, 26)\n"
				+ "8    Formless Nurturing (FRF, 129)\n"
				+ "1    Feral Krushok (FRF, 128)\n"
				+ "1    Destructor Dragon (FRF, 127)\n"
				+ "2    Siege Mastodon (M12, 34)\n"
				+ "Total: 14" + EOL, testOutput.toString());
	}
	
	/***********************************************************************************
	 * peruse() tests, happy path
	 ***********************************************************************************/
	@Test
	public void perusalIsPrintedWithOneCard() throws Exception {
		Card testCard = new ObjectMapper().readValue(getClass().getResourceAsStream("/shivandragon.json"), Card.class);
		testCard.count = 1;
		uut.cardbase.addCard(testCard);
				
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse");
		} finally {
			System.setOut(console);
		}
		
		try (Scanner scanner = new Scanner(getClass().getResourceAsStream("singleCardPerusal"))) {
			assertEquals(scanner.useDelimiter("\\Z").next() + EOL, testOutput.toString());
		}
	}
	
	@Test
	public void perusalIsPrintedWithZeroCards() throws Exception {
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Total: 0" + EOL, testOutput.toString());
	}
	
	@Test
	public void perusalIsPrintedWithManyCards() throws Exception {
		uut = new CardbaseCLI(getClass().getResource("/testbase.cb").getFile());
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse");
		} finally {
			System.setOut(console);
		}
		
		try (Scanner scanner = new Scanner(getClass().getResourceAsStream("multipleCardsPerusal"))) {
			assertEquals(scanner.useDelimiter("\\Z").next() + EOL, testOutput.toString());
		}
	}
	
	@Test
	public void specificPerusalWithValidArgumentIsPrinted() throws Exception {
		uut = new CardbaseCLI(getClass().getResource("/testbase.cb").getFile());
		// dummy set just so the uut knows the set to peruse from
		FullCardSet fcs = new FullCardSet();
		fcs.code = "FRF";
		uut.selectedSet = fcs;
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse 129");
		} finally {
			System.setOut(console);
		}
		
		try (Scanner scanner = new Scanner(getClass().getResourceAsStream("specificCardPerusal"))) {
			assertEquals(scanner.useDelimiter("\\Z").next() + EOL, testOutput.toString());
		}
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void specificPerusalWithInvalidArgument() throws Exception {
		uut = new CardbaseCLI(getClass().getResource("/testbase.cb").getFile());
		// dummy set just so the uut knows the set to peruse from
		FullCardSet fcs = new FullCardSet();
		fcs.code = "FRF";
		uut.selectedSet = fcs;
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse 100");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Card not in cardbase." + EOL, testOutput.toString());
	}
	
	@Test
	public void specificPerusalWithNoSelectedSet() throws Exception {
		uut = new CardbaseCLI(getClass().getResource("/testbase.cb").getFile());
		uut.selectedSet = null;
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse 100");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Please select a set before perusing a specific card." + EOL, testOutput.toString());
	}
	
	/***********************************************************************************
	 * remove() tests, happy path
	 ***********************************************************************************/
	@Test
	public void removeValidAmountOfExistingCard() throws Exception {
		uut = new CardbaseCLI(getClass().getResource("/testbase.cb").getFile());
		// dummy set just so the uut knows the set to remove from
		FullCardSet fcs = new FullCardSet();
		fcs.code = "FRF";
		uut.selectedSet = fcs;
		
		uut.interpretInput("remove 129 3");
		
		assertEquals("Wrong number of cards was removed.", uut.cardbase.getCard("FRF", "129").count, new Integer(6));		
	}
	
	@Test
	public void removeExceedingAmountOfExistingCard() throws Exception {
		uut = new CardbaseCLI(getClass().getResource("/testbase.cb").getFile());
		// dummy set just so the uut knows the set to remove from
		FullCardSet fcs = new FullCardSet();
		fcs.code = "FRF";
		uut.selectedSet = fcs;
		
		uut.interpretInput("remove 128 3");
		
		assertNull("Card was not removed successfully.", uut.cardbase.getCard("FRF", "128"));
	}
	
	@Test
	public void removeExactAmountOfExistingCard() throws Exception {
		uut = new CardbaseCLI(getClass().getResource("/testbase.cb").getFile());
		// dummy set just so the uut knows the set to remove from
		FullCardSet fcs = new FullCardSet();
		fcs.code = "FRF";
		uut.selectedSet = fcs;
		
		uut.interpretInput("remove 128 1");
		
		assertNull("Card was not removed successfully.", uut.cardbase.getCard("FRF", "128"));
	}
	
	@Test
	public void removeSingleExistingCardWithoutAmount() throws Exception {
		uut = new CardbaseCLI(getClass().getResource("/testbase.cb").getFile());
		// dummy set just so the uut knows the set to remove from
		FullCardSet fcs = new FullCardSet();
		fcs.code = "FRF";
		uut.selectedSet = fcs;
		
		uut.interpretInput("remove 128");
		
		assertNull("Card was not removed successfully.", uut.cardbase.getCard("FRF", "128"));
	}
	
	@Test
	public void removeMultipleExistingCardWithoutAmount() throws Exception {
		uut = new CardbaseCLI(getClass().getResource("/testbase.cb").getFile());
		// dummy set just so the uut knows the set to remove from
		FullCardSet fcs = new FullCardSet();
		fcs.code = "FRF";
		uut.selectedSet = fcs;
		
		uut.interpretInput("remove 129");
		
		assertNull("Card was not removed successfully.", uut.cardbase.getCard("FRF", "129"));
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void removeNonExistentCardWithoutAmount() throws Exception {
		// dummy set just so the uut knows the set to remove from
		FullCardSet fcs = new FullCardSet();
		fcs.code = "FRF";
		uut.selectedSet = fcs;
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("remove 128");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Card FRF 128 is not in the cardbase." + EOL, testOutput.toString());
	}
	
	@Test
	public void removeNonExistentCardWithAmount() throws Exception {
		// dummy set just so the uut knows the set to remove from
		FullCardSet fcs = new FullCardSet();
		fcs.code = "FRF";
		uut.selectedSet = fcs;
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("remove 128 2");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Card FRF 128 is not in the cardbase." + EOL, testOutput.toString());
	}
	
	@Test
	public void removeZeroOfExistingCard() throws Exception {
		uut = new CardbaseCLI(getClass().getResource("/testbase.cb").getFile());
		// dummy set just so the uut knows the set to remove from
		FullCardSet fcs = new FullCardSet();
		fcs.code = "FRF";
		uut.selectedSet = fcs;
		
		uut.interpretInput("remove 129 0");
		
		assertEquals("Card amount should not have changed.", uut.cardbase.getCard("FRF", "129").count, new Integer(8));
	}
	
	@Test
	public void removeNegativeAmountOfExistingCard() throws Exception {
		uut = new CardbaseCLI(getClass().getResource("/testbase.cb").getFile());
		// dummy set just so the uut knows the set to remove from
		FullCardSet fcs = new FullCardSet();
		fcs.code = "FRF";
		uut.selectedSet = fcs;

		uut.interpretInput("remove 129 -10");
		
		assertEquals("Card amount should not have changed.", uut.cardbase.getCard("FRF", "129").count, new Integer(8));
	}
	
	@Test
	public void removeWithNoSetSelected() throws Exception {
		uut = new CardbaseCLI(getClass().getResource("/testbase.cb").getFile());
		uut.selectedSet = null;
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("remove 100");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Please select a set before removing cards." + EOL, testOutput.toString());
	}
	
	/***********************************************************************************
	 * add() tests, happy path
	 ***********************************************************************************/
	// add
	
	
	/*
	 * Edge cases
	 */
	@Test
	public void invalidCommandWithNoSelectedSet() throws Exception {
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("cOmMand5 argumEnt1 argument2");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Please select a set before adding cards." + EOL, testOutput.toString());
	}
	
	@Test
	public void invalidCommandWithSelectedSet() throws Exception {
		// dummy set just so the uut knows the set to remove from
		FullCardSet fcs = new FullCardSet();
		fcs.name = "Fate Reforged";
		fcs.cards = new HashMap<String, Card>();
		uut.selectedSet = fcs;
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("cOmMand5 argumEnt1 argument2");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("cOmMand5 does not correspond to a card in Fate Reforged." + EOL, testOutput.toString());
	}
	
//	@Test
//	public void blankInput() throws Exception {
//		String[] processedInput = uut.sanitiseInput("");
//		
//		assertEquals("Wrong array length.", 1, processedInput.length);
//		assertEquals("", processedInput[0]);
//	}
//	
//	@Test
//	public void onlyWhiteSpace() throws Exception {
//		String[] processedInput = uut.sanitiseInput("  		  	");
//		
//		assertEquals("Wrong array length.", 1, processedInput.length);
//		assertEquals("", processedInput[0]);
//	}
//	
//	@Test
//	public void leadingTrailingAndIntermediaryWhiteSpace() throws Exception {
//		String[] processedInput = uut.sanitiseInput("  \t  this   \twas \t  \t  a triumph  \t\t    ");
//		
//		assertEquals("Wrong array length.", 4, processedInput.length);
//		assertEquals("this", processedInput[0]);
//		assertEquals("was", processedInput[1]);
//		assertEquals("a", processedInput[2]);
//		assertEquals("triumph", processedInput[3]);
//	}
	
	/***********************************************************************************
	 * undo() tests, happy path
	 ***********************************************************************************/	
}




























