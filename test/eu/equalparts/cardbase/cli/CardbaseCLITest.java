package eu.equalparts.cardbase.cli;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
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
	 * User input tests, happy path
	 ***********************************************************************************/
	@Test
	public void commandAndArgumentsSeparatedBySpaces() throws Exception {
		String[] processedInput = uut.sanitiseInput("cOmMand5 argumEnt1 argument2");
		
		assertEquals("Wrong array length.", 3, processedInput.length);
		assertEquals("cOmMand5", processedInput[0]);
		assertEquals("argumEnt1", processedInput[1]);
		assertEquals("argument2", processedInput[2]);
	}
	
	@Test
	public void commandAndNoArguments() throws Exception {
		String[] processedInput = uut.sanitiseInput("Someth1ng");
		
		assertEquals("Wrong array length.", 1, processedInput.length);
		assertEquals("Someth1ng", processedInput[0]);
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void blankInput() throws Exception {
		String[] processedInput = uut.sanitiseInput("");
		
		assertEquals("Wrong array length.", 1, processedInput.length);
		assertEquals("", processedInput[0]);
	}
	
	@Test
	public void onlyWhiteSpace() throws Exception {
		String[] processedInput = uut.sanitiseInput("  		  	");
		
		assertEquals("Wrong array length.", 1, processedInput.length);
		assertEquals("", processedInput[0]);
	}
	
	@Test
	public void leadingTrailingAndIntermediaryWhiteSpace() throws Exception {
		String[] processedInput = uut.sanitiseInput("  \t  this   \twas \t  \t  a triumph  \t\t    ");
		
		assertEquals("Wrong array length.", 4, processedInput.length);
		assertEquals("this", processedInput[0]);
		assertEquals("was", processedInput[1]);
		assertEquals("a", processedInput[2]);
		assertEquals("triumph", processedInput[3]);
	}
	
	/***********************************************************************************
	 * File name sanity tests, happy path
	 ***********************************************************************************/
	@Test
	public void reasonableFileNameWithoutExtension() throws Exception {
		String processedName = uut.sanitiseFileName("f1lename");
		
		assertEquals("f1lename.cb", processedName);
	}
	
	@Test
	public void reasonableFileNameWithExtension() throws Exception {
		String processedName = uut.sanitiseFileName("f1lename.cb");
		
		assertEquals("f1lename.cb", processedName);
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void nameWithBrokenExtension() throws Exception {
		String processedName = uut.sanitiseFileName("f1lename.c b");
		
		assertEquals("f1lename.cb", processedName);
	}
	
	@Test
	public void nameWithWrongExtension() throws Exception {
		String processedName = uut.sanitiseFileName("f1lename.tar");
		
		assertEquals("f1lename.tar.cb", processedName);
	}
	
	@Test
	public void nameWithIllegalCharacters() throws Exception {
		String processedName = uut.sanitiseFileName("f1lEnämẽ\n\t\"--._-//?   \t^|#ŧ@fhw9vLL'''");
		
		assertEquals("f1lEnm--._-//fhw9vLL.cb", processedName);
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
				uut.help();
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
				uut.write(testFile.getAbsolutePath());
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
			uut.write(testFile.getAbsolutePath());
			String cardJSON = scanner.useDelimiter("\\Z").next();
			Card testCard = new ObjectMapper().readValue(cardJSON, Card.class);
			testCard.count = 1;
			uut.cardbase.addCard(testCard);

			try {
				System.setOut(new PrintStream(testOutput));
				uut.write();
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
			uut.write();
		} finally {
			System.setOut(console);
		}
		assertEquals("Please provide a file name." + EOL, testOutput.toString());
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void pathProvidedIsDirectory() throws Exception {
		File directory = tempFolder.newFolder("testdirectory.cb");
		try {
			System.setOut(new PrintStream(testOutput));
			uut.write(directory.getAbsolutePath());
		} finally {
			System.setOut(console);
		}
		assertEquals("Could not write to \"" + directory.getAbsolutePath() + "\"." + EOL, testOutput.toString());
	}
	
	/***********************************************************************************
	 * version() tests, happy path
	 ***********************************************************************************/
	@Test
	public void correctVersionIsPrinted() throws Exception {
		try {
			System.setOut(new PrintStream(testOutput));
			uut.version();
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
		uut.exit();
		
		assertEquals("Incorrect state for exit flag.", true, uut.exit);
	}
	
	@Test
	public void saveReminderIsPrintedIfPromptFlagIsRaised() throws Exception {
		uut.savePrompt = true;
		try {
			System.setOut(new PrintStream(testOutput));
			uut.exit();
		} finally {
			System.setOut(console);
		}
		assertEquals("Don't forget to save. If you really wish to quit without saving, type \"exit\" again." + EOL, testOutput.toString());
		assertEquals("Incorrect state for exit flag.", false, uut.exit);
	}
	
	@Test
	public void exitFlagIsRaisedAfterSavePromptIsAcknowledged() throws Exception {
		uut.savePrompt = true;
		
		uut.exit();
		
		assertEquals("Incorrect state for exit flag.", false, uut.exit);
		assertEquals("Incorrect state for save flag.", false, uut.savePrompt);

		uut.exit();
		
		assertEquals("Incorrect state for exit flag.", true, uut.exit);
	}
	
	/***********************************************************************************
	 * sets() tests, happy path
	 ***********************************************************************************/
	// mock MTG universe
	
	/***********************************************************************************
	 * set() tests, happy path
	 ***********************************************************************************/
	// mock MTG universe
	
	/***********************************************************************************
	 * glance() tests, happy path
	 ***********************************************************************************/
	@Test
	public void glanceIsPrinted() throws Exception {
		uut.savePrompt = true;
		
		uut.exit();
		
		assertEquals("Incorrect state for exit flag.", false, uut.exit);
		assertEquals("Incorrect state for save flag.", false, uut.savePrompt);

		uut.exit();
		
		assertEquals("Incorrect state for exit flag.", true, uut.exit);
	}
	
	/***********************************************************************************
	 * peruse() tests, happy path
	 ***********************************************************************************/
	
	/***********************************************************************************
	 * undo() tests, happy path
	 ***********************************************************************************/
	
	/***********************************************************************************
	 * remove() tests, happy path
	 ***********************************************************************************/
	
	/***********************************************************************************
	 * add() tests, happy path
	 ***********************************************************************************/
	
}




























