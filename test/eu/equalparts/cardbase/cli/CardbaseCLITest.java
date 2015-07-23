package eu.equalparts.cardbase.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.equalparts.cardbase.cli.CardbaseCLI;

public class CardbaseCLITest {

	private CardbaseCLI uut;
	private static StringBuilder output = new StringBuilder();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.setOut(new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				output.append((char) b);
			}
		}, true));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/***********************************************************************************
	 * Constructor tests, happy path
	 ***********************************************************************************/
	@Test
	public void initialiseWithCardbaseFile() throws Exception {
		uut = new CardbaseCLI(getClass().getResource("testbase.cb").getPath());
		
		
		
	}

}
