package dad.javafx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import dad.javafx.model.BitcoinRepository;
import dad.javafx.model.CouchBitcoin;
import dad.javafx.utils.Connection;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.adapter.JavaBeanBooleanPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanDoubleProperty;
import javafx.beans.property.adapter.JavaBeanDoublePropertyBuilder;
import javafx.beans.property.adapter.JavaBeanStringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.NumberStringConverter;

public class Controller implements Initializable {

	private Connection connection;
	@FXML
	private GridPane root;
	
	
	private Task<Void> taskUploadValue;
	private Task<Void> taskGetValue;
	
	private ObjectProperty<CouchBitcoin> couchbitcoin_property;

	@FXML
	private LineChart<Number, Number> lineChart_bitcoins;

	@FXML
	private CategoryAxis axisX_lineChart;

	@FXML
	private NumberAxis axisY_lineChart;

	@FXML
	private TextField textField_valorCartera, textField_valorBitcoin, textField_inversion;

	@FXML
	private Button button_comprar, button_vender, button_start;

	private XYChart.Series series;

	public Controller() throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View.fxml"));
		loader.setController(this);
		loader.load();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		axisX_lineChart.setLabel("Tiempo");

		lineChart_bitcoins.setTitle("Mercado Bitcoins " + Calendar.getInstance().get(Calendar.YEAR));

		series = new XYChart.Series<>();
		
		connection = new Connection();
		
		couchbitcoin_property = new SimpleObjectProperty<>();

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
					
					CouchBitcoin cb = connection.bajandoDatosdeCouchDB();
										
					JavaBeanDoubleProperty prop = JavaBeanDoublePropertyBuilder.create()
							.bean(cb)
							.name("euros")
							.build();
					
					
					
					//prop.bind(textField_valorBitcoin.textProperty());
					//textField_valorBitcoin.textProperty().bind(prop);
					//Bindings.bindBidirectional(textField_valorBitcoin.textProperty(), prop, new NumberStringConverter());
					couchbitcoin_property.set(cb);
					//connection.updatingCouchBitcoin(cb);
					
					while(true)
					{
						cb = connection.bajandoDatosdeCouchDB();
						System.out.println(cb.getEuros());
						System.out.println("hola");
						textField_valorBitcoin.setText(String.valueOf(cb.getEuros()));
					}
					
					/*
					prop.addListener((o, ov, nv)->{
						if (nv!=null)
						{
							System.out.println(nv + nv.toString());
							textField_valorBitcoin.setText(nv.toString());
						}
							
						
					});
					*/
					
					//connection.updatingCouchBitcoin();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				
				
				return null;
			}
		};
		
		taskUploadValue.exceptionProperty().addListener((observable, oldValue, newValue) ->  {
			  if(newValue != null) {
			    Exception ex = (Exception) newValue;
			    ex.printStackTrace();
			  }
			});
		
		taskGetValue.exceptionProperty().addListener((observable, oldValue, newValue) ->  {
			  if(newValue != null) {
			    Exception ex = (Exception) newValue;
			    ex.printStackTrace();
			  }
			});

		new Thread(taskUploadValue).start();
		new Thread(taskGetValue).start();
    }

	public void fillData(Double value, Double time) {

		series.getData().add(new XYChart.Data(time, value));

	}

	public GridPane getRoot() {
		return root;
	}

}
