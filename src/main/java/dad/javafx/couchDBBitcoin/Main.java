package dad.javafx.couchDBBitcoin;

import org.ektorp.CouchDbConnector;

import com.fasterxml.jackson.databind.ObjectMapper;

import dad.javafx.model.ApiBitcoin;
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
		
		connector.create(c.getAPIBitcoinCurrent().get());
		
	}

}
