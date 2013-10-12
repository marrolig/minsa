package ni.gob.minsa.malaria.modelo.poblacion.noEntidad;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Clase de soporte para la presentaci�n de la grilla
 * de unidades con la situaci�n vinculada a la misma
 * con relaci�n a la poblacion registrada por comunidad
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 15/05/2012
 * @since jdk1.6.0_21
 */
public class UnidadSituacion implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String municipio;
	private String unidad;
	private String tipoUnidad;
	private BigDecimal ComunidadesExistentes;
	private BigDecimal ComunidadesConRegistro;
	private BigDecimal Poblacion;
	private BigDecimal Situacion;
             
	public UnidadSituacion() {}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getMunicipio() {
		return municipio;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public String getTipoUnidad() {
		return tipoUnidad;
	}

	public void setTipoUnidad(String tipoUnidad) {
		this.tipoUnidad = tipoUnidad;
	}

	public BigDecimal getComunidadesExistentes() {
		return ComunidadesExistentes;
	}

	public void setComunidadesExistentes(BigDecimal comunidadesExistentes) {
		ComunidadesExistentes = comunidadesExistentes;
	}

	public BigDecimal getComunidadesConRegistro() {
		return ComunidadesConRegistro;
	}

	public void setComunidadesConRegistro(BigDecimal comunidadesConRegistro) {
		ComunidadesConRegistro = comunidadesConRegistro;
	}

	public BigDecimal getPoblacion() {
		return Poblacion;
	}

	public void setPoblacion(BigDecimal poblacion) {
		Poblacion = poblacion;
	}

	/**
	 * situaci�n = 1 : confirmada<br>
     * situaci�n = 2 : sin confirmar<br>
     * situaci�n = 3 : sin poblaci�n
	 */
	public BigDecimal getSituacion() {
		return Situacion;
	}

	public void setSituacion(BigDecimal situacion) {
		Situacion = situacion;
	};
	
	
	
}
