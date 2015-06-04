package eu.equalparts.cardbase.query;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.equalparts.cardbase.data.CardBase;
import eu.equalparts.cardbase.data.CardSet;
import eu.equalparts.cardbase.data.MetaCardSet;

public class IO {
	
	public static final String BASE_URL = "http://mtgjson.com/json/";
	public static final String SETS_URL = BASE_URL + "SetList.json";
	
	private static final ObjectMapper mapper = new ObjectMapper();

	public static CardSet getCardSet(String setId) {
		return new CardSet();
	}
	
	public static ArrayList<MetaCardSet> getAllMetaSets() throws JsonParseException, JsonMappingException, IOException {
		//mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);		
		return mapper.readValue(new URL(SETS_URL), new TypeReference<ArrayList<MetaCardSet>>() {});
	}
	
	public static CardBase readCardBase(File file) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(file, CardBase.class);
	}
	
	public static void writeCardBase(File file, CardBase cardBase) throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(file, cardBase);
	}
	
}
