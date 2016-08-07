package eu.equalparts.cardbase.comparator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public final class SpecialFields {
	
	private SpecialFields() {}
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface DirtyNumber {}
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Rarity {}
}
