package eu.equalparts.cardbase.data;

public class MetaCardSet {

	public String name = "";
	public String code = "";
	public String releaseDate = "";
	
	@Override
	public String toString() {
		return String.format("%1$-12s : %2$s", code, name, releaseDate);
	}
	
}
