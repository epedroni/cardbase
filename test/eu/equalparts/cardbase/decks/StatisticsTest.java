package eu.equalparts.cardbase.decks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.equalparts.cardbase.decks.StandaloneDeck;
import eu.equalparts.cardbase.decks.Statistics;

public class StatisticsTest {

	private static StandaloneDeck uut;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		uut = mapper.readValue(StatisticsTest.class.getResourceAsStream("deck.cbd"), StandaloneDeck.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_totalLandCountIsComputedCorrectly() throws Exception {
		int count = Statistics.count(uut, "Land");
		
		assertEquals(23, count);
	}
	
	@Test
	public void test_basicLandCountIsComputedCorrectly() throws Exception {
		int count = Statistics.count(uut, "Basic Land");
		
		assertEquals(20, count);
	}
	
	@Test
	public void test_cardCountIsComputedCorrectly() throws Exception {
		int count = Statistics.count(uut);
		
		assertEquals(60, count);
	}
	
	@Test
	public void test_landPercentageIsComputedCorrectly() throws Exception {
		double percentage = Statistics.calculatePercentage(uut, "Land");
		
		assertTrue("Land percentage should be " + (23.0 / 60.0) + ", is " + percentage, percentage == (23.0 / 60.0));
	}
	
	@Test
	public void test_creaturePercentageIsComputedCorrectly() throws Exception {
		double percentage = Statistics.calculatePercentage(uut, "Creature");
		
		assertTrue("Creature percentage should be " + (24.0 / 60.0) + ", is " + percentage, percentage == (24.0 / 60.0));
	}
	
	@Test
	public void test_creatureCountIsComputedCorrectly() throws Exception {
		int count = Statistics.count(uut, "Creature");
		
		assertEquals(24, count);
	}
	
	@Test
	public void test_sorceryCountIsComputedCorrectly() throws Exception {
		int count = Statistics.count(uut, "Sorcery");
		
		assertEquals(1, count);
	}
	
	@Test
	public void test_instantCountIsComputedCorrectly() throws Exception {
		int count = Statistics.count(uut, "Instant");
		
		assertEquals(6, count);
	}
	
	@Test
	public void test_planeswalkerCountIsComputedCorrectly() throws Exception {
		int count = Statistics.count(uut, "Planeswalker");
		
		assertEquals(0, count);
	}
	
	@Test
	public void test_elfCountIsComputedCorrectly() throws Exception {
		int count = Statistics.count(uut, "Elf");
		
		assertEquals(2, count);
	}
	
	@Test
	public void test_overallCostDistributionIsComputedCorrectly() throws Exception {
		int[] actualCosts = Statistics.computeDistribution(uut);
		int[] expectedCosts = {0, 8, 11, 3, 4, 7, 4};
		
		assertEquals("Array lengths do not match.", expectedCosts.length, actualCosts.length);
		for (int i = 0; i < expectedCosts.length; i++) {
			assertEquals("CMC: " + i, expectedCosts[i], actualCosts[i]);
		}
	}
	
	@Test
	public void test_creatureCostDistributionIsComputedCorrectly() throws Exception {
		int[] actualCosts = Statistics.computeDistribution(uut, "Creature");
		int[] expectedCosts = {0, 3, 6, 2, 2, 7, 4};
		
		assertEquals("Array lengths do not match.", expectedCosts.length, actualCosts.length);
		for (int i = 0; i < expectedCosts.length; i++) {
			assertEquals("CMC: " + i, expectedCosts[i], actualCosts[i]);
		}
	}
	
	@Test
	public void test_instantCostDistributionIsComputedCorrectly() throws Exception {
		int[] actualCosts = Statistics.computeDistribution(uut, "Instant");
		int[] expectedCosts = {0, 2, 4};
		
		assertEquals("Array lengths do not match.", expectedCosts.length, actualCosts.length);
		for (int i = 0; i < expectedCosts.length; i++) {
			assertEquals("CMC: " + i, expectedCosts[i], actualCosts[i]);
		}
	}
	
	@Test
	public void test_planeswalkerCostDistributionIsComputedCorrectly() throws Exception {
		int[] actualCosts = Statistics.computeDistribution(uut, "Planeswalker");
		int[] expectedCosts = {};
		
		assertEquals("Array lengths do not match.", expectedCosts.length, actualCosts.length);
		for (int i = 0; i < expectedCosts.length; i++) {
			assertEquals("CMC: " + i, expectedCosts[i], actualCosts[i]);
		}
	}
	
}