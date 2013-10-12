// -----------------------------------------------
// ControlUnidadPoblacion.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.poblacion;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.estructura.Unidad;

import java.util.Date;


/**
 * Clase de persistencia para la tabla CONTROL_UNIDAD_POBLACION
 * <p>
 * Control de bloqueos de los datos de población anual de comunidades.  
 * Los datos son responsabilidad de las unidades de salud que atienden
 * a dichas comunidades, por lo que el control de bloqueo es a nivel 
 * de unidad de salud.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 09/05/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="CONTROL_UNIDAD_POBLACION",schema="GENERAL",uniqueConstraints=
					@UniqueConstraint(columnNames={"AÑO","UNIDAD"}))
@NamedQueries({
	@NamedQuery(
			name="controlPoblacionPorUnidad",
			query="select tca from ControlUnidadPoblacion tca " +
					"where tca.unidad.codigo=:pUnidad and " +
					      "tca.año=:pAño"),
	@NamedQuery(
		name="unidadesPoblacionConfirmadaPorEntidad",
		query="select tca from ControlUnidadPoblacion tca " +
				   "where tca.unidad.entidadAdtva.codigo=:pEntidadAdtva and " +
						 "tca.año=:pAño " +
				    "order by tca.unidad.municipio.nombre, tca.unidad.nombre")
})
public class ControlUnidadPoblacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CONTROL_UNIDAD_POBLACION_CONTROLUNIDADPOBLACIONID_GENERATOR", sequenceName="GENERAL.CTRL_UNIDADPOBL_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CONTROL_UNIDAD_POBLACION_CONTROLUNIDADPOBLACIONID_GENERATOR")
	@Column(name="CONTROL_UNIDAD_POBLACION_ID",updatable=false)
	private long controlUnidadPoblacionId;

	@Column(nullable=false,updatable=false)
	private Integer año;

    @ManyToOne(targetEntity=Unidad.class)
	@JoinColumn(name="UNIDAD",referencedColumnName="CODIGO",nullable=false,updatable=false)
	private Unidad unidad;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO",updatable=false,nullable=false)
	private Date fechaRegistro;

	@Column(name="USUARIO_REGISTRO",updatable=false,nullable=false)
	private String usuarioRegistro;

    public ControlUnidadPoblacion() {
    }

	public long getControlUnidadPoblacionId() {
		return this.controlUnidadPoblacionId;
	}

	public void setControlUnidadPoblacionId(long controlUnidadPoblacionId) {
		this.controlUnidadPoblacionId = controlUnidadPoblacionId;
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

	public void setUnidad(Unidad unidad) {
		this.unidad = unidad;
	}

	public Unidad getUnidad() {
		return unidad;
	}

}