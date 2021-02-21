package dad.javafx.couchdb.bitcoin.api.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;



@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({ "id", "revision" })
public class CouchBitcoin {
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	
	@JsonProperty("_id")
	private final String cid = "current_value";
	
	@JsonIgnore
	private PropertyChangeSupport support;

	private double usd;
	private double euros;
	private Time time;

	
	public CouchBitcoin()
	{
		support = new PropertyChangeSupport(this);
	}
	
	public CouchBitcoin(ApiBitcoin apibitcoin) {
		support = new PropertyChangeSupport(this);
			loadFromApiBitcoin(apibitcoin);	
	}
	
	public String getCid()
	{
		return cid;
	}
	
	
	
	@JsonProperty("_rev")
	private String revision;


	
	
	public String getRevision() {
		return revision;
	}

	public Time getTime() {
		return time;
	}

	public double getUsd() {
		return usd;
	}

	public double getEuros() {
		return euros;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public void setUsd(double usd) {
		this.usd = usd;
		support.firePropertyChange("usd", this.usd, usd);
	}

	public void setEuros(double euros) {
		double oldeuros = this.euros;
		this.euros = euros;
		support.firePropertyChange("euros", oldeuros, euros);
	}

	public void loadFromApiBitcoin(ApiBitcoin apibitcoin)
	{
		time = apibitcoin.getTime();
		usd = apibitcoin.getBpi().getUSD().getRateFloat();
		euros = apibitcoin.getBpi().getEUR().getRateFloat();
		
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

}
