package dad.javafx.couchdb.bitcoin.ui;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;

import org.ektorp.CouchDbConnector;

import dad.javafx.couchdb.bitcoin.api.model.CarteraCouchDB;
import dad.javafx.couchdb.bitcoin.db.CarteraRepository;
import dad.javafx.couchdb.bitcoin.db.CouchDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ControllerInicio implements Initializable {

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

	private Stage primaryStage;

	private boolean access;

	public ControllerInicio() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Inicio.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		access = false;

		couchDB = new CouchDB();

		tgSign.selectedToggleProperty().addListener((o, ov, nv) -> {

			if (!radio_crearCuenta.isSelected()) {
				textField_valorCartera.setDisable(true);
			} else {
				textField_valorCartera.setDisable(false);
			}

		});

	}

	public GridPane getInicioView() {
		return inicioView;
	}

	@FXML
	void submitAction(ActionEvent event) {

		access = false;

		CarteraCouchDB carteraFormulario = new CarteraCouchDB();

		Consumer<CarteraCouchDB> acceso = cartera -> {
			Optional<CarteraCouchDB> optionalCartera = couchDB.getOptionalCartera(cartera.getNombre(),
					cartera.getPassword());

			if (optionalCartera.isPresent()) {

				try {

					controllerAplicacion = new ControllerAplicacion();
					controllerAplicacion.setConnection(couchDB);
					controllerAplicacion.prepare();
					/*
					 * Scene escena = new Scene(controllerAplicacion.getRoot());
					 * primaryStage.setScene(escena); primaryStage.show();
					 */
					access = true;

				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Cuidado");
				alert.setHeaderText("Error en los datos");
				alert.setContentText("Usuario o contraseña incorrectos");
				alert.showAndWait();
			}
		};

		if (radio_crearCuenta.isSelected()) {

			carteraFormulario.setNombre(textField_usuario.getText());

			carteraFormulario.setPassword(passwordField.getText());

			carteraFormulario.setDineroGanado(Double.parseDouble(textField_valorCartera.getText()));

			carteraFormulario.setCantidadBitcoins(0);

			couchDB.setNewCartera(carteraFormulario);

			acceso.accept(carteraFormulario);

		}

		if (radio_entrar.isSelected()) {

			carteraFormulario.setNombre(textField_usuario.getText());

			carteraFormulario.setPassword(passwordField.getText());

			acceso.accept(carteraFormulario);

		}

		if (radio_borrarCuenta.isSelected()) {

			if (couchDB.deleteCartera(textField_usuario.getText(), passwordField.getText())) {

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Información");
				alert.setHeaderText(null);
				alert.setContentText("¡Cartera borrada con éxito!");

				alert.showAndWait();

			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Cuidado");
				alert.setHeaderText("Error en los datos");
				alert.setContentText("Usuario o contraseña incorrectos");
				alert.showAndWait();
			}
		}

		if ((radio_entrar.isSelected() || radio_crearCuenta.isSelected()) && access) {

			Scene escena = new Scene(controllerAplicacion.getRoot());

			primaryStage.getIcons().add(new Image("/icons/couchdb.png"));
			primaryStage.setScene(escena);
			primaryStage.setTitle("Proyecto CouchDB\t");
			primaryStage.show();
		}
	}

	public void setStage(Stage stage) {

		primaryStage = stage;

	}

}
