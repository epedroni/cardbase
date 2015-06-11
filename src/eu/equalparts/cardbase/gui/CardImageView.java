package eu.equalparts.cardbase.gui;

import eu.equalparts.cardbase.data.Card;
import javafx.scene.image.ImageView;

public class CardImageView extends ImageView {

	private static final String BASE_IMG_URL = "http://magiccards.info/scans/en/";
	
	public CardImageView(Card card) {
		super(makeUrl(card));
	}

	public static String makeUrl(Card card) {
		return BASE_IMG_URL + card.imageCode + "/" + card.number + ".jpg";
	}
	
}
