package eu.equalparts.cardbase.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import eu.equalparts.cardbase.data.Card;
import eu.equalparts.cardbase.utils.MTGUniverse;

public class CardbaseGUI extends Application {

	/**
	 * Run the GUI.
	 * 
	 * @param args arguments passed down to {@code Application.launch()}.
	 */
	public static void main(String... args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Card card = MTGUniverse.getCard("M15", "281");
		ImageView test = new ImageView(CardImageView.makeUrl(card));
		
		Pane parent = new Pane();
		
		parent.getChildren().add(test);
		
		primaryStage.setScene(new Scene(parent));
		
		primaryStage.setTitle("Cardbase");
		primaryStage.show();
	}
}
