package dad.javafx.model;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

public class BitcoinRepository extends CouchDbRepositorySupport<CouchBitcoin> {
	public BitcoinRepository(CouchDbConnector db)
	{
		super(CouchBitcoin.class, db);
	}
}
