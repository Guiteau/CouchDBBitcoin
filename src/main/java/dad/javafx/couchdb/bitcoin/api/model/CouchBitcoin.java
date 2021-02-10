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
	/*
	@JsonProperty("_id")
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	*/
	
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
		System.out.println("Valor en euros: " + euros);
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
		this.euros = euros;
		System.out.println("Actualizando euros: " + euros);
		support.firePropertyChange("euros", this.euros, euros);
	}

	@Override
	public String toString() {
		return "CouchBitcoin [id=" + cid + ", revision=" + revision + ", time=" + time + ", usd=" + usd + ", euros="
				+ euros + "]";
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