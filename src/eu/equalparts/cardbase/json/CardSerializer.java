package eu.equalparts.cardbase.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import eu.equalparts.cardbase.card.Card;

public class CardSerializer extends JsonSerializer<Card> {
	@Override
	public void serialize(Card card, JsonGenerator jGen, SerializerProvider serializerProvider) throws IOException  {
		jGen.writeStartObject();

		if (card.name.get() != null) {
			jGen.writeStringField("name", card.name.get());
		} else {
			jGen.writeNullField("name");
		}

		if (card.layout.get() != null) {
			jGen.writeStringField("layout", card.layout.get());
		} else {
			jGen.writeNullField("layout");
		}

		if (card.manaCost.get() != null) {
			jGen.writeStringField("manaCost", card.manaCost.get());
		} else {
			jGen.writeNullField("manaCost");
		}

		if (card.cmc.get() != null) {
			jGen.writeNumberField("cmc", card.cmc.get());
		} else {
			jGen.writeNullField("cmc");
		}

		if (card.type.get() != null) {
			jGen.writeStringField("type", card.type.get());
		} else {
			jGen.writeNullField("type");
		}

		if (card.rarity.get() != null) {
			jGen.writeStringField("rarity", card.rarity.get());
		} else {
			jGen.writeNullField("rarity");
		}

		if (card.text.get() != null) {
			jGen.writeStringField("text", card.text.get());
		} else {
			jGen.writeNullField("text");
		}

		if (card.flavor.get() != null) {
			jGen.writeStringField("flavor", card.flavor.get());
		} else {
			jGen.writeNullField("flavor");
		}

		if (card.artist.get() != null) {
			jGen.writeStringField("artist", card.artist.get());
		} else {
			jGen.writeNullField("artist");
		}

		if (card.number.get() != null) {
			jGen.writeStringField("number", card.number.get());
		} else {
			jGen.writeNullField("number");
		}

		if (card.power.get() != null) {
			jGen.writeStringField("power", card.power.get());
		} else {
			jGen.writeNullField("power");
		}

		if (card.toughness.get() != null) {
			jGen.writeStringField("toughness", card.toughness.get());
		} else {
			jGen.writeNullField("toughness");
		}

		if (card.loyalty.get() != null) {
			jGen.writeNumberField("loyalty", card.loyalty.get());
		} else {
			jGen.writeNullField("loyalty");
		}

		if (card.multiverseid.get() != null) {
			jGen.writeNumberField("multiverseid", card.multiverseid.get());
		} else {
			jGen.writeNullField("multiverseid");
		}

		if (card.imageName.get() != null) {
			jGen.writeStringField("imageName", card.imageName.get());
		} else {
			jGen.writeNullField("imageName");
		}

		if (card.watermark.get() != null) {
			jGen.writeStringField("watermark", card.watermark.get());
		} else {
			jGen.writeNullField("watermark");
		}

		if (card.setCode.get() != null) {
			jGen.writeStringField("setCode", card.setCode.get());
		} else {
			jGen.writeNullField("setCode");
		}

		jGen.writeEndObject();
	}
}
