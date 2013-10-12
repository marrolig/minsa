// -----------------------------------------------
// ControlEntidadPoblacion.java
// -----------------------------------------------

package ni.gob.minsa.malaria.modelo.poblacion;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;

import java.util.Date;


/**
 * Clase de persistencia para la tabla CONTROL_ENTIDAD_POBLACION
 * <p>
 * Control de bloqueos de los registros de población para todas las 
 * comunidades que son atendidas por las unidades de salud vinculadas 
 * a una entidad administrativa.
 *  
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 09/05/2012
 * @since jdk1.6.0_21 
 */
@Entity
@Table(name="CONTROL_ENTIDAD_POBLACION",schema="GENERAL",
		uniqueConstraints=
			@UniqueConstraint(columnNames={"AÑO","ENTIDAD_ADTVA"}))
@NamedQueries({
	@NamedQuery(
			name="controlPoblacionPorEntidad",
			query="select tca from ControlEntidadPoblacion tca " +
					"where tca.entidadAdtva.codigo=:pEntidadAdtva and " +
					      "tca.año=:pAño"),
	@NamedQuery(
			name="entidadesPoblacionConfirmada",
			query="select tca from ControlEntidadPoblacion tca " +
					"where tca.año=:pAño order by tca.entidadAdtva.codigo" )
})
public class ControlEntidadPoblacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CONTROL_ENTIDAD_POBLACION_CONTROLENTIDADPOBLACIONID_GENERATOR", sequenceName="GENERAL.CTRL_ENTIDADPOBL_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CONTROL_ENTIDAD_POBLACION_CONTROLENTIDADPOBLACIONID_GENERATOR")
	@Column(name="CONTROL_ENTIDAD_POBLACION_ID",updatable=false)
	private long controlEntidadPoblacionId;

	@Column(nullable=false,updatable=false)
	private Integer año;

    @ManyToOne(targetEntity=EntidadAdtva.class)
	@JoinColumn(name="ENTIDAD_ADTVA",referencedColumnName="CODIGO",nullable=false,updatable=false)
	private EntidadAdtva entidadAdtva;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO",updatable=false)
	private Date fechaRegistro;

	@Column(name="USUARIO_REGISTRO",updatable=false)
	private String usuarioRegistro;

    public ControlEntidadPoblacion() {
    }

	public long getControlEntidadPoblacionId() {
		return this.controlEntidadPoblacionId;
	}

	public void setControlEntidadPoblacionId(long controlEntidadPoblacionId) {
		this.controlEntidadPoblacionId = controlEntidadPoblacionId;
	}

	public Integer getAño() {
		return this.año;
	}

	public void setAño(Integer año) {
		this.año = año;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public void setEntidadAdtva(EntidadAdtva entidadAdtva) {
		this.entidadAdtva = entidadAdtva;
	}

	public EntidadAdtva getEntidadAdtva() {
		return entidadAdtva;
	}

}