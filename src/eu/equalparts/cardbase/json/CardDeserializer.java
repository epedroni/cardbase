package eu.equalparts.cardbase.json;


import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import eu.equalparts.cardbase.card.Card;
import eu.equalparts.cardbase.cardfield.IntegerCardField;
import eu.equalparts.cardbase.cardfield.StringCardField;

public class CardDeserializer extends StdDeserializer<Card> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1894617997342518472L;

	protected CardDeserializer(Class<?> vc) {
		super(vc);
	}

	public CardDeserializer() {
		this(null);
	}

	@Override
	public Card deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		
		Card newCard = new Card();
		
		newCard.name = new StringCardField("name", node.hasNonNull("name") ? node.get("name").asText() : null);
		newCard.layout = new StringCardField("layout", node.hasNonNull("layout") ? node.get("layout").asText() : null);
		newCard.manaCost = new StringCardField("manaCost", node.hasNonNull("manaCost") ? node.get("manaCost").asText() : null);
		newCard.cmc = new IntegerCardField("cmc", node.hasNonNull("cmc") ? node.get("cmc").asInt() : null);
		newCard.type = new StringCardField("type", node.hasNonNull("type") ? node.get("type").asText() : null);
		newCard.rarity = new StringCardField("rarity", node.hasNonNull("rarity") ? node.get("rarity").asText() : null);
		newCard.text = new StringCardField("text", node.hasNonNull("text") ? node.get("text").asText() : null);
		newCard.flavor = new StringCardField("flavor", node.hasNonNull("flavor") ? node.get("flavor").asText() : null);
		newCard.artist = new StringCardField("artist", node.hasNonNull("artist") ? node.get("artist").asText() : null);
		newCard.number = new StringCardField("number", node.hasNonNull("number") ? node.get("number").asText() : null);
		newCard.power = new StringCardField("power", node.hasNonNull("power") ? node.get("power").asText() : null);
		newCard.toughness = new StringCardField("toughness", node.hasNonNull("toughness") ? node.get("toughness").asText() : null);
		newCard.loyalty = new IntegerCardField("loyalty", node.hasNonNull("loyalty") ? node.get("loyalty").asInt() : null);
		newCard.multiverseid = new IntegerCardField("multiverseid", node.hasNonNull("multiverseid") ? node.get("multiverseid").asInt() : null);
		newCard.imageName = new StringCardField("imageName", node.hasNonNull("imageName") ? node.get("imageName").asText() : null);
		newCard.watermark = new StringCardField("watermark", node.hasNonNull("watermark") ? node.get("watermark").asText() : null);
		newCard.setCode = new StringCardField("setCode", node.hasNonNull("setCode") ? node.get("setCode").asText() : null);
 
        return newCard;
	}

}
