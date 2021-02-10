package dad.javafx.couchdb.bitcoin.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

import dad.javafx.couchdb.bitcoin.api.model.CouchBitcoin;

public class BitcoinRepository extends CouchDbRepositorySupport<CouchBitcoin> {
	
	public BitcoinRepository(CouchDbConnector db) {
		super(CouchBitcoin.class, db);
	}
	
}
