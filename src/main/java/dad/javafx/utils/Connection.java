package dad.javafx.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

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
import dad.javafx.model.BitcoinRepository;
import dad.javafx.model.Bpi;
import dad.javafx.model.CouchBitcoin;

public class Connection{

	@JsonIgnoreProperties(ignoreUnknown = true)

	private HttpClient httpClient;
	private ObjectMapper objectMapper;
	private CouchDbConnector connector;
	private BitcoinRepository br;
	private CouchBitcoin current_value;

	public Connection() {

		objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		this.connecToCouchDB();

		connector = this.connectToDatabase("bitcoin");
		
		br = new BitcoinRepository(connector);
	}

	private Optional<ApiBitcoin> getAPIBitcoinCurrent() {

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

	private Optional<Bpi> getAPIBitcoinHistorical() {

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

	private void connecToCouchDB() {

		try {
			httpClient = new StdHttpClient.Builder().url("http://127.0.0.1:5984").username("admin").password("sofasito")
					.build();
		} catch (MalformedURLException e) {

			System.out.println("Fallo al crear la conexión a CouchDB. Clase Connection falló.");

			e.printStackTrace();
		}

	}

	private CouchDbConnector connectToDatabase(String databaseName) {

		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);

		CouchDbConnector connector = new StdCouchDbConnector(databaseName, dbInstance);

		connector.createDatabaseIfNotExists();

		return connector;

	}

	private void subirValorBitcoinCouch(ApiBitcoin apiBitcoin) {
		CouchBitcoin cb = br.get("current_value");

		cb.loadFromApiBitcoin(apiBitcoin);
		
		br.update(cb);

	}

	public CouchBitcoin bajandoDatosdeCouchDB()
	{
		
		return br.get("current_value");
	}
	
	public void updatingCouchBitcoin(CouchBitcoin current_value)
	{
		while(true)
		{
			current_value = br.get("current_value");
		}
	}
	
	public void updateCouchBitcoin(CouchBitcoin current_value)
	{
		current_value = null;
		current_value = br.get("current_value");
	}
	

	public void run(CountDownLatch countdownlatch) {

		Optional<ApiBitcoin> maybeBitcoin;
		
		if (!br.contains("current_value"))
			br.add(new CouchBitcoin());
		
		while (true) {
			System.out.println("HIlo 1");
			try {
				maybeBitcoin = this.getAPIBitcoinCurrent();
				
				if (maybeBitcoin.isPresent())
					subirValorBitcoinCouch(maybeBitcoin.get());		
				
				countdownlatch.countDown();
				
				
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}

}
