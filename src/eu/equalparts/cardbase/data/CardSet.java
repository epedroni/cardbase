package eu.equalparts.cardbase.data;

public class CardSet {

	private String name = "";
	private String code = "";
	private String releaseDate = "";
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the releaseDate
	 */
	public String getReleaseDate() {
		return releaseDate;
	}

	@Override
	public String toString() {
		return String.format("%1$-12s : %2$s", code, name, releaseDate);
	}
	
}
