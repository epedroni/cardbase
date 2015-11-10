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

/**
 * These tests do some unusual things, but I think they are reasonable.
 * 
 * @author Eduardo Pedroni
 *
 */
public class CardbaseCLITest {

	private CardbaseCLI uut;
	
	private ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
	private final PrintStream console = System.out;
	private final String EOL = System.getProperty("line.separator");
	
	/**
	 * The test remote URL to "query" from, this is actually the same directory
	 * as the test class. Test files need to be placed accordingly.
	 */
	private final String TEST_REMOTE = getClass().getResource("").toString();
	
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
		uut = new CardbaseCLI(TEST_REMOTE);
		testOutput.reset();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/***********************************************************************************
	 * Start up tests, happy path
	 ***********************************************************************************/
	@Test
	public void welcomeMessageWithoutCardbaseFile() throws Exception {
		try {
			System.setOut(new PrintStream(testOutput));
			uut = new CardbaseCLI(TEST_REMOTE);
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Welcome to Cardbase CLI!\nNo cardbase file was provided, creating a clean cardbase." + EOL, testOutput.toString());
	}
	
	@Test
	public void glanceWithoutCardbaseFile() throws Exception {
		uut = new CardbaseCLI(TEST_REMOTE);
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("glance");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Total: 0" + EOL, testOutput.toString());
	}
	
	@Test
	public void welcomeMessageWithCardbaseFile() throws Exception {
		try {
			System.setOut(new PrintStream(testOutput));
			uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Welcome to Cardbase CLI!\nLoading cardbase from \"" + getClass().getResource("/testbase.cb").getFile() + "\"." + EOL, testOutput.toString());
	}
	
	@Test
	public void glanceWithCardbaseFile() throws Exception {
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("glance");
		} finally {
			System.setOut(console);
		}
		
		assertEquals(getFileContents("multipleCardsGlance") + EOL, testOutput.toString());
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void welcomeMessageWithEmptyCardbaseFileName() throws Exception {
		try {
			System.setOut(new PrintStream(testOutput));
			uut = new CardbaseCLI(TEST_REMOTE, "");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Welcome to Cardbase CLI!\nNo cardbase file was provided, creating a clean cardbase." + EOL, testOutput.toString());
	}
	
	@Test
	public void startWithInvalidArguments() throws Exception {
		File notAFile = tempFolder.newFile();
		tempFolder.delete();

		exception.expect(IllegalArgumentException.class);
		uut = new CardbaseCLI(TEST_REMOTE, notAFile.getAbsolutePath());
	}
	
	// TODO test more invalid file scenarios
	
	/***********************************************************************************
	 * help() tests, happy path
	 ***********************************************************************************/
	@Test
	public void helpInformationIsPrinted() throws Exception {
			String help = getFileContents("/help_en");
			
			try {
				System.setOut(new PrintStream(testOutput));
				uut.interpretInput("help");
			} finally {
				System.setOut(console);
			}
			
			assertEquals(help + EOL, testOutput.toString());
	}
	
	/***********************************************************************************
	 * write() tests, happy path
	 ***********************************************************************************/
	@Test
	public void reloadedCardbaseMatchesOriginal() throws Exception {
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());
		File testFile = tempFolder.newFile("saveTest.cb");
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("write " + testFile.getAbsolutePath());
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Cardbase was saved to \"" + testFile.getAbsolutePath() + "\". "
				+ "Subsequent writes will be done to this same file unless otherwise requested." + EOL, testOutput.toString());
		
		testOutput.reset();
		uut = new CardbaseCLI(TEST_REMOTE, testFile.getAbsolutePath());
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse");
		} finally {
			System.setOut(console);
		}
		
		assertEquals(getFileContents("multipleCardsPerusal") + EOL, testOutput.toString());
	}
	
	@Test
	public void specifiedFileIsSubsequentlyUsedByDefault() throws Exception {
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());
		File testFile = tempFolder.newFile("saveTest.cb");
		
		uut.interpretInput("write " + testFile.getAbsolutePath());
		
		uut.interpretInput("set FRF");
		uut.interpretInput("remove 128");
		uut.interpretInput("100");
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse");
		} finally {
			System.setOut(console);
		}
		String expectedPerusal = testOutput.toString();
		
		testOutput.reset();
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("write");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Cardbase was saved to \"" + testFile.getAbsolutePath() + "\". "
				+ "Subsequent writes will be done to this same file unless otherwise requested." + EOL, testOutput.toString());
		
		uut = new CardbaseCLI(TEST_REMOTE, testFile.getAbsolutePath());
		testOutput.reset();
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse");
		} finally {
			System.setOut(console);
		}
		
		assertEquals(expectedPerusal, testOutput.toString());
		assertNotEquals(getFileContents("multipleCardsPerusal") + EOL, testOutput.toString());
	}

	@Test
	public void userIsPromptedForFileNameIfNoDefaultIsPresent() throws Exception {
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("write");
		} finally {
			System.setOut(console);
		}
		assertEquals("Please provide a file name." + EOL, testOutput.toString());
	}
	
	@Test
	public void fileExtensionIsAddedIfMissing() throws Exception {
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());

		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("write " + tempFolder.getRoot().getAbsolutePath() + "/testSave");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Cardbase was saved to \"" + tempFolder.getRoot().getAbsolutePath() + "/testSave.cb" + "\". "
				+ "Subsequent writes will be done to this same file unless otherwise requested." + EOL, testOutput.toString());
		assertFalse("File without extension exists.", new File(tempFolder.getRoot().getAbsolutePath() + "/testSave").exists());
		assertTrue("File with extension does not exist.", new File(tempFolder.getRoot().getAbsolutePath() + "/testSave.cb").exists());
		
		testOutput.reset();
		uut = new CardbaseCLI(TEST_REMOTE, tempFolder.getRoot().getAbsolutePath() + "/testSave.cb");
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse");
		} finally {
			System.setOut(console);
		}
		
		assertEquals(getFileContents("multipleCardsPerusal") + EOL, testOutput.toString());
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void writeFailsIfProvidedPathIsDirectory() throws Exception {
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
	public void extensionIsAddedEvenIfFileNameAlreadyHasOne() throws Exception {
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());

		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("write " + tempFolder.getRoot().getAbsolutePath() + "/testSave.tar");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Cardbase was saved to \"" + tempFolder.getRoot().getAbsolutePath() + "/testSave.tar.cb" + "\". "
				+ "Subsequent writes will be done to this same file unless otherwise requested." + EOL, testOutput.toString());
		assertFalse("File with incorrect extension exists.", new File(tempFolder.getRoot().getAbsolutePath() + "/testSave.tar").exists());
		assertTrue("File with correct extension does not exist.", new File(tempFolder.getRoot().getAbsolutePath() + "/testSave.tar.cb").exists());
		
		testOutput.reset();
		uut = new CardbaseCLI(TEST_REMOTE, tempFolder.getRoot().getAbsolutePath() + "/testSave.tar.cb");
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse");
		} finally {
			System.setOut(console);
		}
		
		assertEquals(getFileContents("multipleCardsPerusal") + EOL, testOutput.toString());
	}
	
	@Test
	public void illegalCharactersAreRemovedFromFileName() throws Exception {
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());

		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("write " + tempFolder.getRoot().getAbsolutePath() + "/f1lEnämẽ\"--._-");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Cardbase was saved to \"" + tempFolder.getRoot().getAbsolutePath() + "/f1lEnm--._-.cb" + "\". "
				+ "Subsequent writes will be done to this same file unless otherwise requested." + EOL, testOutput.toString());
		assertTrue("File with correct name does not exist.", new File(tempFolder.getRoot().getAbsolutePath() + "/f1lEnm--._-.cb").exists());
		
		testOutput.reset();
		uut = new CardbaseCLI(TEST_REMOTE, tempFolder.getRoot().getAbsolutePath() + "/f1lEnm--._-.cb");
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse");
		} finally {
			System.setOut(console);
		}
		
		assertEquals(getFileContents("multipleCardsPerusal") + EOL, testOutput.toString());
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
		assertTrue("Incorrect version information was printed.", testOutput.toString().matches("CardbaseCLI v[0-9]+\\.[0-9]+" + EOL));
	}
	
	/***********************************************************************************
	 * exit() tests, happy path
	 ***********************************************************************************/
	// try to exit without changing anything, expect no warning
	@Test
	public void exitWithoutChanges() throws Exception {
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("exit");
		} finally {
			System.setOut(console);
		}
		assertEquals("", testOutput.toString());
	}
	
	// try to exit after adding something, expect a warning, try to exit again, expect no warning
	@Test
	public void exitAfterAddingSomething() throws Exception {
		uut.interpretInput("set FRF");
		uut.interpretInput("100");
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("exit");
		} finally {
			System.setOut(console);
		}
		assertEquals("Don't forget to save. If you really wish to quit without saving, type \"exit\" again." + EOL, testOutput.toString());
		
		testOutput.reset();
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("exit");
		} finally {
			System.setOut(console);
		}
		assertEquals("", testOutput.toString());
	}
	
	// try to exit after removing something, expect a warning, try to exit again, expect no warning
	@Test
	public void exitAfterRemovingSomething() throws Exception {
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());
		uut.interpretInput("set FRF");
		uut.interpretInput("remove 128");
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("exit");
		} finally {
			System.setOut(console);
		}
		assertEquals("Don't forget to save. If you really wish to quit without saving, type \"exit\" again." + EOL, testOutput.toString());
		
		testOutput.reset();
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("exit");
		} finally {
			System.setOut(console);
		}
		assertEquals("", testOutput.toString());
	}
	
	// try to exit after adding something, expect a warning, write, try to exit again, expect no warning
	@Test
	public void exitAfterAddingSomethingAndBeforeWriting() throws Exception {
		uut.interpretInput("set FRF");
		uut.interpretInput("100");
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("exit");
		} finally {
			System.setOut(console);
		}
		assertEquals("Don't forget to save. If you really wish to quit without saving, type \"exit\" again." + EOL, testOutput.toString());
		
		testOutput.reset();
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("exit");
		} finally {
			System.setOut(console);
		}
		assertEquals("", testOutput.toString());
	}
	
	// try to exit after removing something, expect a warning, write, try to exit again, expect no warning
	@Test
	public void exitAfterRemovingSomethingAndBeforeWriting() throws Exception {
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());
		uut.interpretInput("set FRF");
		uut.interpretInput("remove 128");
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("exit");
		} finally {
			System.setOut(console);
		}
		assertEquals("Don't forget to save. If you really wish to quit without saving, type \"exit\" again." + EOL, testOutput.toString());
		
		testOutput.reset();
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("exit");
		} finally {
			System.setOut(console);
		}
		assertEquals("", testOutput.toString());
	}
	
	// add something, write, try to exit and expect no warning
	@Test
	public void exitAfterAddingSomethingAndWriting() throws Exception {
		uut.interpretInput("set FRF");
		uut.interpretInput("100");
		uut.interpretInput("write " + tempFolder.newFile().getAbsolutePath());
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("exit");
		} finally {
			System.setOut(console);
		}
		assertEquals("", testOutput.toString());
	}
	
	// remove something, write, try to exit and expect no warning
	@Test
	public void exitAfterRemovingSomethingAndWriting() throws Exception {
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());
		uut.interpretInput("set FRF");
		uut.interpretInput("remove 128");
		uut.interpretInput("write " + tempFolder.newFile().getAbsolutePath());
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("exit");
		} finally {
			System.setOut(console);
		}
		assertEquals("", testOutput.toString());
	}
	
	/***********************************************************************************
	 * sets() tests, happy path
	 ***********************************************************************************/
	@Test
	public void correctSetListIsPrinted() throws Exception {
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("sets");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("LEA          : Limited Edition Alpha\n"
				+ "LEB          : Limited Edition Beta\n"
				+ "ARN          : Arabian Nights\n"
				+ "M15          : Magic 2015 Core Set\n"
				+ "FRF          : Fate Reforged" + EOL, testOutput.toString());
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void fallbackListIsPrintedIfListCannotBeFound() throws Exception {
		File noListLocation = tempFolder.newFolder();
		tempFolder.delete();
		uut = new CardbaseCLI(noListLocation.getAbsolutePath());
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("sets");
		} finally {
			System.setOut(console);
		}
		
		assertEquals(getFileContents("expectedFallbackList") + EOL, testOutput.toString());
	}
	
	/***********************************************************************************
	 * set() tests, happy path
	 ***********************************************************************************/
	@Test
	public void correctSetIsSelected() throws Exception {
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("set M15");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Selected set: Magic 2015 Core Set." + EOL, testOutput.toString());
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void invalidSetIsProvided() throws Exception {		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("set not_a_set");
		} finally {
			System.setOut(console);
		}

		assertEquals("\"not_a_set\" does not correspond to any set (use \"sets\" to see all valid set codes)." 
				+ EOL, testOutput.toString());
	}
	
	/***********************************************************************************
	 * glance() tests, happy path
	 ***********************************************************************************/
	@Test
	public void glanceIsPrintedWithOneCard() throws Exception {
		uut.interpretInput("set M15");
		uut.interpretInput("281");
		
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
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("glance");
		} finally {
			System.setOut(console);
		}
		
		assertEquals(getFileContents("multipleCardsGlance") + EOL, testOutput.toString());
	}
	
	/***********************************************************************************
	 * peruse() tests, happy path
	 ***********************************************************************************/
	@Test
	public void perusalIsPrintedWithOneCard() throws Exception {
		uut.interpretInput("set M15");
		uut.interpretInput("281");
				
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse");
		} finally {
			System.setOut(console);
		}
		
		assertEquals(getFileContents("singleCardPerusal") + EOL, testOutput.toString());
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
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse");
		} finally {
			System.setOut(console);
		}
		
		assertEquals(getFileContents("multipleCardsPerusal") + EOL, testOutput.toString());
	}
	
	@Test
	public void specificPerusalWithValidArgumentIsPrinted() throws Exception {
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());
		uut.interpretInput("set FRF");
		
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse 129");
		} finally {
			System.setOut(console);
		}
		
		assertEquals(getFileContents("specificCardPerusal") + EOL, testOutput.toString());
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void specificPerusalWithInvalidArgument() throws Exception {
		uut.interpretInput("set FRF");
		
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
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());
		uut.interpretInput("set FRF");
		
		uut.interpretInput("remove 129 3");
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse 129");
		} finally {
			System.setOut(console);
		}
		
		assertTrue("Perusal indicates wrong number of cards: " + testOutput.toString(), testOutput.toString().startsWith("5    Formless Nurturing (FRF, 129)\n"));
	}
	
	@Test
	public void removeExceedingAmountOfExistingCard() throws Exception {
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());
		uut.interpretInput("set FRF");
		
		uut.interpretInput("remove 128 3");
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse 128");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Card not in cardbase." + EOL, testOutput.toString());
	}
	
	@Test
	public void removeExactAmountOfExistingCard() throws Exception {
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());
		uut.interpretInput("set FRF");
		
		uut.interpretInput("remove 128 1");
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse 128");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Card not in cardbase." + EOL, testOutput.toString());
	}
	
	@Test
	public void removeSingleExistingCardWithoutAmount() throws Exception {
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());
		uut.interpretInput("set FRF");
		
		uut.interpretInput("remove 128");
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse 128");
		} finally {
			System.setOut(console);
		}
		
		assertEquals("Card not in cardbase." + EOL, testOutput.toString());
	}
	
	@Test
	public void removeMultipleExistingCardsWithoutAmount() throws Exception {
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());
		uut.interpretInput("set FRF");
		
		uut.interpretInput("remove 129");
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse 129");
		} finally {
			System.setOut(console);
		}
		
		assertTrue("Perusal indicates wrong number of cards: " + testOutput.toString(), testOutput.toString().startsWith("7    Formless Nurturing (FRF, 129)\n"));
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void removeNonExistentCardWithoutAmount() throws Exception {
		uut.interpretInput("set FRF");
		
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
		uut.interpretInput("set FRF");
		
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
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());
		uut.interpretInput("set FRF");
		
		uut.interpretInput("remove 129 0");
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse 129");
		} finally {
			System.setOut(console);
		}
		
		assertTrue("Card amount should not have changed: " + testOutput.toString(), testOutput.toString().startsWith("8    Formless Nurturing (FRF, 129)\n"));
	}
	
	@Test
	public void removeNegativeAmountOfExistingCard() throws Exception {
		uut = new CardbaseCLI(TEST_REMOTE, getClass().getResource("/testbase.cb").getFile());
		uut.interpretInput("set FRF");
		
		uut.interpretInput("remove 129 -10");
		try {
			System.setOut(new PrintStream(testOutput));
			uut.interpretInput("peruse 129");
		} finally {
			System.setOut(console);
		}
		
		assertTrue("Card amount should not have changed: " + testOutput.toString(), testOutput.toString().startsWith("8    Formless Nurturing (FRF, 129)\n"));
	}
	
	@Test
	public void removeWithNoSetSelected() throws Exception {
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
		uut.interpretInput("set FRF");
		
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
	
	private String getFileContents(String fileName) {
		try (Scanner scanner = new Scanner(getClass().getResourceAsStream(fileName))) {
			return scanner.useDelimiter("\\Z").next();
		}
	}
}




























