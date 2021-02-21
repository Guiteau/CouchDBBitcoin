package dad.javafx.couchdb.bitcoin.ui;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import dad.javafx.couchdb.bitcoin.api.model.CarteraCouchDB;
import dad.javafx.couchdb.bitcoin.api.model.CouchBitcoin;
import dad.javafx.couchdb.bitcoin.db.CouchDB;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.converter.NumberStringConverter;

public class ControllerAplicacion implements Initializable {

	private CouchDB connection;

	private Task<Void> taskUploadValue;

	private Task<Void> taskGetValue;

	private DoubleProperty walletValue;

	private CouchBitcoin cb;

	@FXML
	private ImageView imageView_bitcoin;

	@FXML
	private Label bitcoinWallet;

	@FXML
	private GridPane root;

	@FXML
	private LineChart<String, Number> lineChart_bitcoins;

	@FXML
	private CategoryAxis axisX_lineChart;

	@FXML
	private NumberAxis axisY_lineChart;

	@FXML
	private TextField textField_valorCartera, textField_valorBitcoin, textField_inversion;

	@FXML
	private Button button_comprar, button_vender, button_start;

	private XYChart.Series<String, Number> series;

	public ControllerAplicacion() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View.fxml"));
		loader.setController(this);
		loader.load();

		this.connection = new CouchDB();

	}

	public void prepare() {
		bitcoinWallet.setText(String.valueOf(connection.getCurrentCartera().getCantidadBitcoins())); // el label recibe
																										// la cantidad
																										// de Bitcoins
																										// en CouchDB

		walletValue = new SimpleDoubleProperty(connection.getCurrentCartera().getDineroGanado()); // el DoubleProperty
																									// recibe de entrada
																									// la cantidad
																									// ganada que lleva

		textField_valorCartera.textProperty().bindBidirectional(walletValue, new NumberStringConverter());

		textField_valorBitcoin.textProperty().addListener((o, ov, nv) -> { // cuando el valor del bitcoin cambie
																			// modificamos el valor de walletValue
																			// (DoubleProperty)

			if (!nv.isEmpty()) {

				walletValue.set(Double.parseDouble(bitcoinWallet.getText()) * Double.parseDouble(nv.toString())); // cantidad
																													// bitcoins
																													// en
																													// cartera
																													// *
																													// nuevoValor
																													// del
																													// bitcoin
				connection.storeDineroGanadoCartera(walletValue.get());
			}

		});

		bitcoinWallet.textProperty().addListener((o, ov, nv) -> { // cuando cambie el valor de bitcoins en la cartera
																	// multiplicamos el valor de walletValue por la
																	// cantidad de bitcoins

			if (!nv.isEmpty()) {
				walletValue.set(
						Double.parseDouble(textField_valorBitcoin.getText()) * (Double.parseDouble(nv.toString()))); // valor
																														// del
																														// bitcoin
																														// *
																														// nuevoValor
																														// bitcoinWallet(label)
				connection.storeDineroGanadoCartera(walletValue.get());
				connection.storeCantidadBitcoinsCartera(Double.parseDouble(nv.toString()));
			}

		});

		textField_inversion.setEditable(false); // para que no se puedan comprar ni vender bitcoins sin conocer el valor
												// de mercado
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		imageView_bitcoin.setImage(new Image("/icons/bitcoinIcon.png"));

		axisX_lineChart.setLabel("Tiempo");

		lineChart_bitcoins.setTitle("Mercado Bitcoins " + Calendar.getInstance().get(Calendar.YEAR));

		lineChart_bitcoins.setCursor(Cursor.CROSSHAIR);

		series = new XYChart.Series<>();

	}

	@FXML
	void on_start(ActionEvent event) {

		textField_inversion.setEditable(true); // activamos el textField de nuevo

		CountDownLatch countdownlatch = new CountDownLatch(1);

		taskUploadValue = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				connection.run(countdownlatch);
				return null;
			}
		};

		taskGetValue = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					countdownlatch.await(); // esto no se ejecuta hasta que el hilo haya recogido un dato

					Platform.runLater(() -> lineChart_bitcoins.getData().add(series));

					cb = connection.getCurrent();

					ObjectProperty<CouchBitcoin> cb_prop = new SimpleObjectProperty<>(cb);

					cb_prop.addListener((o, ov, nv) -> {
						System.gc();
						textField_valorBitcoin.setText(String.valueOf(nv.getEuros()));

						long yourmilliseconds = System.currentTimeMillis();
						SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
						Date resultdate = new Date(yourmilliseconds);

						Platform.runLater(() -> fillData(nv.getEuros(), sdf.format(resultdate)));

					});

					while (true) {
						cb_prop.set(connection.getCurrent());
						Thread.sleep(1000);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}
		};

		taskUploadValue.exceptionProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null)
				newValue.printStackTrace();
		});

		taskGetValue.exceptionProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null)
				newValue.printStackTrace();
		});

		new Thread(taskUploadValue).start();
		new Thread(taskGetValue).start();

	}

	public void fillData(Number value, String time) {

		series.getData().add(new XYChart.Data<String, Number>(time, value));

	}

	public GridPane getRoot() {
		return root;
	}

	@FXML
	void on_buy(ActionEvent event) {

		if (!textField_valorBitcoin.getText().isEmpty() && !textField_inversion.getText().isEmpty()) {

			double sumaCompraBitcoins = Double.parseDouble(bitcoinWallet.getText())
					+ Double.parseDouble(textField_inversion.getText()); // sumamos el valor del label más el valor de
																			// la inversión

			bitcoinWallet.setText(String.valueOf(Math.round(sumaCompraBitcoins * 100.0) / 100.0));
		}

		textField_inversion.setText(""); // vacíamos el textField de la inversión

		/*
		 * Double investEuros = Double.parseDouble(textField_inversion.getText());
		 * walletValue.subtract(investEuros);
		 * bitcoinWallet.setText(String.valueOf(Double.parseDouble(textField_inversion.
		 * getText() )/ cb.getEuros()));
		 */
	}

	@FXML
	void on_sell(ActionEvent event) {

		if (!textField_valorBitcoin.getText().isEmpty() && !textField_inversion.getText().isEmpty()) {

			double restaCompraBitcoins = Double.parseDouble(bitcoinWallet.getText())
					- Double.parseDouble(textField_inversion.getText()); // restamos el valor del label menos el valor
																			// de la inversión

			if (restaCompraBitcoins >= 0) {

				bitcoinWallet.setText(String.valueOf(Math.round(restaCompraBitcoins * 100.0) / 100.0));
			}
		}

		textField_inversion.setText("");

		/*
		 * Double sellBitcoins = Double.parseDouble(bitcoinWallet.getText());
		 * 
		 * Double change = sellBitcoins * cb.getEuros(); walletValue.add(change);
		 */
	}

	public void setConnection(CouchDB couchDB) {
		this.connection = couchDB;
	}

}
