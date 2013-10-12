// -----------------------------------------------
// Pais.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.poblacion;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia para la tabla PAISES en el esquema GENERAL.
 * 
 */
@Entity
@Table(name="PAISES", schema="GENERAL")
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="pais.listar",
			query="select tp from Pais tp " +
					"order by tp.nombre"),
	@NamedQuery(
			name="pais.encontrarPorCodigo",
			query="select tp from Pais tp " +
					"where tp.codigoAlfados=:pCodigo ")
})
public class Pais implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PAIS_ID", updatable=false)
	private long paisId;

	@Column(name="NOMBRE", unique=true,nullable=false,length=100)
	private String nombre;
	
	@Column(name="CODIGO_NUMERICO", unique=true, nullable=false, length=80)
	private String codigoNumerico;

	@Column(name="CODIGO_ALFADOS",unique=true, nullable=false, length=8)
	private String codigoAlfados;

	@Column(name="CODIGO_ALFATRES",unique=true, nullable=false, length=12)
	private String codigoAlfatres;

	@Column(name="CODIGO_ISO",unique=true, nullable=false, length=80)
	private String codigoIso;

    public Pais() {
    }

	public long getPaisId() {
		return paisId;
	}

	public void setPaisId(long paisId) {
		this.paisId = paisId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigoNumerico() {
		return codigoNumerico;
	}

	public void setCodigoNumerico(String codigoNumerico) {
		this.codigoNumerico = codigoNumerico;
	}

	public String getCodigoAlfados() {
		return codigoAlfados;
	}

	public void setCodigoAlfados(String codigoAlfados) {
		this.codigoAlfados = codigoAlfados;
	}

	public String getCodigoAlfatres() {
		return codigoAlfatres;
	}

	public void setCodigoAlfatres(String codigoAlfatres) {
		this.codigoAlfatres = codigoAlfatres;
	}

	public String getCodigoIso() {
		return codigoIso;
	}

	public void setCodigoIso(String codigoIso) {
		this.codigoIso = codigoIso;
	}
	
}