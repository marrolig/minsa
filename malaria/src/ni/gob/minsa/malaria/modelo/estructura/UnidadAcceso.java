// -----------------------------------------------
// UnidadAcceso.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.estructura;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import ni.gob.minsa.malaria.modelo.BaseEntidadAcceso;

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia asociada a la tabla UNIDADES_ACCESOS 
 * en el esquema GENERAL.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 22/09/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="UNIDADES_ACCESOS",schema="GENERAL",
			uniqueConstraints=@UniqueConstraint(columnNames={"UNIDAD"}))
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="unidadAcceso.encontrarPorUnidad",
			query="select ua from UnidadAcceso ua " +
					"where ua.unidad.codigo=:pCodigo ")
})
public class UnidadAcceso extends BaseEntidadAcceso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="UNIDADES_ACCESOS_ID_GENERATOR", sequenceName="GENERAL.UNIDADACCESOS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="UNIDADES_ACCESOS_ID_GENERATOR")
	@Column(name="UNIDAD_ACCESO_ID",updatable=false,nullable=false)
	private long unidadAccesoId;

	@NotNull(message="La asociación con la unidad de salud es requerida")
	@OneToOne(targetEntity=Unidad.class,fetch=FetchType.LAZY)
	@JoinColumn(name="UNIDAD",nullable=false,updatable=false)
	private Unidad unidad;
	
    public UnidadAcceso() {
    }

	public void setUnidadAccesoId(long unidadAccesoId) {
		this.unidadAccesoId = unidadAccesoId;
	}

	public long getUnidadAccesoId() {
		return unidadAccesoId;
	}

	public void setUnidad(Unidad unidad) {
		this.unidad = unidad;
	}

	public Unidad getUnidad() {
		return unidad;
	}

}