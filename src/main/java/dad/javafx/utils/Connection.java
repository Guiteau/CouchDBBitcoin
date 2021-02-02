package dad.javafx.utils;

import java.net.MalformedURLException;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

public class Connection {

	private HttpClient httpClient;

	public Connection() {

		try {
			httpClient = new StdHttpClient.Builder()
					.url("http://127.0.0.1:5984")
					.username("admin")
					.password("admin")
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

}
