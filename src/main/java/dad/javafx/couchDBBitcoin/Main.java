package dad.javafx.couchDBBitcoin;

import dad.javafx.utils.Connection;

public class Main {

	public static void main(String[] args) {
		
		Connection c = new Connection();
		
		Thread threadConnectionBitcoin = new Thread(c);
		
		threadConnectionBitcoin.start();
		
		App.main(args);

	}

}
