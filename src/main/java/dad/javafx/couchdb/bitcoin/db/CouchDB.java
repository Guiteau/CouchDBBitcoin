package dad.javafx.couchdb.bitcoin.db;

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

import dad.javafx.couchdb.bitcoin.api.CoinDeskApi;
import dad.javafx.couchdb.bitcoin.api.model.ApiBitcoin;
import dad.javafx.couchdb.bitcoin.api.model.CarteraCouchDB;
import dad.javafx.couchdb.bitcoin.api.model.CouchBitcoin;

public class CouchDB {

	@JsonIgnoreProperties(ignoreUnknown = true)

	private CouchDbConnector connector;
	private BitcoinRepository br;
	private CarteraRepository cr;
	private String usuario;
	private String password;

	public String getUsuario() {
		return usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public CouchDB() {
		HttpClient dbClient = this.connectToCouchDB();
		connector = this.connectToDatabase(dbClient, "bitcoin");
		br = new BitcoinRepository(connector);
		cr = new CarteraRepository(connector);
		this.usuario = "";
		this.password = "";
	}

	private HttpClient connectToCouchDB() {
		try {
			return new StdHttpClient.Builder()
								.url("http://127.0.0.1:5984")
								.username("admin")
								.password("admin")
								.build();
		} catch (MalformedURLException e) {
			System.out.println("Fallo al crear la conexión a CouchDB. Clase Connection falló.");
			e.printStackTrace();
		}
		return null;
	}

	private CouchDbConnector connectToDatabase(HttpClient dbClient, String databaseName) {
		CouchDbInstance dbInstance = new StdCouchDbInstance(dbClient);
		CouchDbConnector connector = new StdCouchDbConnector(databaseName, dbInstance);
		connector.createDatabaseIfNotExists();
		return connector;
	}
	
	
	public boolean deleteCartera(String usuario, String password) {
		
		Optional<CarteraCouchDB> optionalCartera = getOptionalCartera(usuario, password);
					
		if(optionalCartera.isPresent()) {
			
			cr.remove(optionalCartera.get());
			
			return true;
			
		}
		
		return false;
		
	}

	public void setNewCartera(CarteraCouchDB cartera) {
			
		cr.add(cartera);
		
	}
	
	
	public CarteraCouchDB getCurrentCartera() {
		
		return cr.get(usuario);
	}
	
	// Recupera una cartera ya existente
	public Optional<CarteraCouchDB> getOptionalCartera(String usuario, String password) {
		
		CarteraCouchDB cartera = cr.get(usuario);
		
		this.usuario = usuario;
		this.password = password;
		
		Optional<CarteraCouchDB> optionalCartera = Optional.ofNullable(cartera);
		
		if(cartera.getPassword().compareTo(password) == 0) {
			
			return optionalCartera;
		}
		
		return Optional.empty();
	}
	
	
	public void storeCantidadBitcoinsCartera(double cantidadBitcoins) {
		
		CarteraCouchDB cartera = getCurrentCartera();
		
		cartera.setCantidadBitcoins(cantidadBitcoins);

		cr.update(cartera);
		
	}
	
	public void storeDineroGanadoCartera(double dineroBitcoins) {
		
		CarteraCouchDB cartera = getCurrentCartera();
		
		cartera.setDineroGanado(dineroBitcoins);
		
		cr.update(cartera);
		
	}
	
	public double getCantidadDineroGanadoCartera() {
		
		return this.getCurrentCartera().getDineroGanado();
		
	}
	
	public double getCantidadBitcoinsCartera() {
		
		return this.getCurrentCartera().getCantidadBitcoins();
		
	}

	private void storeBitcoin(ApiBitcoin apiBitcoin) {
		CouchBitcoin cb = br.get("current_value");  
		cb.loadFromApiBitcoin(apiBitcoin);
		br.update(cb);
	}

	public CouchBitcoin getCurrent() {
		return br.get("current_value");
	}

	public void run(CountDownLatch countdownlatch) {

		if (!br.contains("current_value")) {
			br.add(new CouchBitcoin());
		}

		while (true) {

			ApiBitcoin bitcoin = CoinDeskApi.getAPIBitcoinCurrent();

			if (bitcoin != null) {
				storeBitcoin(bitcoin);
			}

			countdownlatch.countDown();

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}
