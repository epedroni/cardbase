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
import eu.equalparts.cardbase.filtering.Filter;
import eu.equalparts.cardbase.filtering.Filter.FilterType;
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

	/*
	 * Filter tests, happy path
	 */
	@Test
	public void filterByNameEquals() throws Exception {
		Filter filter = new Filter(FilterType.EQUALS, "name", "Callow jushi");
		
		CardFiltering.filterByField(testCards, filter);
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Callow Jushi", testCards.get(0).name.get());
	}
	
	@Test
	public void filterByNameContains() throws Exception {
		Filter filter = new Filter(FilterType.CONTAINS, "name", "sh");
		
		CardFiltering.filterByField(testCards, filter);
		
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
		Filter filter = new Filter(FilterType.REGEX, "name", ".+?n");
		
		CardFiltering.filterByField(testCards, filter);
		
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
		Filter filter = new Filter(FilterType.GREATER_THAN, "name", "0");
		exception.expect(IllegalArgumentException.class);
		
		CardFiltering.filterByField(testCards, filter);
	}
	
	@Test
	public void filterByNameSmallerThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "name", "0");
		exception.expect(IllegalArgumentException.class);
		
		CardFiltering.filterByField(testCards, filter);
	}

	@Test
	public void filterByLayoutEquals() throws Exception {
		Filter filter = new Filter(FilterType.EQUALS, "layout", "flip");
		
		CardFiltering.filterByField(testCards, filter);
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Callow Jushi", testCards.get(0).name.get());
	}
	
	@Test
	public void filterByLayoutContains() throws Exception {
		Filter filter = new Filter(FilterType.CONTAINS, "layout", "l");
		
		CardFiltering.filterByField(testCards, filter);
		
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
		Filter filter = new Filter(FilterType.REGEX, "layout", "fl[a-z]p");
		
		CardFiltering.filterByField(testCards, filter);
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Callow Jushi", testCards.get(0).name.get());
	}
	
	@Test
	public void filterByLayoutGreaterThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "layout", "5");
		exception.expect(IllegalArgumentException.class);
		
		CardFiltering.filterByField(testCards, filter);
	}
	
	@Test
	public void filterByLayoutSmallerThan() throws Exception {
		Filter filter = new Filter(FilterType.SMALLER_THAN, "layout", "5");
		exception.expect(IllegalArgumentException.class);
		
		CardFiltering.filterByField(testCards, filter);
	}
	
	@Test
	public void filterByManaCostEquals() throws Exception {
		Filter filter = new Filter(FilterType.EQUALS, "manaCost", "{X}{U}{U}");
		
		CardFiltering.filterByField(testCards, filter);
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Disrupting Shoal", testCards.get(0).name.get());
	}
	
	@Test
	public void filterByManaCostContains() throws Exception {
		Filter filter = new Filter(FilterType.CONTAINS, "manaCost", "B");
		
		CardFiltering.filterByField(testCards, filter);
		
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
		Filter filter = new Filter(FilterType.REGEX, "manaCost", "(\\{G\\}){8}");
		
		CardFiltering.filterByField(testCards, filter);
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Khalni Hydra", testCards.get(0).name.get());
	}
	
	@Test
	public void filterByManaCostGreaterThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "manaCost", "5");
		
		exception.expect(IllegalArgumentException.class);
		CardFiltering.filterByField(testCards, filter);
	}
	
	@Test
	public void filterByManaCostSmallerThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "manaCost", "5");
		exception.expect(IllegalArgumentException.class);
		
		CardFiltering.filterByField(testCards, filter);
	}
	
	@Test
	public void filterByCMCEquals() throws Exception {
		Filter filter = new Filter(FilterType.EQUALS, "cmc", "5");
		
		CardFiltering.filterByField(testCards, filter);
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Coerced Confession", testCards.get(0).name.get());
	}
	
	@Test
	public void filterByCMCContains() throws Exception {
		Filter filter = new Filter(FilterType.CONTAINS, "cmc", "5");
		
		CardFiltering.filterByField(testCards, filter);
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Coerced Confession", testCards.get(0).name.get());
	}
	
	@Test
	public void filterByCMCRegex() throws Exception {
		Filter filter = new Filter(FilterType.REGEX, "cmc", "5");
		
		CardFiltering.filterByField(testCards, filter);
	}
	
	@Test
	public void filterByCMCGreaterThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "cmc", "5");
		
		CardFiltering.filterByField(testCards, filter);
		
		assertEquals("Wrong list size.", 4, testCards.size());
		int i = 0;
		String[] names = {
				"Khalni Hydra",
				"Nightmare",
				"Shivan Dragon",
				"Sorin Markov",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}
	
	@Test
	public void filterByCMCSmallerThan() throws Exception {
		Filter filter = new Filter(FilterType.SMALLER_THAN, "cmc", "5");
		
		CardFiltering.filterByField(testCards, filter);
		
		assertEquals("Wrong list size.", 3, testCards.size());
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Disrupting Shoal",
				"Ugin's Construct",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}

	@Test
	public void filterByTypeEquals() throws Exception {
		Filter filter = new Filter(FilterType.EQUALS, "type", "Sorcery");
		
		CardFiltering.filterByField(testCards, filter);
		
		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Coerced Confession", testCards.get(0).name.get());
	}
	
	@Test
	public void filterByTypeContains() throws Exception {
		Filter filter = new Filter(FilterType.CONTAINS, "type", "Creature");
		
		CardFiltering.filterByField(testCards, filter);
		
		assertEquals("Wrong list size.", 5, testCards.size());
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Khalni Hydra",
				"Nightmare",
				"Shivan Dragon",
				"Ugin's Construct",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}
	
	@Test
	public void filterByTypeRegex() throws Exception {
		Filter filter = new Filter(FilterType.REGEX, "type", "[^ ]+? —.*");
		
		CardFiltering.filterByField(testCards, filter);
		
		assertEquals("Wrong list size.", 6, testCards.size());
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Khalni Hydra",
				"Nightmare",
				"Shivan Dragon",
				"Disrupting Shoal",
				"Sorin Markov",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}
	
	@Test
	public void filterByTypeGreaterThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "type", "0");
		exception.expect(IllegalArgumentException.class);
		
		CardFiltering.filterByField(testCards, filter);
	}
	
	@Test
	public void filterByTypeSmallerThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "name", "0");
		exception.expect(IllegalArgumentException.class);
		
		CardFiltering.filterByField(testCards, filter);
	}

	@Test
	public void filterByRarityEquals() throws Exception {
		Filter filter = new Filter(FilterType.EQUALS, "rarity", "Mythic Rare");
		
		CardFiltering.filterByField(testCards, filter);
		
		assertEquals("Wrong list size.", 2, testCards.size());
		int i = 0;
		String[] names = {
				"Khalni Hydra",
				"Sorin Markov",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}
	
	@Test
	public void filterByRarityContains() throws Exception {
		Filter filter = new Filter(FilterType.CONTAINS, "rarity", "Rare");
		
		CardFiltering.filterByField(testCards, filter);
		
		assertEquals("Wrong list size.", 5, testCards.size());
		int i = 0;
		String[] names = {
				"Khalni Hydra",
				"Nightmare",
				"Shivan Dragon",
				"Disrupting Shoal",
				"Sorin Markov",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}
	
	@Test
	public void filterByRarityRegex() throws Exception {
		Filter filter = new Filter(FilterType.REGEX, "rarity", "[^ ]+");
		
		CardFiltering.filterByField(testCards, filter);
		
		assertEquals("Wrong list size.", 6, testCards.size());
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Coerced Confession",
				"Nightmare",
				"Shivan Dragon",
				"Disrupting Shoal",
				"Ugin's Construct",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}
	
	@Test
	public void filterByRarityGreaterThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "rarity", "0");
		exception.expect(IllegalArgumentException.class);
		
		CardFiltering.filterByField(testCards, filter);
	}
	
	@Test
	public void filterByRaritySmallerThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "rarity", "0");
		exception.expect(IllegalArgumentException.class);
		
		CardFiltering.filterByField(testCards, filter);
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
	public void filterBySetCode() throws Exception {

	}
		
	/*
	 * Filter validation tests, happy path
	 */
	@Test
	public void validateNameEqualsSomethingFilter() throws Exception {
		Filter testFilter = new Filter(FilterType.EQUALS, "name", "Card name");
		
		assertTrue(CardFiltering.isFilterValid(testFilter));
	}
	
	@Test
	public void validateFlavourEqualsNothingFilter() throws Exception {
		Filter testFilter = new Filter(FilterType.EQUALS, "flavor", "");
		
		assertTrue(CardFiltering.isFilterValid(testFilter));
	}
	
	@Test
	public void validateTextContainsVigilanceFilter() throws Exception {
		Filter testFilter = new Filter(FilterType.CONTAINS, "text", "vigilance");
		
		assertTrue(CardFiltering.isFilterValid(testFilter));
	}
	
	@Test
	public void validateCMCContainsNumberFilter() throws Exception {
		Filter testFilter = new Filter(FilterType.CONTAINS, "cmc", "5");
		
		assertTrue(CardFiltering.isFilterValid(testFilter));
	}
	
	@Test
	public void validateTypeRegexFilter() throws Exception {
		Filter testFilter = new Filter(FilterType.REGEX, "type", "Legendary [A-z]+? Creature.*");
		
		assertTrue(CardFiltering.isFilterValid(testFilter));
	}
	
	@Test
	public void validateManaCostRegexFilter() throws Exception {
		Filter testFilter = new Filter(FilterType.REGEX, "manaCost", ".*?{./.}.*?");
		
		assertTrue(CardFiltering.isFilterValid(testFilter));
	}
	
	@Test
	public void validateCMCGreaterThanFilter() throws Exception {
		Filter testFilter = new Filter(FilterType.GREATER_THAN, "cmc", "5");
		
		assertTrue(CardFiltering.isFilterValid(testFilter));
	}
	
	@Test
	public void validatePowerGreaterThanFilter() throws Exception {
		Filter testFilter = new Filter(FilterType.GREATER_THAN, "power", "3");
		
		assertTrue(CardFiltering.isFilterValid(testFilter));
	}
	
	@Test
	public void validateLoyaltySmallerThanFilter() throws Exception {
		Filter testFilter = new Filter(FilterType.SMALLER_THAN, "loyalty", "3");
		
		assertTrue(CardFiltering.isFilterValid(testFilter));
	}
	
	@Test
	public void validateToughnessSmallerThanFilter() throws Exception {
		Filter testFilter = new Filter(FilterType.SMALLER_THAN, "toughness", "5");
		
		assertTrue(CardFiltering.isFilterValid(testFilter));
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void validateNonIntegerFieldGreaterThanFilter() throws Exception {
		Filter testFilter = new Filter(FilterType.GREATER_THAN, "name", "5");
		
		assertTrue(!CardFiltering.isFilterValid(testFilter));
	}
	
	@Test
	public void validateNonIntegerValueGreaterThanFilter() throws Exception {
		Filter testFilter = new Filter(FilterType.GREATER_THAN, "cmc", "test");
		
		assertTrue(!CardFiltering.isFilterValid(testFilter));
	}
}
