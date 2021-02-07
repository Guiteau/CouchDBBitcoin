package dad.javafx.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonWriteNullProperties(false)
@JsonIgnoreProperties({ "id", "revision" })
public class CouchBitcoin {
	@JsonProperty("_id")
	private String id;

	@JsonProperty("_rev")
	private String revision;

	private Time time;

	public String getId() {
		return id;
	}

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

	public void setId(String id) {
		this.id = id;
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
		return "CouchBitcoin [id=" + id + ", revision=" + revision + ", time=" + time + ", usd=" + usd + ", euros="
				+ euros + "]";
	}

	private double usd;
	private double euros;

	public CouchBitcoin(ApiBitcoin apibitcoin) {
		time = apibitcoin.getTime();
		usd = apibitcoin.getBpi().getUSD().getRateFloat();
		euros = apibitcoin.getBpi().getEUR().getRateFloat();
	}

}
