package dad.javafx.couchdb.bitcoin.api;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import dad.javafx.couchdb.bitcoin.api.model.ApiBitcoin;
import dad.javafx.couchdb.bitcoin.api.model.Bpi;

public class CoinDeskApi {

	static {
		Unirest.setObjectMapper(new com.mashape.unirest.http.ObjectMapper() {
			private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			@Override
			public String writeValue(Object value) {
				try {
					return mapper.writeValueAsString(value);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return mapper.readValue(value, valueType);
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}
	
	public static ApiBitcoin getAPIBitcoinCurrent() {
		try {
			return Unirest.get("https://api.coindesk.com/v1/bpi/currentprice.json")
					.queryString("hostname", "1")
					.queryString("format", "1")
					.asObject(ApiBitcoin.class)
					.getBody();
		} catch (UnirestException e) {
			e.printStackTrace();			
		}
		return null;
	}
	

	public static Bpi getAPIBitcoinHistorical() {
		try {
			return Unirest.get("") // TODO falta URL
						.queryString("hostname", "1")
						.queryString("format", "1")
						.asObject(Bpi.class)
						.getBody();

		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return null;
	}

}
