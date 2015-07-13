import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.cards.Card;
import eu.equalparts.cardbase.decks.StandaloneDeck;
import eu.equalparts.cardbase.utils.JSON;


public class Testest {
	
	public static void main(String... args) throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
		StandaloneDeck deck = JSON.mapper.readValue(new File("deck.cbd"), StandaloneDeck.class);
		
		System.out.println(deck.name);
		System.out.println("plains: " + deck.plains);
		System.out.println("islands: " + deck.islands);
		System.out.println("swamps: " + deck.swamps);
		System.out.println("mountains: " + deck.mountains);
		System.out.println("forests: " + deck.forests);
		
		int count = 0;
		for (Card card : deck.cards) {
			if (card.type != null && card.type.contains("Instant"))
				System.out.println(card.count + "x " + card.cmc + " (" + card.name + ")");
		}
		System.out.println("total: " + count);
	}
}
