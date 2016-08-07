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
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.equalparts.cardbase.cards.Card;
import eu.equalparts.cardbase.filtering.CardFiltering.FilterType;

public class CardFilteringTest {
	private static List<Card> allTestCards, testCards;

	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		allTestCards = mapper.readValue(CardFilteringTest.class.getResourceAsStream("/testcards.json"), new TypeReference<List<Card>>() {});
	}
	
	@Before
	public void setUp() {
		testCards = new LinkedList<>(allTestCards);
	}

	@Test
	public void filterByNameEquals() throws Exception {
		CardFiltering.filterByField(testCards, "name", FilterType.EQUALS, "Callow jushi");
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Callow Jushi", testCards.get(0).name);
	}
	
	@Test
	public void filterByNameContains() throws Exception {
		CardFiltering.filterByField(testCards, "name", FilterType.CONTAINS, "sh");
		
		assertEquals("Wrong list size.", 3, testCards.size());
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Shivan Dragon",
				"Disrupting Shoal",
		};
		for (Card card : testCards) {
			assertTrue(card.name + " should have been " + names[i] + ", i = " + i, card.name.equals(names[i]));
			i++;
		}
	}
	
	@Test
	public void filterByNameRegex() throws Exception {
		CardFiltering.filterByField(testCards, "name", FilterType.REGEX, ".+?n");
		
		assertEquals("Wrong list size.", 2, testCards.size());
		int i = 0;
		String[] names = {
				"Coerced Confession",
				"Shivan Dragon",
		};
		for (Card card : testCards) {
			assertTrue(card.name + " should have been " + names[i] + ", i = " + i, card.name.equals(names[i]));
			i++;
		}
	}

	@Test
	public void filterByLayoutEquals() throws Exception {
		CardFiltering.filterByField(testCards, "layout", FilterType.EQUALS, "flip");
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Callow Jushi", testCards.get(0).name);
	}
	
	@Test
	public void filterByLayoutContains() throws Exception {
		CardFiltering.filterByField(testCards, "layout", FilterType.CONTAINS, "l");
		
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
			assertTrue(card.name + " should have been " + names[i] + ", i = " + i, card.name.equals(names[i]));
			i++;
		}
	}
	
	@Test
	public void filterByLayoutRegex() throws Exception {
		CardFiltering.filterByField(testCards, "layout", FilterType.REGEX, "fl[a-z]p");
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Callow Jushi", testCards.get(0).name);
	}
	
	@Test
	public void filterByManaCostEquals() throws Exception {
		CardFiltering.filterByField(testCards, "manaCost", FilterType.EQUALS, "{X}{U}{U}");
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Disrupting Shoal", testCards.get(0).name);
	}
	
	@Test
	public void filterByManaCostContains() throws Exception {
		CardFiltering.filterByField(testCards, "manaCost", FilterType.CONTAINS, "B");
		
		assertEquals("Wrong list size.", 3, testCards.size());
		int i = 0;
		String[] names = {
				"Coerced Confession",
				"Nightmare",
				"Sorin Markov",
		};
		for (Card card : testCards) {
			assertTrue(card.name + " should have been " + names[i] + ", i = " + i, card.name.equals(names[i]));
			i++;
		}
	}
	
	@Test
	public void filterByManaCostRegex() throws Exception {
		CardFiltering.filterByField(testCards, "manaCost", FilterType.REGEX, "(\\{G\\}){8}");
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Khalni Hydra", testCards.get(0).name);
	}
	
	@Test
	public void filterByCMCEquals() throws Exception {
		CardFiltering.filterByField(testCards, "cmc", FilterType.EQUALS, "5");
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Coerced Confession", testCards.get(0).name);
	}
	
	@Test
	public void filterByCMCContains() throws Exception {
		CardFiltering.filterByField(testCards, "cmc", FilterType.CONTAINS, "B");
		
		assertEquals("Wrong list size.", 3, testCards.size());
		int i = 0;
		String[] names = {
				"Coerced Confession",
				"Nightmare",
				"Sorin Markov",
		};
		for (Card card : testCards) {
			assertTrue(card.name + " should have been " + names[i] + ", i = " + i, card.name.equals(names[i]));
			i++;
		}
	}
	
	@Test
	public void filterByCMCRegex() throws Exception {
		CardFiltering.filterByField(testCards, "cmc", FilterType.REGEX, "(\\{G\\}){8}");
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Khalni Hydra", testCards.get(0).name);
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
	
	@Test
	public void filterByImageCode() throws Exception {

	}
}
