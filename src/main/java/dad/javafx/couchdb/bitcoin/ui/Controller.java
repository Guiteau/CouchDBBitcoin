package dad.javafx.couchdb.bitcoin.ui;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import dad.javafx.couchdb.bitcoin.api.model.CouchBitcoin;
import dad.javafx.couchdb.bitcoin.db.CouchDB;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.adapter.JavaBeanDoubleProperty;
import javafx.beans.property.adapter.JavaBeanDoublePropertyBuilder;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Controller implements Initializable {

	private CouchDB connection;

	private Task<Void> taskUploadValue;
	
	private Task<Void> taskGetValue;
	
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

	public Controller() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		axisX_lineChart.setLabel("Tiempo");

		lineChart_bitcoins.setTitle("Mercado Bitcoins " + Calendar.getInstance().get(Calendar.YEAR));

		lineChart_bitcoins.setCursor(Cursor.CROSSHAIR);
		
		series = new XYChart.Series<>();

		connection = new CouchDB();

	}

	@FXML
	void on_start(ActionEvent event) {

		CountDownLatch countdownlatch = new CountDownLatch(1);

		taskUploadValue = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				connection.run(countdownlatch);
				return null;
			}
		};
		
		taskGetValue = new Task<Void>()
		{
			@Override
			protected Void call() throws Exception {
				try {
					countdownlatch.await(); // esto no se ejecuta hasta que el hilo haya recogido un dato
					
					Platform.runLater(() -> lineChart_bitcoins.getData().add(series));
										
					CouchBitcoin cb = connection.getCurrent();

					JavaBeanDoubleProperty prop = JavaBeanDoublePropertyBuilder.create().bean(cb).name("euros").build();
		
					ObjectProperty<CouchBitcoin> cb_prop = new SimpleObjectProperty<>(cb);
										
					cb_prop.addListener((o, ov, nv)-> {
					textField_valorBitcoin.setText(String.valueOf(nv.getEuros()));
					
					long yourmilliseconds = System.currentTimeMillis();
					SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");    
					Date resultdate = new Date(yourmilliseconds);
					
					fillData(nv.getEuros(), sdf.format(resultdate));

					});
					
					while(true)
					{
						cb_prop.set(connection.getCurrent());
						Thread.sleep(1000);
					}
					
				} catch (Exception e)
				{
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

}
