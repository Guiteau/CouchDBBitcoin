package dad.javafx.couchdb.bitcoin.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application{
	
	ControllerAplicacion controller;
	
	ControllerInicio controllerInicio;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		controllerInicio = new ControllerInicio();
		
		controllerInicio.setStage(primaryStage);

//		controller = new ControllerAplicacion();
		
		Scene escena = new Scene(controllerInicio.getInicioView());
		
		primaryStage.getIcons().add(new Image("/icons/couchdb.png"));
		primaryStage.setScene(escena);
		primaryStage.setTitle("Proyecto CouchDB\t");
		primaryStage.show();
	
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}

