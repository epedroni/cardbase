package eu.equalparts.cardbase.cardstorage;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.equalparts.cardbase.Cardbase;
import eu.equalparts.cardbase.cards.Card;

/**
 * Tests the sorting functionality.
 * 
 * @author Eduardo Pedroni
 *
 */
public class StandaloneCardContainerSortTest {

	private StandaloneCardContainer uut;
	private static List<Card> testCards;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		testCards = mapper.readValue(StandaloneCardContainerSortTest.class.getResourceAsStream("/testcards.json"), new TypeReference<List<Card>>() {});
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		uut = new Cardbase();
		int[] cardCounts = {1, 2, 3, 8, 1, 15, 1, 1};
		for (int i = 0; i < testCards.size(); i++) {
			uut.addCard(testCards.get(i), cardCounts[i]);
		}
	}

	@Test
	public void sortByName() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("name");
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
	public void sortByLayout() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("layout");
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
	public void sortByManaCost() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("manaCost");
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
	public void sortByCMC() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("cmc");
		int i = 0;
		Integer[] cmcs = {2, 3, 4, 5, 6, 6, 6, 8};
		for (Card card : sortedCards) {
			assertTrue(card.cmc + " should have been " + cmcs[i] + ", i = " + i, card.cmc.equals(cmcs[i]));
			i++;
		}
	}

	@Test
	public void sortByType() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("type");
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
	public void sortByRarity() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("rarity");
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
	public void sortByText() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("text");
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
	public void sortByFlavor() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("flavor");
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
	public void sortByArtist() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("artist");
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
	public void sortByNumber() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("number");
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
	public void sortByPower() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("power");
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
	public void sortByToughness() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("power");
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
	public void sortByLoyalty() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("loyalty");
		int i = 0;
		Integer[] loyalties = {0, 0, 0, 0, 0, 0, 0, 4};
		for (Card card : sortedCards) {
			Integer loyalty = card.loyalty != null ? card.loyalty : 0;
			assertTrue(loyalty + " should have been " + loyalties[i] + ", i = " + i, loyalty.equals(loyalties[i]));
			i++;
		}
	}
	
	@Test
	public void sortByMultiverseID() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("multiverseid");
		int i = 0;
		Integer[] ids = {74128, 74489, 193551, 238330, 366408, 383168, 383172, 391949 };
		for (Card card : sortedCards) {
			Integer id = card.multiverseid != null ? card.multiverseid : 0;
			assertTrue(id + " should have been " + ids[i] + ", i = " + i, id.equals(ids[i]));
			i++;
		}
	}
	
	@Test
	public void sortByImageName() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("imageName");
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
	public void sortByWatermark() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("watermark");
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
	public void sortBySetCode() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("setCode");
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
	
	public void sortByImageCode() throws Exception {
		Collection<Card> sortedCards = uut.sortByField("imageCode");
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
}
