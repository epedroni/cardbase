package eu.equalparts.cardbase.cli;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.equalparts.cardbase.cards.Card;
import eu.equalparts.cardbase.testutils.TestFile;
import eu.equalparts.cardbase.testutils.TestUtils;

public class CardbaseCLITest {

	private CardbaseCLI uut;
	private static StringBuilder output = new StringBuilder();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		System.setOut(new PrintStream(new OutputStream() {
//			@Override
//			public void write(int b) throws IOException {
//				output.append((char) b);
//			}
//		}, true));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		uut = new CardbaseCLI();
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
		String[] processedInput = uut.sanitiseInput(" 	  	this    was    	 a 	 triumph	 	         ");
		
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
		
		assertEquals("f1lEnm--._-fhw9vLL.cb", processedName);
	}
	
	/***********************************************************************************
	 * Write method tests, happy path
	 ***********************************************************************************/
	@Test
	public void writeCardbaseToSpecifiedFile() throws Exception {
		try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/shivandragon.json"));
				TestFile testFile = TestUtils.createValidTestFile("testsave.cb");
				Scanner scanner2 = new Scanner(testFile)) {
			
			String cardJSON = scanner.useDelimiter("\\Z").next();
			Card testCard = new ObjectMapper().readValue(cardJSON, Card.class);
			testCard.count = 1;
			uut.cardbase.addCard(testCard);
			
			uut.write(testFile.getAbsolutePath());
			String save = scanner2.useDelimiter("\\Z").next();
			assertTrue(save.contains(cardJSON));
				
		}
	}
}
