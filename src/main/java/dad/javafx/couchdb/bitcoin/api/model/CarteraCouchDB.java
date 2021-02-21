
package dad.javafx.couchdb.bitcoin.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "_id",
    "_rev",
    "Password",
    "cantidad_bitcoins",
    "dinero_ganado"
})
public class CarteraCouchDB {
	
	@JsonIgnoreProperties(ignoreUnknown = true)

	
    @JsonProperty("_rev")
    private String rev;
    @JsonProperty("_id")
    private String nombre;
    @JsonProperty("Password")
    private String password;
    @JsonProperty("cantidad_bitcoins")
    private double cantidadBitcoins;
    @JsonProperty("dinero_ganado")
    private double dineroGanado;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CarteraCouchDB() {
    }

    /**
     * 
     * @param password
     * @param rev
     * @param cantidadBitcoins
     * @param id
     * @param nombre
     * @param dineroGanado
     */
    public CarteraCouchDB(String rev, String nombre, String password, double cantidadBitcoins, double dineroGanado) {
        super();
        this.rev = rev;
        this.nombre = nombre;
        this.password = password;
        this.cantidadBitcoins = cantidadBitcoins;
        this.dineroGanado = dineroGanado;
    }


    @JsonProperty("_rev")
    public String getRev() {
        return rev;
    }

    @JsonProperty("_rev")
    public void setRev(String rev) {
        this.rev = rev;
    }

    @JsonProperty("_id")
    public String getNombre() {
        return nombre;
    }

    @JsonProperty("_id")
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @JsonProperty("Password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("Password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("cantidad_bitcoins")
    public double getCantidadBitcoins() {
        return cantidadBitcoins;
    }

    @JsonProperty("cantidad_bitcoins")
    public void setCantidadBitcoins(double cantidadBitcoins) {
        this.cantidadBitcoins = cantidadBitcoins;
    }

    @JsonProperty("dinero_ganado")
    public double getDineroGanado() {
        return dineroGanado;
    }

    @JsonProperty("dinero_ganado")
    public void setDineroGanado(double dineroGanado) {
        this.dineroGanado = dineroGanado;
    }
    

}
