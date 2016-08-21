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
		Filter filter = new Filter(FilterType.SMALLER_THAN, "name", "0");
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
		Filter filter = new Filter(FilterType.SMALLER_THAN, "manaCost", "5");
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
		Filter filter = new Filter(FilterType.SMALLER_THAN, "name", "0");
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
		Filter filter = new Filter(FilterType.SMALLER_THAN, "rarity", "0");
		exception.expect(IllegalArgumentException.class);

		CardFiltering.filterByField(testCards, filter);
	}

	@Test
	public void filterByTextEquals() throws Exception {
		Filter filter = new Filter(FilterType.EQUALS, "text", "When Ugin's Construct enters the battlefield, sacrifice a permanent that's one or more colors.");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Ugin's Construct", testCards.get(0).name.get());
	}

	@Test
	public void filterByTextContains() throws Exception {
		Filter filter = new Filter(FilterType.CONTAINS, "text", "Trample");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Khalni Hydra", testCards.get(0).name.get());
	}

	@Test
	public void filterByTextRegex() throws Exception {
		Filter filter = new Filter(FilterType.REGEX, "text", "^Whenever [\\S\\s]+");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Callow Jushi", testCards.get(0).name.get());
	}

	@Test
	public void filterByTextGreaterThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "text", "0");
		exception.expect(IllegalArgumentException.class);

		CardFiltering.filterByField(testCards, filter);
	}

	@Test
	public void filterByTextSmallerThan() throws Exception {
		Filter filter = new Filter(FilterType.SMALLER_THAN, "text", "0");
		exception.expect(IllegalArgumentException.class);

		CardFiltering.filterByField(testCards, filter);
	}

	@Test
	public void filterByFlavorEquals() throws Exception {
		Filter filter = new Filter(FilterType.EQUALS, "flavor", "The thunder of its hooves beats dreams into despair.");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Nightmare", testCards.get(0).name.get());
	}

	@Test
	public void filterByFlavorContains() throws Exception {
		Filter filter = new Filter(FilterType.CONTAINS, "flavor", "of");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 3, testCards.size());
		int i = 0;
		String[] names = {
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
	public void filterByFlavorRegex() throws Exception {
		Filter filter = new Filter(FilterType.REGEX, "flavor", ".*?\"\n—[^\"]+$");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 2, testCards.size());
		int i = 0;
		String[] names = {
				"Coerced Confession",
				"Khalni Hydra",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}

	@Test
	public void filterByFlavorGreaterThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "flavor", "0");
		exception.expect(IllegalArgumentException.class);

		CardFiltering.filterByField(testCards, filter);
	}

	@Test
	public void filterByFlavorSmallerThan() throws Exception {
		Filter filter = new Filter(FilterType.SMALLER_THAN, "flavor", "0");
		exception.expect(IllegalArgumentException.class);

		CardFiltering.filterByField(testCards, filter);
	}

	@Test
	public void filterByArtistEquals() throws Exception {
		Filter filter = new Filter(FilterType.EQUALS, "artist", "Todd Lockwood");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Khalni Hydra", testCards.get(0).name.get());
	}

	@Test
	public void filterByArtistContains() throws Exception {
		Filter filter = new Filter(FilterType.CONTAINS, "artist", "e");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 5, testCards.size());
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Nightmare",
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
	public void filterByArtistRegex() throws Exception {
		Filter filter = new Filter(FilterType.REGEX, "artist", "[^ ]+ [^ ]+ [^ ]+");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Disrupting Shoal", testCards.get(0).name.get());
	}

	@Test
	public void filterByArtistGreaterThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "artist", "0");
		exception.expect(IllegalArgumentException.class);

		CardFiltering.filterByField(testCards, filter);
	}

	@Test
	public void filterByArtistSmallerThan() throws Exception {
		Filter filter = new Filter(FilterType.SMALLER_THAN, "artist", "0");
		exception.expect(IllegalArgumentException.class);

		CardFiltering.filterByField(testCards, filter);
	}

	@Test
	public void filterByNumberEquals() throws Exception {
		Filter filter = new Filter(FilterType.EQUALS, "number", "192");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Khalni Hydra", testCards.get(0).name.get());
	}

	@Test
	public void filterByNumberContains() throws Exception {
		Filter filter = new Filter(FilterType.CONTAINS, "number", "3");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 2, testCards.size());
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Disrupting Shoal",

		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}

	@Test
	public void filterByNumberRegex() throws Exception {
		Filter filter = new Filter(FilterType.REGEX, "number", "[0-9]{1,3}[a-z]");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Callow Jushi", testCards.get(0).name.get());
	}

	@Test
	public void filterByNumberGreaterThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "number", "192");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 3, testCards.size());
		int i = 0;
		String[] names = {
				"Coerced Confession",
				"Nightmare",
				"Shivan Dragon",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}

	@Test
	public void filterByNumberSmallerThan() throws Exception {
		Filter filter = new Filter(FilterType.SMALLER_THAN, "number", "109");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 2, testCards.size());
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Disrupting Shoal",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}

	@Test
	public void filterByPowerEquals() throws Exception {
		Filter filter = new Filter(FilterType.EQUALS, "power", "*");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Nightmare", testCards.get(0).name.get());
	}

	@Test
	public void filterByPowerContains() throws Exception {
		Filter filter = new Filter(FilterType.CONTAINS, "power", "8");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Khalni Hydra", testCards.get(0).name.get());
	}

	@Test
	public void filterByPowerRegex() throws Exception {
		Filter filter = new Filter(FilterType.REGEX, "power", "[0-9]+");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 4, testCards.size());
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Khalni Hydra",
				"Shivan Dragon",
				"Ugin's Construct",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}

	@Test
	public void filterByPowerGreaterThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "power", "5");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Khalni Hydra", testCards.get(0).name.get());
	}

	@Test
	public void filterByPowerSmallerThan() throws Exception {
		Filter filter = new Filter(FilterType.SMALLER_THAN, "power", "5");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 2, testCards.size());
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Ugin's Construct",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}

	@Test
	public void filterByToughnessEquals() throws Exception {
		Filter filter = new Filter(FilterType.EQUALS, "toughness", "5");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 2, testCards.size());
		int i = 0;
		String[] names = {
				"Shivan Dragon",
				"Ugin's Construct",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}

	@Test
	public void filterByToughnessContains() throws Exception {
		Filter filter = new Filter(FilterType.CONTAINS, "toughness", "*");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Nightmare", testCards.get(0).name.get());
	}

	@Test
	public void filterByToughnessRegex() throws Exception {
		Filter filter = new Filter(FilterType.REGEX, "toughness", "[0-9]+");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 4, testCards.size());
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Khalni Hydra",
				"Shivan Dragon",
				"Ugin's Construct",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}

	@Test
	public void filterByToughnessGreaterThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "toughness", "5");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Khalni Hydra", testCards.get(0).name.get());
	}

	@Test
	public void filterByToughnessSmallerThan() throws Exception {
		Filter filter = new Filter(FilterType.SMALLER_THAN, "toughness", "5");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Callow Jushi", testCards.get(0).name.get());
	}

	@Test
	public void filterByLoyaltyEquals() throws Exception {
		Filter filter = new Filter(FilterType.EQUALS, "loyalty", "5");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 0, testCards.size());
	}

	@Test
	public void filterByLoyaltyContains() throws Exception {
		Filter filter = new Filter(FilterType.CONTAINS, "loyalty", "test");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 0, testCards.size());
	}

	@Test
	public void filterByLoyaltyRegex() throws Exception {
		Filter filter = new Filter(FilterType.REGEX, "loyalty", "[0-9]+");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Sorin Markov", testCards.get(0).name.get());
	}

	@Test
	public void filterByLoyaltyGreaterThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "loyalty", "2");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Sorin Markov", testCards.get(0).name.get());
	}

	@Test
	public void filterByLoyaltySmallerThan() throws Exception {
		Filter filter = new Filter(FilterType.SMALLER_THAN, "loyalty", "1");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 0, testCards.size());
	}

	@Test
	public void filterByMultiverseIDEquals() throws Exception {
		Filter filter = new Filter(FilterType.EQUALS, "multiverseid", "74489");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Callow Jushi", testCards.get(0).name.get());
	}

	@Test
	public void filterByMultiverseIDContains() throws Exception {
		Filter filter = new Filter(FilterType.CONTAINS, "multiverseid", "38");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 3, testCards.size());
		int i = 0;
		String[] names = {
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
	public void filterByMultiverseIDRegex() throws Exception {
		Filter filter = new Filter(FilterType.REGEX, "multiverseid", ".{5}");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 2, testCards.size());
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Disrupting Shoal",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}

	@Test
	public void filterByMultiverseIDGreaterThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "multiverseid", "300000");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 4, testCards.size());
		int i = 0;
		String[] names = {
				"Coerced Confession",
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
	public void filterByMultiverseIDSmallerThan() throws Exception {
		Filter filter = new Filter(FilterType.SMALLER_THAN, "multiverseid", "10000");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 0, testCards.size());
	}

	@Test
	public void filterByImageNameEquals() throws Exception {
		Filter filter = new Filter(FilterType.EQUALS, "imageName", "nightmare");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Nightmare", testCards.get(0).name.get());
	}

	@Test
	public void filterByImageNameContains() throws Exception {
		Filter filter = new Filter(FilterType.CONTAINS, "imageName", "co");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 2, testCards.size());
		int i = 0;
		String[] names = {
				"Coerced Confession",
				"Ugin's Construct",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}

	@Test
	public void filterByImageNameRegex() throws Exception {
		Filter filter = new Filter(FilterType.REGEX, "imageName", ".+? .+?");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 7, testCards.size());
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Coerced Confession",
				"Khalni Hydra",
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
	public void filterByImageNameGreaterThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "imageName", "10");

		exception.expect(IllegalArgumentException.class);
		CardFiltering.filterByField(testCards, filter);
	}

	@Test
	public void filterByImageNameSmallerThan() throws Exception {
		Filter filter = new Filter(FilterType.SMALLER_THAN, "imageName", "10");

		exception.expect(IllegalArgumentException.class);
		CardFiltering.filterByField(testCards, filter);
	}

	@Test
	public void filterBySetCodeEquals() throws Exception {
		Filter filter = new Filter(FilterType.EQUALS, "setCode", "GTC");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 1, testCards.size());
		assertEquals("Coerced Confession", testCards.get(0).name.get());
	}

	@Test
	public void filterBySetCodeContains() throws Exception {
		Filter filter = new Filter(FilterType.CONTAINS, "setCode", "o");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 3, testCards.size());
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Khalni Hydra",
				"Disrupting Shoal",
		};
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}

	@Test
	public void filterBySetCodeRegex() throws Exception {
		Filter filter = new Filter(FilterType.REGEX, "setCode", "M[0-9]{2}");

		CardFiltering.filterByField(testCards, filter);

		assertEquals("Wrong list size.", 3, testCards.size());
		int i = 0;
		String[] names = {
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
	public void filterBySetCodeGreaterThan() throws Exception {
		Filter filter = new Filter(FilterType.GREATER_THAN, "setCode", "10");

		exception.expect(IllegalArgumentException.class);
		CardFiltering.filterByField(testCards, filter);
	}

	@Test
	public void filterBySetCodeSmallerThan() throws Exception {
		Filter filter = new Filter(FilterType.SMALLER_THAN, "setCode", "10");

		exception.expect(IllegalArgumentException.class);
		CardFiltering.filterByField(testCards, filter);
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
