package eu.equalparts.cardbase.decks;

import eu.equalparts.cardbase.containers.ReferenceCardContainer;

public class ReferenceDeck extends ReferenceCardContainer {
	private String name = "";

	public ReferenceDeck() {}
	
	public ReferenceDeck(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
