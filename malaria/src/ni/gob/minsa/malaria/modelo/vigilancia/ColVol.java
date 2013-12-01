// -----------------------------------------------
// ColVol.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.vigilancia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ni.gob.minsa.malaria.modelo.BaseEntidadCreacion;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.sis.SisPersona;

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia asociada a la tabla COLVOLS 
 * en el esquema SIVE.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 22/06/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="COLVOLS",schema="SIVE",
			uniqueConstraints=@UniqueConstraint(columnNames={"PERSONA"}))
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="ColVol.listarPorUnidad",
			query="select tc from ColVol tc " +
					"where tc.unidad.unidadId=:pUnidadId and " +
					"      (:pTodos=1 or (:pTodos=0 and (tc.fechaFin is null or tc.fechaFin>CURRENT_DATE))) " +
					"order by tc.sisPersona.comunidadResidencia.nombre, " +
					"		  tc.sisPersona.primerApellido, " +
					"		  tc.sisPersona.segundoApellido, " +
					"		  tc.sisPersona.primerNombre, " +
					"		  tc.sisPersona.segundoNombre"),
	@NamedQuery(
			name="ColVol.encontrarPorPersona",
			query="select tc from ColVol tc " +
							"where tc.sisPersona.personaId=:pPersonaId and " +
							"      tc.fechaFin is null or tc.fechaFin>CURRENT_DATE"),
	@NamedQuery(
			name="ColVol.listarPorNombre",
			query="select tc from ColVol tc " +
					"where tc.unidad.unidadId=:pUnidadId and " +
					"      (:pTodos=1 or (:pTodos=0 and (tc.fechaFin is null or tc.fechaFin>CURRENT_DATE))) and " +
					"      FUNC('CATSEARCH',tc.sisPersona.sndNombre, :pCodigoNombre,null)>0 " +
					"order by tc.sisPersona.comunidadResidencia.nombre, " +
					"		  tc.sisPersona.primerApellido, " +
					"		  tc.sisPersona.segundoApellido, " +
					"		  tc.sisPersona.primerNombre, " +
					"		  tc.sisPersona.segundoNombre")
})				
public class ColVol extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="COLVOLS_COLVOL_ID_GENERATOR", sequenceName="SIVE.COLVOLS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COLVOLS_COLVOL_ID_GENERATOR")
	@Column(name="COLVOL_ID",updatable=false,nullable=false)
	private long colVolId;

	@NotNull(message="La persona es requerida")
    @OneToOne(targetEntity=SisPersona.class)
	@JoinColumn(name="PERSONA",nullable=false,updatable=false)
	private SisPersona sisPersona;

    @NotNull(message="La unidad de salud es requerida")
    @ManyToOne(targetEntity=Unidad.class)
	@JoinColumn(name="UNIDAD",nullable=false,updatable=true,referencedColumnName="CODIGO")
	private Unidad unidad;

    @Size(min=1,max=500,message="El número de caracteres permitidos es de 1 a 500")
    @Column(nullable=true,length=500)
    private String observaciones;

	@Digits(integer=3,fraction=6)
	@Column(nullable=true,precision=10,scale=6)
	private BigDecimal longitud;

	@Digits(integer=3,fraction=6)
	@Column(nullable=true,precision=10,scale=6)
	private BigDecimal latitud;

	@OneToOne(mappedBy="colVol",targetEntity=ColVolAcceso.class,fetch=FetchType.LAZY,optional=true,cascade=CascadeType.ALL)
	private ColVolAcceso colVolAcceso;

	@NotNull(message="La fecha de inicio es requerida")
    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_INICIO",nullable=false)
	private Date fechaInicio;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_FIN",nullable=true)
	private Date fechaFin;

	@OneToOne(mappedBy="colVol",targetEntity=PuestoNotificacion.class,fetch=FetchType.LAZY,optional=true)
	private PuestoNotificacion puestoNotificacion;
	
    public ColVol() {
    }

	public void setColVolId(long colVolId) {
		this.colVolId = colVolId;
	}

	public long getColVolId() {
		return colVolId;
	}

	public void setSisPersona(SisPersona sisPersona) {
		this.sisPersona = sisPersona;
	}

	public SisPersona getSisPersona() {
		return sisPersona;
	}

	public void setUnidad(Unidad unidad) {
		this.unidad = unidad;
	}

	public Unidad getUnidad() {
		return unidad;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public BigDecimal getLongitud() {
		return longitud;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

	public BigDecimal getLatitud() {
		return latitud;
	}

	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}

	public ColVolAcceso getColVolAcceso() {
		return colVolAcceso;
	}

	public void setColVolAcceso(ColVolAcceso colVolAcceso) {
		this.colVolAcceso = colVolAcceso;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public void setPuestoNotificacion(PuestoNotificacion puestoNotificacion) {
		this.puestoNotificacion = puestoNotificacion;
	}

	public PuestoNotificacion getPuestoNotificacion() {
		return puestoNotificacion;
	}
	
}