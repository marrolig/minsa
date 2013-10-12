// -----------------------------------------------
// PuestoComunidad.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.vigilancia;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import ni.gob.minsa.malaria.modelo.BaseEntidadCreacion;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia asociada a la tabla PUESTOS_COMUNIDADES 
 * en el esquema SIVE.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 03/09/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="PUESTOS_COMUNIDADES",schema="SIVE",
	   uniqueConstraints=@UniqueConstraint(columnNames={"PUESTO_NOTIFICACION","COMUNIDAD"}))
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="PuestoComunidad.listarComunidadesPorPuesto",
			query="select pnc from PuestoComunidad pnc " +
					"where (pnc.puestoNotificacion.puestoNotificacionId=:pPuestoNotificacionId) " +
					"order by pnc.comunidad.sector.municipio.nombre, pnc.comunidad.nombre")
})				
public class PuestoComunidad extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PST_CMD_ID_GENERATOR", sequenceName="SIVE.PST_CMD_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PST_CMD_ID_GENERATOR")
	@Column(name="PUESTO_COMUNIDAD_ID",updatable=false,nullable=false)
	private long puestoComunidadId;

	@NotNull(message="El puesto de notificación es requerido")
    @ManyToOne(targetEntity=PuestoNotificacion.class)
	@JoinColumn(name="PUESTO_NOTIFICACION",referencedColumnName="PUESTO_NOTIFICACION_ID",nullable=false, updatable=false)
	private PuestoNotificacion puestoNotificacion;

	@NotNull(message="La comunidad es requerida")
    @ManyToOne(targetEntity=Comunidad.class)
	@JoinColumn(name="COMUNIDAD",referencedColumnName="CODIGO",nullable=false, updatable=true)
	private Comunidad comunidad;
	
    public PuestoComunidad() {
    }

	public long getPuestoComunidadId() {
		return puestoComunidadId;
	}

	public void setPuestoComunidadId(long puestoComunidadId) {
		this.puestoComunidadId = puestoComunidadId;
	}

	public Comunidad getComunidad() {
		return comunidad;
	}

	public void setComunidad(Comunidad comunidad) {
		this.comunidad = comunidad;
	}

	public void setPuestoNotificacion(PuestoNotificacion puestoNotificacion) {
		this.puestoNotificacion = puestoNotificacion;
	}

	public PuestoNotificacion getPuestoNotificacion() {
		return puestoNotificacion;
	}

}