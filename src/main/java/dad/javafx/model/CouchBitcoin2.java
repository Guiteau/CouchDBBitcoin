package dad.javafx.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class CouchBitcoin2 {
		
		@JsonIgnoreProperties(ignoreUnknown = true)
		
		@JsonProperty("_id")
		private final String cid = "current_value";
		private DoubleProperty usd;
		private  DoubleProperty euros;
		
		public DoubleProperty getUsd() {
			return usd;
		}

		public DoubleProperty getEuros() {
			return euros;
		}

		@JsonProperty("_rev")
		private String revision;

		private Time time;

		public String getCid()
		{
			return cid;
		}
		
		private void init()
		{
			usd = new SimpleDoubleProperty();
			euros = new SimpleDoubleProperty();
		}
		
		
		public CouchBitcoin2()
		{
			init();
		}
		
		public CouchBitcoin2(ApiBitcoin apibitcoin) {
			init();
			loadFromApiBitcoin(apibitcoin);	
		}
		
		
		

		
		
		public String getRevision() {
			return revision;
		}

		

		public void setRevision(String revision) {
			this.revision = revision;
		}

		@Override
		public String toString() {
			return "CouchBitcoin [id=" + cid + ", revision=" + revision + ", time=" + time + ", usd=" + usd + ", euros="
					+ euros + "]";
		}

		
		
		
		
		public void loadFromApiBitcoin(ApiBitcoin apibitcoin)
		{
			time = apibitcoin.getTime();
			usd.set(apibitcoin.getBpi().getUSD().getRateFloat());
			euros.set(apibitcoin.getBpi().getEUR().getRateFloat());
		}

	

}
