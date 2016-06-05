package eu.equalparts.cardbase;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.cardstorage.StandaloneCardContainer;
import eu.equalparts.cardbase.utils.JSON;

/**
 * Provides a variety of utility methods to interact with an optionally loaded cardbase.
 * 
 * @author Eduardo Pedroni
 */
public class Cardbase extends StandaloneCardContainer {
	
	/**
	 * The decks which have been saved along with this collection of cards.
	 */
//	@JsonProperty private Map<Integer, Object> decks;
	
	/**
	 * Creates a clean cardbase.
	 */
	public Cardbase() {
		
	}
	
	/**
	 * Creates and returns a cardbase with the contents of a file.
	 *
	 * @param cardbaseFile the cardbase JSON to load.
	 * 
	 * @throws JsonParseException if the specified file does not contain valid JSON.
	 * @throws JsonMappingException if the specified file structure does not match that of {@code Cardbase}.
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 * 
	 * @return the initialised {@code Cardbase} object.
	 */
	public static Cardbase load(File cardbaseFile) throws JsonParseException, JsonMappingException, IOException {
		return JSON.mapper.readValue(cardbaseFile, Cardbase.class);
	}
	
	/**
	 * Writes the {@code Cardbase} instance to the provided file in JSON format.
	 * 
	 * @param file the file to which to write the {@code Cardbase}.
	 * @param cardbase the {@code Cardbase} to write out.
	 * 
	 * @throws JsonGenerationException if the data structure given does not generate valid JSON.
	 * @throws JsonMappingException if the data structure given does not generate valid JSON as well?
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 */
	public void write(File outputFile) throws JsonGenerationException, JsonMappingException, IOException {
		JSON.mapper.writeValue(outputFile, this);
	}
}
