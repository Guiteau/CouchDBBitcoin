package dad.javafx.couchdb.bitcoin.db;

import java.net.MalformedURLException;
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
import dad.javafx.couchdb.bitcoin.api.model.CouchBitcoin;

public class CouchDB {

	@JsonIgnoreProperties(ignoreUnknown = true)

	private CouchDbConnector connector;
	private BitcoinRepository br;

	public CouchDB() {
		HttpClient dbClient = this.connectToCouchDB();
		connector = this.connectToDatabase(dbClient, "bitcoin");
		br = new BitcoinRepository(connector);
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
