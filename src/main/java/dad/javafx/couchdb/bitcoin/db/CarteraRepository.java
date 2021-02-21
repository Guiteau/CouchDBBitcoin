package dad.javafx.couchdb.bitcoin.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

import dad.javafx.couchdb.bitcoin.api.model.CarteraCouchDB;

public class CarteraRepository  extends CouchDbRepositorySupport<CarteraCouchDB> {
	
	public CarteraRepository(CouchDbConnector db) {
		super(CarteraCouchDB.class, db);
	}

}
