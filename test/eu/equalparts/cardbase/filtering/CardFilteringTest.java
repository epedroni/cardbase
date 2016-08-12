package eu.equalparts.cardbase.filtering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.core.type.TypeReference;

import eu.equalparts.cardbase.card.Card;
import eu.equalparts.cardbase.filtering.CardFiltering.Filter;
import eu.equalparts.cardbase.json.JSON;

public class CardFilteringTest {
	private static List<Card> allTestCards, testCards;

	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		allTestCards = JSON.mapper.readValue(CardFilteringTest.class.getResourceAsStream("/testcards.json"), new TypeReference<List<Card>>() {});
	}
	
	@Before
	public void setUp() {
		testCards = new LinkedList<>(allTestCards);
	}

	@Test
	public void filterByNameEquals() throws Exception {
		CardFiltering.filterByField(testCards, "name", Filter.EQUALS, "Callow jushi");
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Callow Jushi", testCards.get(0).name.get());
	}
	
	@Test
	public void filterByNameContains() throws Exception {
		CardFiltering.filterByField(testCards, "name", Filter.CONTAINS, "sh");
		
		assertEquals("Wrong list size.", 3, testCards.size());
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Shivan Dragon",
				"Disrupting Shoal",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}
	
	@Test
	public void filterByNameRegex() throws Exception {
		CardFiltering.filterByField(testCards, "name", Filter.REGEX, ".+?n");
		
		assertEquals("Wrong list size.", 2, testCards.size());
		int i = 0;
		String[] names = {
				"Coerced Confession",
				"Shivan Dragon",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}
	
	@Test
	public void filterByNameGreaterThan() throws Exception {
		CardFiltering.filterByField(testCards, "name", Filter.GREATER_THAN, "5");
		
		assertEquals("Wrong list size.", 8, testCards.size());
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Coerced Confession",
				"Khalni Hydra",
				"Nightmare",
				"Shivan Dragon",
				"Disrupting Shoal",
				"Sorin Markov",
				"Ugin's Construct",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}

	@Test
	public void filterByLayoutEquals() throws Exception {
		CardFiltering.filterByField(testCards, "layout", Filter.EQUALS, "flip");
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Callow Jushi", testCards.get(0).name.get());
	}
	
	@Test
	public void filterByLayoutContains() throws Exception {
		CardFiltering.filterByField(testCards, "layout", Filter.CONTAINS, "l");
		
		assertEquals("Wrong list size.", 8, testCards.size());
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Coerced Confession",
				"Khalni Hydra",
				"Nightmare",
				"Shivan Dragon",
				"Disrupting Shoal",
				"Sorin Markov",
				"Ugin's Construct",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}
	
	@Test
	public void filterByLayoutRegex() throws Exception {
		CardFiltering.filterByField(testCards, "layout", Filter.REGEX, "fl[a-z]p");
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Callow Jushi", testCards.get(0).name.get());
	}
	
	@Test
	public void filterByManaCostEquals() throws Exception {
		CardFiltering.filterByField(testCards, "manaCost", Filter.EQUALS, "{X}{U}{U}");
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Disrupting Shoal", testCards.get(0).name.get());
	}
	
	@Test
	public void filterByManaCostContains() throws Exception {
		CardFiltering.filterByField(testCards, "manaCost", Filter.CONTAINS, "B");
		
		assertEquals("Wrong list size.", 3, testCards.size());
		int i = 0;
		String[] names = {
				"Coerced Confession",
				"Nightmare",
				"Sorin Markov",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}
	
	@Test
	public void filterByManaCostRegex() throws Exception {
		CardFiltering.filterByField(testCards, "manaCost", Filter.REGEX, "(\\{G\\}){8}");
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Khalni Hydra", testCards.get(0).name.get());
	}
	
	@Test
	public void filterByCMCEquals() throws Exception {
		CardFiltering.filterByField(testCards, "cmc", Filter.EQUALS, "5");
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Coerced Confession", testCards.get(0).name.get());
	}
	
	@Test
	public void filterByCMCContains() throws Exception {
		CardFiltering.filterByField(testCards, "cmc", Filter.CONTAINS, "5");
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Coerced Confession", testCards.get(0).name.get());
	}
	
	@Test
	public void filterByCMCRegex() throws Exception {
		
	}

	@Test
	public void filterByType() throws Exception {

	}

	@Test
	public void filterByRarity() throws Exception {

	}

	@Test
	public void filterByText() throws Exception {

	}
	
	@Test
	public void filterByFlavor() throws Exception {

	}
	
	@Test
	public void filterByArtist() throws Exception {

	}
	
	@Test
	public void filterByNumber() throws Exception {

	}
	
	@Test
	public void filterByPower() throws Exception {

	}
	
	@Test
	public void filterByToughness() throws Exception {

	}
	
	@Test
	public void filterByLoyalty() throws Exception {

	}
	
	@Test
	public void filterByMultiverseID() throws Exception {

	}
	
	@Test
	public void filterByImageName() throws Exception {

	}
	
	@Test
	public void filterByWatermark() throws Exception {

	}
	
	@Test
	public void filterBySetCode() throws Exception {

	}
}
