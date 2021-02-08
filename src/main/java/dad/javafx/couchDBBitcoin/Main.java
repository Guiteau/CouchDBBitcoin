package dad.javafx.couchDBBitcoin;

import java.util.Optional;

import org.ektorp.CouchDbConnector;

import dad.javafx.model.ApiBitcoin;
import dad.javafx.model.BitcoinRepository;
import dad.javafx.model.CouchBitcoin;
import dad.javafx.utils.Connection;

public class Main {

	public static void main(String[] args) {
		
		Connection c = new Connection();
		c.connecToCouchDB();
		
		BitcoinRepository br = new BitcoinRepository(c.connectToDatabase("bitcoin"));
		CouchBitcoin cb = br.get("b95da5ff74517559180952eb2000387c");
		
		while (true)
		{
			Optional<ApiBitcoin> maybe_apiBitcoin = c.getAPIBitcoinCurrent();
			if (maybe_apiBitcoin.isPresent())
			{
				cb.loadFromApiBitcoin((maybe_apiBitcoin.get()));
			
				br.update(cb);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(cb);
			}
		}
		
	}

}
