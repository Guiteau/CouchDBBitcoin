package dad.javafx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

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

public class Controller implements Initializable {

	@FXML
	private GridPane root;

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
		

	}

	public void fillData(Double value, Double time) {

		series.getData().add(new XYChart.Data(time, value));

	}

	public GridPane getRoot() {
		return root;
	}

}
