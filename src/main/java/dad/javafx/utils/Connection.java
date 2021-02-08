package dad.javafx.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import dad.javafx.model.ApiBitcoin;
import dad.javafx.model.Bpi;

public class Connection implements Runnable {

	@JsonIgnoreProperties(ignoreUnknown = true)

	private HttpClient httpClient;
	private ObjectMapper objectMapper;

	public Connection() {

		objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
	/*	CouchDbConnector connector;
		
		connector = this.connectToDatabase("BitcoinDB");

		
		
		connector.create();*/

	}

	public Optional<ApiBitcoin> getAPIBitcoinCurrent() {

		String response = "";

		Optional<ApiBitcoin> maybeApiBitcoin = Optional.empty();

		try {
			response = Unirest.get("https://api.coindesk.com/v1/bpi/currentprice.json").queryString("hostname", "1")
					.queryString("format", "1").asString().getBody();

			maybeApiBitcoin = Optional.ofNullable(objectMapper.readValue(response, ApiBitcoin.class));

		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return maybeApiBitcoin;

	}

	public Optional<Bpi> getAPIBitcoinHistorical() {

		String response = "";

		Optional<Bpi> maybeApiBitcoinHistorical = Optional.empty();

		try {
			response = Unirest.get("") // falta URL
					.queryString("hostname", "1").queryString("format", "1").asString().getBody();

			maybeApiBitcoinHistorical = Optional.ofNullable(objectMapper.readValue(response, Bpi.class));

		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return maybeApiBitcoinHistorical;

	}

	public void connecToCouchDB() {

		try {
			httpClient = new StdHttpClient.Builder().url("http://127.0.0.1:5984").username("admin").password("sofasito")
					.build();
		} catch (MalformedURLException e) {

			System.out.println("Fallo al crear la conexión a CouchDB. Clase Connection falló.");

			e.printStackTrace();
		}

	}

	public CouchDbConnector connectToDatabase(String databaseName) {

		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);

		CouchDbConnector connector = new StdCouchDbConnector(databaseName, dbInstance);

		connector.createDatabaseIfNotExists();

		return connector;

	}

	public void subirValorBitcoinCouch(ApiBitcoin apiBitcoin) {  
		
		//TODO COMPLETAR
		
		apiBitcoin.getBpi().getEUR();
		
	}

	@Override
	public void run() {

		Optional<ApiBitcoin> optionalApiBitcoin;

		while (true) {

			try {
				optionalApiBitcoin = this.getAPIBitcoinCurrent();

				if (optionalApiBitcoin.isPresent())

					this.subirValorBitcoinCouch(optionalApiBitcoin.get());

				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
