package eu.equalparts.test.cardbase;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;

import eu.equalparts.cardbase.Cardbase;
import eu.equalparts.cardbase.cards.Card;
import eu.equalparts.cardbase.utils.JSON;

/**
 * Tests the sorting functionality.
 * 
 * @author Eduardo Pedroni
 *
 */
public class CardbaseSortTest {

	private Cardbase cardbase;
	private static List<Card> testCards;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testCards = JSON.mapper.readValue(CardbaseSortTest.class.getResourceAsStream("testcards.json"), new TypeReference<List<Card>>() {});
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		cardbase = new Cardbase();
		for (Card card : testCards) {
			cardbase.addCard(card);
		}
	}

	@Test
	public void test_sortByName() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("name");
		int i = 0;
		String[] names = {
				"Callow Jushi",
				"Coerced Confession",
				"Disrupting Shoal",
				"Khalni Hydra",
				"Nightmare",
				"Shivan Dragon",
				"Sorin Markov",
				"Ugin's Construct",
		};
		for (Card card : sortedCards) {
			assertTrue(card.name + " should have been " + names[i] + ", i = " + i, card.name.equals(names[i]));
			i++;
		}
	}

	@Test
	public void test_sortByLayout() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("layout");
		int i = 0;
		String[] layouts = {
				"flip",
				"normal",
				"normal",
				"normal",
				"normal",
				"normal",
				"normal",
				"normal",
		};
		for (Card card : sortedCards) {
			assertTrue(card.layout + " should have been " + layouts[i] + ", i = " + i, card.layout.equals(layouts[i]));
			i++;
		}
	}

	@Test
	public void test_sortByManaCost() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("manaCost");
		int i = 0;
		String[] costs = {
				"{1}{U}{U}",
				"{3}{B}{B}{B}",
				"{4}",
				"{4}{R}{R}",
				"{4}{U/B}",
				"{5}{B}",
				"{G}{G}{G}{G}{G}{G}{G}{G}",
				"{X}{U}{U}",
		};
		for (Card card : sortedCards) {
			assertTrue(card.manaCost + " should have been " + costs[i] + ", i = " + i, card.manaCost.equals(costs[i]));
			i++;
		}
	}

	@Test
	public void test_sortByCMC() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("cmc");
		int i = 0;
		Integer[] cmcs = {2, 3, 4, 5, 6, 6, 6, 8};
		for (Card card : sortedCards) {
			assertTrue(card.cmc + " should have been " + cmcs[i] + ", i = " + i, card.cmc.equals(cmcs[i]));
			i++;
		}
	}

	@Test
	public void test_sortByType() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("type");
		int i = 0;
		String[] types = {
				"Artifact Creature — Construct",
				"Creature — Dragon",
				"Creature — Human Wizard",
				"Creature — Hydra",
				"Creature — Nightmare Horse",
				"Instant — Arcane",
				"Planeswalker — Sorin",
				"Sorcery",
		};
		for (Card card : sortedCards) {
			assertTrue(card.type + " should have been " + types[i] + ", i = " + i, card.type.equals(types[i]));
			i++;
		}
	}

	@Test
	public void test_sortByRarity() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("rarity");
		int i = 0;
		String[] rarities = {
				"Uncommon",
				"Uncommon",
				"Uncommon",
				"Rare",
				"Rare",
				"Rare",
				"Mythic Rare",
				"Mythic Rare",
		};
		for (Card card : sortedCards) {
			assertTrue(card.rarity + " should have been " + rarities[i] + ", i = " + i, card.rarity.equals(rarities[i]));
			i++;
		}
	}

	@Test
	public void test_sortByText() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("text");
		int i = 0;
		String[] texts = {
				"+2: Sorin Markov deals 2 damage to target creature or player and you gain 2 life.\n−3: Target opponent's life total becomes 10.\n−7: You control target player during that player's next turn.",
				"Flying (This creature can't be blocked except by creatures with flying or reach.)\nNightmare's power and toughness are each equal to the number of Swamps you control.",
				"Flying (This creature can't be blocked except by creatures with flying or reach.)\n{R}: Shivan Dragon gets +1/+0 until end of turn.",
				"Khalni Hydra costs {G} less to cast for each green creature you control.\nTrample",
				"Target player puts the top four cards of his or her library into his or her graveyard. You draw a card for each creature card put into that graveyard this way.",
				"When Ugin's Construct enters the battlefield, sacrifice a permanent that's one or more colors.",
				"Whenever you cast a Spirit or Arcane spell, you may put a ki counter on Callow Jushi.\nAt the beginning of the end step, if there are two or more ki counters on Callow Jushi, you may flip it.",
				"You may exile a blue card with converted mana cost X from your hand rather than pay Disrupting Shoal's mana cost.\nCounter target spell if its converted mana cost is X.",
		};
		for (Card card : sortedCards) {
			assertTrue(card.text + " should have been " + texts[i] + ", i = " + i, card.text.equals(texts[i]));
			i++;
		}
	}
	
	@Test
	public void test_sortByFlavor() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("flavor");
		int i = 0;
		String[] flavors = {
				"",
				"",
				"",
				"\"Ask the right questions in the right way and truth is inevitable.\"\n—Lazav",
				"\"In ages past, bargains were struck and promises were made. Now we must collect on our debt. Begin the hymns.\"\n—Moruul, Khalni druid",
				"The thunder of its hooves beats dreams into despair.",
				"The undisputed master of the mountains of Shiv.",
				"While trapping the Eldrazi on Zendikar, Ugin learned little from Sorin, but he gleaned the rudiments of lithomancy from Nahiri.",
		};
		for (Card card : sortedCards) {
			String flavor = card.flavor != null ? card.flavor : "";
			assertTrue(flavor + " should have been " + flavors[i] + ", i = " + i, flavor.equals(flavors[i]));
			i++;
		}
	}
	
	@Test
	public void test_sortByArtist() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("artist");
		int i = 0;
		String[] artists = {
				"Donato Giancola",
				"Mathias Kollros",
				"Michael Komarck",
				"Peter Mohrbacher",
				"Scott M. Fischer",
				"Todd Lockwood",
				"Tsutomu Kawade",
				"Vance Kovacs",
		};
		for (Card card : sortedCards) {
			assertTrue(card.artist + " should have been " + artists[i] + ", i = " + i, card.artist.equals(artists[i]));
			i++;
		}
	}
	
	@Test
	public void test_sortByNumber() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("number");
		int i = 0;
		String[] numbers = {
				"31a",
				"33",
				"109",
				"164",
				"192",
				"217",
				"276",
				"281",
		};
		for (Card card : sortedCards) {
			assertTrue(card.number + " should have been " + numbers[i] + ", i = " + i, card.number.equals(numbers[i]));
			i++;
		}
	}
	
	@Test
	public void test_sortByPower() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("power");
		int i = 0;
		String[] powers = {
				"",
				"",
				"",
				"*",
				"2",
				"4",
				"5",
				"8",
		};
		for (Card card : sortedCards) {
			String power = card.power != null ? card.power : "";
			assertTrue(power + " should have been " + powers[i] + ", i = " + i, power.equals(powers[i]));
			i++;
		}
	}
	
	@Test
	public void test_sortByToughness() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("power");
		int i = 0;
		String[] toughnesses = {
				"",
				"",
				"",
				"*",
				"2",
				"5",
				"5",
				"8",
		};
		for (Card card : sortedCards) {
			String toughness = card.toughness != null ? card.toughness : "";
			assertTrue(toughness + " should have been " + toughnesses[i] + ", i = " + i, toughness.equals(toughnesses[i]));
			i++;
		}
	}
	
	@Test
	public void test_sortByLoyalty() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("loyalty");
		int i = 0;
		Integer[] loyalties = {0, 0, 0, 0, 0, 0, 0, 4};
		for (Card card : sortedCards) {
			Integer loyalty = card.loyalty != null ? card.loyalty : 0;
			assertTrue(loyalty + " should have been " + loyalties[i] + ", i = " + i, loyalty.equals(loyalties[i]));
			i++;
		}
	}
	
	@Test
	public void test_sortByMultiverseID() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("multiverseid");
		int i = 0;
		Integer[] ids = {74128, 74489, 193551, 238330, 366408, 383168, 383172, 391949 };
		for (Card card : sortedCards) {
			Integer id = card.multiverseid != null ? card.multiverseid : 0;
			assertTrue(id + " should have been " + ids[i] + ", i = " + i, id.equals(ids[i]));
			i++;
		}
	}
	
	@Test
	public void test_sortByImageName() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("imageName");
		int i = 0;
		String[] names = {
				"callow jushi",
				"coerced confession",
				"disrupting shoal",
				"khalni hydra",
				"nightmare",
				"shivan dragon",
				"sorin markov",
				"ugin's construct",
		};
		for (Card card : sortedCards) {
			assertTrue(card.imageName + " should have been " + names[i] + ", i = " + i, card.imageName.equals(names[i]));
			i++;
		}
	}
	
	@Test
	public void test_sortByWatermark() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("watermark");
		int i = 0;
		String[] watermarks = {
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"Dimir",
		};
		for (Card card : sortedCards) {
			String watermark = card.watermark != null ? card.watermark : "";
			assertTrue(watermark + " should have been " + watermarks[i] + ", i = " + i, watermark.equals(watermarks[i]));
			i++;
		}
	}
	
	@Test
	public void test_sortBySetCode() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("setCode");
		int i = 0;
		String[] sets = {
				"BOK",
				"BOK",
				"FRF",
				"GTC",
				"M12",
				"M15",
				"M15",
				"ROE",
		};
		for (Card card : sortedCards) {
			assertTrue(card.setCode + " should have been " + sets[i] + ", i = " + i, card.setCode.equals(sets[i]));
			i++;
		}
	}
	
	public void test_sortByImageCode() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("imageCode");
		int i = 0;
		String[] codes = {
				"bok",
				"bok",
				"frf",
				"gtc",
				"m12",
				"m15",
				"m15",
				"roe",
		};
		for (Card card : sortedCards) {
			assertTrue(card.imageCode + " should have been " + codes[i] + ", i = " + i, card.imageCode.equals(codes[i]));
			i++;
		}
	}
	
	@Test
	public void test_sortByCount() throws Exception {
		Collection<Card> sortedCards = cardbase.sort("count");
		int i = 0;
		Integer[] counts = {1, 1, 1, 1, 2, 3, 8, 15 };
		for (Card card : sortedCards) {
			assertTrue(card.count + " should have been " + counts[i] + ", i = " + i, card.count.equals(counts[i]));
			i++;
		}
	}
	
}
