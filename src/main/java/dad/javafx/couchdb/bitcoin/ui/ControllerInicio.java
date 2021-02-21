package dad.javafx.couchdb.bitcoin.ui;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.ektorp.CouchDbConnector;

import dad.javafx.couchdb.bitcoin.api.model.CarteraCouchDB;
import dad.javafx.couchdb.bitcoin.db.CarteraRepository;
import dad.javafx.couchdb.bitcoin.db.CouchDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

public class ControllerInicio implements Initializable{
	
    @FXML
    private GridPane inicioView;

    @FXML
    private TextField textField_usuario;

    @FXML
    private TextField textField_valorCartera;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button button_submit;

    @FXML
    private RadioButton radio_entrar;

    @FXML
    private ToggleGroup tgSign;

    @FXML
    private RadioButton radio_crearCuenta;

    @FXML
    private RadioButton radio_borrarCuenta;
    
    private ControllerAplicacion controllerAplicacion;
    
    private CouchDB couchDB;
    
	public ControllerInicio() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Inicio.fxml"));
		loader.setController(this);
		loader.load();
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		couchDB = new CouchDB();
		
		
		tgSign.selectedToggleProperty().addListener((o, ov, nv) -> {
			
			if(!radio_crearCuenta.isSelected()) {
				textField_valorCartera.setDisable(true);
			}else {
				textField_valorCartera.setDisable(false);
			}
			
		});
		
		
	}

	public GridPane getInicioView() {
		return inicioView;
	}
   
    @FXML
    void submitAction(ActionEvent event) {
    	
    	couchDB.setUsuario(textField_usuario.getText());
    	couchDB.setPassword(passwordField.getText());

    	if(radio_crearCuenta.isSelected()) {
    		
    		CarteraCouchDB cartera = new CarteraCouchDB();
    		
    		cartera.setNombre(textField_usuario.getText());
    		
    		cartera.setPassword(passwordField.getText());
    	
    		cartera.setDineroGanado(Double.parseDouble(textField_valorCartera.getText()));
    		
    		cartera.setCantidadBitcoins(0);
    		
    		couchDB.setCurrentCartera(cartera);
    	}
    	
    	if(radio_entrar.isSelected()) {
    		    		
    		Optional<CarteraCouchDB> optionalCartera = couchDB.getOptionalCartera(textField_usuario.getText(), passwordField.getText());
    		
    		if(optionalCartera.isPresent()) {
    			
    			try {
    				
					controllerAplicacion = new ControllerAplicacion();
					controllerAplicacion.setCarteraCouchDB(optionalCartera.get());
					
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
    			
    		}else {
    			Alert alert = new Alert(AlertType.WARNING);
    			alert.setTitle("Cuidado");
    			alert.setHeaderText("Error en los datos");
    			alert.setContentText("Usuario o contraseña incorrectos");
    			alert.showAndWait();
    		}
    		
    	}
    	
    	
    }

	
}
