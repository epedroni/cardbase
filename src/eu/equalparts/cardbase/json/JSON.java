package eu.equalparts.cardbase.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import eu.equalparts.cardbase.card.Card;
import eu.equalparts.cardbase.card.FullCardSet;

/**
 * This class simply holds an {@code ObjectMapper} to be used whenever JSON must be parsed.
 * In the future it may be removed in favour of individual mappers for each function.
 * 
 * @author Eduardo Pedroni
 */
public final class JSON {
	
	/**
	 * The Jackson {@code ObjectMapper} which parses fetched JSON files.
	 */
	public static final ObjectMapper mapper = createMapper();

	/**
	 * Private constructor, this class is not to be instantiated.
	 */
	private JSON() {}
	
	/**
	 * Instantiate and configure Jackson mapper statically. 
	 * 
	 * @return the {@code ObjectMapper}, ready to use.
	 */
	private static ObjectMapper createMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		// classes don't necessarily use all json fields
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// disable auto detection
		objectMapper.disable(MapperFeature.AUTO_DETECT_CREATORS,
	            MapperFeature.AUTO_DETECT_FIELDS,
	            MapperFeature.AUTO_DETECT_GETTERS,
	            MapperFeature.AUTO_DETECT_IS_GETTERS);
		
		SimpleModule sm = new SimpleModule();
		sm.addDeserializer(Card.class, new CardDeserializer());
		sm.addSerializer(Card.class, new CardSerializer());
		sm.addDeserializer(FullCardSet.class, new FullCardSetDeserializer());
		
		objectMapper.registerModule(sm);
		return objectMapper;
	}
}
