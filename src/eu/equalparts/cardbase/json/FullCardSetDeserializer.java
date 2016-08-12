package eu.equalparts.cardbase.json;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import eu.equalparts.cardbase.card.Card;
import eu.equalparts.cardbase.card.FullCardSet;

public class FullCardSetDeserializer extends StdDeserializer<FullCardSet> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1894617997342518472L;

	protected FullCardSetDeserializer(Class<?> vc) {
		super(vc);
	}

	public FullCardSetDeserializer() {
		this(null);
	}

	@Override
	public FullCardSet deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode jsonTree = jp.getCodec().readTree(jp);

		FullCardSet fcs = new FullCardSet();
		
		/*
		 * These fields are critical, if any of them is not present an exception is thrown.
		 */
		if (jsonTree.hasNonNull("name")) {
			fcs.name = jsonTree.get("name").asText();
		} else {
			throw new JsonMappingException("Field \"name\" not found.");
		}
		
		String setCode;
		if (jsonTree.hasNonNull("code")) {
			setCode = jsonTree.get("code").asText();
			fcs.code = setCode;
		} else {
			throw new JsonMappingException("Field \"code\" not found.");
		}
			
		if (jsonTree.hasNonNull("releaseDate")) {
			fcs.releaseDate = jsonTree.get("releaseDate").asText();
		} else {
			throw new JsonMappingException("Field \"releaseDate\" not found.");
		}
		
		if (jsonTree.hasNonNull("cards")) {
			// attempt to read card list as a POJO using the standard mapper
			List<Card> rawList = jsonTree.get("cards").traverse(JSON.mapper).readValueAs(new TypeReference<List<Card>>() {});
			// generate the map
			Map<String, Card> cardMap = new HashMap<String, Card>();
			for (Card card : rawList) {
				// add set code for convenience
				card.setCode.set(setCode);
				cardMap.put(card.number.get(), card);
			}
			fcs.cards = cardMap;
		} else {
			throw new JsonMappingException("Field \"cards\" not found.");
		}
		
		/*
		 * These fields are optional and are set to null if not present.
		 */
		fcs.border = jsonTree.hasNonNull("border") ? jsonTree.get("border").asText() : null;
		fcs.type = jsonTree.hasNonNull("type") ? jsonTree.get("type").asText() : null;
		fcs.block = jsonTree.hasNonNull("block") ? jsonTree.get("block").asText() : null;
		fcs.magicCardsInfoCode = jsonTree.hasNonNull("magicCardsInfoCode") ? jsonTree.get("magicCardsInfoCode").asText() : null;
		
		return fcs;
	}

}
