// -----------------------------------------------
// ColVolAcceso.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.vigilancia;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import ni.gob.minsa.malaria.modelo.BaseEntidadAcceso;

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia asociada a la tabla COLVOLS_ACCESOS 
 * en el esquema SIVE.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 22/06/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="COLVOLS_ACCESOS",schema="SIVE",
			uniqueConstraints=@UniqueConstraint(columnNames={"COLVOL"}))
@Cache(alwaysRefresh=true,disableHits=true)
public class ColVolAcceso extends BaseEntidadAcceso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="COLVOLS_ACCESOS_ID_GENERATOR", sequenceName="SIVE.COLVOLACCESOS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COLVOLS_ACCESOS_ID_GENERATOR")
	@Column(name="COLVOL_ACCESO_ID",updatable=false,nullable=false)
	private long colVolAccesoId;

	@NotNull(message="La asociación con el colaborador voluntario es requerida")
	@OneToOne(targetEntity=ColVol.class)
	@JoinColumn(name="COLVOL",nullable=false,updatable=false)
	private ColVol colVol;
	
    public ColVolAcceso() {
    }

	public void setColVolAccesoId(long colVolAccesoId) {
		this.colVolAccesoId = colVolAccesoId;
	}

	public long getColVolAccesoId() {
		return colVolAccesoId;
	}

	public void setColVol(ColVol colVol) {
		this.colVol = colVol;
	}

	public ColVol getColVol() {
		return colVol;
	}

}