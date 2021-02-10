
package dad.javafx.couchdb.bitcoin.api.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "USD",
    "GBP",
    "EUR"
})
public class Bpi {

    @JsonProperty("USD")
    private USD uSD;
    @JsonProperty("GBP")
    private GBP gBP;
    @JsonProperty("EUR")
    private EUR eUR;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Bpi() {
    }

    /**
     * 
     * @param eUR
     * @param gBP
     * @param uSD
     */
    public Bpi(USD uSD, GBP gBP, EUR eUR) {
        super();
        this.uSD = uSD;
        this.gBP = gBP;
        this.eUR = eUR;
    }

    @JsonProperty("USD")
    public USD getUSD() {
        return uSD;
    }

    @JsonProperty("USD")
    public void setUSD(USD uSD) {
        this.uSD = uSD;
    }

    @JsonProperty("GBP")
    public GBP getGBP() {
        return gBP;
    }

    @JsonProperty("GBP")
    public void setGBP(GBP gBP) {
        this.gBP = gBP;
    }

    @JsonProperty("EUR")
    public EUR getEUR() {
        return eUR;
    }

    @JsonProperty("EUR")
    public void setEUR(EUR eUR) {
        this.eUR = eUR;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("uSD", uSD).append("gBP", gBP).append("eUR", eUR).append("additionalProperties", additionalProperties).toString();
    }

}
