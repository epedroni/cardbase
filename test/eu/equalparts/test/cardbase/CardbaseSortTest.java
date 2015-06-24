package eu.equalparts.test.cardbase;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;

import eu.equalparts.cardbase.Cardbase;
import eu.equalparts.cardbase.data.Card;
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
			cardbase.addCard(card, 1);
		}
	}

	@Test
	public void test_sortByName() throws Exception {

		Collection<Card> sortedCards = cardbase.sort("name");

		int i = 0;
		String[] names = {"Callow Jushi",
						  "Coerced Confession",
						  "Disrupting Shoal",
						  "Khalni Hydra",
						  "Nightmare",
						  "Shivan Dragon",
						  "Sorin Markov",
						  "Ugin's Construct" };
		
		for (Card card : sortedCards) {
			assertTrue(card.name + " should have been " + names[i] + ", i = " + i, card.name.equals(names[i]));
			i++;
		}
	}
	
	@Test
	public void test_sortByLayout() throws Exception {

		Collection<Card> sortedCards = cardbase.sort("layout");

		int i = 0;
		String[] layouts = {"flip",
						    "normal",
						    "normal",
						    "normal",
						    "normal",
						    "normal",
						    "normal",
						    "normal" };
		
		for (Card card : sortedCards) {
			assertTrue(card.layout + " should have been " + layouts[i] + ", i = " + i, card.layout.equals(layouts[i]));
			i++;
		}
	}
	
	@Test
	public void test_sortByManaCost() throws Exception {
		
		
		
//		Collection<Card> sortedCards = cardbase.sort("name");
//
//		int i = 0;
//		String[] layouts = {"flip",
//						  "normal",
//						  "normal",
//						  "normal",
//						  "normal",
//						  "normal",
//						  "normal",
//						  "normal" };
//		
//		for (Card card : sortedCards) {
//			assertTrue(card.layout + " should have been " + layouts[i] + ", i = " + i, card.layout.equals(layouts[i]));
//			i++;
//		}
		fail("todo");
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
		String[] types = {"Artifact Creature — Construct",
						  "Creature — Dragon",
						  "Creature — Human Wizard",
						  "Creature — Hydra",
						  "Creature — Nightmare Horse",
						  "Instant — Arcane",
						  "Planeswalker — Sorin",
						  "Sorcery"};
		
		for (Card card : sortedCards) {
			assertTrue(card.type + " should have been " + types[i] + ", i = " + i, card.type.equals(types[i]));
			i++;
		}
	}
	
	@Test
	public void test_sortByRarity() throws Exception {

		Collection<Card> sortedCards = cardbase.sort("rarity");

		int i = 0;
		String[] rarities = {"Uncommon",
							 "Uncommon",
						     "Uncommon",
						     "Rare",
						     "Rare",
						     "Rare",
						     "Mythic Rare",
						     "Mythic Rare"};
		
		for (Card card : sortedCards) {
			assertTrue(card.rarity + " should have been " + rarities[i] + ", i = " + i, card.rarity.equals(rarities[i]));
			i++;
		}
	}
}
