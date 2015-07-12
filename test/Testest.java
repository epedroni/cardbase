import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.Cardbase;
import eu.equalparts.cardbase.data.Card;
import eu.equalparts.cardbase.data.StandaloneDeck;
import eu.equalparts.cardbase.utils.JSON;


public class Testest {
	
	public static void main(String... args) throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
		StandaloneDeck deck = JSON.mapper.readValue(new File("control.cbd"), StandaloneDeck.class);
		
		System.out.println(deck.name);
		System.out.println("plains: " + deck.plains);
		System.out.println("islands: " + deck.islands);
		System.out.println("swamps: " + deck.swamps);
		System.out.println("mountains: " + deck.mountains);
		System.out.println("forests: " + deck.forests);
		
		for (Card card : deck.cards) {
			System.out.println(card.count + "x " + card.name);
		}
		
		Cardbase cards = new Cardbase(new File("cards.cb"));
		List<Card> missingCards = cards.getMissingCards(deck);
		
		for (Card card : missingCards) {
			System.out.println(card.count + "x " + card.name);
		}
	}
}
