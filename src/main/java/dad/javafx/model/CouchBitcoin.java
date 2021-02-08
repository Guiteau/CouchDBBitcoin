package dad.javafx.model;

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

	public String getCid()
	{
		return cid;
	}
	
	
	
	@JsonProperty("_rev")
	private String revision;

	private Time time;

	
	
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
	}

	public void setEuros(double euros) {
		this.euros = euros;
	}

	@Override
	public String toString() {
		return "CouchBitcoin [id=" + cid + ", revision=" + revision + ", time=" + time + ", usd=" + usd + ", euros="
				+ euros + "]";
	}

	private double usd;
	private double euros;
	
	
	public CouchBitcoin()
	{}
	
	public CouchBitcoin(ApiBitcoin apibitcoin) {
			loadFromApiBitcoin(apibitcoin);	
	}
	
	public void loadFromApiBitcoin(ApiBitcoin apibitcoin)
	{
		time = apibitcoin.getTime();
		usd = apibitcoin.getBpi().getUSD().getRateFloat();
		euros = apibitcoin.getBpi().getEUR().getRateFloat();
	}

}
