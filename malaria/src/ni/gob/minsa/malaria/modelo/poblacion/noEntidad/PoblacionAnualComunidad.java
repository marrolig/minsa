// -----------------------------------------------
// PoblacionAnualComunidad.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.poblacion.noEntidad;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Clase no persistente de carácter auxiliar para transferir los resultados
 * directamente desde la consulta y su utilización en el interfaz del usuario
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 08/05/2012
 * @since jdk1.6.0_21

 */
public class PoblacionAnualComunidad implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private BigDecimal año;
	private long poblacionComunidadId;
	private long comunidadId;
	private String codigo;
	private String nombre;
	private String tipoArea;
    private BigDecimal hogares;
	private BigDecimal manzanas;
	private BigDecimal poblacion;
	private BigDecimal viviendas;
	@SuppressWarnings("unused")
	private boolean valido;
	
    public PoblacionAnualComunidad() {
    }

	public PoblacionAnualComunidad(BigDecimal año, long poblacionComunidadId, long comunidadId, String codigo, String nombre, 
			String tipoArea, BigDecimal hogares, BigDecimal manzanas,
			BigDecimal poblacion, BigDecimal viviendas) {

		this.año = año;
		this.poblacionComunidadId=poblacionComunidadId;
		this.comunidadId=comunidadId;
		this.codigo=codigo;
		this.nombre=nombre;
		this.tipoArea = tipoArea;
		this.hogares = hogares;
		this.manzanas = manzanas;
		this.poblacion = poblacion;
		this.viviendas = viviendas;
	}

	public BigDecimal getAño() {
		return this.año;
	}

	public void setAño(BigDecimal año) {
		this.año = año;
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

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setPoblacionComunidadId(long poblacionComunidadId) {
		this.poblacionComunidadId = poblacionComunidadId;
	}

	public long getPoblacionComunidadId() {
		return poblacionComunidadId;
	}

	public void setComunidadId(long comunidadId) {
		this.comunidadId = comunidadId;
	}

	public long getComunidadId() {
		return comunidadId;
	}

	public void setTipoArea(String tipoArea) {
		this.tipoArea = tipoArea;
	}

	public String getTipoArea() {
		return tipoArea;
	}


	public boolean isValido() {

		boolean iValido=true;
		if ((this.poblacion==null) || this.poblacion.intValue()==0) {
			if (this.hogares!=null && this.hogares.intValue()>0) iValido=false;
			if (iValido && this.viviendas!=null && this.viviendas.intValue()>0) iValido=false;
			if (iValido && this.manzanas!=null && this.manzanas.intValue()>0) iValido=false;
		}

		return iValido;
	}

}