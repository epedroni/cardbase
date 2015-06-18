package eu.equalparts.cardbase.gui;

import eu.equalparts.cardbase.data.Card;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class CardView extends ImageView {

	private static final String BASE_IMG_URL = "http://magiccards.info/scans/en/";
	
	public CardView(Card card) {
		super(makeUrl(card));
		
		addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				setVisible(false);
			}
		});
		
	}

	public static String makeUrl(Card card) {
		return BASE_IMG_URL + card.imageCode + "/" + card.number + ".jpg";
	}
	
}
