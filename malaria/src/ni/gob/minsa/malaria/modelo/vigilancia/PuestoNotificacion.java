// -----------------------------------------------
// PuestoNotificacion.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.vigilancia;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ni.gob.minsa.malaria.modelo.BaseEntidadCreacion;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia asociada a la tabla PUESTOS_NOTIFICACION 
 * en el esquema SIVE.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 03/09/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="PUESTOS_NOTIFICACION",schema="SIVE",
	   uniqueConstraints={@UniqueConstraint(columnNames={"UNIDAD"}),
			              @UniqueConstraint(columnNames={"COLVOL"})
			             }
	   )
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="PuestoNotificacion.listarUnidadesPorEntidad",
			query="select pn from PuestoNotificacion pn " +
					"where (pn.unidad IS NOT NULL AND pn.unidad.entidadAdtva.entidadAdtvaId=:pEntidadAdtvaId) AND " +
					"      (:pTodos=1 OR (:pTodos=0 AND (pn.fechaCierre IS NULL OR pn.fechaCierre>CURRENT_DATE))) " +
					"order by pn.unidad.municipio.nombre, " +
					"		  pn.unidad.nombre"),
	@NamedQuery(
			name="PuestoNotificacion.listarPuestosPorUnidad",
			query="select pn from PuestoNotificacion pn " +
					"where (pn.colVol IS NOT NULL and pn.colVol.unidad.unidadId=:pUnidadId) AND " +
					"      (:pTodos=1 or (:pTodos=0 and (pn.fechaCierre IS NULL or pn.fechaCierre>CURRENT_DATE))) " +
					"order by pn.colVol.sisPersona.primerApellido, " +
					"	   pn.colVol.sisPersona.segundoApellido, " +
					"	   pn.colVol.sisPersona.primerNombre, " +
					"	   pn.colVol.sisPersona.segundoNombre"),
	@NamedQuery(
			name="PuestoNotificacion.listarPuestosPorNombre",
			query="select pn from PuestoNotificacion pn " +
					"where pn.colVol IS NOT NULL AND " +
					"      pn.colVol.unidad.unidadId=:pUnidadId AND " +
					"      (:pTodos=1 OR (:pTodos=0 AND (pn.fechaCierre IS NULL OR pn.fechaCierre>CURRENT_DATE))) AND " +
					"      FUNC('CATSEARCH',pn.colVol.sisPersona.sndNombre, :pCodigoNombre,null)>0 " +
					"order by pn.colVol.sisPersona.primerApellido, " +
					"		  pn.colVol.sisPersona.segundoApellido, " +
					"		  pn.colVol.sisPersona.primerNombre, " +
					"		  pn.colVol.sisPersona.segundoNombre"),
	@NamedQuery(
			name="PuestoNotificacion.listarMunicipiosPorEntidad",
			query="select distinct tm from DivisionPolitica tm where tm.divisionPoliticaId in ( " +
					"		select pn.unidad.municipio.divisionPoliticaId from PuestoNotificacion pn " +
					"		where (pn.unidad IS NOT NULL AND pn.unidad.entidadAdtva.entidadAdtvaId=:pEntidadAdtvaId) AND " +
					"      (:pTodos=1 OR (:pTodos=0 AND (pn.fechaCierre IS NULL OR pn.fechaCierre>CURRENT_DATE))) " +
					" ) or tm.divisionPoliticaId in( " +
					" 		select pnc.colVol.unidad.municipio.divisionPoliticaId from PuestoNotificacion pnc " +
					" 		where (pnc.colVol IS NOT NULL and pnc.colVol.unidad.entidadAdtva.entidadAdtvaId=:pEntidadAdtvaId) AND " +
					"      (:pTodos=1 or (:pTodos=0 and (pnc.fechaCierre IS NULL or pnc.fechaCierre>CURRENT_DATE))) " +
					" ) " +
					"order by tm.nombre"
	),
	@NamedQuery(
			name="PuestoNotificacion.encontrarPorUnidad",
			query="select pn from PuestoNotificacion pn " +
							"where pn.unidad IS NOT NULL and pn.unidad.unidadId=:pUnidadId " +
							"AND (:pActiva=0 OR (:pActiva=1 AND (pn.fechaCierre IS NULL OR pn.fechaCierre>CURRENT_DATE)))"),
	@NamedQuery(
			name="PuestoNotificacion.encontrarPorColVol",
			query="select pn from PuestoNotificacion pn " +
					"where pn.colVol IS NOT NULL and pn.colVol.colVolId=:pColVolId"),
	@NamedQuery(
			name="PuestoNotificacion.encontrarPorClave",
			query="select pn from PuestoNotificacion pn " +
					"where pn.clave=:pClave AND (pn.fechaCierre IS NULL OR pn.fechaCierre>CURRENT_DATE)")
})				
public class PuestoNotificacion extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PSTNOTI_ID_GENERATOR", sequenceName="SIVE.PSTNOTI_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PSTNOTI_ID_GENERATOR")
	@Column(name="PUESTO_NOTIFICACION_ID",updatable=false,nullable=false)
	private long puestoNotificacionId;

	@NotNull(message="La clave es requerida")
	@Column(name="CLAVE", updatable=true, nullable=false)
	private String clave;
	
    @OneToOne(targetEntity=Unidad.class)
	@JoinColumn(name="UNIDAD",referencedColumnName="CODIGO",updatable=false)
	private Unidad unidad;

    @OneToOne(targetEntity=ColVol.class)
	@JoinColumn(name="COLVOL",updatable=true)
	private ColVol colVol;

    @Size(min=1,max=500,message="El número de caracteres permitidos es de 1 a 500")
    @Column(nullable=true,length=500)
    private String observaciones;

	@NotNull(message="La fecha de apertura es requerida")
    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_APERTURA",nullable=false)
	private Date fechaApertura;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_CIERRE",nullable=true)
	private Date fechaCierre;
    
	@OneToMany(mappedBy="puestoNotificacion",
			   targetEntity=PuestoComunidad.class,
			   fetch=FetchType.LAZY)
	private List<PuestoComunidad> comunidadesPuesto;
    
	
    public PuestoNotificacion() {
    }

	public long getPuestoNotificacionId() {
		return puestoNotificacionId;
	}

	public void setPuestoNotificacionId(long puestoNotificacionId) {
		this.puestoNotificacionId = puestoNotificacionId;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Unidad getUnidad() {
		return unidad;
	}

	public void setUnidad(Unidad unidad) {
		this.unidad = unidad;
	}

	public ColVol getColVol() {
		return colVol;
	}

	public void setColVol(ColVol colVol) {
		this.colVol = colVol;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public void setComunidadesPuesto(List<PuestoComunidad> comunidadesPuesto) {
		this.comunidadesPuesto = comunidadesPuesto;
	}

	public List<PuestoComunidad> getComunidadesPuesto() {
		return comunidadesPuesto;
	}

	@Override
	public String toString() {
		return String.valueOf(puestoNotificacionId);
	}
}