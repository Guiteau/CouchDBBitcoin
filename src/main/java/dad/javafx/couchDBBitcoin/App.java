package dad.javafx.couchDBBitcoin;

import dad.javafx.controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application{
	
	Controller controller;

	@Override
	public void start(Stage primaryStage) throws Exception {


		controller = new Controller();
		
		Scene escena = new Scene(controller.getRoot(), 700, 400);
		
	//	primaryStage.getIcons().add(new Image("couchgDB.png"));
		primaryStage.setScene(escena);
		primaryStage.setTitle("Proyecto CouchDB\t");
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {

		launch(args);
	}

}
