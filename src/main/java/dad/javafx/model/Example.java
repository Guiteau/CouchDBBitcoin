
package dad.javafx.model;

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
    "time",
    "disclaimer",
    "chartName",
    "bpi"
})
public class Example {

    @JsonProperty("time")
    private Time time;
    @JsonProperty("disclaimer")
    private String disclaimer;
    @JsonProperty("chartName")
    private String chartName;
    @JsonProperty("bpi")
    private Bpi bpi;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Example() {
    }

    /**
     * 
     * @param chartName
     * @param bpi
     * @param time
     * @param disclaimer
     */
    public Example(Time time, String disclaimer, String chartName, Bpi bpi) {
        super();
        this.time = time;
        this.disclaimer = disclaimer;
        this.chartName = chartName;
        this.bpi = bpi;
    }

    @JsonProperty("time")
    public Time getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(Time time) {
        this.time = time;
    }

    @JsonProperty("disclaimer")
    public String getDisclaimer() {
        return disclaimer;
    }

    @JsonProperty("disclaimer")
    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    @JsonProperty("chartName")
    public String getChartName() {
        return chartName;
    }

    @JsonProperty("chartName")
    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    @JsonProperty("bpi")
    public Bpi getBpi() {
        return bpi;
    }

    @JsonProperty("bpi")
    public void setBpi(Bpi bpi) {
        this.bpi = bpi;
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
        return new ToStringBuilder(this).append("time", time).append("disclaimer", disclaimer).append("chartName", chartName).append("bpi", bpi).append("additionalProperties", additionalProperties).toString();
    }

}
