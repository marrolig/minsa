package ni.gob.minsa.malaria.modelo.poblacion.noEntidad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Clase de soporte para la presentación de la grilla
 * de viviendas vinculadas a una comunidad, con la información
 * de la última encuesta con la situación vinculada a la misma
 * con relación a la poblacion registrada por comunidad
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 25/05/2012
 * @since jdk1.6.0_21
 */
public class ViviendaUltimaEncuesta implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private long viviendaId; 
	private String codigo;
	private String tipoVivienda;
	private String jefeFamilia;
	private String condicionOcupacion;
	private BigDecimal habitantes;
	private Date fechaUltimaEncuesta;
	private BigDecimal existenFactoresRiesgo;
	private BigDecimal pasivo;
	private BigDecimal latitud;
	private BigDecimal longitud;
             
	public ViviendaUltimaEncuesta() {}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getTipoVivienda() {
		return tipoVivienda;
	}

	public void setTipoVivienda(String tipoVivienda) {
		this.tipoVivienda = tipoVivienda;
	}

	public String getJefeFamilia() {
		return jefeFamilia;
	}

	public void setJefeFamilia(String jefeFamilia) {
		this.jefeFamilia = jefeFamilia;
	}

	public String getCondicionOcupacion() {
		return condicionOcupacion;
	}

	public void setCondicionOcupacion(String condicionOcupacion) {
		this.condicionOcupacion = condicionOcupacion;
	}

	public BigDecimal getHabitantes() {
		return habitantes;
	}

	public void setHabitantes(BigDecimal habitantes) {
		this.habitantes = habitantes;
	}

	public Date getFechaUltimaEncuesta() {
		return fechaUltimaEncuesta;
	}

	public void setFechaUltimaEncuesta(Date fechaUltimaEncuesta) {
		this.fechaUltimaEncuesta = fechaUltimaEncuesta;
	}

	public BigDecimal getExistenFactoresRiesgo() {
		return existenFactoresRiesgo;
	}

	public void setExistenFactoresRiesgo(BigDecimal existenFactoresRiesgo) {
		this.existenFactoresRiesgo = existenFactoresRiesgo;
	}

	public BigDecimal getPasivo() {
		return pasivo;
	}

	public void setPasivo(BigDecimal pasivo) {
		this.pasivo = pasivo;
	}

	public void setViviendaId(long viviendaId) {
		this.viviendaId = viviendaId;
	}

	public long getViviendaId() {
		return viviendaId;
	}

	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}

	public BigDecimal getLatitud() {
		return latitud;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

	public BigDecimal getLongitud() {
		return longitud;
	}
	
}
