package eu.equalparts.cardbase.io;

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

import eu.equalparts.cardbase.data.Card;
import eu.equalparts.cardbase.data.FullCardSet;
import eu.equalparts.cardbase.data.Cardbase;
import eu.equalparts.cardbase.data.CardSet;

/**
 * Class responsible for all I/O operations, such as fetching content from remote servers, reading from
 * and writing to files.
 * <br>
 * All relevant methods here are static, this class should not be instantiated.
 * 
 * @author Eduardo Pedroni
 */
public class IO {
	
	/**
	 * The base URL from where the information is fetched.
	 */
	private static final String BASE_URL = "http://mtgjson.com/json/";
	/**
	 * The URL where the complete list of sets is fetched.
	 */
	private static final String SETS_URL = BASE_URL + "SetList.json";
	/**
	 * The Jackson ObjectMapper which parses fetched JSON files.
	 */
	private static final ObjectMapper mapper = createMapper();

	/**
	 * Private constructor, this class is not to be instantiated.
	 */
	private IO() {}
	
	/**
	 * Instantiate and configure Jackson mapper statically. 
	 * 
	 * @return the {@code ObjectMapper}, ready to use.
	 */
	private static ObjectMapper createMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		// TODO decide what to do about this
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}

	/**
	 * Fetches a complete set by code, where the code is a short string determined by WotC.
	 * The full list of valid codes can be acquired with {@code IO.getCardSetList()}.
	 * 
	 * @param setCode the code of the set to be fetched.
	 * @return the complete specified set in a {@code FullCardSet} object.
	 * 
	 * @throws JsonParseException if underlying input contains invalid content of type JsonParser supports (JSON for default case).
	 * @throws JsonMappingException if the input JSON structure does not match structure expected for result type (or has other mismatch issues).
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 */
	public static FullCardSet getFullCardSet(String setCode) throws JsonParseException, JsonMappingException, IOException {
		FullCardSet fullCardSet = mapper.readValue(new URL(BASE_URL + setCode + ".json"), FullCardSet.class);
		// MTG JSON does not include set code in the card information, but it is useful for sorting
		for (Card card : fullCardSet.getCards()) {
			card.setCode = setCode;
		}
		return fullCardSet;
	}
	
	/**
	 * @return a list of all card sets in the form of {@code CardSet} objects.
	 * 
	 * @throws JsonParseException if underlying input contains invalid content of type JsonParser supports (JSON for default case).
	 * @throws JsonMappingException if the input JSON structure does not match structure expected for result type (or has other mismatch issues).
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 */
	public static ArrayList<CardSet> getCardSetList() throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(new URL(SETS_URL), new TypeReference<ArrayList<CardSet>>() {});
	}
	
	/**
	 * Attemps to the read the specified file into a {@code Cardbase.} Exceptions are thrown as outlined below.
	 * 
	 * @param file the file to read.
	 * @return a {@code Cardbase} object equivalent to the given file.
	 * 
	 * @throws JsonParseException if underlying input contains invalid content of type JsonParser supports (JSON for default case).
	 * @throws JsonMappingException if the input JSON structure does not match structure expected for result type (or has other mismatch issues).
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 */
	public static Cardbase readCardbase(File file) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(file, Cardbase.class);
	}
	
	/**
	 * Writes the provided {@code Cardbase} to the provided file in JSON format.
	 * 
	 * @param file the file to which to write the {@code Cardbase}.
	 * @param cardbase the {@code Cardbase} to write out.
	 * 
	 * @throws JsonGenerationException if the data structure given does not generate valid JSON.
	 * @throws JsonMappingException if the data structure given does not generate valid JSON as well?
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 */
	public static void writeCardbase(File file, Cardbase cardbase) throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(file, cardbase);
	}
	
}
