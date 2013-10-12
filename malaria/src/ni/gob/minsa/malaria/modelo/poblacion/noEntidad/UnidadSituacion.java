package ni.gob.minsa.malaria.modelo.poblacion.noEntidad;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Clase de soporte para la presentación de la grilla
 * de unidades con la situación vinculada a la misma
 * con relación a la poblacion registrada por comunidad
 * <p>
 * @author Marlon Arróliga
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
	 * situación = 1 : confirmada<br>
     * situación = 2 : sin confirmar<br>
     * situación = 3 : sin población
	 */
	public BigDecimal getSituacion() {
		return Situacion;
	}

	public void setSituacion(BigDecimal situacion) {
		Situacion = situacion;
	};
	
	
	
}
