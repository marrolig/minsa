// -----------------------------------------------
// PoblacionAnualSector.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.poblacion.noEntidad;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Clase no persistente de car�cter auxiliar para transferir los resultados
 * directamente desde la consulta y su utilizaci�n en el interfaz del usuario
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 08/05/2012
 * @since jdk1.6.0_21

 */
public class PoblacionAnualSector implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private BigDecimal a�o;
	private long sectorId;
	private String codigo;
	private String nombre;
	private int comunidades;
    private BigDecimal hogares;
	private BigDecimal manzanas;
	private BigDecimal poblacion;
	private BigDecimal viviendas;
	
    public PoblacionAnualSector() {
    }

	public PoblacionAnualSector(BigDecimal a�o, long sectorId, String codigo, String nombre, 
			int comunidades, BigDecimal hogares, BigDecimal manzanas,
			BigDecimal poblacion, BigDecimal viviendas) {

		this.a�o = a�o;
		this.sectorId=sectorId;
		this.codigo=codigo;
		this.nombre=nombre;
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

	public void setComunidades(int comunidades) {
		this.comunidades = comunidades;
	}

	public int getComunidades() {
		return comunidades;
	}

	public void setSectorId(long sectorId) {
		this.sectorId = sectorId;
	}

	public long getSectorId() {
		return sectorId;
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

}