package eu.equalparts.cardbase.query;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
	
	private static final ObjectMapper mapper = createMapper();

	private static ObjectMapper createMapper() {
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return om;
	}
	
	/**
	 * @param code
	 * @return the actual cardset (containing cards).
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static CardSet getCardSet(String code) throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
		return mapper.readValue(new URL(BASE_URL + code + ".json"), CardSet.class);
	}
	
	/**
	 * @return a list of metadata for every available set.
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static ArrayList<MetaCardSet> getAllMetaSets() throws JsonParseException, JsonMappingException, IOException {	
		return mapper.readValue(new URL(SETS_URL), new TypeReference<ArrayList<MetaCardSet>>() {});
	}
	
	/**
	 * @param file
	 * @return a CardBase object equivalent to the given file.
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static CardBase readCardBase(File file) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(file, CardBase.class);
	}
	
	/**
	 * Writes the provided CardBase to the provided file in JSON format.
	 * 
	 * @param file
	 * @param cardBase
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void writeCardBase(File file, CardBase cardBase) throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(file, cardBase);
	}
	
}
