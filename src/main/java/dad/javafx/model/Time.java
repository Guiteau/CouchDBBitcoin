
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
    "updated",
    "updatedISO",
    "updateduk"
})
public class Time {

    @JsonProperty("updated")
    private String updated;
    @JsonProperty("updatedISO")
    private String updatedISO;
    @JsonProperty("updateduk")
    private String updateduk;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Time() {
    }

    /**
     * 
     * @param updateduk
     * @param updatedISO
     * @param updated
     */
    public Time(String updated, String updatedISO, String updateduk) {
        super();
        this.updated = updated;
        this.updatedISO = updatedISO;
        this.updateduk = updateduk;
    }

    @JsonProperty("updated")
    public String getUpdated() {
        return updated;
    }

    @JsonProperty("updated")
    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @JsonProperty("updatedISO")
    public String getUpdatedISO() {
        return updatedISO;
    }

    @JsonProperty("updatedISO")
    public void setUpdatedISO(String updatedISO) {
        this.updatedISO = updatedISO;
    }

    @JsonProperty("updateduk")
    public String getUpdateduk() {
        return updateduk;
    }

    @JsonProperty("updateduk")
    public void setUpdateduk(String updateduk) {
        this.updateduk = updateduk;
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
        return new ToStringBuilder(this).append("updated", updated).append("updatedISO", updatedISO).append("updateduk", updateduk).append("additionalProperties", additionalProperties).toString();
    }

}
