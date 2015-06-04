package eu.equalparts.cardbase.data;

public class MetaCardSet {

	private String name = "";
	private String code = "";
	private String releaseDate = "";
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getReleaseDate() {
		return releaseDate;
	}
	
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	@Override
	public String toString() {
		return String.format("%1$-12s : %2$s", code, name, releaseDate);
	}
	
}
