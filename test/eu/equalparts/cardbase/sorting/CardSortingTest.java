package eu.equalparts.cardbase.sorting;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.core.type.TypeReference;

import eu.equalparts.cardbase.card.Card;
import eu.equalparts.cardbase.json.JSON;

/**
 * Tests the sorting functionality.
 * 
 * @author Eduardo Pedroni
 *
 */
public class CardSortingTest {
	private static List<Card> testCards;
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testCards = JSON.mapper.readValue(CardSortingTest.class.getResourceAsStream("/testcards.json"), new TypeReference<List<Card>>() {});
	}

	@Test
	public void sortByName() throws Exception {
		CardSorting.sortByField(testCards, "name");
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
		for (Card card : testCards) {
			assertTrue(card.name.get() + " should have been " + names[i] + ", i = " + i, card.name.get().equals(names[i]));
			i++;
		}
	}

	@Test
	public void sortByLayout() throws Exception {
		CardSorting.sortByField(testCards, "layout");
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
		for (Card card : testCards) {
			assertTrue(card.layout.get() + " should have been " + layouts[i] + ", i = " + i, card.layout.get().equals(layouts[i]));
			i++;
		}
	}

	@Test
	public void sortByManaCost() throws Exception {
		CardSorting.sortByField(testCards, "manaCost");
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
		for (Card card : testCards) {
			assertTrue(card.manaCost.get() + " should have been " + costs[i] + ", i = " + i, card.manaCost.get().equals(costs[i]));
			i++;
		}
	}

	@Test
	public void sortByCMC() throws Exception {
		CardSorting.sortByField(testCards, "cmc");
		int i = 0;
		Integer[] cmcs = {2, 3, 4, 5, 6, 6, 6, 8};
		for (Card card : testCards) {
			assertTrue(card.cmc.get() + " should have been " + cmcs[i] + ", i = " + i, card.cmc.get().equals(cmcs[i]));
			i++;
		}
	}

	@Test
	public void sortByType() throws Exception {
		CardSorting.sortByField(testCards, "type");
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
		for (Card card : testCards) {
			assertTrue(card.type.get() + " should have been " + types[i] + ", i = " + i, card.type.get().equals(types[i]));
			i++;
		}
	}

	@Test
	public void sortByRarity() throws Exception {
		CardSorting.sortByField(testCards, "rarity");
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
		for (Card card : testCards) {
			assertTrue(card.rarity.get() + " should have been " + rarities[i] + ", i = " + i, card.rarity.get().equals(rarities[i]));
			i++;
		}
	}

	@Test
	public void sortByText() throws Exception {
		CardSorting.sortByField(testCards, "text");
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
		for (Card card : testCards) {
			assertTrue(card.text.get() + " should have been " + texts[i] + ", i = " + i, card.text.get().equals(texts[i]));
			i++;
		}
	}
	
	@Test
	public void sortByFlavor() throws Exception {
		CardSorting.sortByField(testCards, "flavor");
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
		for (Card card : testCards) {
			String flavor = card.flavor.get() != null ? card.flavor.get() : "";
			assertTrue(flavor + " should have been " + flavors[i] + ", i = " + i, flavor.equals(flavors[i]));
			i++;
		}
	}
	
	@Test
	public void sortByArtist() throws Exception {
		CardSorting.sortByField(testCards, "artist");
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
		for (Card card : testCards) {
			assertTrue(card.artist.get() + " should have been " + artists[i] + ", i = " + i, card.artist.get().equals(artists[i]));
			i++;
		}
	}
	
	@Test
	public void sortByNumber() throws Exception {
		CardSorting.sortByField(testCards, "number");
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
		for (Card card : testCards) {
			assertTrue(card.number.get() + " should have been " + numbers[i] + ", i = " + i, card.number.get().equals(numbers[i]));
			i++;
		}
	}
	
	@Test
	public void sortByPower() throws Exception {
		CardSorting.sortByField(testCards, "power");
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
		for (Card card : testCards) {
			String power = card.power.get() != null ? card.power.get() : "";
			assertTrue(power + " should have been " + powers[i] + ", i = " + i, power.equals(powers[i]));
			i++;
		}
	}
	
	@Test
	public void sortByToughness() throws Exception {
		CardSorting.sortByField(testCards, "power");
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
		for (Card card : testCards) {
			String toughness = card.toughness.get() != null ? card.toughness.get() : "";
			assertTrue(toughness + " should have been " + toughnesses[i] + ", i = " + i, toughness.equals(toughnesses[i]));
			i++;
		}
	}
	
	@Test
	public void sortByLoyalty() throws Exception {
		CardSorting.sortByField(testCards, "loyalty");
		int i = 0;
		Integer[] loyalties = {0, 0, 0, 0, 0, 0, 0, 4};
		for (Card card : testCards) {
			Integer loyalty = card.loyalty.get() != null ? card.loyalty.get() : 0;
			assertTrue(loyalty + " should have been " + loyalties[i] + ", i = " + i, loyalty.equals(loyalties[i]));
			i++;
		}
	}
	
	@Test
	public void sortByMultiverseID() throws Exception {
		CardSorting.sortByField(testCards, "multiverseid");
		int i = 0;
		Integer[] ids = {74128, 74489, 193551, 238330, 366408, 383168, 383172, 391949 };
		for (Card card : testCards) {
			Integer id = card.multiverseid.get() != null ? card.multiverseid.get() : 0;
			assertTrue(id + " should have been " + ids[i] + ", i = " + i, id.equals(ids[i]));
			i++;
		}
	}
	
	@Test
	public void sortByImageName() throws Exception {
		CardSorting.sortByField(testCards, "imageName");
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
		for (Card card : testCards) {
			assertTrue(card.imageName.get() + " should have been " + names[i] + ", i = " + i, card.imageName.get().equals(names[i]));
			i++;
		}
	}
	
	@Test
	public void sortByWatermark() throws Exception {
		CardSorting.sortByField(testCards, "watermark");
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
		for (Card card : testCards) {
			String watermark = card.watermark.get() != null ? card.watermark.get() : "";
			assertTrue(watermark + " should have been " + watermarks[i] + ", i = " + i, watermark.equals(watermarks[i]));
			i++;
		}
	}
	
	@Test
	public void sortBySetCode() throws Exception {
		CardSorting.sortByField(testCards, "setCode");
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
		for (Card card : testCards) {
			assertTrue(card.setCode.get() + " should have been " + sets[i] + ", i = " + i, card.setCode.get().equals(sets[i]));
			i++;
		}
	}
	
	/*
	 * Edge cases
	 */
	@Test
	public void sortFieldDoesNotExist() throws Exception {
		exception.expect(NoSuchFieldException.class);
		CardSorting.sortByField(testCards, "not a field name");
	}
}
