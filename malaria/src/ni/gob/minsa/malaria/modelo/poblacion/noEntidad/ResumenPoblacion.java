// -----------------------------------------------
// ResumenPoblacionSector.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.poblacion.noEntidad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Resumen de los datos de poblaci�n para un a�o espec�fico y un sector
 * espec�fico o unidad espec�fica, desglosando los datos a nivel del tipo de �rea asociada
 * a las comunidades, i.e. Urbano y Rural.
 * <p>
 * Clase no persistente de car�cter auxiliar para transferir los resultados
 * directamente desde la consulta y su utilizaci�n en el interfaz del usuario.
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 08/05/2012
 * @since jdk1.6.0_21

 */
public class ResumenPoblacion implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final Comparator<ResumenPoblacion> ORDEN_TIPO_AREA =
		new Comparator<ResumenPoblacion>() {
			public int compare(ResumenPoblacion ev1, ResumenPoblacion ev2) {
				return ev1.tipoArea.compareTo(ev2.tipoArea);
			}
	};
	private BigDecimal a�o;
	private String tipoArea;
	private Long comunidades;
	private BigDecimal porcentajeComunidades;
    private BigDecimal hogares;
	private BigDecimal porcentajeHogares;
	private BigDecimal manzanas;
	private BigDecimal porcentajeManzanas;
	private BigDecimal poblacion;
	private BigDecimal porcentajePoblacion;
	private BigDecimal viviendas;
	private BigDecimal porcentajeViviendas;
	
    public ResumenPoblacion() {
    }

	public ResumenPoblacion(String tipoArea,
			Long comunidades, BigDecimal hogares, BigDecimal manzanas,
			BigDecimal poblacion, BigDecimal viviendas) {
		super();
		this.tipoArea = tipoArea;
		this.comunidades = comunidades;
		this.hogares = hogares;
		this.manzanas = manzanas;
		this.poblacion = poblacion;
		this.viviendas = viviendas;
	}

	public BigDecimal getA�o() {
		return this.a�o;
	}

	public void setA�o(BigDecimal a�o) {
		this.a�o = a�o;
	}

	public BigDecimal getHogares() {
		return this.hogares;
	}

	public void setHogares(BigDecimal hogares) {
		this.hogares = hogares;
	}

	public BigDecimal getManzanas() {
		return this.manzanas;
	}

	public void setManzanas(BigDecimal manzanas) {
		this.manzanas = manzanas;
	}

	public BigDecimal getPoblacion() {
		return this.poblacion;
	}

	public void setPoblacion(BigDecimal poblacion) {
		this.poblacion = poblacion;
	}

	public BigDecimal getViviendas() {
		return this.viviendas;
	}

	public void setViviendas(BigDecimal viviendas) {
		this.viviendas = viviendas;
	}

	public void setComunidades(Long comunidades) {
		this.comunidades = comunidades;
	}


	public Long getComunidades() {
		return comunidades;
	}

	public void setTipoArea(String tipoArea) {
		this.tipoArea = tipoArea;
	}

	public String getTipoArea() {
		return tipoArea;
	}

	public void setPorcentajeComunidades(BigDecimal porcentajeComunidades) {
		this.porcentajeComunidades = porcentajeComunidades;
	}

	public BigDecimal getPorcentajeComunidades() {
		return porcentajeComunidades;
	}

	public void setPorcentajeHogares(BigDecimal porcentajeHogares) {
		this.porcentajeHogares = porcentajeHogares;
	}

	public BigDecimal getPorcentajeHogares() {
		return porcentajeHogares;
	}

	public void setPorcentajeManzanas(BigDecimal porcentajeManzanas) {
		this.porcentajeManzanas = porcentajeManzanas;
	}

	public BigDecimal getPorcentajeManzanas() {
		return porcentajeManzanas;
	}

	public void setPorcentajePoblacion(BigDecimal porcentajePoblacion) {
		this.porcentajePoblacion = porcentajePoblacion;
	}

	public BigDecimal getPorcentajePoblacion() {
		return porcentajePoblacion;
	}

	public void setPorcentajeViviendas(BigDecimal porcentajeViviendas) {
		this.porcentajeViviendas = porcentajeViviendas;
	}

	public BigDecimal getPorcentajeViviendas() {
		return porcentajeViviendas;
	}

}