package dad.javafx.couchdb.bitcoin.db;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import dad.javafx.couchdb.bitcoin.api.model.CarteraCouchDB;
import dad.javafx.couchdb.bitcoin.ui.ControllerAplicacion;

public class ConsumerExample {
	
	public void consumerMethod() {
		
		CouchDB couchDB = new CouchDB();
		
		boolean access = false;
		
		Function<CarteraCouchDB, Optional<CarteraCouchDB>> acceso = x -> {
			
    		Optional<CarteraCouchDB> optionalCartera = couchDB.getOptionalCartera(x.getNombre(), x.getPassword());	
    		
    		return optionalCartera;	   			
			
		};
		
		
	}
	

}
