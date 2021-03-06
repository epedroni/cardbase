package eu.equalparts.cardbase.card;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardSetInformation {

	@JsonProperty private String name;
	@JsonProperty private String code;
	@JsonProperty private String releaseDate;
	
	/**
	 * @return the set's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the set code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the set's release date.
	 */
	public String getReleaseDate() {
		return releaseDate;
	}

	@Override
	public String toString() {
		return String.format("%1$-12s : %2$s", code, name, releaseDate);
	}
}
