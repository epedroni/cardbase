package eu.equalparts.cardbase.data;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.query.IO;

public class CardBaseManager {
	
	private ArrayList<MetaCardSet> metaSets;

	public static void main(String... args) {
		
	}
	
	/**
	 * Parse a cardbase file and create an associated CardBase object.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public CardBaseManager(File cardbase) throws JsonParseException, JsonMappingException, IOException {
		metaSets = IO.getAllMetaSets();
	}

	/**
	 * Create an empty CardBase.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public CardBaseManager() throws JsonParseException, JsonMappingException, IOException {
		metaSets = IO.getAllMetaSets();
	}
	
	public ArrayList<MetaCardSet> getAllMetaSets() {
		return metaSets;
	}
	
}
