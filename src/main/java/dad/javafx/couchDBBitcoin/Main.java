package dad.javafx.couchDBBitcoin;

import java.util.Optional;

import org.ektorp.CouchDbConnector;

import com.fasterxml.jackson.databind.ObjectMapper;

import dad.javafx.model.ApiBitcoin;
import dad.javafx.model.CouchBitcoin;
import dad.javafx.utils.Connection;

public class Main {

	public static void main(String[] args) {
		
		Connection c = new Connection();
		
		ApiBitcoin a = new ApiBitcoin();
		
		CouchDbConnector connector;
		
		c.connecToCouchDB();
		c.connectToDatabase("persons");
		System.out.println(c.getAPIBitcoinCurrent());
		
		connector = c.connectToDatabase("Bitcoin");			
		
		
		Optional<ApiBitcoin> maybeBitcoin = c.getAPIBitcoinCurrent();
		if (maybeBitcoin.isPresent()) {
			
			CouchBitcoin cBitcoin = new CouchBitcoin(maybeBitcoin.get());
			
			System.out.println(cBitcoin);
			connector.create(cBitcoin);
		}

	}

}
