// -----------------------------------------------
// PropiedadUnidad.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.seguridad;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.estructura.Unidad;

/**
 * Clase de persistencia para la tabla PROPIEDADES_UNIDADES
 * <p>
 * Unidades de salud que tiene adscrita una o más propiedades
 * o características funcionales
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 26/12/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="PROPIEDADES_UNIDADES",schema="GENERAL")
public class PropiedadUnidad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PROPUND_ID_GENERATOR", sequenceName="GENERAL.PROPUNDS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROPUND_ID_GENERATOR")
	@Column(name="PROPIEDAD_UNIDAD_ID",updatable=false)
	private long propiedadUnidadId;

    @ManyToOne(targetEntity=Unidad.class)
	@JoinColumn(name="UNIDAD",referencedColumnName="CODIGO",nullable=false,updatable=false)
	private Unidad unidad;

	@Column(name="PROPIEDAD_SISTEMA",nullable=false,updatable=false)
	private String propiedad;
	
    public PropiedadUnidad() {
    }

	public void setPropiedadUnidadId(long propiedadUnidadId) {
		this.propiedadUnidadId = propiedadUnidadId;
	}

	public long getPropiedadUnidadId() {
		return propiedadUnidadId;
	}

	public void setUnidad(Unidad unidad) {
		this.unidad = unidad;
	}

	public Unidad getUnidad() {
		return unidad;
	}

	public void setPropiedad(String propiedad) {
		this.propiedad = propiedad;
	}

	public String getPropiedad() {
		return propiedad;
	}

}